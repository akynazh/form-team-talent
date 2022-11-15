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
    let a_id = e.currentTarget.dataset.a_id
    let t_id = e.currentTarget.dataset.t_id
    util.route(`/pages/page_team/detail/detail?t_id=${t_id}&a_id=${a_id}&a_type=1`, 0)
  }
})