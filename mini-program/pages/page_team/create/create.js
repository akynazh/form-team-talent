const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    t_name: "",
    t_desc: "",
    t_total: 2
  },
  onTotalChange(event) {
    this.setData({
      t_total: event.detail
    })
    console.log(this.data.t_total)
  },
  onLoad(params) {
    this.setData({
      a_id: params.a_id,
      a_type: params.a_type
    })
  },
  createTeam() {
    let that = this
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    console.log(that.data)
    wx.request({
      url: `${baseUrl}/api/team/add`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          let t_id = res.data.obj.t_id
          util.route(`/pages/page_team/detail/detail?a_id=${a_id}&t_id=${t_id}`, 1, 1)
          wx.showToast({
            title: '创建成功',
          })
        }
      },
      fail() {util.fail()}
    })
  }
})
