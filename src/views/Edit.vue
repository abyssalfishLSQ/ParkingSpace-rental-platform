<template>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small">

      <el-form-item label="小区名">
        <el-input v-model="form.parkingAddress" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-input v-model="form.status" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="价格（每小时）">
        <el-input v-model="form.priceNow" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="车位主人联系方式">
        <el-input v-model="form.ownerphone" autocomplete="off"></el-input>
      </el-form-item>
<!--      <el-form-item label="本周租用次数">
        <el-input v-model="form.weekUseCnt" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="本月租用次数">
        <el-input v-model="form.monthUseCnt" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="本周平均价格">
        <el-input v-model="form.weekAvgPrice" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="本月平均价格">
        <el-input v-model="form.monthAvgPrice" autocomplete="off"></el-input>
      </el-form-item>-->
    </el-form>
    <el-divider content-position="left">车位图片</el-divider>

    <el-upload
        class="avatar-uploader"
        action="http://localhost:9090/file/upload"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
    >
      <el-divider content-position="left"></el-divider>
      <img v-if="form.parkingUrl" :src="form.parkingUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>

    <el-button type="primary" @click="save">确 定</el-button>

  </el-card>

</template>


<script>
export default {
  name: "Edit",
  data(){
    return{
      form:{},
    }

  },
  created() {
    this.getParking().then(res => {
      console.log(res)
      this.form = res
    })
  },
  methods:{
    async getParking() {
      return (await this.request.get("/parking/" + this.$route.query.parkingid)).data
    },
    save() {
      this.request.post("/parking", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.parkingUrl = res
    },

  }
}
</script>

<style scoped>

</style>