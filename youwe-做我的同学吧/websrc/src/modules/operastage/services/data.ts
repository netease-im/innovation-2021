import { STAGE_HEIGHT_PLAYER, STAGE_WIDTH_PLAYER } from "@/config";
import { log as blog } from "../deps";
import { StageItemManager } from "../stage/StageItemManager";

import { Application } from "pixi.js";
import { reactive, Ref, ref } from "vue";
import { user } from "@/modules/user/state";
const log = blog.namespace("room-service", 0, { level: -1 });
export const roomData = reactive({
  id: "",
  info: null as any,
  players: [] as string[],
  users: [] as string[],
  story: null as any,
  charactors: [] as any[],
  uid: user._id,
  stepCount: 0,
});
 
export const managers = {
  stage: null as StageItemManager | null,
  app: null as Application | null
};


