const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    activities: [],
    a_type: ""
  },
  onLoad(params) {
    let a_type = params.a_type
    let that = this
    let url = `${baseUrl}/api/activity/get/pub`
    if (a_type == 1) url = `${baseUrl}/api/activity/get/my`
    wx.request({
      url: url,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          for (let a of obj) {
            if (a.a_end_date < util.getCurrentTime()) {
              a.a_name = a.a_name + "(已经结束)"
            }
          }
          that.setData({
            a_type: a_type,
            activities: obj
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  toActivityDetail(e) {
    let a_type = this.data.a_type
    if (a_type == 1) {
      util.route(`/pages/page_activity/detail/detail?a_id=${e.currentTarget.id}&a_type=${a_type}`)
    } else {
      util.route(`/pages/page_activity/detail/detail?a_id=${e.currentTarget.id}&a_type=${a_type}`, 0)
    }

  }
})