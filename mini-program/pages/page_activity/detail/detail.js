const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    activity: {
      a_id: "",
      a_name: "",
      a_desc: "",
      a_end_date: "",
      a_is_public: "",
      a_qrcode_path: ""
    },
  },
  onLoad(params) {
    let that = this
    wx.request({
      url: `${base_url}/api/activity/get/id?a_id=${params.a_id}`,
      success(res) {
        if (util.check_success(res)) {
          console.log(res)
          that.setData({
            activity: res.data.obj
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  remove_activity() {
    let that = this;
    wx.showModal({
      title: '删除活动',
      content: '确认删除？',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${base_url}/api/activity/remove?a_id=${that.data.activity.a_id}`,
            header: util.get_auth_header(),
            method: 'POST',
            success(res) {
              if (util.check_success(res)) {
                util.route("/pages/page_activity/activity/personal/personal")
                wx.showToast({
                  title: '操作成功',
                })
              }
            },
            fail() {
              util.fail()
            }
          })
        }
      }
    })
  },
  update_activity() {
    util.route(`/pages/page_activity/update/update?a_id=${this.data.activity.a_id}`, 0)
  },
  show_team() {
    let a_id = this.data.activity.a_id
    util.route(`/pages/page_team/team/team?a_id=${a_id}`, 0)
  },
})