const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

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
  createTeam() {
    let that = this
    let aId = this.data.aId
    let aType = this.data.aType
    console.log(that.data)
    wx.request({
      url: `${baseUrl}/api/team/add`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: that.data,
      success(res) {
        if (util.checkSuccess(res)) {
          let tId = res.data.obj.tId
          util.route(`/pages/page_team/detail/detail?aId=${aId}&tId=${tId}`, 1, 1)
          wx.showToast({
            title: '创建成功',
          })
        }
      },
      fail() {util.fail()}
    })
  }
})
