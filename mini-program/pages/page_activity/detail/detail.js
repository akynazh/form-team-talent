const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    activity: {},
    activeNames: [],
  },
  onActiveDescChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  onLoad(params) {
    let that = this
    wx.request({
      url: `${baseUrl}/api/activity/get/id?a_id=${params.a_id}`,
      success(res) {
        if (util.checkSuccess(res)) {
          that.setData({
            a_id: params.a_id,
            a_type: params.a_type,
            activity: res.data.obj
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  removeActivity() {
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    wx.showModal({
      title: '删除活动',
      content: '确认删除？',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/activity/remove?a_id=${a_id}`,
            header: util.getAuthHeader(),
            method: 'POST',
            success(res) {
              if (util.checkSuccess(res)) {
                util.route(`/pages/page_activity/activity/activity?a_id=${a_id}&a_type=${a_type}`)
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
    let a_type = this.data.a_type
    let a_id = this.data.a_id
    let activity = JSON.stringify(this.data.activity)
    util.route(`/pages/page_activity/update/update?a_id=${a_id}&a_type=${a_type}&activity=${activity}`)
  },
  showTeam() {
    let a_type = this.data.a_type
    let a_id = this.data.a_id
    util.route(`/pages/page_team/team/team?a_id=${a_id}&a_type=${a_type}`, 0)
  },
  createTeam() {
    let a_type = this.data.a_type
    let a_id = this.data.a_id
    util.route(`/pages/page_team/create/create?a_id=${a_id}&a_type=${a_type}`)
  }
})