const app = getApp()
Page({
  data: {

  },

  onLoad:function(options){

  },
  getInfo(){
    wx.getUserProfile({
      desc:'获取用户必要的信息',
      success(res){
        console.log(res)

        app.globalData.userInfo = res.userInfo
        wx.setStorageSync('userInfo', res.userInfo)//键值对

        wx.navigateBack({
          success(res){
            wx.showToast({
              title: '授权成功！',
            })
          }
        })


      }
    })
   
    


  }

})