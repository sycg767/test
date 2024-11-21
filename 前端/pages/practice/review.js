Page({
  data: {
    questions: [],
    currentIndex: 0,
    totalQuestions: 0,
    currentQuestion: null,
    userAnswer: null,
    showAnswer: false,
    isCollected: false,
    isLastQuestion: false
  },

  onLoad(options) {
    const questions = JSON.parse(options.questions || '[]')
    this.setData({
      questions,
      totalQuestions: questions.length,
      currentQuestion: questions[0],
      isLastQuestion: questions.length === 1
    })
  },

  // 选择选项（单选、多选）
  onOptionSelect(e) {
    if (this.data.showAnswer) return
    
    const { id } = e.currentTarget.dataset
    const { currentQuestion } = this.data
    
    if (currentQuestion.type === 'single') {
      // 单选题处理
      currentQuestion.options.forEach(option => {
        option.selected = option.id === id
      })
    } else {
      // 多选题处理
      const option = currentQuestion.options.find(opt => opt.id === id)
      option.selected = !option.selected
    }
    
    this.setData({ currentQuestion })
  },

  // 选择判断题答案
  onJudgeSelect(e) {
    if (this.data.showAnswer) return
    
    const { value } = e.currentTarget.dataset
    this.setData({ userAnswer: value })
  },

  // 提交答案
  submitAnswer() {
    const { currentQuestion, userAnswer } = this.data
    let isCorrect = false
    
    if (currentQuestion.type === 'judge') {
      isCorrect = userAnswer === currentQuestion.answer
    } else {
      const selectedOptions = currentQuestion.options.filter(opt => opt.selected)
      if (currentQuestion.type === 'single') {
        isCorrect = selectedOptions.length === 1 && selectedOptions[0].isCorrect
      } else {
        const correctOptions = currentQuestion.options.filter(opt => opt.isCorrect)
        isCorrect = selectedOptions.length === correctOptions.length &&
          selectedOptions.every(opt => opt.isCorrect)
      }
    }
    
    this.setData({ 
      showAnswer: true,
      ['currentQuestion.isCorrect']: isCorrect
    })

    // 更新错题记录
    if (isCorrect) {
      // TODO: 调用API更新错题状态
    }
  },

  // 下一题
  nextQuestion() {
    if (this.data.isLastQuestion) {
      wx.navigateBack()
      return
    }
    
    const nextIndex = this.data.currentIndex + 1
    this.setData({
      currentIndex: nextIndex,
      currentQuestion: this.data.questions[nextIndex],
      showAnswer: false,
      userAnswer: null,
      isLastQuestion: nextIndex === this.data.questions.length - 1
    })
  },

  // 切换收藏状态
  async toggleCollect() {
    const { currentQuestion, isCollected } = this.data
    try {
      // TODO: 调用API切换收藏状态
      this.setData({ isCollected: !isCollected })
      wx.showToast({
        title: !isCollected ? '收藏成功' : '取消收藏',
        icon: 'success'
      })
    } catch (error) {
      console.error('操作收藏失败:', error)
      wx.showToast({
        title: '操作失败',
        icon: 'none'
      })
    }
  }
}) 