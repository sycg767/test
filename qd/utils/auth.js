export const checkLogin = () => {
  const userInfo = getApp().globalData.userInfo;
  if (!userInfo) {
    wx.showModal({
      title: '提示',
      content: '请先登录后再开始刷题',
      confirmText: '去登录',
      success: (res) => {
        if (res.confirm) {
          wx.navigateTo({
            url: '/pages/login/index'
          });
        }
      }
    });
    return false;
  }
  return true;
}; 