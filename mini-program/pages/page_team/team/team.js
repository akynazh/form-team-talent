const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    teams: [],
    a_id: '',
  },
  onLoad(params) {
    this.data.a_id = params.a_id
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/by_a_id?a_id=${that.data.a_id}`,
      header: util.getAuthHeader(),
      success(res) {
        console.log(res)
        if (util.checkSuccess(res)) {
          that.setData({
            teams: res.data.obj,
            a_id: that.data.a_id
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  createTeam() {
    util.route(`/pages/page_team/create/create?a_id=${this.data.a_id}`)
  },
  showDetail(e) {
    util.route(`/pages/page_team/detail/detail?t_id=${e.currentTarget.id}&a_id=${this.data.a_id}`, 0)
  }
})