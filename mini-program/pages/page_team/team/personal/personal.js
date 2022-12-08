const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../../utils/util"

Page({
  data: {
    teams: [],
  },
  onLoad() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/my`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          that.setData({
            teams: res.data.obj,
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  toTeamDetail(e) {
    let aId = e.currentTarget.dataset.aid
    let tId = e.currentTarget.dataset.tid
    util.route(`/pages/page_team/detail/detail?tId=${tId}&aId=${aId}&aType=1&manage=1`)
  }
})