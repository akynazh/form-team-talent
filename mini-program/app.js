// app.js
App({
  onLaunch() {
		wx.login({
			success(res) {
				console.log(res)
			}
		})
  },
  globalData: {
    userInfo: null,
    openid:null
  }
})
