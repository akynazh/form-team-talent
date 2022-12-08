const app = getApp()
let util = require("../../utils/util")

Page({
  toCreateActivity() {
    util.route("/pages/page_activity/create/create")
  },
  viewPublicActivity() {
    util.route("/pages/page_activity/activity/activity?aType=0", 0)
  },
  login() {
    util.auth("/pages/page_me/page_me")
  },
  scanQrcode() {
    wx.scanCode({
      success (res) {
        let url = res.result
        util.route(url)
      }
    })
  }
})