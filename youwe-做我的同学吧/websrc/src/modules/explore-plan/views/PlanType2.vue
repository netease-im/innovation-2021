<template>
    <ion-header translucent>
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button @click.prevent="back" text="返回"></ion-back-button>
            </ion-buttons>
            <ion-title>子类型</ion-title>
            <ion-buttons slot="end">
                <ion-button @click.stop="cancelPick">关闭</ion-button>
            </ion-buttons>
        </ion-toolbar>
    </ion-header>
    <ion-content fullscreen>
        <ion-list>
            <ion-item @click="pick1(item)" v-for="item in subList">{{ item.name }}</ion-item>
        </ion-list>
    </ion-content>
</template>

<script lang="ts" setup>
import { modalController, IonContent, IonItem, IonBackButton, IonItemGroup, IonItemDivider, IonList, IonButton, IonTitle, IonToolbar, IonButtons, IonHeader, IonNavLink } from '@ionic/vue';
import { computed, inject, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { cancelPick, confirmPick, subList } from "../plantype.service";
const nav = document.querySelector<any>("ion-nav")
const pick1 = item => {
    confirmPick(item)
}
const back = () => nav.pop()

</script>

<style scoped lang="scss">
</style>