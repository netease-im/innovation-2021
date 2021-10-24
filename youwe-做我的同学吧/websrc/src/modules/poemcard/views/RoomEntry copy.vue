<template>
    <div>
        <p>
            <ion-text>{{ data.desc }}</ion-text>
        </p>
        <ion-button @click="join" size="small">加入</ion-button>
    </div>
</template>

<script lang="ts" setup>
import { IonButton, IonText, } from '@ionic/vue';

import RoomStage from "./room.vue"

import { computed, reactive, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { setTarget, targetRoom } from '../data';
const props = defineProps({
    data: Object
})
const data = computed(() => props.data || {})
const router = useRouter()

const join = () => {
    setTarget(data.value)
    router.push(`/poemcard/room/${data.value._id}`)
}
</script>

<style scoped lang="scss">
</style>