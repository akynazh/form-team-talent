const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

Page({
  data: {
    requests: [],
    type: "",
    activeNames: []
  },
  onActiveDescChange(event) {
    this.setData({
      activeNames: event.detail,
    });
  },
  onLoad(params) {
    let type = params.type
    let status = params.status !== undefined ? status : 2
    let that = this
    wx.request({
      url: `${baseUrl}/api/req/get?type=${type}&status=${status}`,
      header: util.getAuthHeader(),
      success(res) {
        let reqs = res.data.obj
        for (let req of reqs) {
          req.send_date = util.getFormatTimeByMillis(req.send_date)
        }
        if (util.checkSuccess(res)) {
          that.setData({
            requests: reqs,
            type: type
          })
        }
      },
      fail() {util.fail()}
    })
  },
  removeRequest(e) {
    let id = e.currentTarget.dataset.id
    console.log(id)
    wx.showModal({
      title: "撤销请求",
      content: "确认撤销？",
      success(res) {
        let that = this
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/req/remove/${id}`,
            method: "POST",
            header: util.getAuthHeader(),
            fail() {util.fail()},
            success(res) {
              if (util.checkSuccess(res)) {
                util.route("/pages/page_request/page_request?type=0", 1, 1)
                wx.showToast({
                  title: '操作成功',
                })
              }
            }
          })
        }
      }
    })
  },
  handleRequest(e) {
    let id = e.currentTarget.dataset.id
    let agree = e.currentTarget.dataset.agree
    wx.showModal({
      title: "处理请求",
      content: agree == 1 ? "确认同意该用户加入吗？" : "确认拒绝该用户加入吗？",
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/req/handle?id=${id}&agree=${agree}`,
            method: "POST",
            header: util.getAuthHeader(),
            fail() {util.fail()},
            success(res) {
              if (util.checkSuccess(res)) {
                util.route("/pages/page_request/page_request?type=1", 1, 1)
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