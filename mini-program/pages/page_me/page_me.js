const app = getApp()
const baseUrl = app.globalData.baseUrl
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
      url: `${baseUrl}/api/user/get/info`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          that.setData({
            u_name: obj.u_name,
            u_stu_num: obj.u_stu_num,
            u_school: obj.u_school
          })
        }
      },
      fail(res) {
        util.alertFail(title="请求失败，请重试")
      }
    })
	},
	updateInfo() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/user/update`,
      header: util.getAuthHeader(),
      method: "POST",
      data: {
        u_name: that.data.u_name,
        u_stu_num: that.data.u_stu_num,
        u_school: that.data.u_school
      },
      success(res) {
        if (util.checkSuccess(res)) {
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
