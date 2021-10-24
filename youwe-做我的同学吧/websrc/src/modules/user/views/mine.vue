<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>我的资料</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <IonItem>
                <IonLabel >昵称</IonLabel>
                <IonText>{{user.name}}</IonText>
            </IonItem>
            <IonItem>
                <ion-button @click="logout" full=true>退出</ion-button>
            </IonItem>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import {
    IonButton,
    IonButtons,
    IonBackButton,
    IonContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar,
    IonText,
    IonItem,
    IonLabel,
} from "@ionic/vue";

import { inject } from "vue";
const user= inject<any>("user")
console.log(user)
const logout = ()=> {
    Accounts.logout();
    location.reload()
}

</script>

<style scoped>
form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100%;
}
ion-item {
    --padding-start: 0;
}
ion-button {
    margin-top: 0.75rem;
}
</style>