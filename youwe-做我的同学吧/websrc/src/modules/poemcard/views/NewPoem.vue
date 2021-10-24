<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>新增诗词</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <form ref="form" @submit="submit(); $event.preventDefault()">
                <ion-item>
                    <ion-input placeholder="名称" @ionChange="upd($event, 'name')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input placeholder="作者" @ionChange="upd($event, 'poet')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-textarea placeholder="诗句" @ionChange="upd($event, 'lines')"></ion-textarea>
                </ion-item>
                <ion-button expand="full" color="primary" type="submit">创建</ion-button>
            </form>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { revoke } from "@/modules/core";
import { showConfirm } from "@/modules/overlay/alert";
import {
    IonButton,
    IonButtons,
    IonBackButton,
    IonContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar,
    IonInput,
    IonItem,
    IonLabel,
    IonDatetime,
    IonTextarea,
    IonModal,
    IonNav,
    useBackButton
} from "@ionic/vue";
import { OperaCharactors } from "shared/meteor";
import { onMounted, provide, reactive, ref } from "vue";
import { useRouter } from "vue-router";
const data = reactive({
    "name": "",
    "poet": "",
    "lines": []
})
const form = ref<HTMLFormElement>()
const upd = (ev: any, key) => {
    const val = ev.detail.value;
    switch (key) {
        case "poet":
        case "name":
            data[key] = val
            break;
        case "lines":
            data.lines = val.split('\n').filter(e => !!e)
            break;
        default:
            break;
    }
}
const submit = () => {
    if (!data.name) return showWarn("名称不可少")
    if (data.lines.length < 1) return showWarn("诗句怎么少于一行")
    revoke("poem.lines.create", { ...data }).then(([err]) => {
        if (!err) {
            showConfirm("创建成功")
        }
        data.name = data.poet = ""
        data.lines.splice(0)
        form.value?.reset()
    })
}

function showWarn(arg0: string) {
    throw new Error("Function not implemented.");
}
</script>

<style scoped>
</style>