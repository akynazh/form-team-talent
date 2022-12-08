const app = getApp()
let util = require("../../../utils/util")

Page({
  data: {
    aName: "",
    aDesc: "",
    aEndDate: "",
    aEndDateShow: "",
    aIsPublic: "0",
    aType: "",
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
      aIsPublic: name
    });
    console.log(this.data.aIsPublic)
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
      aEndDate: this.data.currentDate,
      aEndDateShow: util.getFormatTimeByMillis(this.data.currentDate)
    });
  },
  checkForm() {
    if (this.data.aName.trim() == '' || this.data.aEndDate == '') {
      return false
    }
    return true
  },
  async sendCreateActivity() {
    const res = await app.call({
      url: `/api/activity/add`,
      method: 'POST',
      data: this.data,
    })
    if (util.checkSuccess(res)) {
      let obj = res.data.obj
      let aId = obj.aId
      let aType = this.data.aType
      util.route(`/pages/page_activity/activity/activity?aId=${aId}&aType=${aType}`, 1, 1)
      wx.showToast({
        title: '创建成功',
      })
    }
  },
  async createActivity() {
    let that = this
    if (this.checkForm()) {
      wx.showModal({
        title: '创建活动',
        content: '确认创建？',
        success(res) {
          if (res.confirm) {
            that.setData({
              aType: that.data.aIsPublic == 1 ? 0 : 1
            })
            that.sendCreateActivity()
          }
        }
      })
    } else {
      util.alertFail('表单未正确填写')
    }
  },
})
