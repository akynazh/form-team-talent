const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Component({
  properties: {},
  data: {},
  methods: {
    nav_to_page(e) {
      let page_url = ""
      let idx = e.currentTarget.id
      if (idx == "1") {
        util.route("/pages/page_home/page_home", 0)
      }
      else if (idx == "2") {
        util.route("/pages/page_activity/activity/activity", 0)
      }
      else if (idx == "3") {
        util.route("/pages/page_me/page_me")
      }
    }
  }
})
