<template>
  <a-drawer
    title="新增景点"
    :maskClosable="false"
    width=700
    placement="right"
    :closable="false"
    @close="onClose"
    :visible="userAddVisiable"
    style="height: calc(100% - 55px);overflow: auto;padding-bottom: 53px;">
    <a-form :form="form" layout="vertical">
      <a-row :gutter="10">
        <a-col :span="12">
          <a-form-item label='景点名称'>
            <a-input v-decorator="[
            'name',
            { rules: [{ required: true, message: '请输入名称!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='票价'>
            <a-input-number style="width: 100%" :min="0" :step="0.1" v-decorator="[
            'price',
            { rules: [{ required: true, message: '请输入票价!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='所在地'>
            <a-input style="width: 70%" v-decorator="[
            'address'
            ]"/>
            <a-button type="primary" style="width: 30%" @click="showChildrenDrawer">
              选择地址
            </a-button>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='景区等级'>
            <a-select v-decorator="[
                'level',
                ]">
              <a-select-option value="4A景区">4A景区</a-select-option>
              <a-select-option value="5A景区">5A景区</a-select-option>
              <a-select-option value="6A景区">6A景区</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24"></a-col>
        <a-col :span="12">
          <a-form-item label='开园时间'>
            <a-input v-decorator="[
            'startDate',
            { rules: [{ required: true, message: '请输入开园时间!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="12">
          <a-form-item label='结束时间'>
            <a-input v-decorator="[
            'endDate',
            { rules: [{ required: true, message: '请输入结束时间!' }] }
            ]"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='优选旅店'>
            <a-select
              v-decorator="[
              'hotelIds',
              ]"
              mode="multiple"
              style="width: 100%"
              placeholder="Please select"
              @change="handleChange"
            >
              <a-select-option v-for="(item, index) in hotelList" :key="index" :value="item.id">
                {{ item.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='景区特色'>
            <a-textarea v-decorator="[
            'feature'
            ]" :rows="4"/>
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item label='图册' v-bind="formItemLayout">
            <a-upload
              style="width: 100%"
              name="avatar"
              action="http://127.0.0.1:9527/file/fileUpload/"
              list-type="picture-card"
              :file-list="fileList"
              @preview="handlePreview"
              @change="picHandleChange"
            >
              <div v-if="fileList.length < 8">
                <a-icon type="plus" />
                <div class="ant-upload-text">
                  Upload
                </div>
              </div>
            </a-upload>
            <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
              <img alt="example" style="width: 100%" :src="previewImage" />
            </a-modal>
          </a-form-item>
        </a-col>
      </a-row>
    </a-form>
    <drawerMap :childrenDrawerShow="childrenDrawer" @handlerClosed="handlerClosed"></drawerMap>
    <div class="drawer-bootom-button">
      <a-popconfirm title="确定放弃编辑？" @confirm="onClose" okText="确定" cancelText="取消">
        <a-button style="margin-right: .8rem">取消</a-button>
      </a-popconfirm>
      <a-button @click="handleSubmit" type="primary" :loading="loading">提交</a-button>
    </div>
  </a-drawer>
</template>
<script>
import baiduMap from '@/utils/map/baiduMap'
import drawerMap from '@/utils/map/searchmap/drawerMap'
import AFormItem from 'ant-design-vue/es/form/FormItem'
const formItemLayout = {
  labelCol: {span: 24},
  wrapperCol: {span: 24}
}
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'UserAdd',
  props: {
    userAddVisiable: {
      default: false
    }
  },
  data () {
    return {
      formItemLayout,
      mapId: 'area',
      cardShow: false,
      local: '',
      loading: false,
      childrenDrawer: false,
      form: this.$form.createForm(this),
      validateStatus: '',
      help: '',
      localPoint: {},
      stayAddress: '',
      fileList: [],
      previewVisible: false,
      previewImage: '',
      hotelList: [],
      defaultHotel: []
    }
  },
  components: {
    AFormItem,
    drawerMap
  },
  mounted () {
    this.getScenicList()
  },
  methods: {
    handleChange (value) {
      console.log(value)
    },
    getScenicList () {
      this.$get('/cos/hotel-info/list').then((r) => {
        this.hotelList = r.data.data
      })
    },
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
    handlerClosed (localPoint) {
      this.childrenDrawer = false
      if (localPoint != null && localPoint !== undefined) {
        this.localPoint = localPoint
        let address = baiduMap.getAddress(localPoint)
        address.getLocation(localPoint, (rs) => {
          if (rs != null) {
            if (rs.address !== undefined && rs.address.length !== 0) {
              this.stayAddress = rs.address
            }
            if (rs.surroundingPois !== undefined) {
              if (rs.surroundingPois.address !== undefined && rs.surroundingPois.address.length !== 0) {
                this.stayAddress = rs.surroundingPois.address
              }
            }
            this.form.getFieldDecorator('address')
            let obj = {}
            obj['address'] = this.stayAddress
            this.form.setFieldsValue(obj)
          }
        })
      }
    },
    reset () {
      this.loading = false
      this.form.resetFields()
    },
    showChildrenDrawer () {
      this.childrenDrawer = true
    },
    onChildrenDrawerClose () {
      this.childrenDrawer = false
    },
    onClose () {
      this.reset()
      this.$emit('close')
    },
    handleSubmit () {
      // 获取图片List
      let images = []
      this.fileList.forEach(image => {
        images.push(image.response)
      })
      this.form.validateFields((err, values) => {
        if (!err) {
          this.loading = true
          values.images = images.length > 0 ? images.join(',') : null
          if (this.localPoint.lng !== undefined && this.localPoint.lat !== undefined) {
            values.point = this.localPoint.lng.toString() + ',' + this.localPoint.lat
          }
          if (values.hotelIds !== undefined && values.hotelIds !== null && values.hotelIds.length !== 0) {
            values.hotelIds = values.hotelIds.join(',')
          }
          this.$post('/cos/scenic-info', {
            ...values
          }).then((r) => {
            this.reset()
            this.$emit('success')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>
