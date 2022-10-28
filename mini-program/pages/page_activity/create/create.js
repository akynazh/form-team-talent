// pages/activity_creat/activity_creat.js
Page({
  data: {
    a_id: "" /**进入时创建 */,
    a_name: "",
    a_holder_id: "",
    a_desc: "",
    a_end_date: "",
    a_is_public: false,
    qr_code: null /**进入时创建 */,
  },
  GetName(e) {
    this.setData({
      name: e.detail.value,
    });
  },
  GetHolder(e) {
    this.setData({
      holder: e.detail.value,
    });
  },
  GetContent(e) {
    this.setData({
      content: e.detail.value,
    });
  },
  GetRequirment(e) {
    this.setData({
      requirment: e.detail.value,
    });
  },
  numberselect: function (e) {
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    let index = e.detail.value;
    this.setData({
      humannumber: index,
    });
    // console.log(this.data.humannumber)
  },
  dateselect: function (e) {
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    let index = e.detail.value;
    this.setData({
      enddate: index,
    });
    //console.log(this.data.enddate)
  },
  publicChange(e) {
    let that = this;
    //console.log(that.data.isPublic)
    if (that.data.isPublic == true) {
      this.setData({
        isPublic: false,
      });
    } else if (that.data.isPublic == false) {
      this.setData({
        isPublic: true,
      });
    }
    // console.log(that.data.isPublic)
  },
  Cancle() {
    this.setData({
      name: "",
      holder: "",
      content: "",
      requirment: "",
      humannumber: "",
      enddate: "",
      isPublic: "false",
    });
    console.log(this.data.enddate);
    wx.navigateBack();
  },
  Save() {
    /**（未做）将data中元素上传至云端 */
    wx.navigateBack();
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
});
