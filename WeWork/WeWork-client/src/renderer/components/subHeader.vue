
<template>
<el-menu  class="el-menu-demo" mode="horizontal">
  <el-menu-item @click="reLocate('/createPlan')"  >创建计划</el-menu-item>
  <el-menu-item @click="reLocate('/workbench')"  >计划工作台</el-menu-item>
  <el-menu-item @click="reLocate('/myPlan')" >我的计划</el-menu-item>
  <el-menu-item @click="reLocate('/coWork')" >我的合作</el-menu-item>
  <el-menu-item @click="reLocate('/userCenter')"  >个人中心</el-menu-item>
</el-menu>
</template>

<script>
export default {
  methods:{
    reLocate(path){
      this.$router.push(path)
    }
  }
}
</script>

<style>

</style>