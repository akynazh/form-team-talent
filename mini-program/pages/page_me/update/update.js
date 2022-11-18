const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    u_name: "",
    u_sex: "",
    u_school: "",
    u_stu_num: "",
    u_major: "",
    sexColumns: ["男", "女", "其它"]
  },
  onSexChange(event) {
    this.setData({
      u_sex: event.detail
    })
  },
  onLoad(params) {
    let info = JSON.parse(params.info)
    let user = info.user
    this.setData({
      u_name: user.u_name,
      u_sex: user.u_sex,
      u_school: user.u_school,
      u_stu_num: user.u_stu_num,
      u_major: user.u_major
    })
  },
  updateInfo() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/user/update`,
      header: util.getAuthHeader(),
      method: "POST",
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          util.route('/pages/page_me/page_me', 1, 1)
          wx.showToast({
            title: '更新成功',
          })
        }
      },
      fail() {
        util.fail()
      }
    })
	}
})