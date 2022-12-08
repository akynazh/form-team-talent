const app = getApp()
let util = require("../../../../utils/util")

Page({
  data: {
    teams: [],
  },
  async onLoad() {
    let that = this
    const res = await app.call({
      url: `/api/team/get/my`,
    })
    if (util.checkSuccess(res)) {
      that.setData({
        teams: res.data.obj,
      })
    }
  },
  toTeamDetail(e) {
    let aId = e.currentTarget.dataset.aid
    let tId = e.currentTarget.dataset.tid
    util.route(`/pages/page_team/detail/detail?tId=${tId}&aId=${aId}&aType=1&manage=1`)
  }
})