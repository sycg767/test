App({
  globalData: {
    userInfo: null,
    registerInfo: null
  },

  onLaunch() {
    // 只检查本地存储的用户信息
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.globalData.userInfo = userInfo;
    }
  }
}); 