const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    t_id: "",
    team_leader_name: "",
    team: {},
    members: {}
  },
  onActiveDescChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  onLoad(params) {
    let a_id = params.a_id
    let a_type = params.a_type
    let t_id = params.t_id
    this.setData({
      a_id: a_id,
      a_type: a_type,
      t_id: t_id
    })
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/id?t_id=${t_id}`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          that.setData({
            team: obj.team,
            members: obj.members,
            team_leader_name: obj.team_leader_name
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  removeTeam() {
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    let t_id = this.data.t_id
    wx.showModal({
      title: "删除小组",
      content: "确定删除？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/team/remove?t_id=${t_id}`,
            method: 'POST',
            header: util.getAuthHeader(),
            success(res) {
              if (util.checkSuccess(res)) {
                util.route(`/pages/page_team/team/team?a_id=${a_id}&a_type=${a_type}`)
                wx.showToast({
                  title: '操作成功',
                })
              }
            },
            fail() {util.fail()}
          })
        }
      }
    })
  },
  updateTeam() {
    let team_json = JSON.stringify(this.data.team)
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    let t_id = this.data.t_id
    util.route(`/pages/page_team/update/update?a_id=${a_id}&a_type=${a_type}&t_id=${t_id}&team=${team_json}`)
  },
  joinTeam() {
    let t_id = this.data.t_id
    let a_id = this.data.a_id
    wx.showModal({
      title: "加入小组",
      content: "确认加入？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/user/join/team?a_id=${a_id}&t_id=${t_id}`,
            header: util.getAuthHeader(),
            method: 'POST',
            fail() {util.fail()},
            success(res) {
              if (util.checkSuccess(res)) {
                util.route(`/pages/page_activity/activity/personal/personal`)
                wx.showToast({
                  title: '操作成功',
                })
              }
            }
          })
        }
      }
    })
  }
})