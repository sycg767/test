import { userAnswerAPI } from '../../services/api.js';
import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    records: [],
    statistics: {
      totalQuestions: 0,
      correctCount: 0,
      correctRate: 0,
      todayCount: 0
    },
    loading: false,
    page: 0,
    size: 10,
    hasMore: true,
    currentBank: null
  },

  onLoad() {
    if (!checkLogin()) {
      wx.navigateBack();
      return;
    }
    
    const app = getApp();
    this.setData({
      currentBank: app.globalData.currentBank
    }, () => {
      this.loadStatistics();
      this.loadRecords();
    });
  },

  // 加载统计信息
  async loadStatistics() {
    try {
      const userId = getApp().globalData.userInfo.id;
      const stats = await userAnswerAPI.getStatistics(userId);
      this.setData({
        statistics: {
          totalQuestions: stats.totalQuestions || 0,
          correctCount: stats.correctCount || 0,
          correctRate: ((stats.correctCount / stats.totalQuestions) * 100).toFixed(1) || 0,
          todayCount: stats.todayCount || 0
        }
      });
    } catch (error) {
      console.error('加载统计信息失败:', error);
    }
  },

  // 加载做题记录
  async loadRecords() {
    if (this.data.loading || !this.data.hasMore) return;

    try {
      this.setData({ loading: true });
      const userId = getApp().globalData.userInfo.id;
      const bankId = this.data.currentBank?.id;
      
      const response = await userAnswerAPI.getBankRecords(userId, bankId, {
        page: this.data.page,
        size: this.data.size
      });

      const records = response.content || [];
      
      if (!records || records.length === 0) {
        this.setData({
          hasMore: false,
          loading: false
        });
        return;
      }

      this.setData({
        records: [...this.data.records, ...records],
        page: this.data.page + 1,
        hasMore: !response.last,
        loading: false
      });
    } catch (error) {
      console.error('加载做题记录失败:', error);
      this.setData({ loading: false });
    }
  },

  // 查看题目详情
  goToQuestionDetail(e) {
    const { questionId } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/question/detail?id=${questionId}`
    });
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({
      records: [],
      page: 0,
      hasMore: true
    }, () => {
      Promise.all([
        this.loadStatistics(),
        this.loadRecords()
      ]).then(() => {
        wx.stopPullDownRefresh();
      });
    });
  },

  // 上拉加载更多
  onReachBottom() {
    this.loadRecords();
  }
}); 