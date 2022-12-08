const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    activities: [],
    aType: "",
    url: ""
  },
  onLoad(params) {
    let aType = params.aType
    let url = `/api/activity/get/pub`
    if (aType == 1) url = `/api/activity/get/my`
    this.setData({
      aType: aType,
      url: url
    })
  },
  async onShow() {
    let that = this
    const res = await app.call({
      url: that.data.url,
    })
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
  toActivityDetail(e) {
    let aType = this.data.aType
    util.route(`/pages/page_activity/detail/detail?aId=${e.currentTarget.id}&aType=${aType}`)
  }
})