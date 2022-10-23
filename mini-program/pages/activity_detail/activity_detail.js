// pages/activity_detail/activity_detail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    QRcode:null,
    name:'思政演讲',
    holder:'wft',
    content:'思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲思政演讲',
    requirement:'微信小程序微信小程序微信小程序微信小程序微信小程序',
    humannumber:'10',
    group_number:3,
    group:[
      {"group_name":"nnnnns",
     "group_id":"44454"},
     {"group_name":"554s",
     "group_id":"44454"},
     {"group_name":"nnnns",
     "group_id":"474"}
    ],
    endTime:'2022-10-19'
  },
  return_button(){
    wx.navigateBack()
  },
  ToGroupCreat(){
    wx.navigateTo({
      url: '/pages/group_creat/group_creat',
    })
    /**（未做）activity_id传入 */
  },
  ToGroupDetail(e){
    console.log(e)
    let idx= e.currentTarget.dataset.id;
    //console.log(idx)
    let that=this
     /**（未做）跳转到对应group_detail */
  },
  deletedeleteActivity(){
    /**（未做）从云端删除活动 */
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