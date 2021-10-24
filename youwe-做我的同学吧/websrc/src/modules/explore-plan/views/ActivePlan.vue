<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-back-button text="返回"></ion-back-button>
                </ion-buttons>
                <ion-title>激活计划</ion-title>
            </ion-toolbar>
        </ion-header>

        <ion-content class="ion-padding" v-if="plan">
            <form @submit="submit">
                <ion-item>
                    <ion-input placeholder="计划名称" disabled="true" :value="plan.name"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>{{ plan.types[0]?.name }}-{{ plan.types[1]?.name }}</ion-label>
                    <ion-button>修改类型</ion-button>
                </ion-item>
                <ion-item>
                    <ion-textarea
                        value="做我的同学吧~"
                        placeholder="稍微详细描述下你想要达到的目标、计划学习的内容会更容易找到合适的伙伴"
                        @ionChange="($event) => data.desc = $event.detail.value"
                    ></ion-textarea>
                </ion-item>
                <ion-item>
                    <ion-label>起点</ion-label>
                    <ion-datetime
                        :value="plan.startTime"
                        month-short-names="一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月"
                        :month-names="months"
                        disabled="true"
                        display-format="YYYY MMM D HH:mm"
                    ></ion-datetime>
                </ion-item>
                <ion-item>
                    <ion-label>终点</ion-label>
                    <ion-datetime
                        :value="plan.closeTime"
                        month-short-names="一月,二月,三月,四月,五月,六月,七月,八月,九月,十月,十一月,十二月"
                        :month-names="months"
                        disabled="true"
                        display-format="YYYY MMM D HH:mm"
                    ></ion-datetime>
                </ion-item>
                <ion-button style="margin-top: 2rem;" expand="full" color="primary" type="submit">激活</ion-button>
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
    useBackButton,
} from "@ionic/vue";
import { computed, inject, onMounted, provide, reactive, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map((v) => `${v}月`);
import PlanType1 from "./PlanType1.vue";
import { startPickType, showModal } from "../plantype.service";
import { showConfirm, showWarn } from "@/modules/overlay/alert";
import { revoke, useMongo } from "@/modules/core";
import { clampTime } from "../../../../shared/core";
import { getGoingList, mineShips } from "../plan.data";
import { IUserProfile } from "shared/types/user";
import { Ship } from "../deps";
import { hideLoading, showLoading } from "@/modules/overlay/loading";
import { MineShips } from "@/meteor";
const route = useRoute();
const goingList = getGoingList()
const plan = computed(() =>
    goingList.length > 0
        ? {
            ...goingList[0],
            startTime: new Date(goingList[0].startAt * 1000).toISOString(),
            closeTime: new Date(goingList[0].closeAt * 1000).toISOString(),
        }
        : null
);

const data = reactive({
    desc: "做我的同学吧，一起探索~~",
    types: [] as any[],
});
const user = inject<IUserProfile>("user")
const router = useRouter();
const submit = async (ev: Event) => {
    ev.preventDefault()
    if (!data.desc) return showWarn("还是填一下描述吧，或者用默认的")
    const ship = mineShips?.[0]
    if (!ship) {
        const res = await showConfirm("暂无飞船嘞，时候创建还是加入别人的飞船了")
        if (!res) return
        await showLoading()
        const [err1, res1] = await revoke("explore-plan.ship", plan.value._id, { desc: data.desc })
        await hideLoading()
        if (err1) return
        const res2 = await showConfirm("飞船准备就绪，立刻激活飞行计划吗？")
        if (!res2) return
    }
    await showLoading()
    const [err2, res2] = await revoke("explore-plan.active", { planId: plan.value._id, })
    await hideLoading()
    console.log([err2, res2])
    if (!err2) {
        const type = plan.value.types[1].key;
        let prefix;
        if (type === "word-card") prefix = '/poemcard/room/'
        if (type === "piying") prefix = '/opera/piyin/i/'
        router.push(prefix + res2)
    }

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
