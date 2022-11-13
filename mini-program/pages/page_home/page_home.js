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
  viewPersonalActivity() {
    util.route("/pages/page_activity/activity/activity?a_type=1")
  },
  login() {
    util.route('/pages/page_home/page_home')
  },
  viewMyTeam() {
    util.route("/pages/page_team/team/personal/personal")
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