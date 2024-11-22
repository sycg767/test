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
    showAnswerCard: false
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
  },

  // 加载题目
  async loadQuestions() {
    try {
      wx.showLoading({ title: '加载中...' });
      const questions = await questionAPI.getPracticeQuestions({
        bankId: this.data.bankId,
        mode: this.data.mode,
        count: 10
      });

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
    console.log('选项点击事件:', e);
    
    if (this.data.showAnswer) return;
    
    const { id } = e.currentTarget.dataset;
    console.log('选中的选项ID:', id);
    
    const { currentIndex, questions } = this.data;
    const currentQuestion = {...questions[currentIndex]};
    console.log('当前题目:', currentQuestion);
    
    const isSingle = currentQuestion.type === 'SINGLE';
    
    // 更新选项状态
    const updatedOptions = currentQuestion.options.map(opt => ({
      ...opt,
      selected: opt.id === id ? !opt.selected : isSingle ? false : opt.selected
    }));

    console.log('更新后的选项:', updatedOptions);

    // 更新整个questions数组
    const updatedQuestions = [...questions];
    updatedQuestions[currentIndex] = {
      ...currentQuestion,
      options: updatedOptions
    };

    this.setData({
      questions: updatedQuestions,
      currentQuestion: updatedQuestions[currentIndex]
    }, () => {
      console.log('数更新后的状态:', this.data);
    });
  },

  // 提交答案
  async submitAnswer() {
    const { currentQuestion, mode } = this.data;
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

      await questionAPI.submitAnswer({
        questionId: currentQuestion.id,
        answer: userAnswer,
        isCorrect,
        mode
      });

      this.setData({ showAnswer: true });
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
    const duration = Math.floor((new Date() - this.data.startTime) / 1000 / 60);
    const correctCount = this.data.questions.filter(q => 
      q.options.filter(opt => opt.selected).map(opt => opt.key).join(',') === q.answer
    ).length;

    wx.redirectTo({
      url: `/pages/practice/result?totalQuestions=${this.data.questions.length}&correctCount=${correctCount}&duration=${duration}&mode=${this.data.mode}`
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
    this.setData({
      currentIndex: current,
      currentQuestion: this.data.questions[current],
      showAnswer: false, // 切换题目时重置答案显示状态
      isLastQuestion: current === this.data.questions.length - 1
    });
    
    // 检查当前题目的收藏状态
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
  }
})