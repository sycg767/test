Component({
  properties: {
    question: {
      type: Object,
      value: {
        id: 0,
        title: '',
        type: 'single',
        reviewCount: 0,
        wrongCount: 0,
        status: 'new',
        createdAt: '',
        categoryId: 0,
        categoryName: ''
      }
    }
  },

  data: {
    statusMap: {
      'new': '未复习',
      'reviewing': '复习中',
      'mastered': '已掌握'
    }
  },

  methods: {
    onTap() {
      const { question } = this.properties;
      this.triggerEvent('tap', { question });
    }
  }
}); 