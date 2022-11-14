const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    t_id: "",
    t_name: "",
    t_desc: "",
    t_total: ""
  },
  onTotalChange(event) {
    this.setData({
      t_total: event.detail
    })
    console.log(this.data.t_total)
  },
  onLoad(params) {
    let team = JSON.parse(params.team)
    this.setData({
      a_id: params.a_id,
      a_type: params.a_type,
      t_id: params.t_id,
      t_name: team.t_name,
      t_desc: team.t_desc,
      t_total: team.t_total
    })
  },
  updateTeam() {
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    let t_id = this.data.t_id
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/update`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          util.route(`/pages/page_team/detail/detail?a_id=${a_id}&t_id=${t_id}&a_type=${a_type}`)
          wx.showToast({
            title: '操作成功',
          })
        }
      },
      fail() {util.fail()}
    })
  }
})