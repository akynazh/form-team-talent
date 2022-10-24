// pages/me_page/me_page.js
const app = getApp()
const {toMePage, toHomePage, toActivityPage} = require('../../utils/nav')

Page({
	toMePage,
	toHomePage,
	toActivityPage,
  data: {
    me_avatar: null,/**云端储存头像地址 */
    me_name:"wangfeiy",
    /**（未做）建议这里默认值设置成微信头像和微信昵称 */
  },
  /**修改个人信息跳转 */
  ToPersonInformation(){
    wx.navigateTo({
      url: '/pages/person_information/person_information',
    })
  },
})