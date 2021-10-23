<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
  export default {
    name: 'we-work'
  }
</script>

<style>
  /* CSS */
html{
  height:100%;
}
body{
  margin:0;
  height:100%;
}
#app{
  height:100%
}


/* 修改滚轮样式 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
  background: transparent;
}
 
::-webkit-scrollbar-thumb {
  background: transparent;
  border-radius: 4px;
}
 
:hover::-webkit-scrollbar-thumb {
  background: hsla(0, 0%, 53%, 0.4);
}
 
:hover::-webkit-scrollbar-track {
  background: hsla(0, 0%, 53%, 0.1);
}
</style>
