<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px; position: relative; top: 5px; right: 5px">
      <b style="color: white" v-show="logoTextShow">车位出租共享平台</b>
    </div>
    <el-menu-item index="/home">
      <template slot="title">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span>
      </template>
    </el-menu-item>
      <!--管理员侧栏菜单-->
    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/user" v-if="isAdmin">
        <i class="el-icon-s-custom"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
      <el-menu-item index="/file" v-if="isAdmin">
        <i class="el-icon-document"></i>
        <span slot="title">文件管理</span>
      </el-menu-item>
      <el-menu-item index="/parking" v-if="isAdmin">
        <i class="el-icon-files"></i>
        <span slot="title">车位信息</span>
      </el-menu-item>
    </el-submenu>
      <el-menu-item index="/parking" v-if="!isAdmin">
        <i class="el-icon-files"></i>
        <span slot="title">车位信息</span>
      </el-menu-item>
      <el-menu-item index="/publish" v-if="!isAdmin">
        <i class="el-icon-edit-outline"></i>
        <span slot="title">我的发布</span>
      </el-menu-item>
      <el-menu-item index="/myorder" v-if="!isAdmin">
        <i class="el-icon-shopping-cart-2"></i>
        <span slot="title">我的订单</span>
      </el-menu-item>


  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  computed: {
    isAdmin() {
      console.log(this.user.roletype)
      return this.user.roletype === 'admin'; // 假设管理员角色为 'admin'
    }
  }
}
</script>

<style scoped>

</style>
