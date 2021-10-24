const logger = (msg: string) => console.log(`[EE] ${msg}`);
const es = Symbol("");
const ep = Symbol("");

export class EventEmiter<A extends string | number, D, T> {
  [es]: { [key: string]: T[] } = Object.create(null);
  static [ep]: { [key: string]: EventEmiter<any, any, any> } =
    Object.create(null);
  static find<A extends string | number, D = any, T = (data: D) => any>(
    name: string
  ): EventEmiter<A, D, T> {
    return this[ep][name] || new EventEmiter<A, D, T>(name);
  }
  constructor(public name: string) {
    if (EventEmiter[ep][name]) {
      return EventEmiter[ep][name];
    }
    EventEmiter[ep][name] = this;
  }
  reflect(obj: any) {
    const t: any = Object.create(null);
    ["on", "emit", "off", "once"].forEach((key) => {
      t[key] = {
        get: () => (this as any)[key].bind(obj),
      };
    });
    Object.defineProperties(obj, t);
  }
  on(ev: A, cb: T) {
    if (Array.isArray(this[es][ev])) {
      this[es][ev].push(cb);
    } else {
      this[es][ev] = [cb];
    }
  }
  off(ev?: A, cb?: T) {
    if (ev && !this[es][ev]) {
      return;
    }
    if (!ev) {
      logger(`rm items for all `);
      this[es] = Object.create(null);
    } else if (!cb) {
      logger(`rm items for ev ${ev}`);
      delete this[es][ev];
    } else {
      const i = this[es][ev].indexOf(cb);
      if (i > -1) {
        logger(`rm items for ev ${ev} on cb ,left ${this[es][ev].length}`);
        this[es][ev].splice(i, 1);
      }
    }
  }
  async emit(ev: A, data?: D) {
    if (Array.isArray(this[es][ev])) {
      for (const hand of this[es][ev]) {
        if (typeof hand === "function") {
          logger(`call ${ev} cb`);
          try {
            const stop = await hand(data);
            if (stop === true) {
              break;
            }
          } catch (error) {}
        }
      }
    }
    if (!String(ev).endsWith("@@post")) {
      this.emit((ev + "@@post") as any, data);
    }
  }
  once(ev: A, cb: any) {
    const ncb: any = (v: any) => {
      cb(v);
      this.off(ev, ncb);
    };
    this.on(ev, ncb);
  }
}
