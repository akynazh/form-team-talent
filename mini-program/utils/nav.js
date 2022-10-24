module.exports = {
	toHomePage, toMePage, toActivityPage
}

const app = getApp()
const {auth} = require('auth')

function toHomePage(){
	let page_url = "/pages/home_page/home_page";
	if (app.globalData.userInfo == null) {
		auth(page_url);
	} else {
		wx.redirectTo({
			url: page_url,
		});
	}
}

function toActivityPage(){
	let page_url = "/pages/activity_page/activity_page";
	if (app.globalData.userInfo == null) {
		auth(page_url);
	} else {
		wx.redirectTo({
			url: page_url,
		});
	}
}

function toMePage(){
	let page_url = "/pages/me_page/me_page"
	if (app.globalData.userInfo == null) {
		auth(page_url)
	} else {
		wx.redirectTo({
			url: page_url,
		});
	}
}