const app = getApp()
const base_url = app.globalData.base_url
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
      url: `${base_url}/api/team/get/by_a_id?a_id=${that.data.a_id}`,
      header: util.get_auth_header(),
      success(res) {
        console.log(res)
        if (util.check_success(res)) {
          that.setData({
            teams: res.data.obj
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  create_team() {
    util.route(`/pages/page_team/create/create?a_id=${this.data.a_id}`)
  }
})