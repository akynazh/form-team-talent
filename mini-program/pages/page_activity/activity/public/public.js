const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../../utils/util"

Page({
  data: {
    activities: []
  },
  onLoad() {
    let that = this
    wx.request({
      url: `${base_url}/api/activity/get/pub`,
      success(res) {
        if (util.check_success(res)) {
          let obj = res.data.obj
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
    util.route(`../../detail/detail?id=${e.currentTarget.id}`, 0)
  }
})