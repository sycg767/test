Component({
  properties: {
    questions: {
      type: Array,
      value: []
    },
    currentIndex: {
      type: Number,
      value: 0
    }
  },

  data: {
    showCard: false
  },

  methods: {
    // 切换答题卡显示状态
    toggleCard() {
      this.setData({
        showCard: !this.data.showCard
      });
    },

    // 跳转到指定题目
    jumpToQuestion(e) {
      const { index } = e.currentTarget.dataset;
      this.triggerEvent('jump', { index });
      this.toggleCard();
    },

    // 关闭答题卡
    closeCard() {
      this.setData({
        showCard: false
      });
    }
  }
}); 