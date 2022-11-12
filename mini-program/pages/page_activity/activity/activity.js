const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    activities: []
  },
  onLoad(params) {
    console.log(params)
    let type = params.type
    let that = this
    let url = `${base_url}/api/activity/get/pub`
    if (type == 1) url = `${base_url}/api/activity/get/my`
    wx.request({
      url: url,
      header: util.get_auth_header(),
      success(res) {
        if (util.check_success(res)) {
          let obj = res.data.obj
          for (let a of obj) {
            if (a.a_end_date < util.currentTime()) {
              a.a_name = a.a_name + "(已经结束)"
            }
          }
          that.setData({
            activities: obj
          })
        }
      },
      fail(res) {
        util.fail()
      }
    })
  },
  to_activity_detail(e) {
    util.route(`/pages/page_team/detail/detail?a_id=${e.currentTarget.id}`)
  }
})