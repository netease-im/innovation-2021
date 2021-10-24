<template>
  <ion-badge style="position:fixed;right:10px;top:4px;z-index: 333;">{{ stateText }}</ion-badge>
  <ion-badge
    @click="showPoem=true"
    style="position:fixed;right:10px;top:28px;z-index: 333;"
  >{{ deskData.poem?.name }}</ion-badge>
  <div class="board fcol" v-if="deskData.allWords">
    <div class="used-zone f1">
      <div>异兽血量：{{ deskData.useScore }}</div>
      <div class="frow" style="flex-wrap: wrap;">
        <div class="card" v-for="card in useWords">{{ card.t }}</div>
      </div>
    </div>
    <div class="pick-zone" v-if="optWords.length > 0 && !isIdle">
      <div
        class="card"
        :class="{ active: picked?.id === card.id }"
        @click="pick(card)"
        v-for="card in optWords"
      >{{ card.t }}</div>
    </div>
    <div class="my-zone" v-show="going">
      <div class="actions pd frow" style="margin:4px;justify-content: space-between;">
        <div class="frow" @click="exec">
          <IonButton size="small" data-action="push" :disabled="execControl.cantUse">弃</IonButton>
          <IonButton size="small" :disabled="execControl.cantExc" data-action="exch">换</IonButton>
          <IonButton size="small" data-action="pull" :disabled="execControl.cantPick">拿</IonButton>
          <IonButton size="small" data-action="next">跳</IonButton>
        </div>
        <div>{{ mineData.score }}</div>
        <!-- <button class="btn btn-primary" data-action="bingo">组</button> -->
      </div>
      <div class="hand-zone" @click="handleClick">
        <div
          class="card"
          v-for="card in handWords"
          :class="{ active: pickedWords.includes(card.r) }"
          :data-id="card.id"
        >{{ card.t }}</div>
      </div>
    </div>
  </div>
  <div v-else>
    <ion-button @click="start">开始</ion-button>
  </div>
  <div class="poem-preview" v-show="showPoem" @click="showPoem=false">
    <div v-for="line in deskData?.poem?.lines">{{ line }}</div>
  </div>
</template>

<script setup lang="ts">
import { IonButton, IonBadge } from "@ionic/vue";
import { PoemCardRecord, PoemCardRecords } from "@/meteor";
import { makeCollectionItemReactive } from "@/modules/core/mapCollection";
import { computed, ref, watch } from "vue";
import { onHandWordClick, execControl, execCmd, pickedWords, picked, pickOpt, updateExecControl } from "./practice";
import { revoke, subscribe } from "@/modules/core";
const props = defineProps(["profile"])
const showPoem = ref(false);
const stateText = computed(() => {
  return ['等待中', '进行中', '算分中', '奖励中'][deskData.stage]
})

const pick = card => pickOpt(optWords.value, card, handWords.value)

subscribe("poem-record", props.profile._id)
const deskData = makeCollectionItemReactive<PoemCardRecord>(PoemCardRecords, { session: props.profile._id })

const mineData = computed(() => deskData.userMap?.[Meteor.userId()!] || { cards: [], score: 0 })
const handWords = computed(() => mineData.value.words?.map(str => {
  console.log(str)
  const [t, id] = str.split(" ")
  return { t, id, a: false, r: str }
}) || [])
const useWords = computed(() => (deskData.useWords || []).map(str => {
  const [t, id] = str.split(" ")
  return { t, id }
}))
watch(mineData, v => console.error(mineData.value), { immediate: true })
const optWords = computed(() => (deskData.optWords || []).map(str => {
  const [t, id] = str.split(" ")
  return { t, id, r: str }
}))
const isIdle = computed(() => (deskData.needActions || []).indexOf(Meteor.userId()!) === -1)
const handleClick = (ev) => onHandWordClick(ev.target.dataset.id, handWords.value, optWords.value)
const going = computed(() => [1, 2, 3, 4, 5].includes(deskData.stage!))
const exec = async $event => {
  switch ($event.target.dataset.action) {
    case "push":
      if (pickedWords[0]) {
        await revoke('poem.wordcard.use', props.profile._id, pickedWords[0])
      }
      pickedWords.splice(0)
      break;
    case "pull":
      if (picked.value?.r) {
        revoke('poem.wordcard.pick', props.profile._id, picked.value.r)
      }
      picked.value = null
      break;
    case "exch":
      if (pickedWords[0][0] !== pickedWords[1][0]) {
        revoke('poem.wordcard.exchange', props.profile._id, [pickedWords[0], pickedWords[1]])
      }
      pickedWords.splice(0)
      break;
    case "next":
      revoke('poem.wordcard.next', props.profile._id).then(([err]) => {
        if (err) alert(err)
      })
      break;

    default:
      break;
  }
}

const start = () => revoke("poem.wordcard.start", props.profile._id)

</script>

<style scoped lang="scss">
.board {
  justify-content: space-between;
  height: 100%;
  padding: 1rem;
  box-sizing: border-box;
  position: relative;
  .state {
    position: absolute;
    z-index: 2;
    top: 4px;
    left: 4px;
  }
}
.hand-zone {
  justify-content: flex-end;
  align-items: flex-end;
}
.pick-zone {
  height: 60px;
}
.hand-zone,
.pick-zone {
  gap: 8px;
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  .card {
    background-color: gold;
    border: 1px solid transparent;
    height: 64px;
    width: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .card.active {
    border-color: red;
  }
}
.used-zone {
  justify-content: flex-start;
  .card {
    background-color: gold;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 8px 4px 0;
  }
}
.poem-preview {
  z-index: 333;
  top: 100px;
  position: fixed;
  right: 100px;
  background-color: rgba(0, 0, 0, 0.4);
  padding: 1rem;
}
</style>
