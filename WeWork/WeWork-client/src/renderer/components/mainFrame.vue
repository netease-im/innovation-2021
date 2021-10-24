<template>
    <div class="bodyDiv">
    <main-head></main-head>
    <sub-header></sub-header>
    <router-view v-if="refresh" class="mainDiv"></router-view>
    </div>
</template>

<script>
import MainHead from './MainHead.vue'
import SubHeader from './subHeader.vue'
export default {
  components: { MainHead,SubHeader },
  data(){
    return {
      refresh:true
    }
  },
  methods:{
    reload(){
      this.refresh=false

      //nextTick: Dom数据更新之后调用
      this.$nextTick(function(){
        this.refresh=true
        
      })
    }
  },
  provide(){
    return{
      reload:this.reload
    }
  }
}
</script>

<style scoped>
.bodyDiv{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: 0 1px 6px 1px rgba(0, 0, 0, 0.3);
  justify-content: space-between;
  overflow: hidden;
  position: relative;
  background-color:rgb(230, 240, 255);
}
.mainDiv{
  width:100%;
  height: 100%;
  overflow-y: auto;
  word-break: break-all;
  flex-grow: 1;
  position: relative;
  overflow-x: hidden;
  padding-bottom: 1px;
  box-sizing: border-box;
}

</style>