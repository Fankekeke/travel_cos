const app = getApp();
let http = require('../../../utils/request')
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    TabbarBot: app.globalData.tabbar_bottom,
    bulletinList: [],
    TabCur: 0,
    scrollLeft: 0
  },
  onLoad: function (options) {
    this.getPostInfo()
  },
  tabSelect(e) {
    console.log(e.currentTarget.dataset.id)
    this.setData({
      TabCur: e.currentTarget.dataset.id,
      scrollLeft: (e.currentTarget.dataset.id-1)*60
    })
  },
  getPostInfo() {
    http.get('getBulletinList').then((r) => {
      r.data.forEach(item => {
        if (item.images !== null) {
          item.imagesList = item.images.split(',')
        }
      });
      this.setData({bulletinList: r.data})
    })
  }
});
