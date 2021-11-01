<template>
    <ion-header translucent>
        <ion-toolbar>
            <ion-title>主类型</ion-title>
            <ion-buttons slot="end">
                <ion-button @click="cancelPick">关闭</ion-button>
            </ion-buttons>
        </ion-toolbar>
    </ion-header>
    <ion-content fullscreen>
        <ion-list>
            <ion-item @click="pick(item)" v-for="item in mainList">
                <ion-text>{{ item.name }}</ion-text>
                <ion-label slot="end" v-if="item.key === choice.main">已选中</ion-label>
            </ion-item>
        </ion-list>
    </ion-content>
</template>

<script lang="ts" setup>
import { modalController, IonContent, IonItem, IonLabel, IonItemGroup, IonItemDivider, IonList, IonButton, IonTitle, IonToolbar, IonButtons, IonHeader, IonText } from '@ionic/vue';
import { computed, inject, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import PlanType2 from "./PlanType2.vue";

import { pickMain, choice, mainList, cancelPick } from "../plantype.service";
const nav = document.querySelector<any>("ion-nav")
const pick = item => {
    pickMain(item)
    nav.push(PlanType2)
}
</script>

<style scoped lang="scss">
ion-item {
    display: flex;
    justify-content: space-between;
}
</style>