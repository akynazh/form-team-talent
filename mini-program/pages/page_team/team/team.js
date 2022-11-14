const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    teams: [],
    a_id: '',
    a_type: ''
  },
  onLoad(params) {
    let a_id = params.a_id
    let a_type = params.a_type
    this.setData({
      a_id: a_id,
      a_type: a_type
    })
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/by_a_id?a_id=${a_id}`,
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
    console.log(this.data.teams.length)
  },
  createTeam() {
    let a_type = this.data.a_type
    let a_id = this.data.a_id
    util.route(`/pages/page_team/create/create?a_id=${a_id}&a_type=${a_type}`)
  },
  toTeamDetail(e) {
    let a_type = this.data.a_type
    let a_id = this.data.a_id
    util.route(`/pages/page_team/detail/detail?t_id=${e.currentTarget.id}&a_id=${a_id}&a_type=${a_type}`, 0)
  }
})