import { log } from "../log";
import { EventCallbackFunc, EventCallbackConf, EventConfig } from "./types";

/**
 *
 */
export default class EventHandlerBucket<T = string> {
  public timestamp = 0;
  public interval = 0;
  public readonly name: T;
  private handlers: EventCallbackConf[];
  /**
   *
   * @param name
   * @param interval 最小间隔：秒
   */
  constructor(name: T, interval = 0) {
    this.interval = interval * 1000;
    this.handlers = [];
    this.name = name;
  }
  /**
   * name
   */
  public get freeTime() {
    return this.timestamp + this.interval;
  }
  /**
   * delete
   */
  public delete(e: EventCallbackFunc | EventConfig) {
    if (typeof e === "function") {
      const i = this.handlers.findIndex((el) => el.func === e);
      if (i > -1) {
        this.handlers.splice(i, 1);
        return true;
      }
      return false;
    } else if (typeof e === "object") {
      if (e.name) {
        let handler: any;
        if (e.hook) {
          handler = (el: EventCallbackConf) =>
            el.conf.name === e.name && el.conf.hook === e.hook;
        } else {
          handler = (el: EventCallbackConf) => el.conf.name === e.name;
        }
        const i = this.handlers.findIndex(handler);
        if (i > -1) {
          this.handlers.splice(i, 1);
        }
      } else {
        log(`delete all handler of ${this.name}`);
        this.handlers = [];
      }
      return true;
    } else {
      console.warn("function 与 config 缺一不可");
    }
  }
  /**
   * refresh
   * @returns boolean break or not
   */
  public refresh() {
    const now = Date.now();
    this.handlers = this.handlers.filter(
      (el) => !el.conf.boom || el.conf.boom > now
    );
    if (this.handlers.length === 0) {
      return true;
    }
    if (
      this.interval &&
      this.timestamp &&
      this.timestamp + this.interval > now
    ) {
      return true;
    }
    this.timestamp = now;
    return false;
  }
  /**
   * update
   */
  public update() {
    this.timestamp = Date.now();
  }
  /**
   * name
   */
  public add(func: EventCallbackFunc, conf: EventConfig) {
    switch (conf?.uniq) {
      case "pre":
      case "on":
      case "after":
        if (
          this.handlers.some(
            (e) => e.conf.name === conf.name && conf.uniq === e.conf.hook
          )
        ) {
          return log(`重复事件${this.name}${conf.name}`);
        }
        break;
      case true:
        if (this.handlers.some((e) => e.conf.name === conf.name)) {
          return log(`重复事件${this.name}${conf.name}`);
        }
        break;
    }
    if (typeof conf.ttl === "number" && !conf.boom) {
      conf.boom = Date.now() + conf.ttl;
    }
    if (conf.top) {
      this.handlers.unshift({ conf, func });
    } else {
      this.handlers.push({ conf, func });
    }
  }
  /**
   * onces
   */
  public onces() {
    const onces: EventCallbackConf[] = [];
    const news: EventCallbackConf[] = [];
    this.handlers.forEach((el) => {
      (el.conf.once ? onces : news).push(el);
    });
    this.handlers = news;
    return onces;
  }
  public hook(name: string) {
    return this.handlers.filter((el) => el.conf.hook === name);
  }
}
