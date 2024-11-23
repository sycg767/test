Page({
  data: {
    totalQuestions: 0,
    correctCount: 0,
    seconds: 0,
    mode: '',
    bankId: '',
    correctRate: '0%',
    timeStr: '00:00'
  },

  onLoad(options) {
    const { totalQuestions, correctCount, seconds, mode, bankId } = options;
    const correctRate = ((correctCount / totalQuestions) * 100).toFixed(1) + '%';
    
    const totalSeconds = parseInt(seconds);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const remainSeconds = totalSeconds % 60;
    
    let timeStr = '';
    if (hours > 0) {
      timeStr = `${hours}时${minutes.toString().padStart(2, '0')}分${remainSeconds.toString().padStart(2, '0')}秒`;
    } else {
      timeStr = `${minutes.toString().padStart(2, '0')}分${remainSeconds.toString().padStart(2, '0')}秒`;
    }
    
    this.setData({
      totalQuestions: parseInt(totalQuestions),
      correctCount: parseInt(correctCount),
      seconds: totalSeconds,
      mode,
      bankId,
      correctRate,
      timeStr
    });
  },

  // 查看错题
  viewWrongQuestions() {
    wx.navigateTo({
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