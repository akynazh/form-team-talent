const app = getApp()
const baseUrl = app.globalData.baseUrl
import * as util from "../../../utils/util"

Page({
  data: {
    a_name: "",
    a_desc: "",
    a_end_date: "",
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
    let myDate = new Date(this.data.currentDate)
    this.setData({ 
      show: false,
      a_end_date: util.getTimeByDate(myDate)
    });
  },
  checkForm() {
    if (this.data.a_name.trim() == '' || this.data.a_end_date.trim() == '') {
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
                  util.route(`/pages/page_activity/activity/activity?a_id=${a_id}&a_type=${a_type}`)
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
