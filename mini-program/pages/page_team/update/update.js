const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    t_id: "",
    t_desc: "",
    t_name: "",
    t_total: ""
  },
  onLoad(params) {
    console.log(params)
    let team = JSON.parse(params.team)
    this.setData({
      a_id: params.a_id,
      t_id: params.t_id,
      t_desc: team.t_desc,
      t_name: team.t_name,
      t_total: team.t_total
    })
  },
  updateTeam() {
    let a_id = this.data.a_id
    let t_id = this.data.t_id
    let that = this
    console.log(this.data)
    wx.request({
      url: `${baseUrl}/api/team/update`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          util.route(`/pages/page_team/detail/detail?a_id=${a_id}&t_id=${t_id}`)
          wx.showToast({
            title: '操作成功',
          })
        }
      },
      fail() {util.fail()}
    })
  }
})