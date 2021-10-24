<template>
  <section class="controller">
    <div class="frow cnty">
      <ion-fab vertical="bottom" horizontal="start" slot="fixed">
        <ion-fab-button>{{ myCharactorName }}</ion-fab-button>
        <ion-fab-list side="top">
          <ion-fab-button @click="pick(cha.name)" :disabled="cha.picked" v-for="cha in finalChars">
            <ion-text style="font-size: 0.5rem;">{{ cha.name }}</ion-text>
          </ion-fab-button>
        </ion-fab-list>
        <ion-fab-list side="end">
          <ion-fab-button @click="play({ name: '' })">停止</ion-fab-button>
          <ion-fab-button
            v-for="ani in animations"
            :key="'act_' + ani"
            @click="play({ name: ani })"
          >{{ ani }}</ion-fab-button>
        </ion-fab-list>
      </ion-fab>

      <!-- <el-radio-group size="mini" class="frow" @change="chooseCharactor" v-model="myCharactorName">
        <el-radio-button label="暂无" name="char"></el-radio-button>
        <el-radio-button
          v-for="cha in charactors"
          :key="cha.id"
          :label="cha.name"
          name="char"
          :disabled="cha.picked"
        ></el-radio-button>
      </el-radio-group>-->
    </div>
    <div v-if="hasChar" class="mgt">
      <div class="frow fwrap">
        <!-- <DirPanelPlus
          :size="controlPanelSize"
          :viewMode="viewMode"
          @move="handleMove"
        />-->
        <div class="ani-picker f1" style="min-width: 120px; align-content: flex-start">
          <!-- <ion-button size="mini" class="ani-item" @click="play({ name: '' })" round>停止</ion-button> -->
          <!-- <Button
              size="mini"
              class="ani-item"
              @click="controller.exec('flip', { dir: 0 })"
              round
            >
              转身
          </Button>-->
        </div>
      </div>
    </div>
  </section>
</template>

<script lang="ts" setup>
import { IonButton, IonFab, IonFabButton, IonFabList, IonText } from "@ionic/vue";
import { computed, defineComponent, inject, reactive, ref, watch } from "vue";
import { managers } from "../../services";
import { makeCollectionReactive } from "../../../core/mapCollection";
import { OperaStageCharactorDatas } from "../../deps";
import { OperaStageCharactorData } from "shared/meteor";
import { revoke } from "@/modules/core";
const props = defineProps(["items"])

const charactors = computed(() => props.items || [])

const finalChars = computed(() => {
  const ret: any[] = []
  if (myItem.value) ret.push({ name: "暂无" })
  charactors.value.forEach(e => {
    if (!stageChars.some(el => e.name === el.name)) {
      ret.push({
        name: e.name
      })
    }
  })
  return ret
})
const stageChars = reactive<OperaStageCharactorData[]>([])
const myItem = computed(() => stageChars.find(el => el.userId === Meteor.userId()))

makeCollectionReactive(stageChars, OperaStageCharactorDatas, {})
const myCharactorName = computed(() => {
  return myItem.value?.name || "暂无";
})
let myCharBackup: any = "";
const animations = ref([]);
const controlPanelSize = computed(() => 120);

const hasChar = computed(() => myCharactorName.value !== "暂无")

watch(
  myCharactorName,
  (nv, ov) => {
    myCharBackup = ov || "暂无";
  },
  { immediate: true }
);

managers.stage?.on("char-mine", (char) => {
  animations.value = char?.animations || [];
});

const roomId = inject<string>("roomId")
const emiter = defineEmits(['pick', 'exec'])
const pick = async (v: string) => {
  if (v === myCharactorName.value) return
  const char = charactors.value.find(el => el.name === v)
  const [err, res] = await revoke("opera.stage.char.pick", { roomId, charId: char?._id })
  if (!err) {

  }
}
const handleMove = ({ dx = 0, dy = 0, time = 2 }) => {
  moveTo({
    x: Math.round(dx * 3),
    y: Math.round(dy * 3),
    time,
  });
}
const moveTo = ({
  x = undefined as number | undefined,
  y = undefined as number | undefined,
  time = 1,
}) => {
  console.log(`moveBy ${x} ${y} `);
  const player = myItem.value
  if (!player) {
    return;
  }
  // time = Math.max(
  //   1,
  //   Math.ceil(
  //     (Math.sqrt((x || 0) ** 2 + (y || 0) ** 2) * 3) / this.controlPanelSize
  //   )
  // );
  // if (typeof x === "number") {
  //   x = (player!.x + x) as number;
  // }
  // if (typeof y === "number") {
  //   y = (player!.y + y) as number;
  //   y = y < 20 ? 20 : y > 900 ? 900 : y;
  // }
  emiter("exec", {
    name: "moveBy",
    data: {
      x,
      y,
      time,
    },
  });
}
const play = (data: { name: string; time?: number }) => {
  emiter("exec", {
    name: "play",
    data,
  });
}
</script>

<style lang="scss" scoped>
section.controller {
  padding: 0.5rem;
  position: fixed;
  bottom: 0;
  .ani-picker {
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    margin-left: 1rem;
    .ani-item {
      margin: 0 0.25rem 0 0;
    }
  }
  .tip {
    color: white;
    margin: 0 0.5rem;
    font-size: 0.8rem;
  }
  .params {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    padding: 0.5rem;
    .distance {
      display: flex;
      align-items: center;
    }
  }
}
</style>
