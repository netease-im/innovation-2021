export class TaskQueue {
  private tasks: any[];
  private timeout: number;
  private busy: boolean;
  constructor(t = 30000) {
    this.tasks = [];
    this.timeout = t;
    this.busy = false;
  }
  public get idle() {
    return this.tasks.length === 0;
  }
  private get taskCount() {
    return this.tasks.length;
  }
  public exec<T>(fun: () => any): Promise<T> {
    return new Promise((res, rej) => {
      this.tasks.push({ fun, res, rej, date: Date.now() });
      this.scan();
    });
  }
  private async scan() {
    if (this.idle || this.busy) {
      return;
    }
    this.busy = true;
    await this.handle(this.tasks.shift());
    setTimeout(() => {
      this.busy = false;
      this.scan();
    }, 0);
  }
  private async handle({
    fun,
    rej,
    res,
    date,
  }: {
    fun: () => any;
    rej: (e: any) => any;
    res: (e: any) => any;
    date: number;
  }) {
    if (Date.now() - date > this.timeout) {
      rej("超时");
    }
    try {
      res(await fun());
    } catch (error) {
      rej(error);
    }
  }
}
