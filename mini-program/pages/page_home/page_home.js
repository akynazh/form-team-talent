const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

Page({
  toCreateActivity() {
    util.route("/pages/page_activity/create/create")
  },
  viewPublicActivity() {
    util.route("/pages/page_activity/activity/activity?a_type=0", 0)
  },
  login() {
    util.route('/pages/page_home/page_home')
  },
  scanQrcode() {
    wx.scanCode({
      success (res) {
        console.log(res)
        let url = res.result
        console.log(url)
        util.route(url)
      }
    })
  }
})