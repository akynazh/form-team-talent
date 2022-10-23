// pages/home_page/home_page.js
const app = getApp()
Page({
  
  /**
   * 页面的初始数据
   */
  data: {
    my_task_number:4,/**显示任务栏条件 */
    my_task:[{/*当前任务框内显示的*/
      "name" : "制作数据库",
      "group_id": "115222",}, 
      {"name" :"制作ppt",
       "group_id" : "352222",},
      {"name" :"开发前端",
       "group_id" : "1555445",},
      {"name" :"摆烂",
       "group_id" : "441445",}]/**通过group——id（group创建时生成）建立连接 */
  },
  
  ToGroupDetail(e){
    var task = e.currentTarget.dataset.task;
    //console.log(task.group_id);
    /**（未做）跳转到group_id对应group_detail界面 */
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
  onLoad(options){
    console.log(app.globalData.userInfo)
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