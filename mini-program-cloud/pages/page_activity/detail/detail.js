const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    aId: "",
    aType: "",
    aEndDateShow: "",
    activity: {},
    activeNames: [],
    owner: false,
  },
  onActiveDescChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  async onLoad(params) {
    let that = this
    const res = await app.call({
      url: `/api/activity/get/id?aId=${params.aId}`,
    })
    if (util.checkSuccess(res)) {
      let activity = res.data.obj.activity
      let owner = res.data.obj.owner
      console.log(owner)
      that.setData({
        aId: params.aId,
        aType: params.aType,
        aEndDateShow: util.getFormatTimeByMillis(activity.aEndDate),
        activity: activity,
        owner: owner
      })
    }
  },
  async sendRemoveActivity(aId, aType) {
    const res = await app.call({
      url: `/api/activity/remove?aId=${aId}`,
      method: 'POST',
    })
    if (util.checkSuccess(res)) {
      util.route(`/pages/page_activity/activity/activity?aType=${aType}`, 1, 1)
      wx.showToast({
        title: '操作成功',
      })
    }
  },
  async removeActivity() {
    let aId = this.data.aId
    let aType = this.data.aType
    let that = this
    wx.showModal({
      title: '删除活动',
      content: '确认删除？',
      success(res) {
        if (res.confirm) {
          that.sendRemoveActivity(aId, aType)
        }
      }
    })
  },
  updateActivity() {
    let aType = this.data.aType
    let aId = this.data.aId
    let activity = JSON.stringify(this.data.activity)
    util.route(`/pages/page_activity/update/update?aId=${aId}&aType=${aType}&activity=${activity}`)
  },
  showTeam() {
    let aType = this.data.aType
    let aId = this.data.aId
    util.route(`/pages/page_team/team/team?aId=${aId}&aType=${aType}`)
  },
  createTeam() {
    let aType = this.data.aType
    let aId = this.data.aId
    util.route(`/pages/page_team/create/create?aId=${aId}&aType=${aType}`)
  }
})