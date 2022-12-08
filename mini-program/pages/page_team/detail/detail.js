const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    aId: "",
    aType: "",
    tId: "",
    manage: "",
    teamLeaderName: "",
    content: "",
    reqHidden: true,
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
    console.log(params)
    let aId = params.aId
    let aType = params.aType
    let tId = params.tId
    let manage = params.manage || 0
    this.setData({
      aId: aId,
      aType: aType,
      tId: tId,
      manage: manage
    })
    let that = this
    wx.request({
      url: `${baseUrl}/api/team/get/id?tId=${tId}`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          let obj = res.data.obj
          that.setData({
            team: obj.team,
            members: obj.members,
            teamLeaderName: obj.teamLeaderName,
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
    let aId = this.data.aId
    let aType = this.data.aType
    let tId = this.data.tId
    let manage = this.data.manage
    wx.showModal({
      title: "解散小组",
      content: "确定解散？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/team/remove?tId=${tId}`,
            method: 'POST',
            header: util.getAuthHeader(),
            success(res) {
              if (util.checkSuccess(res)) {
                if (manage == 0) {
                  util.route(`/pages/page_team/team/team?aId=${aId}&aType=${aType}`, 1, 1)
                } else if (manage == 1) {
                  util.route('/pages/page_team/team/personal/personal', 1, 1)
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
    let teamJson = JSON.stringify(this.data.team)
    let aId = this.data.aId
    let aType = this.data.aType
    let tId = this.data.tId
    let manage = this.data.manage
    util.route(`/pages/page_team/update/update?aId=${aId}&aType=${aType}&tId=${tId}&team=${teamJson}&manage=${manage}`)
  },
  joinTeam() {
    this.setData({
      reqHidden: false
    })
  },
  cancelSend() {
    this.setData({
      reqHidden: true
    })
  },
  sendReqJoinTeam() {
    this.setData({
      reqHidden: true
    })
    let that = this
    wx.request({
      url: `${baseUrl}/api/req/send`,
      header: util.getAuthHeader(),
      method: 'POST',
      data: {
        aId: that.data.aId,
        tId: that.data.tId,
        content: that.data.content,
      },
      fail() { 
        util.fail() 
      },
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