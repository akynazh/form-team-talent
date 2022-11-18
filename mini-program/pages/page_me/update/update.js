const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
  },
  onLoad(params) {
    let info = JSON.parse(params.info)
    this.setData({
      u_name: info.u_name,
      u_stu_num: info.u_stu_num,
      u_school: info.u_school
    })
    console.log(this.data)
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