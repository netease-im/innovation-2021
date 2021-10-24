import { log } from "../log";
import EventHandlerBucket from "./EventHandlerBucket";
import { StorageMessager } from "./storageMessager";
import { EventCallbackFunc, EventConfig } from "./types";

export class Channel<T = string> {
  public static pool = new Set<Channel<any>>();
  public static connect<T>(n: string, opts: any) {
    let ret: Channel<T> | null = null;
    this.pool.forEach((el) => {
      if (el.name === n) {
        ret = el;
      }
    });
    return ret || new Channel<T>(n, opts);
  }
  public name: string;
  public interval?: number;
  private proxy?: BroadcastChannel | StorageMessager;
  private map = new Map<T, EventHandlerBucket<T>>();
  private constructor(
    n: string,
    opts: { interval?: number; local?: boolean } = {}
  ) {
    this.name = n;
    this.interval = Number(opts.interval) > 0 ? Number(opts.interval) : 0;
    if (!opts.local) {
      this.proxy = window.BroadcastChannel
        ? new BroadcastChannel(n)
        : new StorageMessager(n);
      this.proxy.onmessage = ({ data: { name, data } }) => {
        this.selfHandle(name, JSON.parse(data));
      };
      this.proxy.onmessageerror = (e: any) => {
        log(e, `${this.name} channel`);
      };
    }
    Channel.pool.add(this);
  }
  /**
   * pre
   */
  public pre(
    name: T,
    func: EventCallbackFunc,
    conf: EventConfig = { uniq: true },
    ctx?: any
  ) {
    conf.hook = "pre";
    return this.add(name, func, conf, ctx);
  }
  /**
   * after
   */
  public post(
    name: T,
    func: EventCallbackFunc,
    conf: EventConfig = { uniq: true },
    ctx?: any
  ) {
    conf.hook = "after";
    return this.add(name, func, conf, ctx);
  }
  public on(
    name: T,
    func: EventCallbackFunc,
    conf: EventConfig = { uniq: true },
    ctx?: any
  ) {
    conf.hook = "on";
    return this.add(name, func, conf, ctx);
  }
  public once(
    name: T,
    func: EventCallbackFunc,
    conf: EventConfig = { uniq: true },
    ctx?: any
  ) {
    conf.once = true;
    conf.hook = "on";
    return this.add(name, func, conf, ctx);
  }
  public async emit(name: T, data?: any) {
    log(`emit ${name} ---------`);
    log(data);
    this.proxy?.postMessage({
      name,
      data: JSON.stringify(data),
    });
    return this.selfHandle(name, data);
  }
  public off(name: T, conf: EventConfig | EventCallbackFunc) {
    const t = this.map.get(name);
    log(`try off event ${name}`);
    if (!t) {
      return true;
    }
    if (typeof conf === "object") {
      if (conf?.name === "all") {
        this.map.delete(name);
        return true;
      }
    }
    return t.delete(conf);
  }
  public dispose() {
    this.map.clear();
    this.proxy?.close();
  }
  public view() {
    log(this.map);
  }
  /**
   *
   * @param name 事件名
   * @param data 数据
   * @description pre 原始对象，on 传入 [前处理对象X，原始对象，原始备份X] once 前处理对象X after 前处理对象X，原始备份X [x freeze]
   */
  private async selfHandle(name: T, data: any = {}) {
    const t = this.map.get(name);
    log(`handle ${name} ----------`);
    log(data);
    if (!t) {
      log(`${name} no handler`);
      return false;
    }
    if (t.refresh()) {
      log(`${name} no live handler`);
      return true;
    }
    let pData: any;
    let oData: any;
    if (data) {
      oData = JSON.parse(JSON.stringify(data));
      Object.freeze(oData);
    }

    for (const hand of t.hook("pre")) {
      log(`pre ${hand.conf.name}  handling`);
      await hand.func(data);
    }
    if (data) {
      pData = JSON.parse(JSON.stringify(data));
      Object.freeze(pData);
    }
    for (const hand of t.hook("on")) {
      log(`on ${hand.conf.name}  handling`);
      const stop = await hand.func(pData, data, oData);
      if (stop) {
        log(`on ${hand.conf.name}  handling breaked`);
        break;
      }
    }
    for (const hand of t.onces()) {
      const stop = hand.func(pData);
      log(`once ${hand.conf.name}  handling`);
      if (stop) {
        log(`once ${hand.conf.name}  handling breaked`);
        break;
      }
    }
    t.hook("after").forEach((el) => {
      log(`once ${el.conf.name}  handling`);
      el.func(pData, oData);
    });
    // t.update();
    return true;
  }

  private add(name: T, cb: EventCallbackFunc, conf: EventConfig, ctx?: any) {
    let t = this.map.get(name);
    log(`add ${name}  listener`);
    log(conf);
    if (!t) {
      if (this.interval && !conf.interval) {
        conf.interval = this.interval;
      }
      t = new EventHandlerBucket<T>(name, conf.interval);
      this.map.set(name, t);
    }
    if (!conf.name) {
      if (conf.uniq) {
        conf.name = "default";
      } else {
        console.warn(`建议提供name of ${name}`);
      }
    }
    t.add(cb, conf);
    if (ctx) {
      const ou = ctx.unmounted;
      ctx.unmounted = () => {
        ou.call(ctx);
        this.off(name, cb);
      };
    }
    return () => {
      this.off(name, cb);
    };
  }
}

export const conBus = function<T = any>(
  name: string,
  options: { interval?: number; local?: boolean } = {}
) {
  return Channel.connect<T>(name, options);
};
