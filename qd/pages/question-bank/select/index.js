Page({
  data: {
    banks: []
  },

  onLoad() {
    this.loadBanks()
  },

  async loadBanks() {
    try {
      // TODO: 调用API获取题库列表
      const banks = [
        {
          id: 1,
          name: '408综合真题',
          questionCount: 1000,
          userCount: 5000
        },
        {
          id: 2,
          name: '数据结构专项',
          questionCount: 500,
          userCount: 3000
        },
        {
          id: 3,
          name: '计算机网络',
          questionCount: 800,
          userCount: 4000
        }
      ]
      this.setData({ banks })
    } catch (error) {
      console.error('获取题库列表失败:', error)
    }
  },

  // 选择题库
  selectBank(e) {
    const { bank } = e.currentTarget.dataset
    const app = getApp()
    app.globalData.currentBank = bank
    
    wx.showToast({
      title: '已选择题库',
      icon: 'success'
    })
    
    setTimeout(() => {
      wx.navigateBack()
    }, 1500)
  }
}) 