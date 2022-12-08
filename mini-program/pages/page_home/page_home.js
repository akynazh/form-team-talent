const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

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
        console.log(url)
        util.route(url)
      }
    })
  }
})