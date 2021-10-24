<template>
  <canvas id="dirPanel" :width="size" :height="size" :style="{ width, height }">
  </canvas>
</template>

<script lang="ts">
// @ is an alias to /src

import { log as blog } from "@/lib/hj/log";
import { Options, Vue } from "vue-class-component";
let lastReport = 0;
const log = blog.namespce("panel-log");
@Options({
  name: "DirPanelPlus",
  props: {
    size: { default: 160 },
    viewMode: { default: "pc" },
  },
  emits: {
    move: null,
  },
})
export default class DirPanelPlus extends Vue {
  size!: number;
  viewMode!: string;
  halfSize = 50;
  busy: any;
  repoter!: any;
  reportData!: any;
  get width() {
    return this.size + "px";
  }
  get height() {
    return this.size + "px";
  }
  ctx!: CanvasRenderingContext2D;
  top!: number;
  left!: number;
  mounted() {
    this.halfSize = Math.round(this.size / 2);
    this.$nextTick(() => {
      const el = document.querySelector("canvas#dirPanel") as HTMLCanvasElement;
      this.ctx = el.getContext("2d") as any;
      this.ctx.lineJoin = "round";
      this.setupHandlers(el);
      const t = this.$el.getClientRects()[0];
      this.top = t.top;
      this.left = t.left;
    });
  }
  setupHandlers(el: HTMLCanvasElement) {
    if (this.viewMode === "pc") {
      el.onmousedown = (e) => {
        log("mouse down");
        this.active(e.offsetX, e.offsetY);
        this.moving = true;
      };
      el.onmousemove = (e) => {
        if (!this.moving) {
          return;
        }
        log("mouse move");
        this.active(e.offsetX, e.offsetY);
        e.preventDefault();
      };
      el.onmouseup = (e) => {
        log("mouse up");
        if (!this.moving) return;
        this.deactive(e.offsetX, e.offsetY);
      };
      el.onmouseleave = this.actCancle;
      return;
    }
    const parsePos = (e: TouchEvent): [number, number] => {
      const t = e.touches[0] || e.changedTouches[0];
      if (!t) return [0, 0];
      const x = t.clientX - this.left;
      const y = document.body.scrollTop + t.clientY - this.top;
      return [x, y];
    };
    el.ontouchstart = (e) => {
      this.active(...parsePos(e));
      this.moving = true;
    };
    el.ontouchcancel = this.actCancle;
    el.ontouchmove = (e) => {
      if (!this.moving) {
        return;
      }
      e.preventDefault();
      this.active(...parsePos(e));
    };
    el.ontouchend = (e) => {
      if (!this.moving) return;
      this.deactive(...parsePos(e));
    };
  }
  moving = false;
  deactive(x: number, y: number) {
    this.actCancle();
    // this.report({
    //   dx: x - this.halfSize,
    //   dy: y - this.halfSize,
    // });
  }
  actCancle() {
    this.moving = false;
    this.ctx.clearRect(0, 0, this.size, this.size);
    clearTimeout(this.repoter);
    this.repoter = null;
  }
  active(x = 0, y = 0) {
    this.ctx.clearRect(0, 0, this.size, this.size);
    this.ctx.beginPath();
    this.ctx.moveTo(this.halfSize, this.halfSize);
    this.ctx.lineTo(x, y);
    const dx = x - this.halfSize;
    const dy = y - this.halfSize;
    const dist = Math.sqrt(dx ** 2 + dy ** 2);
    this.ctx.lineWidth = Math.round((dist / this.halfSize) * 8);
    this.ctx.closePath();
    this.ctx.stroke();
    const time = Math.max(
      1,
      Math.ceil((Math.sqrt((x || 0) ** 2 + (y || 0) ** 2) * 3) / this.halfSize)
    );
    this.report({ dx, dy, time });
  }
  report(data: any) {
    if (Math.abs(data?.dx) < 5 && Math.abs(data?.dy) < 5) {
      return;
    }
    this.reportData = data;
    if (Date.now() - lastReport < 200) {
      return;
    }
    lastReport = Date.now();
    let reped = this.reportData;
    this.$emit("move", reped);
  }
}
</script>

<style lang="scss" scoped>
canvas#dirPanel {
  flex: 0;
  border-radius: 50%;
  // background-color: rgba(255, 255, 255, 0.6);
  background-image: radial-gradient(
    rgba(255, 255, 255, 1),
    rgba(255, 255, 255, 0.6),
    rgba(255, 255, 255, 0.3)
  );
  // background: radial-gradient(
  //   center center,
  //   circle contain,
  //   white 0%,
  //   rgba(255, 255, 255, 0.8) 25%,
  //   rgba(255, 255, 255, 0.6) 40%,
  //   rgba(255, 255, 255, 0.5) 60%,
  //   rgba(255, 255, 255, 0.4) 80%,
  //   rgba(255, 255, 255, 0.3) 100%
  // );
}
</style>
