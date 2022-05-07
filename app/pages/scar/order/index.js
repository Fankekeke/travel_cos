const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        roomInfo: null,
        roomTypeId: null,
        hotelInfo: null,
        userInfo: null,
        startDate: new Date(),
        days: 1,
        allPrice: 0
    },
    onLoad: function (options) {
        this.dataFormat()
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    userInfo: res.data
                })
                this.setData({ roomTypeId: options.roomTypeId })
                this.getRoomTypeDetail(options.roomTypeId)
            },
            fail: res => {
                wx.showToast({
                    title: '请先进行登录',
                    icon: 'none',
                    duration: 2000
                })
            }
        })
    },
    priceSum(event) {
        this.setData({
            days: event.detail.value,
            allPrice: event.detail.value * this.data.roomInfo.price
        })
    },
    submit() {
        wx.showLoading({
            title: '正在模拟支付',
        })
        setTimeout(() => {
            let data = { typeId: this.data.roomInfo.id, hotelId: this.data.hotelInfo.id, price: this.data.allPrice, userId: this.data.userInfo.id, dayNum: this.data.days }
            http.post('orderAdd', data).then((r) => {
                wx.showToast({
                    title: '支付成功',
                    icon: 'success',
                    duration: 1000
                })
                setTimeout(() => {
                    wx.navigateBack({ changed: true });
                }, 1000)
            })
            wx.hideLoading()
        }, 1000)
    },
    dataFormat() {
        var date = new Date();
        var nowMonth = date.getMonth() + 1;
        var strDate = date.getDate();
        var seperator = "-";
        if (nowMonth >= 1 && nowMonth <= 9) {
            nowMonth = "0" + nowMonth;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var nowDate = date.getFullYear() + seperator + nowMonth + seperator + strDate;
        this.setData({
            startDate: nowDate
        })
    },
    getRoomTypeDetail(roomTypeId) {
        http.get('getRoomTypeDetail', { roomTypeId }).then((r) => {
            r.roomType.image = r.roomType.images.split(',')[0]
            this.setData({
                allPrice: r.roomType.price,
                roomInfo: r.roomType,
                hotelInfo: r.hotel
            })
        })
    },
});
