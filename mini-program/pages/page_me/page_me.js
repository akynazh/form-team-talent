// 获取应用实例
const app = getApp()
const {alert_fail} = require("../../utils/util");
const base_url = app.globalData.serverUrl;

Page({
	data: {
		u_name: "dsaf",
		u_stu_num: "",
		u_school: "sdf"
	},
	onLoad() {
    let token = wx.getStorageSync('auth') || "";
    let my_header = token != "" ? {"auth": token} : {};
    let that = this;
    wx.request({
      url: `${base_url}/api/user/get/info`,
      header: my_header,
      success(res) {
        let obj = res.data.obj;
        that.setData({
          u_name: obj.u_name,
          u_stu_num: obj.u_stu_num,
          u_school: obj.u_school
        })
      },
      fail(res) {
        alert_fail(title="获取失败", content=res.data.msg)
      }
    })
	},
	update_info() {
    console.log("update info")
    let token = wx.getStorageSync('auth') || "";
    let my_header = token != "" ? {"auth": token} : {};
    let that = this;
    console.log(that.data)
    wx.request({
      url: `${base_url}/api/user/update`,
      header: my_header,
      method: "POST",
      data: {
        u_name: that.data.u_name,
        u_stu_num: that.data.u_stu_num,
        u_school: that.data.u_school
      },
      success(res) {
        console.log(res)
      },
      fail(res) {
        alert_fail("更新失败", res.data.msg);
      }
    })
	}
})
