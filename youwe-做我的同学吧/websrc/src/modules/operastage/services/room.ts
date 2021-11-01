
import { ref, SetupContext, computed, watch } from "vue";

import { managers, roomData } from "./data";
import { StageItemManager } from "../stage/StageItemManager";
import { Application } from "pixi.js";
import { STAGE_HEIGHT_PLAYER, STAGE_WIDTH_PLAYER } from "@/config";
import { conBus, log as al, makeArr } from "../deps";
import { joinRoom, leftRoom } from "@/modules/media/services/room";
import { subscribe, unsubscribe } from "@/modules/core/api";
import { useRoute } from "vue-router";
import { MTracker, OperaStageCharactorDatas } from "../deps";


const log = al.namespace("room-service", 1, { level: 1 });


function setupManagers(app: PIXI.Application) {
  log("setup managers for room ----- ");
  const stage = new StageItemManager(app.stage);
  stage.setBg(makeArr(roomData.story!.stageImages)[0]);
  managers.app = app;
  managers.stage = stage;
  log({ managers });
  managers.stage?.emit("ready");
}

function initRoomData(
  roomId: string
) {
  if (roomId !== roomData.id) {
    roomData.id = roomId;
    roomData.info = null;
    managers?.app?.destroy();
    managers.app = null;
    managers.stage = null;
  }
  subscribe('opera-stage-chars', roomId);
  log(roomData);
}

function setupConnect(story:any) {
  log("connect room-----------");
  const route = useRoute()
  let roomId = route.params.id as string;
  roomData.story = story;
  const charactorsInStage = ref<any[]>([]);
  MTracker.autorun(() => {
    const list = OperaStageCharactorDatas.find().fetch()
    managers.stage?.recoverCharactores(list);
    console.log(list)
  })
  initRoomData(roomId)
  roomData.users.splice(0);
  return {
    charactorsInStage,
    roomId
  }
}


export const roomService = {
  setupConnect,
  init(view: HTMLCanvasElement) {
    const app = new Application({
      width: STAGE_WIDTH_PLAYER,
      height: STAGE_HEIGHT_PLAYER,
      view,
    });
    app.stage.interactive = true;
    setupManagers(app);
  },
  bus: conBus("room", { local: true }),
  left() {
    leftRoom();
    unsubscribe("opera-stage-chars");
  }
};
