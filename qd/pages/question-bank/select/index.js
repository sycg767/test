import { questionBankAPI } from '../../../services/api.js';

Page({
  data: {
    banks: [],
    loading: false,
    error: null
  },

  onLoad() {
    this.loadBanks()
  },

  async loadBanks() {
    this.setData({ loading: true, error: null });

    try {
      const res = await questionBankAPI.getQuestionBanks();
      this.setData({ 
        banks: res.content || [],
        loading: false
      });
    } catch (error) {
      console.error('获取题库列表失败:', error);
      this.setData({ 
        error: error.message || '获取题库列表失败',
        loading: false
      });
      wx.showToast({
        title: '获取题库列表失败',
        icon: 'none'
      });
    }
  },

  // 选择题库
  selectBank(e) {
    const { bank } = e.currentTarget.dataset;
    const app = getApp();
    app.globalData.currentBank = bank;
    
    wx.showToast({
      title: '已选择题库',
      icon: 'success'
    });
    
    setTimeout(() => {
      wx.navigateBack();
    }, 1500);
  }
}); 