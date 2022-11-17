const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    t_id: "",
    manage: "",
    team_leader_name: "",
    content: "",
    req_hidden: true,
    team: {},
    members: {},
    owner: false
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
    let manage = params.manage || 0
    this.setData({
      a_id: a_id,
      a_type: a_type,
      t_id: t_id,
      manage: manage
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
            team_leader_name: obj.team_leader_name,
            owner: obj.owner
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
    let manage = this.data.manage
    wx.showModal({
      title: "解散小组",
      content: "确定解散？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/team/remove?t_id=${t_id}`,
            method: 'POST',
            header: util.getAuthHeader(),
            success(res) {
              if (util.checkSuccess(res)) {
                if (manage == 0) {
                  util.route(`/pages/page_team/team/team?a_id=${a_id}&a_type=${a_type}`)
                } else if (manage == 1) {
                  util.route('/pages/page_team/team/personal/personal')
                }
                wx.showToast({
                  title: '操作成功',
                })
              }
            },
            fail() { util.fail() }
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
    let manage = this.data.manage
    util.route(`/pages/page_team/update/update?a_id=${a_id}&a_type=${a_type}&t_id=${t_id}&team=${team_json}&manage=${manage}`)
  },
  joinTeam() {
    this.setData({
      req_hidden: false
    })
  },
  cancelSend() {
    this.setData({
      req_hidden: true
    })
  },
  sendReqJoinTeam() {
    let that = this
    wx.request({
      url: `${baseUrl}/api/req/send`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: {
        a_id: that.data.a_id,
        t_id: that.data.t_id,
        content: that.data.content,
      },
      fail() { util.fail() },
      success(res) {
        if (util.checkSuccess(res)) {
          util.route(`/pages/page_request/page_request?type=0`)
          wx.showToast({
            title: '操作成功',
          })
        }
      }
    })
  }
})