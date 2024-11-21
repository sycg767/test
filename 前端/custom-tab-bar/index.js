Component({
  data: {
    selected: 0
  },
  methods: {
    switchTab(e) {
      const path = e.currentTarget.dataset.path;
      const index = path === '/pages/index/index' ? 0 : 
                    path === '/pages/question-bank/index' ? 1 : 2;
      
      this.setData({
        selected: index
      });
      
      wx.switchTab({
        url: path,
        fail: (error) => {
          console.error('切换失败:', error);
        }
      });
    }
  }
}) 