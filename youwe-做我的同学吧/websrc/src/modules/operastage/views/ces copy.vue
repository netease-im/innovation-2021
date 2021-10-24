<template>
  
</template>

<script lang="ts" setup>

</script>

<style lang="scss" scoped>
.text {
  width: 300px;
  height: 200px;
  position: absolute;
  left: 50%;
  margin-left: -150px;
  background-image: -webkit-linear-gradient(
    left,
    blue,
    #66ffff 10%,
    #cc00ff 20%,
    #cc00cc 30%,
    #ccccff 40%,
    #00ffff 50%,
    #ccccff 60%,
    #cc00cc 70%,
    #cc00ff 80%,
    #66ffff 90%,
    blue 100%
  );
  -webkit-text-fill-color: transparent; /* 将字体设置成透明色 */
  background-clip: text; /* 裁剪背景图，使文字作为裁剪区域向外裁剪 */
  background-size: 200% 100%;
  animation: masked-animation 4s linear infinite;
  font-size: 2rem;
}

@keyframes masked-animation {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: -100% 0;
  }
}
</style>
