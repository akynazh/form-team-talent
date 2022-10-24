module.exports = {
	auth
}

const app = getApp()
async function auth(url) {
	wx.getUserProfile({
		desc: '获取用户必要的信息',
		success(res) {
			console.log(res)
			app.globalData.userInfo = res.userInfo
			wx.setStorageSync('userInfo', res.userInfo)
			wx.redirectTo({
				url: url,
			})
		},
		fail(err) {
			console.log(err)
		}
	});
}

