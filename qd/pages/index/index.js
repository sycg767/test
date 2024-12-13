import { checkLogin } from '../../utils/auth.js';

Page({
  data: {
    // 用户学习数据
    todayStudyTime: 0,
    continuousDays: 0,
    totalQuestions: 0,

    // 今日学习计划
    todayPlans: [
      {
        id: 1,
        name: '数据结构基础',
        total: 50,
        finished: 30,
        percent: 60
      },
      {
        id: 2,
        name: '计算机网络',
        total: 40,
        finished: 10,
        percent: 25
      }
    ],

    // 推荐题库
    recommendBanks: [
      {
        id: 1,
        name: '408综合真题',
        coverUrl: '/images/default-bank.png',
        questionCount: 1000,
        userCount: 5000
      },
      {
        id: 2,
        name: '数据结构专项',
        coverUrl: '/images/bank2.png',
        questionCount: 500,
        userCount: 3000
      }
    ],

    // 知识点导航
    knowledgePoints: [
      {
        id: 1,
        name: '数据结构',
        questionCount: 500
      },
      {
        id: 2,
        name: '计算机网络',
        questionCount: 400
      },
      {
        id: 3,
        name: '操作系���',
        questionCount: 300
      },
      {
        id: 4,
        name: '计算机组成',
        questionCount: 350
      }
    ],

    // 排行榜
    rankList: [
      {
        id: 1,
        nickname: '学习达人',
        avatarUrl: '/images/default-avatar.png',
        studyTime: 120
      },
      {
        id: 2,
        nickname: '刷题王者',
        avatarUrl: '/images/avatar2.png',
        studyTime: 100
      },
      {
        id: 3,
        nickname: '勤奋小白',
        avatarUrl: '/images/avatar3.png',
        studyTime: 80
      }
    ],

    // 当前题库状态
    currentBank: null
  },

  onLoad() {
    this.fetchUserStudyData()
    this.fetchRecommendBanks()
  },

  onShow() {
    if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      this.getTabBar().setData({
        selected: 0
      })
    }
    // 获取全局题库状态
    const app = getApp()
    this.setData({
      currentBank: app.globalData.currentBank
    })
  },

  // 获取用户学习数据
  async fetchUserStudyData() {
    try {
      // TODO: 调用API获取用户学习数据
      this.setData({
        todayStudyTime: 45,
        continuousDays: 7,
        totalQuestions: 235
      })
    } catch (error) {
      console.error('获取用户学习数据失败:', error)
    }
  },

  // 获取推荐题库
  async fetchRecommendBanks() {
    try {
      // TODO: 调用API获取推荐题库
      // 目前使用模拟数据
    } catch (error) {
      console.error('获取推荐题库失败:', error)
    }
  },

  // 功能入口点击处理
  onActionTap(e) {
    if (!checkLogin()) return
    if (!this.data.currentBank) {
      wx.showToast({
        title: '请先选择题库',
        icon: 'none'
      })
      return
    }
    
    const type = e.currentTarget.dataset.type
    switch(type) {
      case 'random':
        wx.navigateTo({
          url: `/pages/practice/index?mode=random&bankId=${this.data.currentBank.id}`
        })
        break
      case 'wrong':
        wx.navigateTo({
          url: '/pages/wrong-questions/index'
        })
        break
      case 'favorite':
        wx.navigateTo({
          url: '/pages/collection/index'
        })
        break
      case 'mock':
        wx.navigateTo({
          url: `/pages/practice/index?mode=mock&bankId=${this.data.currentBank.id}`
        })
        break
    }
  },

  // 更多计划
  onPlanMore() {
    wx.navigateTo({
      url: '/pages/study-plan/index'
    })
  },

  // 更多题库
  onMoreBanks() {
    wx.switchTab({
      url: '/pages/question-bank/index'
    })
  },

  // 更多排行
  onMoreRank() {
    wx.navigateTo({
      url: '/pages/rank/index'
    })
  },

  // 题库点击
  onBankTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/question-bank/detail?id=${id}`
    })
  },

  // 知识点点击
  onKnowledgeTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/knowledge/detail?id=${id}`
    })
  },

  // 选择题库
  goToQuestionBank() {
    if (!checkLogin()) return;
    
    wx.navigateTo({
      url: '/pages/question-bank/select/index'
    });
  }
}) 