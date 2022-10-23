// pages/person_information/person_information.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    me_name:'wft',/*(未做)进入页面时设置为云端值 */
    me_student_number:'sds',
    me_grade:'sda',
    me_school:'dddd',
  },
  getName(e){
    this.setData({
      me_name: e.detail.value
    })
  },
  getStuNumber(e){
    this.setData({
      me_student_number: e.detail.value
    })
  },
  getGrade(e){
    this.setData({
      me_grade: e.detail.value
    })
  },
  getSchool(e){
    this.setData({
      me_school: e.detail.value
    })
    console.log(this.data.me_school)
  },
  Cancle(){
    this.setData({
      GroupTalk:null,
      LeaderName:'',
      GroupGoal:'',
      task_number:0,
      task_name:[]
    })
    wx.navigateBack()
  },
  Save(){
    /**（未做）将data中元素上传至云端 */
    wx.navigateBack()
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