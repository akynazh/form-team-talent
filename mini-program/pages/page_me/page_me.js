const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

Page({
	data: {
    user: {},
    avatarUrl: "",
    sexShow: ""
	},
	onLoad() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/user/get/info`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let user = res.data.obj
          let sexShow = "其它"
          if (user.u_sex == "female") sexShow = "女"
          else if (user.u_sex == "male") sexShow = "男"
          that.setData({
            user: user,
            avatarUrl: `/images/${user.u_sex}.png`,
            sexShow: sexShow
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
