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
      wx.navigateTo({
        url: that.properties.pageUrl
      })
    }
  },
})
