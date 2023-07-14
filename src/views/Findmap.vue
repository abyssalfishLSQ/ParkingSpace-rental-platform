<template>
  <div>
    <el-button type="primary" icon="el-icon-arrow-left" @click="goBack">返回</el-button>
    <div id="mapContainer" class="map-container"></div>
  </div>
</template>

<script src="http://api.map.baidu.com/api?v=3.0&ak=AnecKTmBTWf2eeojW2WoM5D5WrvbFINb"></script>

<script>
export default {
  name: "findmap",
  data() {
    return {
      selectedPlace: '',
      map: null,
      showRouteFlag: false,
      route: null
    }
  },
  mounted() {
    this.selectedPlace = this.$route.params.selectedPlace;
    this.initMap();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    initMap() {
      this.map = new BMap.Map("mapContainer");
      this.map.enableScrollWheelZoom(true);
      this.map.addControl(new BMap.NavigationControl());
      this.map.addControl(new BMap.ScaleControl());
      this.map.addControl(new BMap.OverviewMapControl());
      this.map.addControl(new BMap.MapTypeControl());

      // 地理编码获取经纬度坐标
      const geocoder = new BMap.Geocoder();
      geocoder.getPoint(this.selectedPlace, (point) => {
        if (point) {
          // 设置地图中心点和缩放级别
          this.map.centerAndZoom(point, 16);
          // 创建标注并设置其位置为定位地点
          const marker = new BMap.Marker(point);
          this.map.addOverlay(marker);
        } else {
          console.log("地理编码失败");
        }
      });
    },
  }
}
</script>

<style scoped>
.map-container {
  margin-left: 450px; /* 设置为自动居中 */
  margin-top: -50px;
  text-align: center; /* 内部元素居中 */
  width: 80%;
  height: 650px;
  border-radius: 10px;
}
</style>