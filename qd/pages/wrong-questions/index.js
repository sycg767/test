// pages/wrong-questions/index.js
import { questionAPI } from '../../services/api.js';
import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    statistics: {
      totalCount: 0,
      reviewedCount: 0,
      masteredCount: 0,
      needReviewCount: 0
    },
    questions: [],
    loading: false,
    page: 1,
    hasMore: true
  },

  onLoad() {
    if (!checkLogin()) {
      wx.navigateBack();
      return;
    }
    this.loadStatistics();
    this.loadQuestions();
  },

  async loadStatistics() {
    try {
      const userId = getApp().globalData.userInfo.id;
      const stats = await questionAPI.getWrongStatistics(userId);
      this.setData({ statistics: stats });
    } catch (error) {
      console.error('加载错题统计失败:', error);
    }
  },

  async loadQuestions() {
    if (this.data.loading || !this.data.hasMore) return;

    try {
      this.setData({ loading: true });
      const userId = getApp().globalData.userInfo.id;
      const questions = await questionAPI.getWrongQuestions(userId, {
        page: this.data.page - 1,
        size: 10
      });

      if (!questions || questions.length === 0) {
        this.setData({
          hasMore: false,
          loading: false
        });
        return;
      }

      this.setData({
        questions: [...this.data.questions, ...questions],
        page: this.data.page + 1,
        hasMore: questions.length === 10,
        loading: false
      });
    } catch (error) {
      console.error('加载错题失败:', error);
      this.setData({ loading: false });
    }
  },

  startPractice() {
    if (this.data.questions.length === 0) {
      wx.showToast({
        title: '暂无错题可练习',
        icon: 'none'
      });
      return;
    }
    
    const questionIds = this.data.questions.map(q => q.id).join(',');
    wx.navigateTo({
      url: `/pages/practice/index?mode=wrong&questionIds=${questionIds}`
    });
  },

  goToDetail(e) {
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/practice/index?mode=review&questionId=${id}`
    });
  }
});