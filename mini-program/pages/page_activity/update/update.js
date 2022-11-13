const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",

    a_name: "",
    a_desc: "",
    a_end_date: "",
    a_is_public: 0,
  },
  onLoad(params) {
    let that = this
    let a_id = params.a_id
    let a_type = params.a_type
    wx.request({
      url: `${baseUrl}/api/activity/get/id?a_id=${a_id}`,
      header: util.getAuthHeader(),
      success(res) {
        console.log(res)
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          console.log(obj)
          that.setData({
            a_id: a_id,
            a_type: a_type,
            a_name: obj.a_name,
            a_desc: obj.a_desc,
            a_end_date: obj.a_end_date,
            a_is_public: obj.a_is_public
          })
        }
      },
      fail() {
        util.fail()
      }
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
  updateActivity() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/activity/update`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          wx.showToast({
            title: '操作成功',
          })
          util.route("/pages/page_activity/activity/personal/personal")
        }
      },
      fail(res) {
        util.fail()
      }
    })

  },
})
