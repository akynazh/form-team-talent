Component({
  properties: {
    page_url: ""
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
