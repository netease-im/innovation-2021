<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>创建计划</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding">
            <form @submit="submit(); $event.preventDefault()">
                <ion-item>
                    <ion-input
                        placeholder="计划名称"
                        @ionChange="($event) => data.name = $event.detail.value"
                    ></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>{{ data.types[0]?.name }}-{{ data.types[1]?.name }}</ion-label>
                    <ion-button @click="pickType">选择类型</ion-button>
                    <!-- <ion-input
                        placeholder="计划类型"
                        @ionChange="($event) => data.type = $event.detail.value"
                    ></ion-input>-->
                </ion-item>
                <!-- <ion-item>
                    <ion-textarea
                        placeholder="计划描述"
                        @ionChange="($event) => data.name = $event.detail.value"
                    ></ion-textarea>
                </ion-item>-->
                <ion-item>
                    <ion-label>起点</ion-label>
                    <ion-datetime
                        :min="now"
                        :value="now"
                        month-short-names="一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月"
                        :month-names="months"
                        display-format="YYYY MMM D HH:mm"
                        @ionChange="data.startTime = $event.detail.value"
                    ></ion-datetime>
                </ion-item>
                <ion-item>
                    <ion-label>终点</ion-label>
                    <ion-datetime
                        :min="data.startTime"
                        :value="data.closeTime"
                        month-short-names="一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月"
                        :month-names="months"
                        display-format="YYYY MMM D HH:mm"
                        @ionChange="data.closeTime = $event.detail.value"
                    ></ion-datetime>
                </ion-item>
                <ion-button expand="full" color="primary" type="submit">创建</ion-button>
            </form>
        </ion-content>
        <ion-modal :is-open="showModal" css-class="my-custom-class">
            <ion-nav :root="PlanType1"></ion-nav>
        </ion-modal>
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
    IonDatetime,
    IonTextarea,
    IonModal,
    IonNav,
    useBackButton
} from "@ionic/vue";
import { onMounted, provide, reactive, ref } from "vue";
import { useRouter } from "vue-router";
const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map(v => `${v}月`)
import PlanType1 from "./PlanType1.vue";
import { startPickType, showModal } from "../plantype.service";
import { showConfirm, showWarn } from "@/modules/overlay/alert";
import { revoke } from "@/modules/core";
import { clampTime } from "../../../../shared/core";
import { hideLoading, showLoading } from "@/modules/overlay/loading";

const now = new Date().toISOString()
const endDef = new Date(Date.now() + 300000).toISOString()
const data = reactive({
    name: "",
    startTime: now,
    closeTime: endDef,
    desc: "",
    types: [] as any[]
});
const router = useRouter();
const submit = async () => {
    if (!data.name || data.types.length < 2) return showWarn('计划名和类型必不可少呀')
    await showLoading()
    const startAt = clampTime(data.startTime)
    const [err, id] = await revoke("explore-plan.add", {
        name: data.name,
        startAt,
        closeAt: clampTime(data.closeTime),
        types: data.types
    })
    console.log([err, id])
    await hideLoading()
    if (!err) {
        if (startAt <= clampTime(Date.now())) {
            const res = await showConfirm("开始时间到啦！是时候前往激活计划")
            if (res) {
                router.replace(`/explore/active-plan/${id}`)
                return
            }
        }
        router.go(-1)
    }
}
const pickType = () => startPickType().then(
    res => res && (data.types = [res.main, res.sub])
)

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