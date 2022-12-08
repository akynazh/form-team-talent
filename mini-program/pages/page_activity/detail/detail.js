const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    aId: "",
    aType: "",
    aEndDateShow: "",
    activity: {},
    activeNames: [],
    owner: false,
    baseUrl: app.globalData.baseUrl
  },
  onActiveDescChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  onLoad(params) {
    let that = this
    wx.request({
      url: `${baseUrl}/api/activity/get/id?aId=${params.aId}`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          console.log(res)
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
      fail() {
        util.fail()
      }
    })
  },
  removeActivity() {
    let aId = this.data.aId
    let aType = this.data.aType
    wx.showModal({
      title: '删除活动',
      content: '确认删除？',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/activity/remove?aId=${aId}`,
            header: util.getAuthHeader(),
            method: 'POST',
            success(res) {
              if (util.checkSuccess(res)) {
                util.route(`/pages/page_activity/activity/activity?aType=${aType}`, 1, 1)
                wx.showToast({
                  title: '操作成功',
                })
              }
            },
            fail() {
              util.fail()
            }
          })
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