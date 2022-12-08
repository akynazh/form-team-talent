const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    aId: "",
    aType: "",
    tId: "",
    tName: "",
    tDesc: "",
    tTotal: "",
    manage: ""
  },
  onTotalChange(event) {
    this.setData({
      tTotal: event.detail
    })
  },
  onLoad(params) {
    let team = JSON.parse(params.team)
    this.setData({
      aId: params.aId,
      aType: params.aType,
      tId: params.tId,
      tName: team.tName,
      tDesc: team.tDesc,
      tTotal: team.tTotal,
      manage: params.manage
    })
  },
  async updateTeam() {
    let aId = this.data.aId
    let aType = this.data.aType
    let tId = this.data.tId
    let manage = this.data.manage
    let that = this
    const res = await app.call({
      url: `/api/team/update`,
      method: 'POST',
      data: that.data,
    })
    if (util.checkSuccess(res)) {
      util.route(`/pages/page_team/detail/detail?aId=${aId}&tId=${tId}&aType=${aType}&manage=${manage}`, 1, 1)
      wx.showToast({
        title: '操作成功',
      })
    }
  }
})