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
import { hideLoading, showLoading } from '@/modules/overlay/loading';
import { revoke } from '@/modules/core';
const props = defineProps({
    data: Object
})
const data = computed(() => props.data || {})
const router = useRouter()

const join = async (ev: any) => {
    setTarget(data.value)
    const t = data.value
    if (!t.members?.includes(Meteor.userId())) {
        showLoading('加入房间中...')
        const [err, res] = await revoke("explore-plan.join", t._id)
        hideLoading()
        if(err) return
    }
    router.push(`/poemcard/room/${data.value._id}`)
    ev.preventDefault()
}
</script>

<style scoped lang="scss">
</style>