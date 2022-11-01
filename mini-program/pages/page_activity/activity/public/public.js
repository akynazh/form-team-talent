const app = getApp()
const base_url = app.globalData.base_url;
import * as util from "../../../../utils/util";

Page({
  data: {
    activities: []
  },
  onLoad() {
    let that = this;
    wx.request({
      url: `${base_url}/api/activity/get/pub`,
      header: util.get_auth_header(),
      success(res) {
        if (util.check_success(res)) {
          that.setData({
            activities: res.data.obj
          })
        }
      },
      fail(res) {
        util.fail();
      }
    })
  }
})