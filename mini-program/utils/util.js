export {
  getFormatTimeByMillis,
  getCurrentFormatTime,
  getFormatTimeByDate,
  alertFail,
  fail,
  getToken,
  getAuthHeader,
  goToPage,
  checkToken,
  checkSuccess,
  route,
  auth
}

const app = getApp()
const baseUrl = app.globalData.baseUrl

function ftime(t) {
  if (t < 10) {
    return `0${t}`
  }
  return t
}
// 获取当前格式化时间
function getCurrentFormatTime() {
  return getFormatTimeByDate(new Date())
}
// 根据Date获取格式化时间
function getFormatTimeByMillis(millis) {
  if (typeof (millis) != "number") {
    millis = parseInt(millis)
  }
  return getFormatTimeByDate(new Date(millis))
}
function getFormatTimeByDate(date) {
  return `${date.getFullYear()}年${ftime(date.getMonth() + 1)}月${ftime(date.getDate())}日`
  // return `${date.getFullYear()}-${ftime(date.getMonth() + 1)}-${ftime(date.getDate())} ${ftime(date.getHours())}:${ftime(date.getMinutes())}:${ftime(date.getSeconds())}`
}

function fail() {
  let content = "操作失败，请重试 >_<"
  alertFail(content)
}
function alertFail(content) {
  wx.showModal({
    title: "操作失败",
    content: content,
    showCancel: false,
    confirmText: "确定",
    confirmColor: "#00BFFF",
  })
}

function getToken() {
  return wx.getStorageSync('auth') || ''
}
function getAuthHeader() {
  let token = getToken()
  let myHeader = token != "" ? { "auth": token } : {}
  return myHeader
}

function goToPage(pageUrl, redirect) {
  if (redirect == 0) {
    wx.navigateTo({
      url: pageUrl,
    })
  } else if (redirect == 1) {
    wx.redirectTo({
      url: pageUrl,
    })
  }
}

function checkToken() {
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${baseUrl}/api/user/auth?check=1`,
      method: 'POST',
      header: getAuthHeader(),
      success: res => {
        if (res.data.code == 200) resolve()
        else reject(res.data.msg)
      }
    })
  })
}

function route(pageUrl, needAuth = 1, redirect = 0) {
  if (needAuth == 0) {
    goToPage(pageUrl, redirect)
  } else {
    if (getToken() != '') {
      checkToken().then(
        () => goToPage(pageUrl, redirect),
        () => auth()
      )
    } else {
      wx.removeStorageSync('auth')
      auth(pageUrl, redirect)
    }
  }
}

function checkSuccess(res) {
  let code = res.data.code
  if (code == 200) {
    return true
  } else if (code == 401) {
    alertFail("认证失败，尝试登录...")
    auth()
    return false
  } else {
    alertFail(res.data.msg)
    return false
  }
}

function auth(pageUrl = "", redirect = 1) {
  wx.showLoading({
    title: "尝试登录..."
  })
  return new Promise((resolve, reject) => {
    wx.login({
      success(res1) {
        wx.request({
          url: `${baseUrl}/api/user/auth?code=${res1.code}`,
          method: 'POST',
          success(res2) {
            if (res2.data.code == 200) {
              wx.hideLoading({
                success: () => { wx.showToast({ title: '登录成功' }) }
              })
              wx.setStorageSync('auth', res2.header.auth)
              if (pageUrl != "") goToPage(pageUrl, redirect)
              resolve()
            } else {
              wx.hideLoading({
                success: () => { alertFail(`${res2.data.msg}`) },
              })
              reject()
            }
          },
          fail() {
            wx.hideLoading({
              success: () => { alertFail("请检查网络") },
            })
            reject()
          }
        })
      },
      fail() {
        wx.hideLoading({
          success: () => { alertFail("请检查网络") },
        })
        reject()
      }
    })
  })
}