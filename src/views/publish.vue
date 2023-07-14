<template>
  <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
    <el-table-column prop="parkingid" label="ID" width="80"></el-table-column>
    <el-table-column prop="parkingAddress" label="小区名" width="140"></el-table-column>

    <el-table-column label="定位" width="120">
      <template slot-scope="scope">
        <el-button  @click="findmap(scope.row.parkingAddress)">点击进入定位</el-button>
      </template>
    </el-table-column>

    <el-table-column prop="priceNow" label="价格" width="120"></el-table-column>
    <el-table-column prop="status" label="预约状态"></el-table-column>
    <el-table-column prop="timeslot" label="可用时间段"></el-table-column>
    <el-table-column label="详细信息">
      <template slot-scope="scope">
        <el-button type="primary" plain @click="viewDetail(scope.row)">
          查看详情
        </el-button>
      </template>
    </el-table-column>
    <el-table-column label="用户操作">
      <template slot-scope="scope">
        <el-button type="success" @click="edit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="del(scope.row.parkingid)">
          <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </template>
    </el-table-column>

  </el-table>
</template>

<script>
export default {
  name: "publish",
  data(){
    return{
      tableData: [],
    }
  },
  created(){
    this.load()
  },
  methods:{
    load() {
      this.request.get("/parking/mypublish").then(res => {
        this.tableData = res.data
        console.log(res)
      })
    },
    del(parkingid) {
      this.request.delete("/parking/" + parkingid).then(res => {
        if (res.data) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    viewDetail(row) {
      // 在此处跳转到新的界面，并传递parking对象作为参数
      this.$router.push({ name: 'detail', query: { parkingid: row.parkingid } });
      console.log(row.parkingid)
    },
    findmap(parkingAddress)
    {
      //点击定位后跳转到定位页面
      this.selectedPlace = parkingAddress;
      this.$router.push({ name: 'findmap' , params: { selectedPlace: this.selectedPlace }});
      // 调用百度地图定位方法，将this.selectedPlace作为参数进行定位操作
      this.mapDialogVisible = true;
    },
    edit(row){
      // 在此处跳转到新的界面，并传递parking对象作为参数
      this.$router.push({ name: 'Edit', query: { parkingid: row.parkingid } });

    },

  }

}
</script>

<style scoped>

</style>