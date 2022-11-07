Component({
  properties: {
    page_url: {
      type: String
    },
    title: {
      type: String
    }
  },
  methods: {
    navigate_back() {
      let that = this
      wx.redirectTo({
        url: that.properties.page_url
      })
    }
  },
})
