export {
  getFormatTimeByMillis,
  getCurrentFormatTime,
  alertFail,
  fail,
  getAuthHeader,
  checkSuccess,
  route,
}

const app = getApp()
const baseUrl = app.globalData.baseUrl

let ftime = (t) => {
  if (t < 10) {
    return `0${t}`
  }
  return t
}
// 获取当前格式化时间
let getCurrentFormatTime = () => {
  return getFormatTimeByDate(new Date())
}
// 根据Date获取格式化时间
let getFormatTimeByMillis = (millis) => {
  if (typeof(millis) != "number") {
    millis = parseInt(millis)
  }
  return getFormatTimeByDate(new Date(millis))
}
let getFormatTimeByDate = (date) => {
  return `${date.getFullYear()}年${ftime(date.getMonth() + 1)}月${ftime(date.getDate())}日`
  // return `${date.getFullYear()}-${ftime(date.getMonth() + 1)}-${ftime(date.getDate())} ${ftime(date.getHours())}:${ftime(date.getMinutes())}:${ftime(date.getSeconds())}`
}

let checkSuccess = res => {
  if (res.data.code != 200) {
    alertFail("请求失败", res.data.msg)
    return false
  }
  return true
}

let fail = () => {
  let title = "失败"
  let content = "操作失败，请重试"
  alertFail(title, content) 
}

let alertFail = (title, content) => {
  wx.showModal({
    title: title,
    content: content,
    showCancel: false,
    confirmText: "确定",
    confirmColor: "#00BFFF",
  })
}

let getAuthHeader = () => {
  let token = wx.getStorageSync('auth') || ''
  let myHeader = token != "" ? { "auth": token } : {}
  return myHeader
}

let getToken = () => {
  return wx.getStorageSync('auth') || ''
}

let route = (pageUrl, needAuth = 1) => {
  if (needAuth == 0) {
    wx.redirectTo({
      url: pageUrl,
    })
  } else {
    if (!app.globalData.isLogin) {
      if (getToken() != '') {
        auth(pageUrl)
      } else {
        wx.getUserProfile({
          desc: '请问是否进行登录？',
          success(res) {
            app.globalData.userInfo = res.userInfo
            wx.showLoading({
              title: "登录中..."
            })
            auth(pageUrl)
          },
        })
      }
    } else {
      wx.redirectTo({
        url: pageUrl,
      })
    }
  }
}

let auth = pageUrl => {
  wx.login({
    success(res1) {
      let url = `${baseUrl}/api/user/auth?code=${res1.code}`
      if (app.globalData.userInfo != null) {
        url += `&nickName=${app.globalData.userInfo.nickName}`
      }
      wx.request({
        url: url,
        header: getAuthHeader(),
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
              url: pageUrl,
            })
            wx.setStorageSync('auth', res2.header.auth)
            app.globalData.isLogin = true
            
            return true
          } else if (res2.data.code == 4011 || res2.data.code == 4012) {
            console.log("token过期，重新登录")
            wx.removeStorageSync('auth')
            auth(pageUrl)
          }
          else {
            wx.hideLoading({
              success: () => { alertFail("登录失败", `${res2.data.msg}`) },
            })
          }
        },
        fail() {
          wx.hideLoading({
            success: () => { alertFail("登录失败", "请检查网络") },
          })
        }
      })
    },
    fail() {
      wx.hideLoading({
        success: () => { alertFail("登录失败", "请检查网络") },
      })
    }
  })
}