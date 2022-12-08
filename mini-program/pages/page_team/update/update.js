const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

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
  updateTeam() {
    let aId = this.data.aId
    let aType = this.data.aType
    let tId = this.data.tId
    let manage = this.data.manage
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/update`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          util.route(`/pages/page_team/detail/detail?aId=${aId}&tId=${tId}&aType=${aType}&manage=${manage}`, 1, 1)
          wx.showToast({
            title: '操作成功',
          })
        }
      },
      fail() {util.fail()}
    })
  }
})