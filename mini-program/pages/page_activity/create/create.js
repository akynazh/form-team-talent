const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    a_name: "",
    a_desc: "",
    a_end_date: "",
    a_is_public: 0,
    current_date: ""
  },
  bindDateChange(e) {
    this.setData({
      a_end_date: e.detail.value + ' ' + util.currentTime_2()
    })
  },
  publicChange(e) {
    if (e.detail.value[0] === undefined) {
      this.setData({
        a_is_public: 0
      })
    } else {
      this.setData({
        a_is_public: 1
      })
    }
  },
  create_activity() {
    let that = this
    wx.request({
      url: `${base_url}/api/activity/add`,
      header: util.get_auth_header(),
      method: 'POST',
      data: that.data,
      success(res) {
        wx.showToast({
          title: '创建成功',
        })
        if (util.check_success(res)) {
          util.route("/pages/page_activity/activity/personal/personal")
        }
      },
      fail(res) {
        util.fail()
      }
    })
  },
})
