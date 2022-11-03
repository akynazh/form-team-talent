const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../utils/util"

Page({
	data: {
		u_name: "dsaf",
		u_stu_num: "",
		u_school: "sdf"
	},
	onLoad() {
    let that = this
    wx.request({
      url: `${base_url}/api/user/get/info`,
      header: util.get_auth_header(),
      success(res) {
        if (util.check_success(res)) {
          let obj = res.data.obj
          that.setData({
            u_name: obj.u_name,
            u_stu_num: obj.u_stu_num,
            u_school: obj.u_school
          })
        }
      },
      fail(res) {
        alert_fail(title="请求失败，请重试")
      }
    })
	},
	update_info() {
    let that = this
    wx.request({
      url: `${base_url}/api/user/update`,
      header: util.get_auth_header(),
      method: "POST",
      data: {
        u_name: that.data.u_name,
        u_stu_num: that.data.u_stu_num,
        u_school: that.data.u_school
      },
      success(res) {
        if (util.check_success(res)) {
          wx.showToast({
            title: '更新成功',
          })
        }
      },
      fail(res) {
        util.fail()
      }
    })
	}
})
