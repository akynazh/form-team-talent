// app.js
App({
  globalData: {
    userInfo: null,
    openid:null
  },
  onLaunch() {
    wx.login({
      success(res) {
        console.log(res)
      }
    })
  }
})
