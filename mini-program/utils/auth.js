module.exports = {
	auth
}

const app = getApp()
function auth(url) {
	wx.getUserProfile({
		desc: '请问是否进行登录？',
		success(res) {
      console.log(res);
      app.globalData.userInfo = res.userInfo;
      app.globalData.islogin = true;
      wx.login({
        success(res) {
          console.log(res);
          wx.request({
            url: `${app.globalData.serverUrl}/api/user/auth?code=${res.code}`,
            method: 'POST',
            success(res) {
              console.log(res);
              if (res.data.code == 200) {
                wx.showToast({
                  title: '登录成功',
                })
                wx.setStorageSync('auth', res.header.auth);
              } else {
                wx.showToast({
                  title: `>_<登录失败，错误原因：${res.data.msg}`,
                })
              }
            },
            fail(res) {
              wx.showToast({
                title: '登录失败，请检查网络>_<',
              })
              console.log('fail to login');
            }
          })
        }
      })
			wx.redirectTo({
				url: url,
			})
		},
		fail(err) {
			console.log(err)
		}
	});
}

