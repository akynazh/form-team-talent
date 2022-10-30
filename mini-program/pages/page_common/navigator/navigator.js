const app = getApp()
const {auth} = require("../../../utils/auth")

// pages/page_common/navigator.js
Component({
  properties: {},
  data: {},
  methods: {
    nav_to_page(e) {
      console.log(e)
      let page_url = "";
      let idx = e.currentTarget.id
      if (idx == "1") page_url = "/pages/page_home/page_home";
      else if (idx == "2") page_url = "/pages/page_activity/activity/activity";
      else if (idx == "3") page_url = "/pages/page_me/page_me";
      if (idx != "1" && !app.globalData.islogin) {
        auth(page_url)
        wx.navigateTo({
          url: page_url,
        })
      } else {
        wx.redirectTo({
          url: page_url,
        });
      }
    }
  }
})
