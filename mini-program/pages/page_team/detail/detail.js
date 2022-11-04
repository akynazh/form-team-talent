const app = getApp()
const base_url = app.globalData.base_url
import * as util from "../../../utils/util"

Page({
  data: {
    group_talk:null,
    group_leader:'xyz',
    group_id:'s777',
    group_name:'weixinxiaoch',
    group_content:'微信小程序 微信小程序微信小程序微信小程序微信小程序微信小程序微信小程序微信小程序微信小程序微信小程序',
    task_number:4,
    task:[
      {"name":"制作ppt",
        "member": "null"},
      {"name":"演讲",
        "member": "null"},
      {"name":"答辩",
        "member": "null"},
      {"name":"学习",
        "member": "null"},
    ]
  },
  return_button(){
    wx.navigateBack()
  },
  memberSelect(e){
    let that=this
    let idx= e.currentTarget.dataset.id
    console.log(idx)
    that.data.task[idx]=e.detail.value
    console.log(that.data.task[idx])
    /**（未做）将此改动上传云端 */
  },
  deleteGroup(){
    /**（未做）从云端删除小组 */
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