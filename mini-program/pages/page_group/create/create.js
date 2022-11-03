const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "" /**从活动界面进入时导入 */,
    g_leader_id: "",
    g_name: "",
    g_desc: "",
    task_number: 0,
    task_name: [],
  },
  getGroupName(e) {
    this.setData({
      GroupName: e.detail.value,
    })
  },
  getLeaderName(e) {
    //console.log(e.detail.value)
    this.setData({
      LeaderName: e.detail.value,
    })
  },
  getGroupGoal(e) {
    //console.log(e.detail.value)
    this.setData({
      GroupGoal: e.detail.value,
    })
  },
  /**从本地找图片替换掉加号图片并且更改变量GroupTalk */
  chooseImage() {
    var that = this
    wx.chooseImage({
      sizeType: ["original", "compressed"],
      sourceType: ["album", "camera"],
      success(res) {
        console.log(res)
        console.log(res.tempFilePaths)
        //上传图片
        that.data.tempImgList = res.tempFilePaths
        that.setData({
          GroupTalk: res.tempFilePaths,
        })
      },
    })
  },
  AddTask() {
    var that = this
    console.log(that.data.task_name)
    this.setData({
      task_number: that.data.task_number + 1,
    })
    let new_task_name = that.data.task_name
    new_task_name.push("null")
    this.setData({
      task_name: new_task_name,
    })
    console.log("task_name: " + that.data.task_name)
    console.log("task_number: " + that.data.task_number)
  },
  DeleteTask(e) {
    console.log(e)
    var idx = e.currentTarget.dataset.id
    console.log("idx" + idx)
    var that = this
    let new_task_name = that.data.task_name
    new_task_name.splice(idx, 1)
    console.log(new_task_name)
    this.setData({
      task_name: new_task_name,
      task_number: that.data.task_number - 1,
    })
    console.log("task_name:" + that.data.task_name)
    console.log("task_number" + that.data.task_number)
  },
  AlterTask(e) {
    console.log(e)
    var idx = e.currentTarget.dataset.id
    //console.log(idx)
    var that = this
    that.data.task_name[idx] = e.detail.value
    console.log(that.data.task_name)
  },
  Cancle() {
    this.setData({
      GroupTalk: null,
      LeaderName: "",
      GroupGoal: "",
      task_number: 0,
      task_name: [],
    })
    wx.navigateBack()
  },
  Save() {
    /**（未做）将data中元素上传至云端 */
    wx.navigateBack()
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {},

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {},
})
