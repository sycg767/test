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

  methods: {
    onQuestionTap(e) {
      const { index } = e.currentTarget.dataset;
      this.triggerEvent('jump', { index });
    }
  }
}); 