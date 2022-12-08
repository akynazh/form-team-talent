const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    activities: [],
    aType: "",
    url: ""
  },
  onShow() {

  },
  onLoad(params) {
    let aType = params.aType
    let url = `${baseUrl}/api/activity/get/pub`
    if (aType == 1) url = `${baseUrl}/api/activity/get/my`
    this.setData({
      aType: aType,
      url: url
    })
  },
  onShow() {
    let that = this
    wx.request({
      url: that.data.url,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          for (let a of obj) {
            if (a.aEndDate < new Date().getTime()) {
              a.aName = a.aName + "(已经结束)"
            }
          }
          that.setData({
            activities: obj
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  toActivityDetail(e) {
    let aType = this.data.aType
    util.route(`/pages/page_activity/detail/detail?aId=${e.currentTarget.id}&aType=${aType}`)
  }
})