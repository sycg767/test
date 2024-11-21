import { questionBankAPI } from '../../services/api.js';
import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    bankId: '',
    bankInfo: {},
    practiceTypes: [
      {
        type: 'sequence',
        name: '顺序练习',
        icon: 'icon-sequence',
        description: '按顺序练习题目'
      },
      {
        type: 'random',
        name: '随机练习',
        icon: 'icon-random',
        description: '随机抽取题目练习'
      },
      {
        type: 'chapter',
        name: '章节练习',
        icon: 'icon-chapter',
        description: '按章节分类练习'
      },
      {
        type: 'test',
        name: '模拟测试',
        icon: 'icon-test',
        description: '模拟真实考试环境'
      }
    ]
  },

  onLoad(options) {
    const { id } = options;
    console.log('Loading bank detail, id:', id);
    this.setData({ bankId: id });
    this.loadBankDetail();
  },

  // 加载题库详情
  async loadBankDetail() {
    try {
      wx.showLoading({ title: '加载中...' });
      console.log('Fetching bank detail for id:', this.data.bankId);
      const bankInfo = await questionBankAPI.getQuestionBank(this.data.bankId);
      console.log('Received bank info:', bankInfo);
      this.setData({ bankInfo });
    } catch (error) {
      console.error('获取题库详情失败:', error);
      wx.showToast({
        title: '获取题库详情失败',
        icon: 'none'
      });
    } finally {
      wx.hideLoading();
    }
  },

  // 选择练习模式
  onModeSelect(e) {
    // 检查登录状态
    if (!checkLogin()) {
      return;
    }
    
    const { type } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/practice/index?mode=${type}&bankId=${this.data.bankId}`
    });
  }
}); 