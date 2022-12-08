const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../utils/util"

Page({
  data: {
    user: {},
    avatarUrl: "",
    sexShow: "",
    isLogin: false
  },
  onLoad() {
    let that = this
    if (util.getToken() != '') {
      util.checkToken().then(
        () => { // 进行登录
          wx.request({
            url: `${baseUrl}/api/user/get/info`,
            header: util.getAuthHeader(),
            success(res) {
              if (util.checkSuccess(res)) {
                let user = res.data.obj
                let sexShow = "其它"
                if (user.uSex == "female") sexShow = "女"
                else if (user.uSex == "male") sexShow = "男"
                that.setData({
                  user: user,
                  avatarUrl: `/images/${user.uSex}.png`,
                  sexShow: sexShow,
                  isLogin: true
                })
              }
            },
            fail() {
              util.fail()
            }
          })
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
