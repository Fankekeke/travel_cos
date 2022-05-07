const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar + 6,
        TabbarBot: app.globalData.tabbar_bottom,
        TabCur: 0,scrollLeft:0,
        SortMenu: [{id:0,name:"综合"},{id:1,name:"销量"},{id:2,name:"新品"},{id:3,name:"价格"}],
        ShopList: [],
        hotelId: null,
        hotelInfo: null,
    },
    onLoad: function (options) {
        this.setData({
            hotelId: options.hotelId
        })
        this.selScenicDetail(options.hotelId)
    },
    selScenicDetail(hotelId) {
        http.get('hotelDetail', {hotelId}).then((r) => {
            let ShopList = []
			r.data.roomTypes.forEach(item => {
                ShopList.push({ index: item.id, image: item.images.split(',')[0], title: item.name, price: item.price, sales: item.num })
			});
            this.setData({
                hotelInfo: r.data,
                ShopList
            })
        })
    },
    tabSelect(e) {
        console.log(e.currentTarget.dataset.id);
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id-1)*60
        })
    },
    btnback: function () {
        wx.navigateBack();
    },
    search: function () {
        wx.navigateTo({
            url: '/pages/shop/search/index'
        });
    }
});
