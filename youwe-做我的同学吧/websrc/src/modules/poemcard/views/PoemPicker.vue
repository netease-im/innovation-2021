<template>
    <ion-modal is-open="true" css-class="my-custom-class">
        <ion-page>
            <ion-header>
                <ion-toolbar>
                    <ion-buttons slot="start">
                        <ion-back-button color="primary" @click="tryBack" text="返回"></ion-back-button>
                    </ion-buttons>
                    <ion-title>请选择诗歌</ion-title>
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
import { Poems } from '@/meteor';
import { revoke } from '@/modules/core';
import { IonPage, IonContent, IonTitle, IonToolbar, IonButtons, IonHeader, IonBackButton, IonList, IonItem, IonText, IonButton, IonModal } from '@ionic/vue';
import { reactive } from '@vue/reactivity';
import { useRouter } from 'vue-router';
const props = defineProps(["session"])
const router = useRouter()
const tryBack = () => () => router.go(-2)
const list = Poems.find().fetch()
console.log(list)



const emitor = defineEmits(["update"])
const bindStory = (item) => {
    console.log({ item, props })
    console.log({ storyId: item._id, session: props.session })
    revoke("poem.wordcard.bind", { id: item._id, session: props.session }).then(([err, res]) => {
        if (err) {
            return
        }
        console.log(res)
        emitor("update", { data: { poemId: res } })
    })
}
</script>

<style scoped lang="scss">
</style>