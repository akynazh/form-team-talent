const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Component({
  data: {
  },
  properties: {
    active: {
      type: String
    }
  },
  methods: {
    nav_to_page(e) {
      let page_name = e.detail
      this.setData({
        active: e.detail
      })
      if (page_name == "home") {
        util.route("/pages/page_home/page_home", 0)
      }
      else if (page_name == "team") {
        util.route("/pages/page_team/team/personal/personal", 0)
      }
      else if (page_name == "me") {
        util.route("/pages/page_me/page_me")
      }
    }
  },
  lifetimes: {
    attached() {
      this.setData({
        active: this.properties.active
      })
    }
  }
})
