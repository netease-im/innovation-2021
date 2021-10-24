interface ResolveSaved {
  id: string;
  res: (...args: any[]) => void;
  rej: (...args: any[]) => void;
  clock: number;
}
export class PromiseQueue {
  private list: ResolveSaved[];
  private ids: string[];
  constructor(len = 10, private ns = "n") {
    this.list = [];
    this.ids = [];
    for (let i = 0; i < len; i++) {
      this.ids.push(i.toString());
    }
  }
  private delayReject(rej: any, id?: string) {
    return setTimeout(() => {
      if (typeof id === "string") {
        const i = this.list.findIndex((el) => el.id === id);
        if (i > -1) {
          this.list.splice(i, 1);
          this.ids.push(id);
        }
      }
      rej("timeout");
    }, 30000) as any;
  }
  public add(res: (...args: any[]) => void, rej: (...args: any[]) => void) {
    if (this.ids.length === 0) {
      const t = this.list.shift() as ResolveSaved;
      t.rej("busy_task");
      clearTimeout(t.clock);
      this.list.push({
        id: t.id,
        res,
        clock: this.delayReject(rej, t.id),
        rej,
      });
      return t.id;
    }
    const id = this.ns + this.ids.shift();
    this.list.push({
      id,
      res,
      rej,
      clock: this.delayReject(rej, id),
    });
    return id;
  }
  public get(id: string) {
    const i = this.list.findIndex((e) => e.id === id);
    if (i > -1) {
      this.ids.push(id.slice(this.ns.length));
      const t = this.list.splice(i, 1)[0];
      clearTimeout(t.clock);
      return t.res;
    }
  }
}
