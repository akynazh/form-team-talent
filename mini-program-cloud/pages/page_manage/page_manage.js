const app = getApp()
let util = require("../../utils/util")

Page({
  data: {

  },
  viewMyActivity() {
    util.route("/pages/page_activity/activity/activity?aType=1")
  },
  viewMyTeam() {
    util.route("/pages/page_team/team/personal/personal")
  },
  viewFromMeReq() {
    util.route("/pages/page_request/page_request?type=0")
  },
  viewToMeReq() {
    util.route("/pages/page_request/page_request?type=1")
  }
})