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
                        placeholder="请输入6-18位密码"
                        @ionChange="($event) => credentials.password = $event.detail.value"
                    ></ion-input>
                </ion-item>
                <ion-item>
                    <ion-input
                        type="password"
                        placeholder="请再次输入密码"
                        @ionChange="($event) => credentials.password1 = $event.detail.value"
                    ></ion-input>
                </ion-item>
                <ion-button @click="trySignup" expand="full" color="primary">注册</ion-button>
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
import { reactive } from "vue";
import { useRouter } from "vue-router";
import { signup } from "../service";

const credentials = reactive({
    username: "",
    password: "",
    password1: ""
});
const router = useRouter();
const trySignup = () => {
    if (credentials.password.length < 6 || credentials.password.length > 18) return alert("密码在6-18位")
    if (credentials.password !== credentials.password1) return alert("两次输入的密码不一致")
    signup(credentials).then(([err]) => {
        !err && router.back();
    })
};

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