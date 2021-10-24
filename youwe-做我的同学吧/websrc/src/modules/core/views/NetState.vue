<template>
    <IonProgressBar
        type="indeterminate"
        :color="offline ? 'danger' : 'primary'"
        :class="[showLoading ? 'connect-state' : 'hide connect-state']"
    ></IonProgressBar>
</template>

<script lang="ts" setup>
import { MTracker, Mcore } from '@/meteor';
import { IonProgressBar } from '@ionic/vue';
import { computed, ref } from 'vue';
const retry = ref(false);
let connected = false;
let offline = ref(false);
const showLoading = computed(() => offline.value || retry.value)
MTracker.autorun(() => {
    const state = Mcore.status()
    if (state.status === "connecting" || state.status === "waiting") {
        retry.value = true
        offline.value = false
    } else if (state.status === "offline") {
        if (connected) { offline.value = true }
    } else if (state.status === "connected") {
        connected = true;
        retry.value = false
        offline.value = false
    }
    // console.log(showLoading.value)
})

</script>

<style scoped lang="scss">
.connect-state {
    position: fixed;
    top: 0;
    transition: all 1s ease-in-out;
    &.hide {
        top: -10px;
    }
}
</style>