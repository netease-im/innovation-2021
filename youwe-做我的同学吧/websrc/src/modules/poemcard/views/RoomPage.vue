<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button color="primary" @click="tryBack" text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>{{ targetRoom!.name }}</ion-title>
                <!-- <ion-badge slot="end">{{ stateText }}</ion-badge> -->
            </ion-toolbar>
        </ion-header>
        <ion-content :scroll-y="false">
            <PraStage v-if="poemId" :profile="info[0]" />
            <div v-else>
                <PoemPicker :session="targetRoom._id" />
            </div>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { IonPage, IonContent, IonTitle, IonToolbar, IonButtons, IonHeader, IonBackButton, IonBadge } from '@ionic/vue';
import PoemPicker from "./PoemPicker.vue";

import PraStage from "./practice.vue"
import { targetRoom } from "../data"
import { useRoute, useRouter } from 'vue-router';
import { computed, onUnmounted, provide, reactive, watch } from 'vue';
import { showConfirm } from '@/modules/overlay/alert';
import { joinRoom, leftRoom } from "../../media/services/room"
import { MineShips, ShipRecord, ShipRecords, ShipsWaiting } from '@/meteor';
import { makeCollectionReactive } from '@/modules/core/mapCollection';
const router = useRouter()
const route = useRoute()
const roomId = route.params.id;
if (!roomId) {
    router.go(-1)
}
provide("roomId", roomId)

const info = reactive<ShipRecord[]>([])

makeCollectionReactive(info, ShipRecords, roomId)

const poemId = computed(() => info[0]?.data?.poemId)
targetRoom.ship = MineShips.findOne(targetRoom.shipId)

const tryBack = (ev) => {
    if (info[0]?.state !== 1) {
        showConfirm("正在游戏中，确认要退出吗").then(v => v && router.go(-1))
    } else {
        router.go(-1)
    }
    ev.preventDefault()
}
joinRoom({
    appkey: "9aae0d6071ab747c5e48493e426a9a3f",
    channelName: route.params.id,
    uid: Date.now(),
    token: null
})

onUnmounted(() => leftRoom())
</script>

<style scoped lang="scss">
</style>