const app = getApp()
const base_url = app.globalData.base_url
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
  update_team() {
    let a_id = this.data.a_id
    let t_id = this.data.t_id
    let that = this
    console.log(this.data)
    wx.request({
      url: `${base_url}/api/team/update`,
      header: util.get_auth_header(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.check_success(res)) {
          wx.showToast({
            title: '操作成功',
          })
          util.route(`/pages/page_team/detail/detail?a_id=${a_id}&t_id=${t_id}`)
        }
      },
      fail() {util.fail()}
    })
  }
})