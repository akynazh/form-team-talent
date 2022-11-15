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
    let that = this
    wx.request({
      url: `${baseUrl}/api/req/get?type=${type}`,
      header: util.getAuthHeader(),
      success(res) {
        if (util.checkSuccess(res)) {
          that.setData({
            requests: res.data.obj,
            type: type
          })
        }
      },
      fail() {util.fail()}
    })
  },
  removeRequest(e) {
    let that = this;
    let id = e.currentTarget.dataset.id
  }
})