import { EventEmiter, log as blog, OperaStageCharactorData } from "../deps";
import { DisplayObject } from "pixi.js";

const log = blog.namespace("stage-item", 2, { level: 1 });

export class StageItem extends EventEmiter<any> {
  public config: StageItemConfig;
  constructor(config: StageItemConfig, public view: DisplayObject) {
    super(`si-${config._id}`);
    this.config = config;
  }
  protected syncState(config: StageItemConfig) {
    const el = this.view;
    log(`-------------- sync status -------------`);
    log(config);
    const x = config.x || 0;
    const y = config.y || 0;
    const [width, height] = config.size || [100, 100];
    el.x = x;
    el.y = y;
    el.pivot.set(0.5, 0.5);
    const rect = el.getBounds();
    el.scale.set(
      Number((width / rect.width).toPrecision(3)),
      Number((height / rect.height).toPrecision(3))
    );
  }
  get x() {
    return this.view.x;
  }
  get y() {
    return this.view.y;
  }
  get metadata(): any {
    return {
      x: this.x,
      y: this.y,
      scale: [this.view.scale.x, this.view.scale.y],
    };
  }
  public updateSate(conf: any) {
    log(`update state ------------ `);
    log(conf);
    if (typeof conf?.x === "number") {
      this.view.x = conf.x;
    }
    if (typeof conf?.y === "number") {
      this.view.y = conf.y;
    }
    if (Array.isArray(conf?.scale)) {
      const [sx, sy] = conf.scale;
      if (typeof sx === "number") {
        this.view.scale.x = sx;
      }
      if (typeof sy === "number") {
        this.view.scale.y = sy;
      }
    }
  }
  public destroy() {
    this.view.destroy();
  }
}
export class StageItemMovable extends StageItem {
  private readonly baseSpeed = 100;
  public speed = [1, 1, 1];
  public dest?: number[] = undefined;
  public readonly movable = true;
  constructor(config: OperaStageCharactorData, view: PIXI.DisplayObject) {
    super(config as any, view);
  }
  get x() {
    return this.view.x;
  }
  get y() {
    return this.view.y;
  }
  set x(v: number) {
    this.view.x = v;
  }
  set y(v: number) {
    this.view.y = v;
  }
  public moveTo({ x = this.x, y = this.y, time = 1 }) {
    const ftime = Math.round(time);
    this.speed = [
      Number(((ftime * Math.abs(x - this.x)) / this.baseSpeed).toFixed(2)),
      Number(((ftime * Math.abs(y - this.y)) / this.baseSpeed).toFixed(2)),
      0,
    ];
    this.dest = [Math.round(x), Math.round(y)];
  }
}

// vx,vy,vt,vrx,vry,vrz
type speedArr = [number, number, number?, number?, number?, number?];
export interface StageItemConfig {
  _id: string;
  key: string; // file key
  name: string; // show name
  src: string;
  flip?: boolean[];
  x?: number;
  y?: number;
  rotation?: number;
  angle?: number;
  size?: [number, number];
}
export interface StageItemDBoneConfig extends StageItemConfig {
  speed?: speedArr;
  srcPath?: string;
}

export interface StageItemPlayableConfig extends StageItemDBoneConfig {
  uid?: string;
  report?: boolean; // for player's own item
}
