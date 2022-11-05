const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    team: {},
    team_leader_name: "",
    a_id: "",
    t_id: "",
    members: {}
  },
  onLoad(params) {
    this.data.a_id = params.a_id
    this.data.t_id = params.t_id
    let that = this
    wx.request({
      url: `${base_url}/api/team/get/id?t_id=${that.data.t_id}`,
      header: util.get_auth_header(),
      success(res) {
        console.log(res)
        if (util.check_success(res)) {
          let obj = res.data.obj
          that.setData({
            team: obj.team,
            team_leader_name: obj.team_leader_name,
            a_id: that.data.a_id,
            t_id: that.data.t_id,
            members: obj.members
          })
        }
      },
      fail() {
        util.fail()
      }
    })
  },
  remove_team() {
    let a_id = this.data.a_id
    let t_id = this.data.t_id
    wx.showModal({
      title: "删除小组",
      content: "确定删除？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${base_url}/api/team/remove?t_id=${t_id}`,
            method: 'POST',
            header: util.get_auth_header(),
            success(res) {
              if (util.check_success(res)) {
                wx.showToast({
                  title: '操作成功',
                })
                util.route(`/pages/page_team/team/team?a_id=${a_id}`)
              }
            },
            fail() {util.fail()}
          })
        }
      }
    })
  },
  update_team() {
    let team_json = JSON.stringify(this.data.team)
    let a_id = this.data.a_id
    let t_id = this.data.t_id
    util.route(`/pages/page_team/update/update?a_id=${a_id}&t_id=${t_id}&team=${team_json}`)
  },
  join_team() {
    let t_id = this.data.t_id
    let a_id = this.data.a_id
    wx.showModal({
      title: "加入小组",
      content: "确认加入？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${base_url}/api/user/join/team?a_id=${a_id}&t_id=${t_id}`,
            header: util.get_auth_header(),
            method: 'POST',
            fail() {util.fail()},
            success(res) {
              if (util.check_success(res)) {
                wx.showToast({
                  title: '操作成功',
                })
                util.route(`/pages/page_activity/activity/personal/personal`)
              }
            }
          })
        }
      }
    })
  }
})