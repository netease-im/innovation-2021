<template>
    <ion-item>
        <ion-badge slot="start">我的</ion-badge>
        <ion-text>{{ !!current ? current.name : "暂无进行中计划" }}</ion-text>
        <div slot="end" v-if="current">
            <ion-button size="small" v-if="going" @click="takeSeat">入座</ion-button>
            <ion-button size="small" v-else @click="activePlan">激活</ion-button>
        </div>
        <div slot="end" v-else>
            <ion-button size="small" @click="toCreate">创建</ion-button>
        </div>
    </ion-item>
</template>

<script lang="ts" setup>
import { MineShips, ShipRecords } from '@/meteor';
import { showWarn } from '@/modules/overlay/alert';
import { setTarget } from '@/modules/poemcard/data';
import { IonBadge, IonItem, IonButton, IonText } from '@ionic/vue';
import { computed } from '@vue/reactivity';

import { useRoute, useRouter } from 'vue-router';
import { ExplorePlans } from '../deps';
import { getGoingList, futureList, historyList, mineShips } from "../plan.data";
const goingList = getGoingList()
const router = useRouter()

const current = computed(() => goingList[0])
const going = computed(() => mineShips[0])
const activePlan = () => router.push(`/explore/active-plan/${current.value._id}`)
const toCreate = () => router.push("/explore/create-plan")
const takeSeat = () => {
    const item = going.value
    const rec = ShipRecords.findOne({ members: Meteor.userId()! })
    // console.log(plan)
    // if (!plan) return showWarn("计划不存在")
    // const ship = MineShips.findOne(item.shipId)
    if (!rec) {
        return router.push(`/explore/active-plan/${item._id}`)
    }
    console.log(rec)
    setTarget(rec)
    if (rec.type.endsWith("word-card"))
        router.push(`/poemcard/room/${rec._id}`)
}
</script>

<style scoped lang="scss">
</style>