import {
  STAGE_BG_IMAGE_BASE,
  STAGE_HEIGHT_PLAYER,
  STAGE_WIDTH_PLAYER,
} from "@/config";
import { log as alog, EventEmiter, OperaStageCharactorDatas, OperaStageCharactorData } from "../deps";

import { PixiArmatureDisplay, PixiFactory } from "dragonBones";
import { Container, Application, Loader, Sprite } from "pixi.js";
import { ref, watch } from "vue";
import { managers, roomData } from "../services";
import { Charactor } from "./Charactor";
import { CharactorMine } from "./CharactorMine";
import { StageItem, StageItemMovable } from "./StageItem";
import { CharactorInStageData } from "./type";
const loader = new Loader("/static/bones/");
const log = alog.namespace("item-manager", 1, { level: 2 });



export class StageItemManager extends EventEmiter<string> {
  private items: { [key: string]: StageItem } = {};
  private watchs: { [key: string]: any } = {};
  private background?: Sprite;
  public processing = false;
  refreshId?: number;
  constructor(public stage: Container) {
    super("stage item manager");
    // rtc.on(rtc.ROOM_EVENTS.REVOKE_CHARACTOR_METHOD, this.revoke as any);
    this.update();
  }
  update() {
    Object.values(this.items).forEach((charactor) => {
      if (!(charactor instanceof Charactor)) {
        return;
      }
      // if (sx != 0) charactor.x += sx;
      // if (sy != 0) charactor.y += sy;
      // if (sr != 0) charactor.el.angle += sr;
      let changed = false;
      if (charactor.dest) {
        let [sx, sy, sr] = charactor.speed;
        const [tx, ty] = charactor.dest;
        if (tx !== charactor.x) {
          changed = true;
          const d = tx - charactor.x;
          if (Math.abs(d) > 1600) {
            sx = -sx;
          }
          let dx =
            d < 0
              ? Math.max(tx, charactor.x - sx)
              : Math.min(tx, charactor.x + sx);
          if (dx < 0) {
            charactor.dest[0] = STAGE_WIDTH_PLAYER + dx;
            dx = STAGE_WIDTH_PLAYER;
          } else if (dx >= STAGE_WIDTH_PLAYER) {
            charactor.dest[0] = dx - STAGE_WIDTH_PLAYER;
            dx = 0;
          } else {
            dx = Number(dx.toFixed(2));
          }
          charactor.x = dx;
        }
        if (ty !== charactor.y) {
          changed = true;
          const d = ty - charactor.y;
          const dy =
            d < 0
              ? Math.max(ty, charactor.y - sy)
              : Math.min(ty, charactor.y + sy);
          charactor.y = dy;
        }
        if (tx === charactor.x && ty === charactor.y) {
          charactor.dest = undefined;
        }
      }
      if (changed && charactor instanceof CharactorMine) {

        OperaStageCharactorDatas.update(charactor.config._id, { $set: { x: charactor.x, y: charactor.y } })
      }
    });
    this.refreshId = requestAnimationFrame(this.update.bind(this));
  }
  private async parseDB({ key, flip }: OperaStageCharactorData) {
    try {
      const factory = PixiFactory.factory;
      if (!(factory as any)._dragonBonesDataMap[key]) {
        const res = ["ske.json", "tex.json", "tex.png"].map(
          (el) => `${key}/${key}_${el}`
        );
        // const loader = this.app.loader
        if (!loader.resources[res[0]]) {
          await new Promise((resolve) => {
            const { resources } = loader;
            const loads = res.filter((key) => !resources[key]);
            if (loads.length > 0)
              return loader.add(loads).load(() => {
                resolve(0);
              });
            resolve(0);
          });
        }
        const { resources } = loader;
        const [rawData, texture, bone] = [
          resources[res[1]].data,
          resources[res[2]].texture,
          resources[res[0]].data,
        ];
        factory.parseTextureAtlasData(rawData, texture);
        factory.parseDragonBonesData(bone);
      }
      const el = factory.buildArmatureDisplay(key) as PixiArmatureDisplay;
      // el.debugDraw = isDev;
      if (flip?.[0]) {
        el.armature.flipX = true;
      }
      el.zIndex = 20;
      return el;
    } catch (error) { }
  }
  public dropItem(item: StageItem) {
    delete this.items[item.config._id];
    this.stage.removeChild(item.view);
    if (item instanceof CharactorMine) {
      this.emit("char-mine");
    }
    item.destroy();
  }
  private async addCharactor(conf: OperaStageCharactorData) {
    let item = this.items[conf._id];
    if (item) {
      if (item instanceof Charactor) return item;
      else this.dropItem(item);
    }
    const view = await this.parseDB(conf);
    if (view) {
      this.stage.addChild(view);
      item = new Charactor(view, conf);
      this.items[conf._id] = item;
    }
    return item;
  }
  public async recoverCharactores(chars: OperaStageCharactorData[]) {
    this.processing = true;
    const ekeys = new Set(Object.keys(this.items));
    chars.forEach((el) => ekeys.delete(el._id));
    ekeys.forEach((ikey) => {
      this.dropItem(this.items[ikey]);
    });
    chars = chars.slice(0);
    while (chars.length > 0) {
      const char = chars.pop()!;
      const eItem = this.items[char._id];
      if (eItem) {
        if ((eItem.config as any).userId === char.userId) {
          continue;
        } else {
          this.dropItem(eItem);
        }
      }
      const conf = Object.assign({}, char);
      if (char.userId === Meteor.userId()) {
        await this.addPlayer(conf);
      } else {
        await this.addCharactor(conf);
      }
    }
    this.resetListen();
  }
  private async addPlayer(conf: OperaStageCharactorData) {
    let item = this.items[conf._id];
    if (item) {
      if (item instanceof CharactorMine) return item;
      else if (conf.userId === Meteor.userId()) this.dropItem(item);
      else return null;
    }
    const view = await this.parseDB(conf);
    if (!view) {
      throw Error("DATA_ERR_VIEW");
    }
    return new Promise((res, rej) => {
      item = new CharactorMine(view, conf, Meteor.userId()!);
      this.emit("char-mine", item);
      view?.on("added", () => {
        res(item);
      });
      view?.on("removed", () => {
        delete this.items[conf._id];
      });
      this.stage.addChild(view);
      this.items[conf._id] = item;
    });
  }
  public setBg(src: string) {
    if (!src) {
      return;
    }
    const fsrc = src.startsWith(STAGE_BG_IMAGE_BASE) ? src : (STAGE_BG_IMAGE_BASE + src);
    const stage = this.stage;
    if (this.background) {
      stage.removeChild(this.background);
    }
    stage.sortableChildren = true;
    const texture = PIXI.Texture.from(fsrc);
    var sprite = new PIXI.Sprite(texture);
    //添加到舞台
    sprite.width = STAGE_WIDTH_PLAYER - 400;
    sprite.height = STAGE_HEIGHT_PLAYER;
    sprite.y = 0;
    sprite.x = 200;
    sprite.zIndex = 0;
    this.background = sprite;
    stage.addChild(sprite);
  }
  private revoke({
    param,
    data,
    sender,
  }: {
    param: string;
    data: string;
    sender: string;
  }) {
    let target;
    const [method, charId] = param.split("-");
    if (
      typeof (Charactor as any).prototype[method] === "function" &&
      (target = this.items[charId]) &&
      (target.config as any).uid === sender
    ) {
      log(`try revode ${method} of ${charId} by ${sender}`);
      (target as any)[method](data);
    }
  }
  public getMyItem() {
    for (const v of Object.values(this.items)) {
      if (v instanceof CharactorMine) {
        return v;
      }
    }
  }
  private async resetListen() {
    for (const key in this.watchs) {
      this.watchs[key]();
      delete this.watchs[key];
    }
    for (const key in this.items) {
      if (this.items[key] instanceof CharactorMine) {
        continue;
      }
      const nkey = `char-${key}`;
      const lq = OperaStageCharactorDatas.find({ _id: key }).observeChanges({
        changed: (_id, fields) => {
          this.items[_id].updateSate(fields)
        }
      })
      this.watchs[key] = lq.stop.bind(lq);
    }
  }
  public async exec(action: string, data: any) {
    const item = this.getMyItem();
    if (!item) {
      return false;
    }
    item.exec(action, data);
  }
  destroy() {
    // rtc.off(rtc.ROOM_EVENTS.REVOKE_CHARACTOR_METHOD, this.revoke as any);
    this.refreshId && cancelAnimationFrame(this.refreshId);
  }
}
