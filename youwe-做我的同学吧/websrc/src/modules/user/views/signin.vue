<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>登录</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <form @submit="$event.preventDefault()">
                <ion-item>
                    <ion-input
                        placeholder="帐号id或者邮箱"
                        @ionChange="($event) => credentials.username = $event.detail.value"
                    ></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input
                        type="password"
                        placeholder="请输入密码"
                        @ionChange="($event) => credentials.password = $event.detail.value"
                    ></ion-input>
                </ion-item>
                <ion-button @click="doLogin" expand="full" color="primary">登录</ion-button>
                <ion-button @click="toSignup" expand="full" color="secondary">注册</ion-button>
            </form>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
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
} from "@ionic/vue";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { signin } from "../service";

const credentials = ref<{ username: string; password: string }>({
    username: "",
    password: "",
});
const router = useRouter();
const doLogin = () => {
    signin(credentials.value).then(([err]) => {
        !err && router.back();
    })
};
const toSignup = () => router.replace('/signup')
</script>

<style scoped>
form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100%;
}
ion-item {
    --padding-start: 0;
}
ion-button {
    margin-top: 0.75rem;
}
</style>