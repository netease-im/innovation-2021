<template>
    <ion-page>
        <ion-header :translucent="true">
            <ion-toolbar>
                <ion-buttons slot="start">
                    <ion-menu-button color="primary"></ion-menu-button>
                </ion-buttons>
                <ion-title>关于</ion-title>
            </ion-toolbar>
        </ion-header>
        <ion-content>
            <ion-slides style="height: 100%;" pager="true" :options="slideOpts">
                <ion-slide>
                    <div>
                        <h1>创建计划并激活时，会分配到一艘XShip，它将载着你在学海和人生的浪花中前行~</h1>
                        <h2>XShip是一艘特殊的时间飞船，行驶在无尽的知识海洋，载着充满好奇心的人们前往每个角落。只需要设置起止时间点开启航线，或者加入他人的航线即可开始探索之旅。</h2>
                        <h3>它不受时间约束24小时起航，它不受空间约束，能够遨游星辰大海，也能进入量子世界，因为它是“虚构的”，或许哪天会成真吧！</h3>
                    </div>
                </ion-slide>
                <ion-slide>
                    <div>
                        <h1>做我的同学吧！共乘星舰游学海</h1>
                        <h3>两个思念的交流，获得的往往不只是两个思想；所以不断尝试，找到最好的伙伴将会让你的旅途更轻松、更快乐~</h3>
                    </div>
                </ion-slide>
                <ion-slide>
                    <div>
                        <h1>做我的同学吧！同进同退百年缘</h1>
                        <h3>知识赐予力量，让我们给予彼此力量，一起跨越人生的难杆@@</h3>
                    </div>
                </ion-slide>
            </ion-slides>
        </ion-content>
    </ion-page>
</template>

<script lang="ts" setup>
import { IonPage, IonContent, IonSlide, IonSlides, IonTitle } from '@ionic/vue';
import { computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const slideOpts = {
    initialSlide: 1,
    speed: 400
};
</script>

<style scoped lang="scss">
ion-slide {
    width: 100vw;
}
</style>