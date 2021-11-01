<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-menu-button color="primary"></ion-menu-button>
                </ion-buttons>
                <ion-title>我的收藏</ion-title>
            </ion-toolbar>
        </ion-header>
        <ion-content>
            <ion-list>
                <ion-item
                    button
                    v-for="item in actions"
                    detail="true"
                    @click="router.push(item.url)"
                >
                    <ion-label>
                        <h3>{{ item.name }}</h3>
                    </ion-label>
                </ion-item>
            </ion-list>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { IonPage, IonContent, IonLabel, IonItem, IonList, IonToolbar, IonHeader, IonButtons, IonMenuButton, IonTitle } from '@ionic/vue';
import { computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const actions = [{
    name: "新增诗歌",
    url: "/poem/lines/create"
}, {
    name: "新增角色",
    url: "/opera/role/add"
}, {
    name: "新增剧本",
    url: "/opera/script/add"
}]
const router = useRouter()
</script>

<style scoped lang="scss">
</style>