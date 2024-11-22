Page({
  data: {
    totalQuestions: 0,
    correctCount: 0,
    duration: 0,
    mode: '',
    correctRate: '0%'
  },

  onLoad(options) {
    const { totalQuestions, correctCount, duration, mode } = options;
    const correctRate = ((correctCount / totalQuestions) * 100).toFixed(1) + '%';
    
    this.setData({
      totalQuestions: parseInt(totalQuestions),
      correctCount: parseInt(correctCount),
      duration,
      mode,
      correctRate
    });
  },

  // 查看错题
  viewWrongQuestions() {
    wx.redirectTo({
      url: '/pages/wrong-questions/index'
    });
  },

  // 再练一次
  practiceAgain() {
    const { mode, bankId } = this.data;
    wx.redirectTo({
      url: `/pages/practice/index?mode=${mode}&bankId=${bankId}`
    });
  },

  // 返回题库
  returnToBank() {
    wx.navigateBack({
      delta: 2  // 返回两层，回到题库页面
    });
  }
}); 