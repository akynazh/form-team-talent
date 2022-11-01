const app = getApp()
const base_url = app.globalData.base_url;
import * as util from "../../../utils/util";

Component({
  properties: {},
  data: {},
  methods: {
    nav_to_page(e) {
      let page_url = "";
      let idx = e.currentTarget.id;
      if (idx == "1") page_url = "/pages/page_home/page_home";
      else if (idx == "2") page_url = "/pages/page_activity/activity/personal/personal";
      else if (idx == "3") page_url = "/pages/page_me/page_me";
      util.route(page_url);
    }
  }
})
