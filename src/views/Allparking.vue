<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入小区名称" suffix-icon="el-icon-search" v-model="parkingAddress"></el-input>

      <el-select v-model="priceNow" placeholder="请选择价位">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="timeslot" placeholder="请选择时间段">
        <el-option
            v-for="item in options1"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-select v-model="grades" placeholder="请选择大众评分区间">
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
<!--      <el-button type="primary">人气车位榜</el-button>-->
      <el-button type="primary">
        <i class="el-icon-circle-plus-outline">
          <router-link to="/add" >新增</router-link>
        </i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
      <el-table-column prop="parkingid" label="ID" width="80"></el-table-column>
      <el-table-column prop="parkingAddress" label="详细地址" width="140"></el-table-column>

      <el-table-column label="定位" width="120">
        <template slot-scope="scope">
        <el-button  @click="findmap(scope.row.parkingAddress)">点击进入定位</el-button>
        </template>
      </el-table-column>

        <el-table-column prop="priceNow" label="总价" width="120"></el-table-column>
      <el-table-column prop="status" label="预约状态"></el-table-column>
      <el-table-column prop="timeslot" label="可用时间段"></el-table-column>
      <el-table-column label="详细信息">
        <template slot-scope="scope">
        <el-button type="primary" plain @click="viewDetail(scope.row)">
          查看详情
      </el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户操作" v-if="!isAdmin">
        <template slot-scope="scope">
        <el-button type="primary" plain @click="confirmBooking(scope.row.parkingid)">预约</el-button>
        </template>
      </el-table-column>
      <el-table-column label="管理员操作"  width="200" align="center" v-if="isAdmin">
        <template slot-scope="scope">
          <el-button type="success" @click="edit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.parkingid)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
export default {
  name: "Allparking",
data() {
  return {
    tableData: [],
    total: 0,
    pageNum: 1,
    pageSize: 10,
    parkingAddress: "",
    priceNow: "",
    status: "",
    timeslot:"",
    grades: "",
    form: {},
    selectedPlace: '',
    mapDialogVisible: false,//控制地图对话框的显示与隐藏：
    user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

    options: [{
      value: '5',
      label: '5rmb以下每小时'
    }, {
      value: '10',
      label: '6rmb~10rmb每小时'
    }, {
      value: '15',
      label: '11rmb~15rmb每小时'
    }, {
      value: '20',
      label: '16rmb~20rmb每小时'
    }],

    options1: [{
      value: '0~3',
      label: '0~3点'
    }, {
      value: '4~6',
      label: '4~6点'
    }, {
      value: '7~9',
      label: '7~9点'
    }, {
      value: '10~12',
      label: '10~12点'
    }, {
      value: '13~15',
      label: '13~15点'
    }, {
      value: '16~18',
      label: '16~18点'
    }, {
      value: '19~21',
      label: '19~21点'
    }, {
      value: '22~24',
      label: '22~24点'
    }],
    options2: [{
      value: 5.0,
      label: '4~5分'
    }, {
      value: 4.0,
      label: '3~4分'
    }, {
      value: 3.0,
      label: '2~3分'
    }],

  }
},
created() {
  this.load()
  //console.log(this.user)
},
  computed: {
    isAdmin() {
      console.log(this.user.roletype)
      return this.user.roletype === 'admin'; // 假设管理员角色为 'admin'
    }
  },
methods: {
  load() {
    this.request.get("/parking/page", {
      params: {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        parkingAddress: this.parkingAddress,
        priceNow: this.priceNow,
        status: this.status,
        timeslot:this.timeslot,
        grades:this.grades

      }
    }).then(res => {
      this.tableData = res.data.records
      this.total = res.data.total
      console.log(this.tableData);
    })
  },

  viewDetail(row) {
    // 在此处跳转到新的界面，并传递parking对象作为参数
    this.$router.push({ name: 'detail', query: { parkingid: row.parkingid } });
    console.log(row.parkingid)
  },

  reset() {
    this.parkingAddress = ""
    this.priceNow = ""
    this.grades = ""
    this.timeslot = ""
    this.load()
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
  handleSizeChange(pageSize) {
    console.log(pageSize)
    this.pageSize = pageSize
    this.load()
  },
  handleCurrentChange(pageNum) {
    console.log(pageNum)
    this.pageNum = pageNum
    this.load()
  },
  findmap(parkingAddress)
  {
    //点击定位后跳转到定位页面
    this.selectedPlace = parkingAddress;
    this.$router.push({ name: 'findmap' , params: { selectedPlace: this.selectedPlace }});
    // 调用百度地图定位方法，将this.selectedPlace作为参数进行定位操作
    this.mapDialogVisible = true;
  },
  confirmBooking(parkingid) {
    this.$confirm('确定要预约吗?', '确认预约', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
        .then(() => {
          // 用户点击确定，执行预约操作
          this.booking(parkingid);
        })
        .catch(() => {
          // 用户点击取消，不执行任何操作
        });
  },
  booking(parkingid) {
    // 构造请求参数
    const requestData = {
      parkingid: parkingid, // 使用传入的parkingid参数作为请求参数
      // 其他需要传输的数据字段...
    };

    // 发送GET请求到后台
    this.request.get('http://localhost:9090/parking/booking', {
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
  edit(row){
    // 在此处跳转到新的界面，并传递parking对象作为参数
    this.$router.push({ name: 'Edit', query: { parkingid: row.parkingid } });

  },

  }
}
</script>
<style scoped>

</style>