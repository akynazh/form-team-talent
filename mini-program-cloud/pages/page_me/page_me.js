const app = getApp()
let util = require("../../utils/util")

Page({
  data: {
    user: {},
    avatarUrl: "",
    sexShow: "",
    isLogin: false
  },
  async sendGetInfo() {
    const res = await app.call({
      url: `/api/user/get/info`,
    })
    if (util.checkSuccess(res)) {
      let user = res.data.obj
      let sexShow = "其它"
      if (user.uSex == "female") sexShow = "女"
      else if (user.uSex == "male") sexShow = "男"
      this.setData({
        user: user,
        avatarUrl: `/images/${user.uSex}.png`,
        sexShow: sexShow,
        isLogin: true
      })
    }
  },
  async onLoad() {
    let that = this
    if (util.getToken() != '') {
      util.checkToken().then(
        () => {
          that.sendGetInfo()
        },
        () => {
          that.setData({
            isLogin: false
          })
        }
      )
    } else {
      that.setData({
        isLogin: false
      })
    }
  },
  login() {
    util.auth().then(
      () => {
        this.setData({
          isLogin: true
        })
      },
      () => {
        this.setData({
          isLogin: false
        })
      }
    )
    
  },
  toUpdatePage() {
    let info = JSON.stringify(this.data)
    util.route(`/pages/page_me/update/update?info=${info}`)
  }
})
