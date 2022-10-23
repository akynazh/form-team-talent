// pages/me_page/me_page.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
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
  /**底部导航栏跳转界面，未授权登录则跳转授权界面 授权后返回 */
  ToHomePage(){
    if(app.globalData.userInfo == null){
      wx.navigateTo({
        url: '/pages/auth/auth',
      })
    }else {
      wx.navigateTo({
        url: '/pages/home_page/home_page',
      })
    }
  },
  ToActivityPage(){
    if(app.globalData.userInfo == null){
      wx.navigateTo({
        url: '/pages/auth/auth',
      })
    }else {
      wx.navigateTo({
        url: '/pages/activity_page/activity_page',
      })
    }
  },
  ToMePage(){
    if(app.globalData.userInfo == null){
      wx.navigateTo({
        url: '/pages/auth/auth',
      })
    }else {
      wx.navigateTo({
        url: '/pages/me_page/me_page',
      })
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})