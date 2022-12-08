const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    teams: [],
    aId: '',
    aType: ''
  },
  onLoad(params) {
    let aId = params.aId
    let aType = params.aType
    this.setData({
      aId: aId,
      aType: aType,
    })
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/byAId?aId=${aId}`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          that.setData({
            teams: res.data.obj,
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  createTeam() {
    let aType = this.data.aType
    let aId = this.data.aId
    util.route(`/pages/page_team/create/create?aId=${aId}&aType=${aType}`)
  },
  toTeamDetail(e) {
    let aType = this.data.aType
    let aId = this.data.aId
    util.route(`/pages/page_team/detail/detail?tId=${e.currentTarget.id}&aId=${aId}&aType=${aType}`)
  }
})