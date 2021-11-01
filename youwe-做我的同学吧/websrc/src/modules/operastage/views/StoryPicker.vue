<template>
    <ion-modal is-open="true" css-class="my-custom-class">
        <ion-page>
            <ion-header>
                <ion-toolbar>
                    <ion-buttons slot="start">
                        <ion-back-button color="primary" @click="tryBack" text="返回"></ion-back-button>
                    </ion-buttons>
                    <ion-title>请选择故事</ion-title>
                    <!-- <ion-badge slot="end">{{ stateText }}</ion-badge> -->
                </ion-toolbar>
            </ion-header>
            <ion-content>
                <ion-list>
                    <ion-item v-for="story in list">
                        <ion-text>{{ story.name }}</ion-text>
                        <ion-button slot="end" size="small" @click="bindStory(story)">选择</ion-button>
                    </ion-item>
                </ion-list>
            </ion-content>
        </ion-page>
    </ion-modal>
</template>

<script lang="ts" setup>
import { revoke } from '@/modules/core';
import { IonPage, IonContent, IonTitle, IonToolbar, IonButtons, IonHeader, IonBackButton, IonList, IonItem, IonText, IonButton, IonModal } from '@ionic/vue';
import { reactive } from '@vue/reactivity';
import { useRouter } from 'vue-router';
import { OperaStorys } from '../deps';
const router = useRouter()
const tryBack = () => () => router.go(-2)
const props = defineProps(["record"])
const list = reactive<any[]>([])
revoke("opera.story.picklist").then(([err, res]) => {
    if (err) {
        return
    }
    console.log(res)
    list.push(...res.list)
})
const emitor = defineEmits(["update"])
const bindStory = (story) => {
    console.log({story,props})
    console.log({ storyId: story._id, recordId: props.record._id })
    revoke("opera.story.bind", { storyId: story._id, recordId: props.record._id }).then(([err, res]) => {
        if (err) {
            return
        }
        console.log(res)
        emitor("update", { data: { storyId: res } })
    })
}
</script>

<style scoped lang="scss">
</style>