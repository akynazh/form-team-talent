export {
  currentTime,
  formatNumber,
  alert_fail,
  get_auth_header,
  check_success,
  route
}

const app = getApp()
const base_url = app.globalData.base_url

let ftime = (t) => {
  if (t < 10) {
    return `0${t}`
  }
  return t
}
let currentTime = () => {
  let date = new Date();
  return `${date.getFullYear()}-${ftime(date.getMonth() + 1)}-${ftime(date.getDate())} ${ftime(date.getHours())}:${ftime(date.getMinutes())}:${ftime(date.getSeconds())}`
}


let formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

let check_success = res => {
  if (res.data.code != 200) {
    alert_fail("请求失败", res.data.msg)
    return false
  }
  return true
}
let fail = () => { alert_fail("请求失败，请重试") }

let alert_fail = (title, content = "") => {
  wx.showModal({
    title: title,
    content: content,
    showCancel: false,
    confirmText: "确定",
    confirmColor: "#00BFFF",
  })
}

let get_auth_header = () => {
  let token = wx.getStorageSync('auth') || ""
  let my_header = token != "" ? { "auth": token } : {}
  return my_header
}

let auth = page_url => {
  wx.login({
    success(res1) {
      let token = wx.getStorageSync('auth') || ""
      let my_header = token != "" ? { "auth": token } : {}
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
            wx.setStorageSync('auth', res2.header.auth)
            app.globalData.islogin = true
            return true
          } else if (res2.data.code == 4011 || res2.data.code == 4012) {
            console.log("token过期，重新登录")
            wx.removeStorageSync('auth')
            auth(page_url)
          }
          else {
            wx.hideLoading({
              success: () => { alert_fail("登录失败", `${res2.data.msg}`) },
            })
          }
        },
        fail() {
          wx.hideLoading({
            success: () => { alert_fail("登录失败", "请检查网络") },
          })
        }
      })
    },
    fail() {
      wx.hideLoading({
        success: () => { alert_fail("登录失败", "请检查网络") },
      })
    }
  })
}

let route = (page_url, need_auth = 1) => {
  console.log(need_auth)
  if (need_auth == 0) {
    wx.redirectTo({
      url: page_url,
    })
  } else {
    if (!app.globalData.islogin) {
      wx.getUserProfile({
        desc: '请问是否进行登录？',
        success(res) {
          app.globalData.userInfo = res.userInfo
          wx.showLoading({
            title: "登录中..."
          })
          auth(page_url)
        },
        fail(err) { }
      })
    } else {
      wx.redirectTo({
        url: page_url,
      })
    }
  }
}