// pages/activity_page/activity_page.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    public_activity_number:4,
    non_public_activity_number:1,
    activity:[
      {"name" :"微信小程序张老师",
      "isOpen" : 1,
       "activity_id" : "352222",},
       {"name" :"卡五星李老师",
        "isOpen" : 1,
       "activity_id" : "844865",},
       {"name" :"思政王老师",
       "isOpen" : 1,
       "activity_id" : "56456222",},
       {"name" :"演讲牛老师",
        "isOpen" : 0,
       "activity_id" : "3742",},
       {"name" :"吹牛逼宋老师",
       "isOpen" : 1,
       "activity_id" : "3422",},
    ]
  },
  ToActivityCreat(){
    wx.navigateTo({
      url: '/pages/activity_creat/activity_creat',
    })
  },
  ScanAndScan(){
    /**(未做）扫码进入对应activity_detail界面) */
    /**二维码内容可以为activity_id */
  },
  ToActivityDetail(e){
    var activity= e.currentTarget.dataset.activity;
    //console.log(activity.activity_id)
    /**（未做）跳转到activity_id对应activity_detail界面 */
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