import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    navItems: [
      {
        id: 1,
        type: 'random',
        text: '随机练习',
        icon: 'iconfont icon-dice'
      },
      {
        id: 2,
        type: 'special',
        text: '专项练习',
        icon: 'iconfont icon-list'
      },
      {
        id: 3,
        type: 'mock',
        text: '模拟考试',
        icon: 'iconfont icon-alarm-clock'
      },
      {
        id: 4,
        type: 'wrong',
        text: '错题本',
        icon: 'iconfont icon-task'
      }
    ],
    recommendBanks: [],
    banners: [
      {
        id: 1,
        imageUrl: '/assets/images/banners/banner1.png',
        linkUrl: '/pages/question-bank/detail?id=1'
      },
      {
        id: 2,
        imageUrl: '/assets/images/banners/banner2.png',
        linkUrl: '/pages/question-bank/detail?id=2'
      },
      {
        id: 3,
        imageUrl: '/assets/images/banners/banner3.png',
        linkUrl: '/pages/question-bank/detail?id=3'
      }
    ]
  },

  onLoad() {
    this.fetchRecommendBanks()
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0  // 设置首页 tab 为选中状态
      })
    }
  },

  // 获取推荐题库
  async fetchRecommendBanks() {
    try {
      // TODO: 调用API获取推荐题库数据
      const recommendBanks = []
      this.setData({ recommendBanks })
    } catch (error) {
      console.error('获取推荐题库失败:', error)
    }
  },

  // 添加功能点击事件处理
  onActionTap(e) {
    if (!checkLogin()) {
      return;
    }
    
    const type = e.currentTarget.dataset.type;
    switch(type) {
      case 'wrong':
        wx.navigateTo({
          url: '/pages/wrong-questions/index'
        });
        break;
      case 'collect':
        wx.navigateTo({
          url: '/pages/collection/index'
        });
        break;
      case 'rank':
        wx.navigateTo({
          url: '/pages/rank/index'
        });
        break;
      case 'history':
        wx.navigateTo({
          url: '/pages/study-record/index'
        });
        break;
    }
  },

  // 导航项点击处理
  onNavItemTap(e) {
    // 检查登录状态
    if (!checkLogin()) {
      return;
    }

    const { type } = e.currentTarget.dataset;
    switch (type) {
      case 'random':
        wx.navigateTo({
          url: '/pages/practice/index?mode=random'
        });
        break;
      case 'special':
        wx.navigateTo({
          url: '/pages/practice/index?mode=special'
        });
        break;
      // ... 其他 case
    }

  },

  // 添加 banner 点击处理
  onBannerTap(e) {
    if (!checkLogin()) {
      return;
    }
    const { url } = e.currentTarget.dataset;
    wx.navigateTo({ url });
  },

  // 添加题库点击处理
  onBankTap(e) {
    if (!checkLogin()) {
      return;
    }
    const { id } = e.currentTarget.dataset;
    wx.navigateTo({
      url: `/pages/question-bank/detail?id=${id}`
    });
  }
}) 