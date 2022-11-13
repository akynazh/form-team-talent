const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    t_id: "",
    t_name: "",
    t_desc: "",
    t_total: ""
  },
  onLoad(params) {
    this.data.a_id = params.a_id
  },
  createTeam() {
    let that = this
    console.log(that.data)
    wx.request({
      url: `${baseUrl}/api/team/add`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          wx.showToast({
            title: '创建成功',
          })
          util.route(`/pages/page_team/team/team?a_id=${that.data.a_id}`)
        }
      },
      fail() {util.fail()}
    })
  }
})
