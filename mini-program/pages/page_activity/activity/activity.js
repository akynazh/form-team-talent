const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  to_personal_activity() {
    util.route('./personal/personal')
  },
  to_public_activity() {
    util.route('./public/public', 0)
  }
})