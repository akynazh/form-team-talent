const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../utils/util"

Page({
  to_create_activity() {
    util.route("/pages/page_activity/create/create")
  },
  view_public_activity() {
    util.route("/pages/page_activity/public/public")
  },
  scan_qrcode() {
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