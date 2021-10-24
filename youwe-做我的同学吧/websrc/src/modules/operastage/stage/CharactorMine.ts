
import { OutlineFilter } from "@pixi/filter-outline";
import { Bone, PixiArmatureDisplay } from "dragonBones";
import { log, OperaStageCharactorData, OperaStageCharactorDatas, sleep } from "../deps";
import { managers } from "../services";
import { Charactor, NodeProp } from "./Charactor";
import { CharactorConfig } from "./type";

export class CharactorMine extends Charactor {
  readonly isPlayer = true;
  private reporting = false;
  private activeBone?: NodeProp;
  private keyTicker: any;
  private onKeyUp() {
    if (this.keyTicker) {
      clearInterval(this.keyTicker);
      this.keyTicker = undefined;
      console.log("clear ticker");
    }
  }
  private onKeyDown(ev: KeyboardEvent) {
    // this.onKeyUp();
    const key = ev.key.toLowerCase();
    const keyMap: { [key: string]: string } = {
      a: "thin",
      s: "short",
      d: "wide",
      w: "tall",
      q: "rot1",
      e: "rot2",
      z: "reset",
    };
    const action = keyMap[key];
    if (action) {
      // this.keyTicker = setInterval(this.execTransform.bind(this, action), 1000);
      this.triggerTransform(action);
    }
  }
  private triggerTransform(action: string) {
    if (!this.activeBone) {
      return;
    }
    const bone = this.activeBone.bone;
    const trans = bone.boneData.transform;
    const initd = this.activeBone.data;
    const id = this.activeBone.id;
    const flipx = this.dir.x;
    let cd: any;
    switch (action) {
      case "thin":
        const dsy = trans.scaleY - 0.2;
        cd = { scaleY: Math.max(0.05, dsy) };
        this.execTransform(bone, cd);
        break;
      case "wide":
        cd = { scaleY: trans.scaleY + 0.1 };
        this.execTransform(bone, cd);
        break;
      case "short":
        const dsx = trans.scaleX - 0.2;
        cd = { scaleX: Math.max(0.05, dsx) };
        this.execTransform(bone, cd);
        break;
      case "tall":
        cd = { scaleX: trans.scaleX + 0.2 };
        this.execTransform(bone, cd);
        break;
      case "rot1":
        cd = {
          rotation: trans.rotation - (flipx ? 0.1 : -0.1),
        };
        this.execTransform(bone, cd);
        break;
      case "rot2":
        cd = {
          rotation: trans.rotation + (flipx ? 0.1 : -0.1),
        };
        this.execTransform(bone, cd);
        break;
      case "reset":
        this.execTransform(bone, initd);
        if (Array.isArray(this.trans.value)) {
          const i = this.trans.value.findIndex((el) => el.id === id);
          if (i > -1) {
            const narr = this.trans.value.slice(0);
            narr.splice(i, 1);
            // managers.reportTrans(this.config.id, narr);
          }
        }
        return;
      default:
        break;
    }
    if (!cd) {
      return;
    }
    const i = this.trans.value.findIndex((el) => el.id === id);
    const narr = this.trans.value.slice(0);
    if (i > -1) {
      Object.assign(narr[i], cd);
    } else {
      narr.push({ id, ...cd });
    }
    // managers.reportTrans(this.config.id, narr);
  }
  constructor(
    view: PixiArmatureDisplay,
    config: OperaStageCharactorData,
    public uid: string
  ) {
    super(view, config);
    this.setupInter();
    console.log(`add char of me ${config._id}`)
    document.body.addEventListener("keydown", this.onKeyDown.bind(this));
    // document.body.addEventListener("keyup", this.onKeyUp.bind(this));
  }
  exec(action: string, data: any) {
    if (action === "moveBy") {
      let x = (this.x + data.x) as number;
      // x = x < 0 ? -x : x > 2000 ? x - 2000 : x;
      let y = (this.y + data.y) as number;
      y = y < 20 ? 20 : y > 900 ? 900 : y;
      data = {
        x,
        y,
        time: data.time,
      };
      // const anis = item.view.animation.animationNames;
      // const name = anis.find((el) =>
      //   data.time > 1
      //     ? el.includes("跑") || el.includes("飞")
      //     : el.includes("走")
      // );
      // item.play({ name });
      // action = "moveTo";
      this.moveTo(data, true);
    } else if (action === "play") {
      this.play(data);
    }
    // log({ action, data, item });
    // const s = await rtc.broadcast(
    //   `${rtc.ROOM_EVENTS.REVOKE_CHARACTOR_METHOD}:${action}-${roomData.uid}`,
    //   data
    // );
    // log({ s, action, data });
    // if (s) {
    // (item as any)[action](data, true);
    // }
  }
  play(data: { name: string; time?: number }) {
    super.play(data);
    this.animation = data.name;
    log(`play ${data.name} and report`);
  }
  public updateSate(conf: any) {
    // my role not need update
  }
  setupInter() {
    // el.buttonMode = true;
    // el.interactive = true;
    const deactive = (el: any) => {
      el.slot.display.filters = [];
      this.activeBone = undefined;
    };
    const active = (el: any) => {
      el.slot.display.filters = [new OutlineFilter(5, 0xff2200) as any];
      this.activeBone = el;
    };
    this.nodes.forEach((node) => {
      const { id, slot } = node;
      const el = slot.display as PIXI.Sprite;
      el.buttonMode = true;
      el.interactive = true;
      let activeData = null as any;
      // const size = el.getBounds();
      el.on("mousedown", (ev: any) => {
        active(node);
        el.filters = [new OutlineFilter(5, 0xff2200) as any];
        const { x, y } = ev.data.global;
        activeData = [x, y];
        ev.stopPropagation();
      });
      // if (id !== "zx") {
      //   let busy = false;
      //   el.on("mousemove", (ev: any) => {
      //     if (!activeData || busy) return;
      //     busy = true;
      //     // // console.log(ev);
      //     const { x, y } = ev.data.global;
      //     const dx = Number((x - activeData[0]).toFixed(2));
      //     const dy = Number((y - activeData[1]).toFixed(2));
      //     this.exec("moveBy", { x: dx * 2, y: dy * 2, time: 2 });
      //     // const dcx = x - cx;
      //     // const dcy = y - cy;
      //     // const d = dx ** 2 + dy ** 2;
      //     // const arc = Math.atan2(dy * dcx > 0 || dx * dcy < 0 ? d : -d, 100);
      //     // // const arc2 = Math.atan2(dy, size.width / 2);
      //     // // const arc = Math.abs(arc1) > Math.abs(arc2) ? arc1 : arc2;
      //     // // if (Math.abs(dx) < Math.abs(dy)) {
      //     // //   transform.scaleX = Math.max(0.1, activeData[2] + dx);
      //     // // } else {
      //     // //   transform.scaleY = Math.max(0.1, activeData[3] + dy);
      //     // // }
      //     // transform.rotation += arc;
      //     activeData = [x, y];
      //     console.log({ dx, dy, x, y });
      //     setTimeout(() => {
      //       busy = false;
      //     }, 100);
      //   });
      // }
      el.on("mouseup", () => {
        deactive(node);
        activeData = null;
      });
      el.on("mouseupoutside", (ev: any) => {
        // if (id === "zx") {
        //   const dx = ev.data.global.x - activeData[0];
        //   const dy = ev.data.global.y - activeData[1];
        //   this.exec("moveBy", { x: dx, y: dy, time: dx > 200 ? 2 : 1 });
        // }
        deactive(node);
        activeData = null;
      });
      el.on("rightdown", (ev: any) => {
        ev.data.originalEvent.preventDefault();
        managers.stage?.emit("right-click-char", ev.data.global);
        ev.stopPropagation();
      });
    });
    this.view.on("added", () => {
      this.view.parent.hitArea = new PIXI.Rectangle(0, 0, 2000, 900);
      this.view.parent.on("mousedown", (ev: any) => {
        const { x, y } = ev.data.global;
        console.log({ x, y });
        this.moveTo({ x, y }, true);
      });
    });
    //
    (window as any).char = this;
  }
  public destroy() {
    super.destroy();
    document.body.removeEventListener("keydown", this.onKeyDown.bind(this));
    // document.body.removeEventListener("keyup", this.onKeyUp.bind(this));
  }
  report($set: Partial<OperaStageCharactorData>) {
    OperaStageCharactorDatas.update(this.config._id, {
      $set
    })
  }
}
