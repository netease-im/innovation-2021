<template>
  <ion-page v-if="target?.data?.storyId">
    <ion-header :translucent="true">
      <ion-toolbar>
        <ion-buttons slot="start">
          <ion-back-button color="primary" @click="tryBack" text="返回"></ion-back-button>
        </ion-buttons>
        <ion-title>{{ target?.name }}</ion-title>
        <!-- <ion-badge slot="end">{{ stateText }}</ion-badge> -->
      </ion-toolbar>
    </ion-header>
    <ion-content :scroll-y="false">
      <OperaStage :story-id="target.data.storyId"></OperaStage>
    </ion-content>
  </ion-page>
  <StoryPicker v-else :record="target" @update="updateStage"></StoryPicker>
</template>

<script lang="ts" setup>
import { IonPage, IonContent, IonTitle, IonToolbar, IonButtons, IonHeader, IonBackButton, IonBadge } from '@ionic/vue';
// import Stage from "./stage/index.vue";
import { defineAsyncComponent, inject, onUnmounted, provide, reactive, ref } from "vue";
// import { roomService } from "../services";
import StoryPicker from "./StoryPicker.vue";

const OperaStage = defineAsyncComponent(() => import("./stage/index.vue"))

// import RoomHeader from "./header.vue";

import { IUserProfile } from "shared/types/user";
import { hideLoading, showLoading } from "@/modules/overlay/loading";
import { useRoute, useRouter } from "vue-router";
import { showConfirm } from '@/modules/overlay/alert';
import { leftRoom } from '@/modules/media/services/room';

const target = reactive<any>(history.state.data);
console.log(target)
const updateStage = data => {
  for (const key in data) {
    target[key] = data[key]
  }
}
provide("stage-brief", target)

const user = inject<any>("user")

// if(!user?._id){
//   const router = useRouter()
//   router.replace("/signin")
// }

// const {
//   roomData,
//   viewMode,
//   players,
//   charactorsInStage,
// } = roomService.setupConnect(props, ctx);
// rtc.on(rtc.ROOM_EVENTS.POPUP_MESSAGE, ({ data, param }) => {
//   showAlert(data);
//   return true;
// });
// const isPlayer = computed(() => players.value.includes(roomData.uid));
// const stageData = reactive({
//   charactors: computed(() => roomData.charactors),
//   charactorsInStage,
//   info: computed(() => roomData.info),
//   story: computed(() => roomData.story),
//   user: roomData.uid,
//   isPlayer,
// });
const handleAction = async function ({ name, data }: any) {
  showLoading();
  // switch (name) {
  //   case "pick":
  //     this.players.push(data);
  //     await roomService.updatePlayers(this.players);
  //     break;
  //   case "drop":
  //     {
  //       const i = this.players.indexOf(data);
  //       if (i > -1) {
  //         this.players.splice(i, 1);
  //         await roomService.updatePlayers(this.players);
  //       }
  //     }
  //     break;

  //   default:
  //     break;
  // }
  hideLoading();
}
const router = useRouter()
const tryBack = (ev) => {
  showConfirm("确认要退出吗").then(v => v && router.go(-1))
  ev.preventDefault()
}

onUnmounted(() => leftRoom())
</script>

<style lang="scss" scoped>
@keyframes masked-animation {
  0% {
    background-position: 0 0;
  }
  100% {
    background-position: -100% 0;
  }
}
</style>
