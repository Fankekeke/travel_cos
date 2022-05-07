const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		hidden: true,
		shopList: []
	},
	onLoad: function (option) {
		this.getShopList()
	},
	shopDeatil(e) {
		wx.navigateTo({
			url: '/pages/shop/goods/details?scenicId='+e.currentTarget.dataset.shopid+''
		});
	},
	getShopList() {
		http.get('scenicList').then((r) => {
			r.data.forEach(element => {
				element.image = element.images.split(',')[0]
			});
            this.setData({
                shopList: r.data
            })
        })
	}
});
