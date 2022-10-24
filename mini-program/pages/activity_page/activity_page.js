// pages/activity_page/activity_page.js
const app = getApp()
const {toMePage, toHomePage, toActivityPage} = require('../../utils/nav')
Page({
	toMePage,
	toHomePage,
	toActivityPage,
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
})