<template>
    <div>
        <p>
            <ion-text>{{ data.desc }}111</ion-text>
        </p>
        <ion-button @click="join" size="small">加入</ion-button>
    </div>
</template>

<script lang="ts" setup>
import { IonButton, IonText, } from '@ionic/vue';

import RoomStage from "./room.vue"

import { computed, reactive, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { deepCopy } from '../deps';
import { hideLoading, showLoading } from '@/modules/overlay/loading';
import { revoke } from '@/modules/core/api';

const props = defineProps({
    data: Object
})
const data = computed(() => props.data || {})
const router = useRouter()

const join = async (ev) => {
    const d = deepCopy(data.value)
    if (!d.members?.includes(Meteor.userId())) {
        showLoading('加入房间中...')
        const [err, res] = await revoke("explore-plan.join", d._id)
        hideLoading()
        if (err) return
    }
    router.push({
        path: `/opera/piyin/i/${d._id}`,
        state: {
            data: d
        }
    })
    // ev.preventDefault()
}
</script>

<style scoped lang="scss">
</style>