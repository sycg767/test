Page({
  data: {
    totalQuestions: 0,
    correctCount: 0,
    wrongCount: 0,
    correctRate: 0,
    duration: 0,
    bankId: '',
    mode: '',
    wrongQuestions: []
  },

  onLoad(options) {
    const { 
      totalQuestions, 
      correctCount, 
      duration, 
      bankId, 
      mode,
      wrongQuestions 
    } = options

    // 计算统计数据
    const wrongCount = totalQuestions - correctCount
    const correctRate = Math.round((correctCount / totalQuestions) * 100)

    this.setData({
      totalQuestions: parseInt(totalQuestions),
      correctCount: parseInt(correctCount),
      wrongCount,
      correctRate,
      duration: parseFloat(duration).toFixed(1),
      bankId,
      mode,
      wrongQuestions: JSON.parse(wrongQuestions || '[]')
    })
  },

  // 查看错题
  onReviewTap() {
    const { wrongQuestions } = this.data
    if (wrongQuestions.length === 0) {
      wx.showToast({
        title: '没有错题哦',
        icon: 'none'
      })
      return
    }
    wx.navigateTo({
      url: '/pages/practice/review?questions=' + JSON.stringify(wrongQuestions)
    })
  },

  // 再练一次
  onRestartTap() {
    const { bankId, mode } = this.data
    wx.redirectTo({
      url: `/pages/practice/index?bankId=${bankId}&mode=${mode}`
    })
  },

  // 返回题库
  onBackTap() {
    wx.navigateBack({
      delta: 2  // 返回题库页面
    })
  }
}) 