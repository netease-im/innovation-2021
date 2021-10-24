<template>
  <div class="board fcol">
    <div class="used-zone f1">
      <div class="card" v-for="card in roomData.used"></div>
    </div>
    <div class="pick-zone" v-if="roomData.pick.length > 0"></div>
    <div class="push-zone">
      <div class="card" v-for="card in roomData.push"></div>
    </div>
    <div class="actions pd" @touchstart="exec">
      <button class="btn btn-primary" data-action="push" :disabled="cantUse">弃</button>
      <button class="btn btn-primary" :disabled="cantExc" data-action="exch">换</button>
      <button class="btn btn-primary" data-action="pull" :disabled="cantPull">拿</button>
      <!-- <button class="btn btn-primary" data-action="bingo">组</button> -->
    </div>
    <div class="hand-zone">
      <div
        class="card"
        v-for="card in roomData.hand"
        :class="{ active: card.actived }"
        @click="toggle(card)"
      ></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, defineComponent, inject } from "vue";
import { IUserProfile } from "../../../../shared/types/user";

const user = inject<IUserProfile>("user");

import { pushCard, togglePick, roomData } from "./data-store";

console.log(user);

const cantUse = computed(
  () => roomData.actived.length !== 1 || roomData.hand.length <= 8
);
const cantPull = computed(
  () => roomData.pick.length < 1 || roomData.hand.length > 13
);
const cantExc = computed(() => roomData.actived.length !== 2);

const toggle = (card: any) => togglePick(card);
const exec = async (e: TouchEvent) => {
  const action = (e.target as HTMLButtonElement)?.dataset?.action;
  switch (action) {
    case "push":
      await pushCard();
      break;

    default:
      break;
  }
};
</script>

<style scoped lang="scss">
.board {
  justify-content: space-between;
  height: 100%;
  padding: 1rem;
  box-sizing: border-box;
}
.hand-zone,
.push-zone {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr 1fr 1fr;
  gap: 8px;
  .card {
    background-color: gold;
    border: 1px solid transparent;
    height: 64px;
  }
  .card.active {
    border-color: red;
  }
}
.push-zone {
  grid-template-columns: repeat(4 60px);
}
.used-zone {
  display: flex;
  justify-content: flex-start;
  .card {
    background-color: gold;
    width: 48px;
    height: 48px;
  }
}
</style>
