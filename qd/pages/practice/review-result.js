Page({
  data: {
    reviewedCount: 0,
    masteredCount: 0,
    continuousCorrectCount: 0,
    spendTime: 0
  },

  onLoad(options) {
    const result = JSON.parse(options.result);
    this.setData({
      ...result
    });
  },

  // 继续复习
  onContinueReview() {
    wx.redirectTo({
      url: '/pages/wrong-questions/index'
    });
  },

  // 返回错题本
  onBackToList() {
    wx.navigateBack({
      delta: 2
    });
  }
}); 