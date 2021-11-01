<template>
    <ion-item class="profile" @click="handleClick">
        <ion-avatar slot="start">
            <img src="https://gravatar.com/avatar/dba6bae8c566f9d4041fb9cd9ada7741?d=identicon&f=y" />
        </ion-avatar>
        <ion-label>{{ hasLogin ? user!.name : '点击登录' }}</ion-label>
    </ion-item>
</template>

<script lang="ts" setup>
import { IonLabel, IonChip, IonAvatar, IonItem } from '@ionic/vue';
import { IUserProfile } from 'shared/types/user';
import { computed, inject, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
const user = inject<IUserProfile>("user")
const hasLogin = computed(() => !!user?._id)
console.log(user)
const router = useRouter()
const handleClick = () => {
    router.push(hasLogin.value ? '/mine' : '/signin').catch(console.error)
}

</script>

<style scoped lang="scss">
.profile {
    margin-bottom: 0.75rem;
}
</style>