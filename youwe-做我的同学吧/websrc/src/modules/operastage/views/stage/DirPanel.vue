<template>
  <div class="dir-panel">
    <div class="frow f1">
      <div class="f1"></div>
      <Button
        @click="$emit('tap', 'up')"
        circle=""
        class="el-icon el-icon-caret-top"
      ></Button>
      <div class="f1"></div>
    </div>
    <div class="frow f1">
      <Button
        circle=""
        @click="$emit('tap', 'left')"
        class="el-icon el-icon-caret-left"
      ></Button>
      <Button
        circle=""
        @click="$emit('tap', 'middle')"
        class="el-icon el-icon-refresh"
      ></Button>
      <Button
        circle=""
        @click="$emit('tap', 'right')"
        class="el-icon el-icon-caret-right"
      ></Button>
    </div>
    <div class="frow f1">
      <div class="f1"></div>
      <Button
        circle=""
        @click="$emit('tap', 'down')"
        class="el-icon el-icon-caret-bottom"
      ></Button>
      <div class="f1"></div>
    </div>
  </div>
</template>

<script lang="ts">
import Button from "@/components/Button.vue";
import { Options, Vue } from "vue-class-component";

@Options({ name: "DirPanel", components: { Button } })
export default class DirPanel extends Vue {}
</script>

<style lang="scss" scoped>
.dir-panel {
  width: 136px;
  height: 136px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 8px;
  .el-icon {
    margin: 0;
  }
}
</style>
