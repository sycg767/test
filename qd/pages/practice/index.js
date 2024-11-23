// pages/practice/index.js
import { questionAPI } from '../../services/api.js'
import { collectionAPI } from '../../services/api.js'
import { checkLogin } from '../../utils/auth.js'

Page({
  data: {
    questions: [],
    currentIndex: 0,
    currentQuestion: null,
    showAnswer: false,
    isLastQuestion: false,
    isCollected: false,
    mode: '',
    bankId: null,
    startTime: null,
    showAnswerCard: false,
    answeredQuestions: {},
    timer: null,
    seconds: 0,
    timeStr: '00:00'
  },

  onLoad(options) {
    if (!checkLogin()) {
      wx.navigateBack();
      return;
    }
    
    const { mode, bankId } = options;
    console.log('Practice mode:', mode, 'bankId:', bankId);
    this.setData({ 
      mode,
      bankId,
      startTime: new Date()
    });
    this.loadQuestions();
    this.checkCollectionStatus();
    this.startTimer();
  },

  // 加载题目
  async loadQuestions() {
    try {
      wx.showLoading({ title: '加载中...' });
      let questions;
      
      if (this.data.mode === 'wrong') {
        // 错题练习模式
        const questionIds = this.options.questionIds?.split(',');
        if (!questionIds || questionIds.length === 0) {
          wx.showToast({
            title: '暂无错题',
            icon: 'none'
          });
          setTimeout(() => wx.navigateBack(), 1500);
          return;
        }
        questions = await questionAPI.getQuestionsByIds(questionIds);
      } else if (this.data.mode === 'review') {
        // 查看单个错题
        const questionId = this.options.questionId;
        const question = await questionAPI.getQuestion(questionId);
        questions = [question];
      } else {
        // 普通练习模式
        questions = await questionAPI.getPracticeQuestions({
          bankId: this.data.bankId,
          mode: this.data.mode,
          count: 10
        });
      }

      console.log('获取到的题目数据:', questions);

      // 处理题目数据
      const processedQuestions = questions.map(q => ({
        ...q,
        options: q.options.map(opt => ({
          ...opt,
          selected: false
        }))
      }));

      console.log('处理后的题目数据:', processedQuestions);

      this.setData({
        questions: processedQuestions,
        currentQuestion: processedQuestions[0],
        isLastQuestion: processedQuestions.length === 1
      });

      this.checkCollectionStatus();
    } catch (error) {
      console.error('加载题目失败:', error);
      wx.showToast({
        title: '加载题目失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 选择选项
  onOptionSelect(e) {
    const { id } = e.currentTarget.dataset;
    const { currentIndex, questions, showAnswer } = this.data;
    
    // 如果已经显示答案或题目已答过，则不允许选择
    if (showAnswer || questions[currentIndex].answered) return;
    
    const currentQuestion = {...questions[currentIndex]};
    const isSingle = currentQuestion.type === 'SINGLE';
    
    const updatedOptions = currentQuestion.options.map(opt => ({
      ...opt,
      selected: opt.id === id ? !opt.selected : isSingle ? false : opt.selected
    }));

    const updatedQuestions = [...questions];
    updatedQuestions[currentIndex] = {
      ...currentQuestion,
      options: updatedOptions
    };

    this.setData({
      questions: updatedQuestions,
      currentQuestion: updatedQuestions[currentIndex]
    });
  },

  // 提交答案
  async submitAnswer() {
    const { currentQuestion, mode, bankId } = this.data;
    const selectedOptions = currentQuestion.options.filter(opt => opt.selected);

    if (selectedOptions.length === 0) {
      wx.showToast({
        title: '请选择答案',
        icon: 'none'
      });
      return;
    }

    try {
      const userAnswer = selectedOptions.map(opt => opt.key).join(',');
      const isCorrect = userAnswer === currentQuestion.answer;
      const userId = getApp().globalData.userInfo.id;

      // 提交答案到后端
      await questionAPI.submitAnswer({
        userId: userId,
        questionId: currentQuestion.id,
        bankId: bankId,
        answer: userAnswer,
        isCorrect: isCorrect,
        mode: mode,
        practiceTime: Math.floor((new Date() - this.data.startTime) / 1000),
        reviewCount: 0
      });

      // 更新本地状态
      const answeredQuestions = { ...this.data.answeredQuestions };
      answeredQuestions[currentQuestion.id] = {
        answer: userAnswer,
        isCorrect: isCorrect
      };

      const questions = [...this.data.questions];
      questions[this.data.currentIndex].answered = true;
      questions[this.data.currentIndex].userAnswer = userAnswer;
      questions[this.data.currentIndex].isCorrect = isCorrect;

      this.setData({ 
        answeredQuestions,
        questions
      });

      // 根据答题结果决定下一步操作
      if (isCorrect) {
        if (this.data.isLastQuestion) {
          this.finishPractice();
        } else {
          this.nextQuestion();
        }
      } else {
        this.setData({ showAnswer: true });
      }

    } catch (error) {
      console.error('提交答案失败:', error);
      wx.showToast({
        title: '提交失败',
        icon: 'none'
      });
    }
  },

  // 下一题
  nextQuestion() {
    if (this.data.isLastQuestion) {
      this.finishPractice();
      return;
    }

    const nextIndex = this.data.currentIndex + 1;
    this.setData({
      currentIndex: nextIndex,
      currentQuestion: this.data.questions[nextIndex],
      showAnswer: false,
      isLastQuestion: nextIndex === this.data.questions.length - 1
    });

    this.checkCollectionStatus();
  },

  // 完成练习
  finishPractice() {
    const seconds = this.data.seconds;
    const correctCount = this.data.questions.filter(q => 
      q.options.filter(opt => opt.selected).map(opt => opt.key).join(',') === q.answer
    ).length;

    wx.redirectTo({
      url: `/pages/practice/result?totalQuestions=${this.data.questions.length}&correctCount=${correctCount}&seconds=${seconds}&mode=${this.data.mode}&bankId=${this.data.bankId}`
    });
  },

  // 检查收藏状态
  async checkCollectionStatus() {
    try {
      const { currentQuestion } = this.data;
      if (!currentQuestion) return;
      
      const userId = getApp().globalData.userInfo.id;
      const result = await collectionAPI.checkCollection(userId, currentQuestion.id);
      this.setData({ isCollected: result.collected });
    } catch (error) {
      console.error('检查收藏状态失败:', error);
    }
  },

  // 切换收藏状态
  async toggleCollect() {
    try {
      const { currentQuestion, isCollected } = this.data;
      const userId = getApp().globalData.userInfo.id;
      
      const result = await collectionAPI.toggleCollection(userId, currentQuestion.id);
      
      this.setData({ isCollected: result.collected });
      
      wx.showToast({
        title: result.message,
        icon: 'success'
      });
    } catch (error) {
      console.error('收藏操作失败:', error);
      wx.showToast({
        title: '操作失败',
        icon: 'none'
      });
    }
  },

  // 监听滑动切换
  onSwiperChange(e) {
    const current = e.detail.current;
    const { questions, answeredQuestions } = this.data;
    const currentQuestion = questions[current];
    
    this.setData({
      currentIndex: current,
      currentQuestion: currentQuestion,
      // 如果题目已答过，显示答案
      showAnswer: currentQuestion.answered || false,
      isLastQuestion: current === questions.length - 1
    });
    
    this.checkCollectionStatus();
  },

  // 跳转到指定题目
  onJumpQuestion(e) {
    const { index } = e.detail;
    this.setData({
      currentIndex: index,
      showAnswerCard: false
    });
  },

  // 显示/隐藏答题卡
  showAnswerCard() {
    this.setData({
      showAnswerCard: !this.data.showAnswerCard
    });
  },

  // 添加计时器相关方法
  startTimer() {
    this.data.timer = setInterval(() => {
      const seconds = this.data.seconds + 1;
      const minutes = Math.floor(seconds / 60);
      const remainSeconds = seconds % 60;
      const timeStr = `${minutes.toString().padStart(2, '0')}:${remainSeconds.toString().padStart(2, '0')}`;
      
      this.setData({
        seconds,
        timeStr
      });
    }, 1000);
  },

  onUnload() {
    // 页面卸载时清除计时器
    if (this.data.timer) {
      clearInterval(this.data.timer);
    }
  }
})