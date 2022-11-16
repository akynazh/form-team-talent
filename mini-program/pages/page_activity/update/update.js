const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_id: "",
    a_type: "",
    a_name: "",
    a_desc: "",
    a_end_date: "",
    a_end_date_show: "",
    a_is_public: "0",
    currentDate: new Date().getTime(),
    minDate: new Date().getTime(),
    formatter(type, value) {
      if (type === 'year') {
        return `${value}年`;
      }
      if (type === 'month') {
        return `${value}月`;
      }
      return value;
    },
    show: false,
  },
  onIsPublicClick(event) {
    const { name } = event.currentTarget.dataset;
    this.setData({
      a_is_public: name
    });
    console.log(this.data.a_is_public)
  },
  showDatePickerPopup() {
    this.setData({ show: true });
  },
  onDatePickerClose() {
    this.setData({ show: false });
  },
  onDatePickerInput(event) {
    this.setData({
      currentDate: event.detail,
    });
  },
  onDatePickerConfirm() {
    this.setData({ 
      show: false,
      a_end_date: this.data.currentDate,
      a_end_date_show: util.getFormatTimeByMillis(this.data.currentDate)
    });
  },
  checkForm() {
    if (this.data.a_name.trim() == '' || this.data.a_end_date.trim() == '') {
      return false
    }
    return true
  },
  onLoad(params) {
    let a_id = params.a_id
    let a_type = params.a_type
    let activity = JSON.parse(params.activity)
    this.setData({
      a_id: a_id,
      a_type: a_type,
      a_name: activity.a_name,
      a_desc: activity.a_desc,
      a_end_date: activity.a_end_date,
      a_end_date_show: util.getFormatTimeByMillis(activity.a_end_date),
      a_is_public: `${activity.a_is_public}`,
    })
  },
  updateActivity() {
    let that = this
    let a_id = this.data.a_id
    let a_type = this.data.a_type
    wx.showModal({
      title: '更新活动',
      content: '确认更新？',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${baseUrl}/api/activity/update`,
            header: util.getAuthHeader(),
            method: 'POST',
            data: that.data,
            success(res) {
              if (util.checkSuccess(res)) {
                util.route(`/pages/page_activity/detail/detail?a_id=${a_id}&a_type=${a_type}`)
                wx.showToast({
                  title: '操作成功',
                })
              }
            },
            fail() {
              util.fail()
            }
          })
        }
      }
    })
  },
})
