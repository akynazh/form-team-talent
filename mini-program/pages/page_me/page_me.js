const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

Page({
	data: {
		u_name: "",
		u_stu_num: "",
    u_school: "",
    avatarUrl: ""
	},
	onLoad() {
    this.setData({
      avatarUrl: wx.getStorageSync('avatarUrl') || ''
    })
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
      fail() {
        util.fail()
      }
    })
  },
  toUpdatePage() {
    let info = JSON.stringify(this.data)
    util.route(`/pages/page_me/update/update?info=${info}`)
  }
})
