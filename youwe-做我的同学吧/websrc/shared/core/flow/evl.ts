class SetExt<T = any> extends Set<T> {
  constructor(args: T[]) {
    super(args);
  }
  find(func: any) {
    for (const el of this) {
      if (func(el)) return el;
    }
    return null;
  }
}
class MapExt<T, M> extends Map<T, M> {
  constructor(...args: any[]) {
    super(...args);
  }
  find(func: any, by = "key"): [T, M] | [] {
    for (const [key, val] of this.entries()) {
      if (func(by === "key" ? key : val)) {
        return [key, val];
      }
    }
    return [];
  }
}
// group
class Environment {
  broadcast(name: string, data: any) {
    this.things.forEach((e) => {});
  }
  rules: any;
  things: SetExt<Thing> = new SetExt([]);
  askJoin(t: Thing) {
    this.things.add(t);
  }
}

const emSymb = Symbol();
class EventEmit {
  private [emSymb] = new MapExt();
  constructor() {}
}

// item

class Thing {
  
}
