module.exports = {
  route
}

const app = getApp();
const {alert_fail} = require("./util");
const base_url = app.globalData.serverUrl;

function auth(page_url) {
  wx.login({
    success(res1) {
      let token = wx.getStorageSync('auth') || "";
      let my_header = token != "" ? {"auth": token} : {}
      wx.request({
        url: `${base_url}/api/user/auth?code=${res1.code}`,
        header: my_header,
        method: 'POST',
        success(res2) {
          console.log(res2)
          if (res2.data.code == 200) {
            wx.hideLoading({
              success: () => {
                wx.showToast({
                  title: '登录成功'
                })
              }
            })
            wx.redirectTo({
              url: page_url,
            })
            wx.setStorageSync('auth', res2.header.auth);
            app.globalData.islogin = true;
            return true;
          } else if (res2.data.code == 4011 || res2.data.code == 4012) {
            console.log("token过期，重新登录")
            wx.removeStorageSync('auth')
            auth(page_url)
          }  
          else {
            wx.hideLoading({
              success: () => {alert_fail("登录失败", `${res2.data.msg}`);},
            })
          }
        },
        fail() {
          wx.hideLoading({
            success: () => {alert_fail("登录失败", "请检查网络");},
          })
        }
      })
    },
    fail() {
      wx.hideLoading({
        success: () => {alert_fail("登录失败", "请检查网络");},
      })
    }
  })
}

function route(page_url) {
  if (!app.globalData.islogin) {
    wx.getUserProfile({
      desc: '请问是否进行登录？',
      success(res) {
        app.globalData.userInfo = res.userInfo;
        wx.showLoading({
          title: "登录中..."
        })
        auth(page_url)
      },
      fail(err) {}
    });
  } else {
    wx.redirectTo({
      url: page_url,
    })
  }
}

