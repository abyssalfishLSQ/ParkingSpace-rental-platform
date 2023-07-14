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
        <div slot="header">用户评分</div>
        <el-rate v-model="userRating" :show-score="true" :allow-half="true" @change="UserRate(scope.row.parkingid,userRating)"></el-rate>
        <el-button type="primary" plain @click="cancel(scope.row.parkingid)">取消预约 <i class="el-icon-edit"></i></el-button>
      </template>
    </el-table-column>

  </el-table>
</template>


<script>


export default {
  name: "MyOrder",
  data(){
    return{
      tableData: [],
      userRating:0,
    }
  },
  created(){
    this.load()
  },
  methods:{
    load() {
      this.request.get("/parking/myorder").then(res => {
        this.tableData = res.data
        console.log(res)
      })
    },
    cancel(parkingid) {
      // 构造请求参数
      const requestData = {
        parkingid: parkingid, // 使用传入的parkingid参数作为请求参数
        // 其他需要传输的数据字段...
      };
      // 发送GET请求到后台
      this.request.get('http://localhost:9090/parking/cancelbooking', {
        params: requestData
      })
          .then(response => {
            // 请求成功处理逻辑
            console.log(response.data); // 根据后台返回的数据进行处理
            this.load();
          })
          .catch(error => {
            // 请求失败处理逻辑
            console.error(error.stack);
          });
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
    UserRate(parkingid,userRating){
      // 构造请求参数
      const requestData = {
        parkingid: parkingid, // 使用传入的parkingid参数作为请求参数
        grades:userRating
      };
      this.request.get('http://localhost:9090/parking/calculatingrate', {
        params: requestData
      }).then(response => {
        // 请求成功处理逻辑
        console.log(response.data); // 根据后台返回的数据进行处理
        this.$message.success("评分成功!");
      })
          .catch(error => {
            // 请求失败处理逻辑
            console.error(error.stack);
            this.$message.error("评分失败，请稍后重试！");
          });
    },


  }

}
</script>

<style scoped>

</style>