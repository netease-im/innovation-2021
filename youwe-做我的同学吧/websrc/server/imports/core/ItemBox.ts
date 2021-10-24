type mapKey = string | number;
export class ItemBox<T = any> {
  private box = new Map<string, T>();
  constructor(private def?: any) {}
  add(name: mapKey, callback: T) {
    this.box.set(String(name), callback);
    return this;
  }
  del(name: mapKey) {
    this.box.delete(String(name));
  }
  get(name: mapKey): T | undefined {
    return this.box.get(String(name)) || this.def;
  }
  get size() {
    return this.box.size;
  }
  each(cb: (val: T, key?: mapKey, map?: Map<string, T>) => void) {
    this.box.forEach(cb);
  }
}
