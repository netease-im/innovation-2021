<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-title>欢迎入港-开始航行吧</ion-title>
                <ion-buttons slot="start">
                    <ion-menu-button color="primary"></ion-menu-button>
                </ion-buttons>
            </ion-toolbar>
            <ion-toolbar>
                <ion-segment @ionChange="currentSeg = $event.detail.value;" value="all">
                    <ion-segment-button value="all">
                        <ion-label>全部</ion-label>
                    </ion-segment-button>
                    <ion-segment-button value="favor">
                        <ion-label>收藏</ion-label>
                    </ion-segment-button>
                </ion-segment>
            </ion-toolbar>
        </ion-header>

        <ion-content :fullscreen="true">
            <ExplorePlanEntry />
            <ion-list id="container" v-show="currentSeg === 'all'">
                <ion-card v-for="i in mergedList">
                    <ion-card-header>
                        <ion-card-title>{{ i.data.name }}</ion-card-title>
                        <ion-card-subtitle style="display: flex;">
                            <ion-badge v-if="i.label">{{ i.label }}</ion-badge>
                            <ion-text style="margin-left: 10px;">舰长：{{ i.leader }}</ion-text>
                        </ion-card-subtitle>
                    </ion-card-header>
                    <ion-card-content>
                        <component :is="i.component" :data="i.data" v-if="i.component" />
                    </ion-card-content>
                </ion-card>
            </ion-list>
            <ion-list id="container" v-show="currentSeg === 'favor'">暂无收藏</ion-list>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { MineShips, ShipRecord, ShipRecords, ShipsWaiting } from '@/meteor';
import { ExplorePlanEntry } from '@/modules/explore-plan';
import { IonButtons, IonContent, IonHeader, IonMenuButton, IonPage, IonTitle, IonToolbar, IonSegment, IonSegmentButton, IonBadge, IonLabel, IonList, IonCard, IonCardHeader, IonCardTitle, IonCardContent, IonCardSubtitle, IonText } from '@ionic/vue';

import { computed, defineComponent, reactive, ref } from 'vue';
import { useRoute } from 'vue-router';
import { RoomEntryTypes } from '../home-entry';
import { makeCollectionReactive } from '../mapCollection';

const currentSeg = ref('all')

const allList = reactive<ShipRecord[]>([])
makeCollectionReactive(allList, ShipsWaiting, {})
const mergedList = computed(() => allList.map(data => {
    console.log(RoomEntryTypes)
    const compDef = RoomEntryTypes.get(data.type);
    const ret = { data } as any
    if (compDef) {
        ret.component = compDef.component;
        ret.label = compDef.name
    }
    ret.leader = Meteor.users.findOne(data.leaderId)?.profile?.name
    return ret
}))


</script>

<style scoped>
</style>