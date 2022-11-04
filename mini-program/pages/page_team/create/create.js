const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    t_id: "",
    t_name: "",
    t_desc: "",
  },
  onLoad(params) {
    this.data.a_id = params.a_id
  },
  create_team() {
    let that = this
    console.log(that.data)
    wx.request({
      url: `${base_url}/api/team/add`,
      header: util.get_auth_header(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.check_success(res)) {
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
