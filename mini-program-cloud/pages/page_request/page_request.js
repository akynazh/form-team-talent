const app = getApp()
let util = require("../../utils/util")

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
  async onLoad(params) {
    let type = params.type
    let status = params.status !== undefined ? status : 2
    let that = this
    const res = await app.call({
      url: `/api/req/get?type=${type}&status=${status}`,
    })
    let reqs = res.data.obj
    for (let req of reqs) {
      req.sendDate = util.getFormatTimeByMillis(req.sendDate)
    }
    if (util.checkSuccess(res)) {
      that.setData({
        requests: reqs,
        type: type
      })
    }
  },
  async sendRemoveRequest(id) {
    const res = await app.call({
      url: `/api/req/remove/${id}`,
      method: "POST",
    })
    if (util.checkSuccess(res)) {
      util.route("/pages/page_request/page_request?type=0", 1, 1)
      wx.showToast({
        title: '操作成功',
      })
    }
  },
  async removeRequest(e) {
    let id = e.currentTarget.dataset.id
    let that = this
    wx.showModal({
      title: "撤销请求",
      content: "确认撤销？",
      success(res) {
        if (res.confirm) {
          that.sendRemoveRequest(id)
        }
      }
    })
  },
  async sendHandleRequest(id, agree) {
    const res = await app.call({
      url: `/api/req/handle?id=${id}&agree=${agree}`,
      method: "POST",
    })
    if (util.checkSuccess(res)) {
      util.route("/pages/page_request/page_request?type=1", 1, 1)
      wx.showToast({
        title: '操作成功',
      })
    }
  },
  async handleRequest(e) {
    let id = e.currentTarget.dataset.id
    let agree = e.currentTarget.dataset.agree
    let that = this
    wx.showModal({
      title: "处理请求",
      content: agree == 1 ? "确认同意该用户加入吗？" : "确认拒绝该用户加入吗？",
      success(res) {
        if (res.confirm) {
          that.sendHandleRequest(id, agree)
        }
      }
    })
  }
})