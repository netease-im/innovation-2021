import dragonBones, { Bone, PixiArmatureDisplay } from "dragonBones";
import { StageItemMovable } from "./StageItem";
import { CharactorConfig } from "./type";
import { managers } from "../services";
import { watch, Ref, ref } from "vue";
import { deepCopy, log as b } from "../deps";
import { OperaStageCharactorData } from "shared/meteor";
const log = b.namespace("charactor", 1, { level: 3 });

export interface NodeProp {
  id: string;
  type: string;
  slot: dragonBones.Slot;
  bone: dragonBones.Bone;
  data: any;
}
export class Charactor extends StageItemMovable {
  public animations: string[] = [];
  protected animation = "";
  public readonly isChar = true;
  public readonly movable = true;
  protected nodes: Map<string, NodeProp> = new Map();
  protected trans: Ref<any[]> = ref([]);
  constructor(public view: PixiArmatureDisplay, config: OperaStageCharactorData) {
    super(config, view);
    this.syncState(config);
  }
  protected syncState(config: any) {
    super.syncState(config);
    const el = this.view;
    el.anchor.set(0.5, 0.5);
    el.armature.flipX = !!config.flip?.[0];
    el.armature.flipY = !!config.flip?.[1];
    const [width, height] = config.size || [100, 100];
    const v = el.armature.armatureData.aabb;
    el.width = Number((width / v.width).toFixed(3));
    el.height = Number((height / v.height).toFixed(3));
    // el.width = 50;
    // el.height = 80;
    // el.animation.play(el.animation.animationNames[0]);
    this.animations = (el.animation.animationNames as any) || [];
    // el.addDBEventListener(dragonBones.EventObject.COMPLETE as any, e => {
    //   if (el.animation.lastAnimationName !== this.animationNames[0])
    //     el.animation.play(this.animationNames[0])
    // }, el)
    if (config.animation) {
      el.animation.play(config.animation, -1);
    }
    const slots = el.armature.getSlots();
    el.armature.getBones().forEach((e) => {
      if (e.name.startsWith("e-")) {
        const id = e.name.slice(2);
        const slot = slots.find((e1) => e1.name === `d-${id}`);
        if (!slot) {
          return;
        }
        this.nodes.set(id, {
          id,
          slot,
          type: "e",
          bone: e,
          data: Object.freeze(deepCopy(e.boneData.transform)),
        });
      } else if (e.name.startsWith("n-")) {
        const id = e.name.slice(2);
        const slot = slots.find((e1) => e1.name === `d-${id}`);
        if (!slot) {
          return;
        }
        this.nodes.set(id, {
          id,
          slot,
          type: "n",
          bone: e,
          data: Object.freeze(deepCopy(e.boneData.transform)),
        });
      }
    });
    // if (!(this as any).isPlayer) {
    //   console.log("setup trans listen");
    //   managers
    //     .subscribe(`trans-${this.config.id}`, this.trans, [])
    //     .then((trans) => {
    //       this.trans = trans;
    //       watch(trans, (nv, ov) => {
    //         if (ov === nv || !Array.isArray(nv)) {
    //           return;
    //         }
    //         nv.forEach((el) => {
    //           const node = this.nodes.get(el.id);
    //           delete el.id;
    //           this.execTransform(node?.bone!, el);
    //         });
    //       });
    //     });
    // }
  }
  protected execTransform(bone: Bone, data: any) {
    if (!bone) {
      return;
    }
    const trans = bone.boneData.transform;
    for (const key in data) {
      if (["scaleX", "scaleY", "rotation"].includes(key)) {
        (trans as any)[key] = data[key];
      }
    }
  }
  public updateSate(conf: any) {
    const dest = [this.x, this.y];
    if (typeof conf?.x === "number") {
      dest[0] = conf.x;
    }
    if (typeof conf?.y === "number") {
      dest[1] = conf.y;
    }
    this.dest = dest;
    delete conf.x;
    delete conf.y;
    super.updateSate(conf);
    // if (typeof conf?.width === "number" && conf.width !== this.view.width) {
    //   this.view.width = conf.width;
    // }
    // if (typeof conf?.height === "number" && conf.height !== this.view.height) {
    //   this.view.height = conf.height;
    // }
    if (Array.isArray(conf?.flip)) {
      const [fx, fy] = conf.flip;
      this.view.armature.flipX = fx;
      this.view.armature.flipY = fy;
    }
    if (Array.isArray(conf?.speed)) {
      this.speed = conf.speed;
    }
    if (conf.animation) {
      this.play({ name: conf.animation });
    }
  }
  get dir() {
    return {
      x:
        (this.config.flip?.[0] && this.view?.armature.flipX) ||
        (!this.config.flip?.[0] && !this.view?.armature.flipX),
    };
  }
  power({ x = 0, y = 0, rotate = 0 }) {
    this.speed = [
      x || this.speed[0],
      y || this.speed[1],
      rotate || this.speed[2],
    ];
  }
  moveTo(e: { x?: number; y?: number; time?: number }, save = false) {
    const x = typeof e.x === "number" ? e.x : this.x;
    const y = typeof e.y === "number" ? e.y : this.y;
    if (x === this.x && y === this.y) return;
    const time = e.time || 1;
    log(`move to ${x} ${y} ${time} from ${this.x} - ${this.y}`);
    let name = this.animations.find((el) => el.indexOf("走") > -1);
    if (time > 1) {
      let nn = this.animations.find(
        (el) => el.indexOf("跑") > -1 || el.indexOf("-快") > -1
      );
      if (nn) name = nn;
    }
    if (name) {
      this.play({
        name,
        time: 0,
      });
    }
    if ((x < this.x && this.dir.x) || (x > this.x && !this.dir.x)) {
      this.flip({ dir: 0 }, false);
    }
    super.moveTo({ x, y, time });
  }
  get metadata() {
    return {
      x: this.x,
      y: this.y,
      speed: this.speed,
      width: this.view.width,
      height: this.view.height,
      flip: [!!this.view.armature.flipX, !!this.view.armature.flipY],
      animation: this.animation,
      // config: { ...this.config, report: 0 },
    };
  }
  flip({ dir = 0 }, reverse = true) {
    let [sx, sy, sr] = this.speed;
    log(`char flip ${dir}`);
    const armature = this.view.armature;
    if (dir == 1) {
      armature.flipY = !this.view.armature.flipY;
      reverse ? (sy = -sy) : "";
    } else {
      armature.flipX = !this.view.armature.flipX;
      reverse ? (sx = -sx) : "";
    }
    this.speed = [sx, sy, sr];
    const ret = [!!armature.flipX, !!armature.flipY];
    this.report({ flip: ret, speed: this.speed })
    return ret
  }
  nextAnima = "";
  play({ name = "", time = -1 }) {
    if (this.view) {
      const ani = this.view.animation;
      if (!name) {
        ani.stop();
        this.report({ animation: undefined })
      } else if (ani.hasAnimation(name)) {
        if (ani.lastAnimationName === name && ani.isPlaying) {
          return;
        }
        console.log("play " + name + time);
        this.view.animation.play(name, time);
        this.report({ animation: { name, time } })
      }
    }
  }
  report(data: Partial<OperaStageCharactorData>) {
    // normal char cant report
  }
  public destroy() {
    super.destroy();
    // managers.unsubscribe(`trans-${this.config.id}`);
  }
}
