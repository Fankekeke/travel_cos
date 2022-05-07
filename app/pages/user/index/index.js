// pages/user/index.js
var app = getApp();
var isLoginSuccess = false;
let http = require('../../../utils/request')

Page({
  data: {
    userInfo: {},//默认为空，但是进入页面我们需要先请求后台接口判断用户是否已经授权过信息，没有获取到的情况下我们需要提醒新用户授权
    hasUserInfo: false,//判断是否展示授权按钮/授权弹窗或者跳到授权页面
    canIUseGetUserProfile: false,//是否支持wx.getUserProfile方法
    starCount: 0,
    forksCount: 0,
    visitTotal: 0,
  },
  onLoad: function (options) {
    //进入页面判断是否可以使用wx.getUserProfile
    if (wx.canIUse('getUserProfile')) {
      this.setData({
        canIUseGetUserProfile: true
      })
    }
    this.loginTap()
    let that = this;
    wx.showLoading({
      title: '数据加载中',
      mask: true,
    })
    let i = 0;
    numDH();
    function numDH() {
      if (i < 20) {
        setTimeout(function () {
          that.setData({
            visitTotal: i,
            forksCount: i,
            visitTotal: i
          })
          i++
          numDH();
        }, 20)
      } else {
        that.setData({
          starCount: that.coutNum(999),
          forksCount: that.coutNum(8888),
          visitTotal: that.coutNum(77777)
        })
      }
    }
    wx.hideLoading()
  },
  loginTap() {
    wx.getStorage({
      key: 'userInfo',
      success: (res) => {
        app.globalData.userInfo = res.data
        this.initLoginMsg()
      }
    })
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.showLoading({
      title: '正在登录...',
    })
    var that = this;
    wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        wx.login({
          success(ress) {
            if (ress.code) {
              http.post('userAdd', {
                userName: res.userInfo.nickName,
                avatar: res.userInfo.avatarUrl,
                sex: res.userInfo.gender == 0 ? 1 : 2,
                openId: ress.code
              }).then((res) => {
                that.setData({
                  userInfo: res.data,
                  hasUserInfo: true
                })
                // 可以将 res 发送给后台解码出 unionId
                app.globalData.userInfo = res.data
                wx.setStorage({
                  key: 'userInfo',
                  data: res.data,
                  success: function () {
                    console.log("成功！")
                  }, fail: function () {
                    console.log("失败！")
                  }
                })
                wx.hideLoading()
              })
              if (app.userInfoReadyCallback) {
                app.userInfoReadyCallback(res)
              }
              that.initLoginMsg();
            } else {
              console.log('登录失败！' + res.errMsg)
            }
          }
        })
      }
    })
  },
  initLoginMsg() {
    if (app.globalData.userInfo) {
      isLoginSuccess = true;
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    }
  },
  getUserInfo(e) {
    // 不推荐使用getUserInfo获取用户信息，预计自2021年4月13日起，getUserInfo将不再弹出弹窗，并直接返回匿名的用户个人信息
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  coutNum(e) {
    if (e > 1000 && e < 10000) {
      e = (e / 1000).toFixed(1) + 'k'
    }
    if (e > 10000) {
      e = (e / 10000).toFixed(1) + 'W'
    }
    return e
  },
  CopyLink(e) {

  },
  showModal(e) {
    this.setData({
      modalName: e.currentTarget.dataset.target
    })
  },
  hideModal(e) {
    this.setData({
      modalName: null
    })
  },
  showQrcode() {

  },

});