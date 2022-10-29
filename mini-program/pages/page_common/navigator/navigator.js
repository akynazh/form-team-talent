const {route} = require("../../../utils/route")

Component({
  properties: {},
  data: {},
  methods: {
    nav_to_page(e) {
      let page_url = "";
      let idx = e.currentTarget.id;
      if (idx == "1") page_url = "/pages/page_home/page_home";
      else if (idx == "2") page_url = "/pages/page_activity/activity/activity";
      else if (idx == "3") page_url = "/pages/page_me/page_me";
      route(page_url);
    }
  }
})
