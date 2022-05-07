const app = getApp();
let http = require('../../../utils/request')
Page({
  data: {
    StatusBar: app.globalData.StatusBar + 6,
    TabbarBot: app.globalData.tabbar_bottom,
    swiperlist: [],
    roomInfo: null,
    roomTypeId: null,
    hotelInfo: null,
    evaluation: []
  },
  onLoad: function (options) {
    this.setData({ roomTypeId: options.roomTypeId })
    this.getRoomTypeDetail(options.roomTypeId)
  },
  getRoomTypeDetail(roomTypeId) {
    http.get('getRoomTypeDetail', { roomTypeId }).then((r) => {
      this.setData({
        swiperlist: r.roomType.images.split(','),
        roomInfo: r.roomType,
        hotelInfo: r.hotel,
        evaluation: r.evaluation
      })
    })
  },
  shopDetail() {
    let that = this
    wx.getLocation({
      type: 'gcj02', // 默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标
      success: function (res) {
        wx.openLocation({
          latitude: Number(that.data.hotelInfo.point.split(',')[1]), // 纬度，范围为-90~90，负数表示南纬
          longitude: Number(that.data.hotelInfo.point.split(',')[0]), // 经度，范围为-180~180，负数表示西经
          scale: 28, // 缩放比例
          name: that.data.hotelInfo.name,
          address: that.data.hotelInfo.address
        })
      }
    })
  },
  buyGoods() {
    if (this.data.roomInfo.num == 0) {
      wx.showToast({
        title: '房间余量不足！',
        icon: 'none',
        duration: 2000
      })
    } else {
      // wx.getStorage({
      //   key: 'userInfo',
      //   success: (res) => {
      //     wx.navigateTo({
      //       url: '/pages/scar/order/index?type=2'
      //     })
      //   },
      //   fail: res => {
      //     wx.showToast({
      //       title: '请先进行登录',
      //       icon: 'none',
      //       duration: 2000
      //     })
      //   }
      // })
      wx.navigateTo({
        url: '/pages/scar/order/index?roomTypeId='+this.data.roomTypeId+''
      })
    }
  }
});
