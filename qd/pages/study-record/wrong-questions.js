Page({
  data: {
    totalCount: 0,
    reviewedCount: 0,
    masteredCount: 0,
    currentCategory: 0,
    categories: [
      { id: 0, name: '全部' },
      { id: 1, name: '语文' },
      { id: 2, name: '数学' },
      { id: 3, name: '英语' }
    ],
    wrongQuestions: []
  },

  onLoad() {
    this.fetchStatistics()
    this.fetchWrongQuestions()
  },

  // 获取统计数据
  async fetchStatistics() {
    try {
      // TODO: 调用API获取统计数据
      const statistics = {
        totalCount: 100,
        reviewedCount: 60,
        masteredCount: 30
      }
      this.setData(statistics)
    } catch (error) {
      console.error('获取统计数据失败:', error)
    }
  },

  // 获取错题列表
  async fetchWrongQuestions() {
    try {
      // TODO: 调用API获取错题列表
      const wrongQuestions = [
        {
          id: 1,
          type: 'single',
          title: '这是一道错题的题目描述，可能很长很长很长很长很长很长很长很长很长很长',
          reviewCount: 2,
          wrongTime: '2024-03-15',
          status: 'reviewing'
        },
        {
          id: 2,
          type: 'multiple',
          title: '这是另一道错题的题目描述',
          reviewCount: 3,
          wrongTime: '2024-03-14',
          status: 'mastered'
        }
      ]
      this.setData({ wrongQuestions })
    } catch (error) {
      console.error('获取错题列表失败:', error)
      wx.showToast({
        title: '获取错题列表失败',
        icon: 'none'
      })
    }
  },

  // 分类切换
  onCategoryTap(e) {
    const { id } = e.currentTarget.dataset
    this.setData({ currentCategory: id })
    this.fetchWrongQuestions()
  },

  // 点击题目
  onQuestionTap(e) {
    const { id } = e.currentTarget.dataset
    wx.navigateTo({
      url: `/pages/practice/review?questions=${JSON.stringify([this.data.wrongQuestions.find(q => q.id === id)])}`
    })
  },

  // 开始复习全部
  onReviewAllTap() {
    wx.navigateTo({
      url: `/pages/practice/review?questions=${JSON.stringify(this.data.wrongQuestions)}`
    })
  }
}) 