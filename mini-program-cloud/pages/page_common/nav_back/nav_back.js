Component({
  properties: {
    title: {
      type: String
    }
  },
  methods: {
    navigateBack() {
      wx.navigateBack({
        delta: 1,
      })
    }
  },
})
