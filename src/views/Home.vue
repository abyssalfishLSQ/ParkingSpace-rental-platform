<template>
  <div>
    <h1>欢迎来到CUGSE车位出租共享平台</h1>
<!--    <h1>车位分布概览</h1>-->
<!--    <div id="mapContainer" class="map-container"></div>-->
  </div>
</template>

<script src="http://api.map.baidu.com/api?v=3.0&ak=AnecKTmBTWf2eeojW2WoM5D5WrvbFINb"></script>


<script>
  export default {
    name: "Home",
    data(){
      return{
        /*map: null,
        showRouteFlag: false,
        route: null,
        points: [
          { lng: 114.227128767, lat: 30.5845606338, name: "时代新世界" },
          { lng: 114.145313266, lat: 30.6382301003, name: "融创观澜府" },
          // 添加更多的地点...
        ],*/

      }
    },
    /*mounted() {
      this.initMap();
      this.map.centerAndZoom(new BMap.Point(points[0].lng, points[0].lat), 15);
    },
    methods:{
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
    },*/

};
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