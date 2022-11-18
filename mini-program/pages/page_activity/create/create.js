const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_name: "",
    a_desc: "",
    a_end_date: "",
    a_end_date_show: "",
    a_is_public: "0",
    a_type: "",
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
    if (this.data.a_name.trim() == '' || this.data.a_end_date == '') {
      return false
    }
    return true
  },
  createActivity() {
    let that = this
    if (this.checkForm()) {
      wx.showModal({
        title: '创建活动',
        content: '确认创建？',
        success(res) {
          if (res.confirm) {
            that.setData({
              a_type: that.data.a_is_public == 1 ? 0 : 1
            })
            wx.request({
              url: `${baseUrl}/api/activity/add`,
              header: util.getAuthHeader(),
              method: 'POST',
              data: that.data,
              success(res) {
                if (util.checkSuccess(res)) {
                  let obj = res.data.obj
                  let a_id = obj.a_id
                  let a_type = that.data.a_type
                  util.route(`/pages/page_activity/activity/activity?a_id=${a_id}&a_type=${a_type}`, 1, 1)
                  wx.showToast({
                    title: '创建成功',
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
    } else {
      util.alertFail('提交失败', '表单未正确填写')
    }
  },
})
