const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar + 6,
        TabbarBot: app.globalData.tabbar_bottom,
        swiperlist: [],
        scenicInfo: null,
        hotelInfo: []
    },
    onLoad: function (options) {
        this.getGoodsDetail(options.scenicId)
        this.getHotelByScenic(options.scenicId)
    },
    getGoodsDetail(scenicId) {
        http.get('scenicDetail', { scenicId }).then((r) => {
            this.setData({
                swiperlist: r.data.images.split(','),
                scenicInfo: r.data
            })
        })
    },
    shopDeatil(e) {
		wx.navigateTo({
			url: '/pages/shop/index/index?hotelId='+e.currentTarget.dataset.shopid+''
		});
	},
    getHotelByScenic(scenicId) {
        http.get('getHotelByScenic', { scenicId }).then((r) => {
            if (r.data !== null) {
                r.data.forEach(element => {
                    element.image = element.images.split(',')[0]
                });
            }
            this.setData({
                hotelInfo: r.data
            })
        })
    },
    openMap: function () {
        let that = this
        wx.getLocation({
            type: 'gcj02', // 默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标
            success: function (res) {
                wx.openLocation({
                    latitude: Number(that.data.scenicInfo.point.split(',')[1]), // 纬度，范围为-90~90，负数表示南纬
                    longitude: Number(that.data.scenicInfo.point.split(',')[0]), // 经度，范围为-180~180，负数表示西经
                    scale: 28, // 缩放比例
                    name: that.data.scenicInfo.name,
                    address: that.data.scenicInfo.address
                })
            }
        })
    }
});
