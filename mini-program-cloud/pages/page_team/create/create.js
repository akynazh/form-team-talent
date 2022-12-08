const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    aId: "",
    aType: "",
    tName: "",
    tDesc: "",
    tTotal: 2
  },
  onTotalChange(event) {
    this.setData({
      tTotal: event.detail
    })
    console.log(this.data.tTotal)
  },
  onLoad(params) {
    this.setData({
      aId: params.aId,
      aType: params.aType
    })
  },
  async createTeam() {
    let that = this
    let aId = this.data.aId
    let aType = this.data.aType
    const res = await app.call({
      url: `/api/team/add`,
      method: 'POST',
      data: that.data,
    })
    console.log(res)
    if (util.checkSuccess(res)) {
      let tId = res.data.obj.tId
      util.route(`/pages/page_team/detail/detail?aId=${aId}&tId=${tId}`, 1, 1)
      wx.showToast({
        title: '创建成功',
      })
    }
  }
})
