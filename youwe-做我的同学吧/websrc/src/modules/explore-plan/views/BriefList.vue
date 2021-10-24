<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-menu-button color="primary"></ion-menu-button>
                </ion-buttons>
                <ion-title>计划列表</ion-title>
            </ion-toolbar>
        </ion-header>
        <ion-content>
            <ion-list>
                <ion-item v-if="goingList.length < 1">
                    <div>暂无进行中计划</div>
                </ion-item>
                <ion-item v-else>
                    <ion-text>{{ goingList[0].name }}</ion-text>
                    <ion-button slot="end" @click="activate(goingList[0])">激活</ion-button>
                </ion-item>
                <ion-item-divider>
                    <ion-label>计划航线</ion-label>
                    <ion-button slot="end" @click="toCreate">创建</ion-button>
                </ion-item-divider>
                <ion-item-group>
                    <ion-item v-for="item of futureList">
                        <ion-text>{{ item.name }}</ion-text>
                        <div slot="end">
                            <ion-text>{{ item.closeDate }}</ion-text>
                        </div>
                    </ion-item>
                </ion-item-group>
                <ion-item-divider>
                    <ion-label>航行记录</ion-label>
                    <ion-button slot="end">更多</ion-button>
                </ion-item-divider>
                <ion-item-group>
                    <ion-item v-for="item of historyList">{{ item.name }}</ion-item>
                </ion-item-group>
            </ion-list>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { IonPage, IonContent, IonItem, IonLabel, IonItemGroup, IonItemDivider, IonList, IonButton, IonTitle, IonToolbar, IonButtons, IonHeader, IonMenuButton, onIonViewWillEnter, IonText } from '@ionic/vue';



import { computed, reactive, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ExplorePlans, fmtDate, timestamp } from '../deps';
import { getGoingList, futureList, historyList } from "../plan.data";
const goingList = getGoingList()
const router = useRouter()
const toCreate = () => router.push("/explore/create-plan")
const activate = (e: any) => router.push(`/explore/active-plan/${e._id}`)
</script>

<style scoped lang="scss">
</style>