<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>新增角色</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <form ref="form" @submit="submit(); $event.preventDefault()">
                <ion-item>
                    <ion-input placeholder="角色名称" @ionChange="upd($event, 'name')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input placeholder="角色Key,资源名" @ionChange="upd($event, 'key')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input type="number" placeholder="角色宽" @ionChange="upd($event, 'width')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input type="number" placeholder="角色高" @ionChange="upd($event, 'height')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input type="number" placeholder="是否翻转" @ionChange="upd($event, 'flip')"></ion-input>
                </ion-item>
                <ion-button expand="full" color="primary" type="submit">创建</ion-button>
            </form>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { revoke } from "@/modules/core";
import { showConfirm, showWarn } from "@/modules/overlay/alert";
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
    "key": "",
    "name": "",
    "size": [100, 280],
    "flip": [0]
})
const form = ref<HTMLFormElement>()
const upd = (ev: any, key) => {
    const val = ev.detail.value;
    switch (key) {
        case "key":
        case "name":
            data[key] = val
            break;
        case "width":
            data.size[0] = parseInt(val) || 100;
            break;
        case "height":
            data.size[1] = parseInt(val) || 100;
            break;
        case "flip":
            data.flip[0] = parseInt(val) || 0;
            break;
        default:
            break;
    }
}
const submit = () => {
    if (!data.name || !data.key) return showWarn("剧名和描述不可少")
    revoke("opera.char.create", { ...data }).then(([err]) => {
        if (!err) {
            showConfirm("角色创建成功")
        }
        data.name = data.key = ""
        form.value?.reset()
    })
}
</script>

<style scoped>
</style>