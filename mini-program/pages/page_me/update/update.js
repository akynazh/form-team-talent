const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    uName: "",
    uSex: "",
    uSchool: "",
    uStuNum: "",
    uMajor: "",
    sexColumns: ["男", "女", "其它"]
  },
  onSexChange(event) {
    this.setData({
      uSex: event.detail
    })
  },
  onLoad(params) {
    let info = JSON.parse(params.info)
    let user = info.user
    this.setData({
      uName: user.uName,
      uSex: user.uSex,
      uSchool: user.uSchool,
      uStuNum: user.uStuNum,
      uMajor: user.uMajor
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