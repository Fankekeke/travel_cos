<template>
  <a-modal v-model="show" title="景点信息" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="scenicData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span>
        </a-col>
        <a-col :span="8"><b>景点名称：</b>
          {{ scenicData.name }}
        </a-col>
        <a-col :span="8"><b>门票价格：</b>
          ￥{{ scenicData.price }}
        </a-col>
        <a-col :span="8"><b>具体地址：</b>
          <a-tooltip>
            <template slot="title">
              {{ scenicData.address }}
            </template>
            {{ scenicData.address.slice(0, 8) }} ...
          </a-tooltip>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>开园时间：</b>{{ scenicData.startDate !== null ? scenicData.startDate : '- -' }}</a-col>
        <a-col :span="8"><b>关闭时间：</b>{{ scenicData.endDate !== null ? scenicData.endDate : '- -' }}</a-col>
        <a-col :span="8"><b>等 级：</b>{{ scenicData.level !== null ? scenicData.level : '- -' }}</a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;"
             v-if="scenicData.images !== null && scenicData.images !== ''">
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            disabled
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">景点特色</span>
        </a-col>
        <a-col :span="24" style="line-height: 25px">{{ scenicData.feature !== null ? scenicData.feature : '暂无景点特色信息' }}</a-col>
      </a-row>
      <br/>
      <br/>
      <div style="padding-left: 8px;padding-right: 8px;">
        <a-card :bordered="false" style="height: 400px">
          <div id="areas" style="width: 100%;height: 350px;box-shadow: 0 0 0 10px white;outline: dashed 8px #009688;"></div>
        </a-card>
      </div>
    </div>
  </a-modal>
</template>

<script>
import baiduMap from '@/utils/map/baiduMap'

export default {
  name: 'ScenicView',
  props: {
    scenicShow: {
      type: Boolean,
      default: false
    },
    scenicData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.scenicShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      nowPoint: null,
      fileList: [],
      previewVisible: false,
      previewImage: ''
    }
  },
  watch: {
    scenicShow: function (value) {
      if (value) {
        if (value && this.scenicData.images !== null && this.scenicData.images !== '') {
          this.imagesInit(this.scenicData.images)
        }
        setTimeout(() => {
          baiduMap.initMap('areas')
          setTimeout(() => {
            this.local(this.scenicData)
          }, 500)
        }, 200)
      }
    }
  },
  methods: {
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    local (scenic) {
      baiduMap.clearOverlays()
      baiduMap.rMap().enableScrollWheelZoom(true)
      // eslint-disable-next-line no-undef
      let point = new BMap.Point(scenic.point.split(',')[0], scenic.point.split(',')[1])
      baiduMap.pointAdd(point)
      baiduMap.findPoint(point, 16)
      // let driving = new BMap.DrivingRoute(baiduMap.rMap(), {renderOptions:{map: baiduMap.rMap(), autoViewport: true}});
      // driving.search(new BMap.Point(this.nowPoint.lng,this.nowPoint.lat), new BMap.Point(scenic.point.split(",")[0],scenic.point.split(",")[1]));
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>
</style>
