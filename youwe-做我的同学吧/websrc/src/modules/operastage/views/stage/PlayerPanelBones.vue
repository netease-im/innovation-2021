<template>
  <teleport to="body">
    <section
      class="body-controller"
      @contextmenu.stop="stop"
      v-show="show"
      :style="{ left: pos.x }"
    >
      <h3>身体控制</h3>
      <div class="fcol cnty" @click="handleClick">
        <div class="bone" id="bz-2">头部</div>
        <div class="bone cz" id="bz-1">颈部</div>
        <div class="frow">
          <div class="fcol zz">
            <div class="bone cz" id="zb-1">左臂1</div>
            <div class="bone cz" id="zb-2">左臂2</div>
            <div class="bone cz" id="zs-1">左手</div>
          </div>
          <div class="f1 fcol cnty">
            <div class="bone" id="st-2">身体2</div>
            <div class="bone" id="st-1">身体1</div>
            <div class="bone" id="zx">中心-丹田</div>
          </div>
          <div class="fcol yz">
            <div class="bone cz" id="yb-1">右臂1</div>
            <div class="bone cz" id="yb-2">右臂2</div>
            <div class="bone cz" id="ys-1">右手</div>
          </div>
        </div>
        <div class="frow">
          <div class="zz">
            <div class="bone cz" id="zt-1">左腿1</div>
            <div class="bone cz" id="zt-2">左腿2</div>
            <div class="bone" id="zj-1">左脚1</div>
            <div class="bone" id="zj-2">左脚2</div>
          </div>
          <div style="width:20px"></div>
          <div class="yz">
            <div class="bone cz" id="yt-1">右腿1</div>
            <div class="bone cz" id="yt-2">右腿2</div>
            <div class="bone" id="yj-1">右脚1</div>
            <div class="bone" id="yj-2">右脚2</div>
          </div>
        </div>
      </div>
      <div>
        <div v-for="fj in fbone" :key="fj.name">{{ fj.name }}</div>
      </div>
    </section>
  </teleport>
</template>

<script lang="ts">
// @ is an alias to /src
import { computed, defineComponent, reactive, ref, watch } from "vue";
import { managers } from "../../services";

export default defineComponent({
  name: "PlayerPanelBones",
  props: {
    myItem: {
      type: Object,
    },
  },
  setup(props) {
    const bones = ref<any>([]);
    const fbone = ref<any>([]);
    const pos = reactive({ x: "px", y: "0px" });
    const show = ref(false);
    let mychar = null as any;
    managers.stage?.on("right-click-char", ({ x, y }) => {
      if (!show.value) {
        const e = mychar.view.getBounds();
        pos.x = (e.left > 500 ? x - e.width - 400 : x + e.width) + "px";
        show.value = true;
      } else {
        show.value = false;
      }
    });
    managers.stage?.on("char-mine", (e) => {
      mychar = e;
      if (e) {
        const b = e.view.armature._armatureData.bones;
        const t = {} as any;
        const ff = [];
        for (const key of Object.keys(b)) {
          if (key.startsWith("e-")) {
            ff.push({
              name: key.slice(2),
              data: b[key].transform,
            });
          } else if (key.startsWith("n-")) {
            t[key.slice(2)] = b[key].transform;
          }
        }
        bones.value.splice(0, fbone.value.length, ...ff);
        bones.value = t;
        console.log({ ff, t });
      }
    });
    return {
      bones,
      fbone,
      pos,
      show,
      stop(e: Event) {
        e.stopPropagation();
        e.preventDefault();
      },
    };
  },
  methods: {
    async move(v: string) {},
    handleClick(e: MouseEvent) {
      const t = e.target as HTMLDivElement;
      if (t?.id) {
        const key = t.id;
        console.log(this.bones[key]);
      }
    },
  },
});
</script>

<style lang="scss" scoped>
section.body-controller {
  position: fixed;
  background-color: rgba(255, 255, 255, 0.9);
  top: 20%;
  height: 60%;
  width: 400px;
  .bone {
    padding: 4px;
    border-radius: 4px;
    border: 1px solid grey;
    text-align: center;
    transform: perspective(2);
    box-shadow: 2px 3px 2px grey;
    position: relative;
  }
  .cz {
    max-width: 2rem;
  }
  #st-1,
  #st-2 {
    height: 4rem;
    width: 3rem;
  }
  // .zz {
  //   transform: rotate(30deg) translateX(-30px);
  // }
  // .yz {
  //   transform: rotate(-30deg) translateX(30px);
  // }
  #bz-2 {
    width: 60px;
    height: 80px;
    border-radius: 20px;
  }
  #zj-1 {
    right: 8px;
  }
  #zj-2 {
    right: 16px;
  }
}
</style>
