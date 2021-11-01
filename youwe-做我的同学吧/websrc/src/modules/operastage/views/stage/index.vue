<template>
  <section class="fcol">
    <div class="stage" @contextmenu="prev">
      <!-- <div class="hider-l"></div> -->
      <canvas ref="stageEl" id="stageCvs" style="width:100vw;" width="2000" height="900"></canvas>
      <!-- <div class="hider-r"></div> -->
    </div>
    <PlayerPanel @pick="handPick" @exec="handExec" :items="charactors" />
    <!-- <PlayerPanelBones v-if="isPlayer" /> -->
    <BGMPlayer ref="bgmPlayer" />
  </section>
</template>

<script lang="ts" setup>
// @ is an alias to /src
import BGMPlayer from "./BGMPlayer.vue";
import PlayerPanel from "./PlayerPanel.vue";
// import PlayerPanelBones from "./PlayerPanelBones.vue";

// import { managers, roomData, roomService } from "../../services";
import { inject, onMounted, provide, ref } from "vue";
import { OperaCharactors, OperaStorys } from "../../deps";
import { managers, roomService } from "../../services";
import { joinRoom } from "@/modules/media/services/room";

const props = defineProps(['storyId'])

const storyId = props.storyId
console.log(storyId)

const story = OperaStorys.findOne(storyId)


const stageBrief = inject<any>("stage-brief")
console.log(story, stageBrief)
const uid = inject<any>("user")?._id;



const charactors = story!.charactors.map(e => {
  return OperaCharactors.findOne(e)
})

console.log(charactors)
const bgmPlayer = ref()
const stageEl = ref()
const { roomId, charactorsInStage } = roomService.setupConnect(story)

provide("roomId", roomId)
onMounted(() => {
  bgmPlayer.value.setVal(0.1)
  bgmPlayer.value.play(story?.bgm[0])
  const canvas = stageEl.value as HTMLCanvasElement;
  roomService.init(canvas);
  console.log(canvas)
})

const handPick = (v: string) => {
  console.log(`pick ${v}`)
  // const i = this.charactorsInstage.findIndex(
  //   (el: any) => el.uid === roomData.uid
  // );
  // const newArr = this.charactorsInstage.slice(0);
  // log(newArr);
  // let changed = false;
  // if (i > -1) {
  //   // delete my  role
  //   const del = newArr.splice(i, 1);
  //   managers.removeChar(del[0].id);
  //   changed = true;
  // }
  // if (v) {
  //   if (!this.charactorsInstage.some((el: any) => el.name === v)) {
  //     const i1 = this.charactors.findIndex((el: any) => el.name === v);
  //     if (i1 > -1) {
  //       newArr.push(
  //         Object.assign({ uid: roomData.uid }, this.charactors[i1])
  //       );
  //     }
  //     changed = true;
  //   } else {
  //     showAlert(`角色${v}已经被别人选了`, "warning");
  //   }
  // }
  // if (changed) {
  //   roomService.updateCharactors(newArr);
  // }
}
const handExec = ({ name, data }: { name: string; data: any }) => {
  managers.stage?.exec(name, data);
}
const prev = (e: any) => {
  e?.preventDefault();
}
joinRoom({
  appkey: "9aae0d6071ab747c5e48493e426a9a3f",
  channelName: roomId,
  uid: Date.now(),
  token: null
})
</script>

<style lang="scss" scoped>
.stage {
  width: 100vw;
}
.player .hider-l,
.player .hider-r {
  display: none;
}
// .hider-r,
// .hider-l {
//   width: 10%;
//   position: absolute;
//   height: 100%;
//   background-color: rgba(0, 0, 0, 0.99);
//   z-index: 10;
// }
// .hider-r {
//   top: 0;
//   right: 0;
// }
// .hider-l {
//   left: 0;
// }
#stageCvs {
  margin: auto;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
}
// .player #stageCvs {
//   left: 0;
// }
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
