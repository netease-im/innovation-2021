<template>
  <Button
    size="mini"
    icon="el-icon-full-screen"
    :style="{
      color,
    }"
    @click="toggle"
    circle=""
  ></Button>
</template>

<script lang="ts" setup>
import { Options, prop, Vue } from "vue-class-component";
import Button from "@/components/Button.vue";
@Options({
  name: "FullscreenSetting",
  props: {
    selector: { default: "canvas" },
  },
  components: { Button },
})
export default class FullscreenSetting extends Vue {
  isFullscreen = false;
  selector!: string;
  get color() {
    return this.isFullscreen ? "green" : "grey";
  }
  private toggle(nv: number) {
    if (this.isFullscreen) {
      document.exitFullscreen();
    } else {
      const el = document.querySelector(this.selector) as HTMLElement;
      el.requestFullscreen();
    }
  }
  delayClock?: number;
  active(v: boolean) {}
}
</script>

<style lang="scss" scoped></style>
