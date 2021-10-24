<template>
  <img id="mainCanvas" :src="drama?.background" :style="{ height }" width="600" height="450" />
</template>

<script lang="ts">
import { Application, Graphics, Rectangle, Container } from "pixi.js";
// @ is an alias to /src
import { Options, Vue } from "vue-class-component";
const resourcePrefix = "/static/resources";

let ctx: CanvasRenderingContext2D | null;

@Options({
  name: "StagePre",
  props: {
    drama: {
      type: Object,
    },
    viewMode: {
      type: String,
      default: "mobile",
    },
    user: {
      type: Object
    },
  },
})
export default class Stage extends Vue {
  readonly drama!: any;
  readonly viewMode!: string;
  get height() {
    const aw = document.body.clientWidth;
    const ah = document.documentElement?.clientHeight || 600;
    return Math.round(Math.min((aw * 9) / 16, (ah * 3) / 5)) + "px";
  }
}
</script>

<style lang="scss" scoped>
#mainCanvas {
  width: 100%;
  background-color: black;
}
section.controller {
  padding: 0.5rem;
  .ani-picker {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    margin-left: 1rem;
    .ani-item {
      margin: 0 0.25rem 0 0;
    }
  }
  .tip {
    color: white;
    margin: 0 0.5rem;
    font-size: 0.8rem;
  }
  .params {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    padding: 0.5rem;
    .distance {
      display: flex;
      align-items: center;
    }
  }
}
</style>
