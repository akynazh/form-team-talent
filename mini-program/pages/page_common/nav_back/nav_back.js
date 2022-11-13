Component({
  properties: {
    pageUrl: {
      type: String
    },
    title: {
      type: String
    }
  },
  methods: {
    navigateBack() {
      let that = this
      wx.redirectTo({
        url: that.properties.pageUrl
      })
    }
  },
})
