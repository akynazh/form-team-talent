App({
  globalData: {
    serviceName: 'form-team-talent'
  },
  async onLaunch() {
    // wx.clearStorage()
    // const res = await this.call({
    //   url: '/api/activity/get/pub',
    //   // method: 'POST'
    // })
  },
  getAuthHeader() {
    let token = wx.getStorageSync('auth') || ''
    if (token != '') {
      return {
        'auth': token,
        'X-WX-SERVICE': this.globalData.serviceName
      }
    } else {
      return {
        'X-WX-SERVICE': this.globalData.serviceName
      }
    }
  },
  /**
   * 封装的微信云托管调用方法
   * @param {*} obj 业务请求信息，可按照需要扩展
   * @param {*} number 请求等待，默认不用传，用于初始化等待
   */
  async call(obj, number = 0) {
    const that = this
    if (that.cloud == null) {
      that.cloud = new wx.cloud.Cloud({
        resourceAppid: 'wx2321b1cd8170b56e', // 微信云托管环境所属账号，服务商appid、公众号或小程序appid
        resourceEnv: 'prod-9glf254k94d60b17', // 微信云托管的环境ID
      })
      await that.cloud.init() // init过程是异步的，需要等待 init 完成才可以发起调用
    }
    try {
      const result = await that.cloud.callContainer({
        path: obj.url, // 填入业务自定义路径和参数，根目录，就是 / 
        method: obj.method || 'GET', // 按照自己的业务开发，选择对应的方法
        data: obj.data,
        // dataType:'text', // 如果返回的不是 json 格式，需要添加此项
        header: that.getAuthHeader()
        // 其余参数同 wx.request
      })
      // console.log(`微信云托管调用结果${result.errMsg} | callid:${result.callID}`)
      return result
    } catch (e) {
      wx.hideLoading({})
      const error = e.toString()
      // 如果错误信息为未初始化，则等待300ms再次尝试，因为 init 过程是异步的
      if (error.indexOf("Cloud API isn't enabled") != -1 && number < 3) {
        return new Promise((resolve) => {
          setTimeout(function () {
            resolve(that.call(obj, number + 1))
          }, 300)
        })
      } else {
        throw new Error(`微信云托管调用失败${error}`)
      }
    }
  }
})