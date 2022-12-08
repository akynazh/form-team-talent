const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    teams: [],
    aId: '',
    aType: ''
  },
  async onLoad(params) {
    let aId = params.aId
    let aType = params.aType
    this.setData({
      aId: aId,
      aType: aType,
    })
    let that = this
    const res = await app.call({
      url: `/api/team/get/byAId?aId=${aId}`,
    })
    if (util.checkSuccess(res)) {
      that.setData({
        teams: res.data.obj,
      })
    }
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