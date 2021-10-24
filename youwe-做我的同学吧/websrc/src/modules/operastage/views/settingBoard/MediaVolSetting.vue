<template>
  <div class="media-vol-panel frow cnty">
    <i class="el-icon-headset" circle=""></i>
    <hj-slider class="f1 mgl" v-model="playVol" :step="0.01" :min="0" :max="1">
    </hj-slider>
  </div>
</template>

<script lang="ts">
import Slider from "@/components/basic/Slider.vue";

@Options({
  name: "MediaVolSetting",
  props: {
    mediaType: { default: "audio" },
    value: { default: 0.1 },
  },
  components: { "hj-slider": Slider },
})
export default class MediaVolSetting extends Vue {
  mediaType!: "audio" | "video";
  value!: number;
  playVol = 0;
  actived = false;
  created() {
    this.playVol = this.value;
    this.$watch("playVol", this.syncPlayVolumn);
  }
  private syncPlayVolumn(nv: number) {
    console.log(nv)
    document.querySelectorAll("audio").forEach((e) => (e.volume = nv));
  }
}
</script>

<style lang="scss" scoped>
.media-vol-panel {
  .el-icon-mic {
    width: 100%;
    height: 100%;
  }
  .slider {
    top: 100%;
    z-index: 99;
  }
  &.panel-active {
    color: cyan;
  }
}
</style>
