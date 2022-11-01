const app = getApp()
const base_url = app.globalData.base_url;
import * as util from "../../utils/util";

Page({
  data: {

  },
  to_create_activity() {
    util.route("/pages/page_activity/create/create");
  },

  view_public_activity() {
    util.route("/pages/page_activity/public/public")
  },
  
  scan_qrcode() {
    console.log("scan qrcode")
  }
  
})