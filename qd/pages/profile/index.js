// pages/profile/index.js
import { userApi } from '../../services/api.js'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: null,
    stats: {
      totalQuestions: 0,
      correctRate: '0%',
      todayCount: 0
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {
    this.checkLoginStatus();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    this.checkLoginStatus();
    if (this.data.userInfo) {
      this.loadUserStats();
    }
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({ selected: 2 });
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = getApp().globalData.userInfo;
    console.log('当前用户信息:', userInfo);
    if (userInfo) {
      this.setData({ 
        userInfo,
        // 设置默认显示数据
        stats: {
          totalQuestions: userInfo.totalQuestions || 100,
          correctRate: userInfo.correctRate || '85%',
          todayCount: userInfo.todayCount || 0
        }
      });
    } else {
      this.setData({ userInfo: null });
    }
  },

  // 跳转到登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/index'
    });
  },

  // 退出登录
  logout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除用户信息
          getApp().globalData.userInfo = null;
          wx.removeStorageSync('userInfo');
          // 更新页面状态
          this.setData({ 
            userInfo: null,
            stats: {
              totalQuestions: 0,
              correctRate: '0%',
              todayCount: 0
            }
          });
        }
      }
    });
  },

  // 跳转到错题本
  goToWrongQuestions() {
    if (!this.data.userInfo) {
      this.goToLogin();
      return;
    }
    wx.navigateTo({
      url: '/pages/wrong-questions/index'
    });
  },

  // 跳转到收藏
  goToCollection() {
    if (!this.data.userInfo) {
      this.goToLogin();
      return;
    }
    wx.navigateTo({
      url: '/pages/collection/index'
    });
  },

  // 跳转到历史记录
  goToHistory() {
    if (!this.data.userInfo) {
      this.goToLogin();
      return;
    }
    wx.navigateTo({
      url: '/pages/study-record/index'
    });
  },

  // 跳转到设置
  goToSettings() {
    if (!this.data.userInfo) {
      this.goToLogin();
      return;
    }
    wx.navigateTo({
      url: '/pages/settings/index'
    });
  },

  async loadUserStats() {
    try {
      const userId = getApp().globalData.userInfo?.id;
      if (!userId) return;

      const stats = await userApi.getUserStats(userId);
      this.setData({ stats });
    } catch (error) {
      console.error('加载用户统计数据失败:', error);
    }
  }
})