import { collectionAPI } from '../../services/api.js';
import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    collections: [],
    loading: false,
    page: 0,
    size: 10,
    hasMore: true
  },

  onLoad() {
    if (!checkLogin()) {
      wx.navigateBack();
      return;
    }
    this.loadCollections();
  },

  // 加载收藏列表
  async loadCollections() {
    if (this.data.loading || !this.data.hasMore) return;

    try {
      this.setData({ loading: true });
      const userId = getApp().globalData.userInfo.id;
      const result = await collectionAPI.getCollections(userId, this.data.page, this.data.size);
      
      this.setData({
        collections: [...this.data.collections, ...result.content],
        page: this.data.page + 1,
        hasMore: !result.last,
        loading: false
      });
    } catch (error) {
      console.error('获取收藏列表失败:', error);
      wx.showToast({
        title: '获取收藏失败',
        icon: 'none'
      });
      this.setData({ loading: false });
    }
  },

  // 取消收藏
  async cancelCollection(e) {
    const { questionId } = e.currentTarget.dataset;
    try {
      const userId = getApp().globalData.userInfo.id;
      await collectionAPI.toggleCollection(userId, questionId);
      
      // 从列表中移除
      const collections = this.data.collections.filter(item => item.question.id !== questionId);
      this.setData({ collections });
      
      wx.showToast({
        title: '取消收藏成功',
        icon: 'success'
      });
    } catch (error) {
      console.error('取消收藏失败:', error);
      wx.showToast({
        title: '取消收藏失败',
        icon: 'none'
      });
    }
  },

  // 跳转到题目详情
  goToQuestion(e) {
    const { questionId } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/question/detail?id=${questionId}`
    });
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      collections: [],
      page: 0,
      hasMore: true
    }, () => {
      this.loadCollections().then(() => {
        wx.stopPullDownRefresh();
      });
    });
  },

  // 上拉加载更多
  onReachBottom() {
    this.loadCollections();
  }
}); 