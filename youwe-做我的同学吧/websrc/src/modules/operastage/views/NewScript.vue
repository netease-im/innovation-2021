<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>新增剧本</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <form ref="form" @submit="submit(); $event.preventDefault()">
                <ion-item>
                    <ion-input placeholder="剧名" @ionChange="upd($event, 'name')"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-textarea placeholder="描述" @ionChange="upd($event, 'desc')"></ion-textarea>
                </ion-item>
                <ion-item>
                    <ion-label>角色：{{ data.charactors.length }}</ion-label>
                    <ion-button slot="end" @click="showPick = true">选择</ion-button>
                </ion-item>
                <ion-item>
                    <ion-input
                        type="string"
                        placeholder="舞台"
                        @ionChange="upd($event, 'stageImages')"
                    ></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input type="string" placeholder="背景乐" @ionChange="upd($event, 'bgm')"></ion-input>
                </ion-item>
                <ion-button expand="full" color="primary" type="submit">创建</ion-button>
            </form>
        </ion-content>
        <ion-modal :is-open="showPick" css-class="my-custom-class">
            <ion-page>
                <ion-header>
                    <ion-toolbar>
                        <ion-buttons slot="start">
                            <ion-back-button color="primary" @click="showPick = false" text="返回"></ion-back-button>
                        </ion-buttons>
                        <ion-title>
                            请选择角色
                            <ion-icon name="heart"></ion-icon>
                        </ion-title>
                    </ion-toolbar>
                </ion-header>
                <ion-content>
                    <ion-list>
                        <ion-item v-for="role in roles" @click="pickRole(role)">
                            <ion-text
                                :color="data.charactors.includes(role._id) ? 'success' : 'primary'"
                            >{{ role.name }}</ion-text>
                        </ion-item>
                    </ion-list>
                </ion-content>
            </ion-page>
        </ion-modal>
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
    IonText,
    IonIcon,
    IonModal,
    IonList,
    IonTextarea
} from "@ionic/vue";
import { onMounted, provide, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { OperaCharactors } from "../deps";
const data = reactive({
    "name": "",
    "desc": "",
    "charactors": [] as any[],
    "stageImages": [] as any[],
    "bgm": [] as any[],
})
const showPick = ref(false)
const roles: any[] = OperaCharactors.find().fetch()
const form = ref<HTMLFormElement>()
const pickRole = (role) => {
    const oi = data.charactors.indexOf(role._id)
    if (oi < 0) data.charactors.push(role._id)
    else data.charactors.splice(oi, 1)
}
const upd = (ev: any, key) => {
    const val = ev.detail.value;
    switch (key) {
        case "desc":
        case "name":
            data[key] = val
            break;
        case "bgm":
        case "stageImages":
            data[key][0] = val;
            break;
    }
}
const submit = () => {
    if (data.charactors.length < 2) return showWarn("角色至少两个吧")
    if (!data.name || !data.desc) return showWarn("剧名和描述不可少")
    if (!data.stageImages[0]) return showWarn("舞台图片不可少")
    revoke("opera.script.create", { ...data }).then(([err]) => {
        if (!err) {
            showConfirm("剧本创建成功")
        }
        data.name = data.desc = ""
        data.charactors.splice(0)
        data.stageImages.splice(0)
        data.bgm.splice(0)
        form.value?.reset()
    })
}
</script>

<style scoped>
</style>