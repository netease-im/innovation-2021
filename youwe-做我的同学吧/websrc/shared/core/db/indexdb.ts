import { log } from "../log";


function open(name: string, version = 1): Promise<[boolean, any]> {
  const DBOpenRequest = window.indexedDB.open(name, version);

  // these event handlers act on the database being opened.
  return new Promise(resolve => {
    DBOpenRequest.onerror = e => {
      log(e);
      resolve([true, e]);
    };
    DBOpenRequest.onsuccess = e => {
      resolve([false, DBOpenRequest.result]);
    };
    DBOpenRequest.onupgradeneeded = e => {
      resolve([false, DBOpenRequest.result]);
    };
  });
}
function drop(name: string) {
  indexedDB.deleteDatabase(name);
}
async function allDatabases() {
  return await (indexedDB as any).databases();
}
function add(store: IDBObjectStore, key: string, val: any) {
  return new Promise<[boolean, any]>(res => {
    const req = store.put(val, key);
    req.onerror = e => {
      res([true, e]);
    };
    req.onsuccess = e => {
      res([false, req.result]);
    };
  });
}
function get(store: IDBObjectStore, key: string) {
  return new Promise<[boolean, any]>(res => {
    const req = store.get(key);
    req.onerror = e => {
      res([true, e]);
    };
    req.onsuccess = e => {
      res([false, req.result]);
    };
  });
}
function reset(name: string) {
  return new Promise<[boolean, any]>(res => {
    const req = indexedDB.deleteDatabase(name);
    req.onerror = e => {
      res([true, e]);
    };
    req.onsuccess = e => {
      res([false, req.result]);
    };
  });
}
export class DB {
  public static async open(name: string): Promise<DB | null> {
    if (typeof name !== 'string') {
      return null;
    }
    const version = Number(localStorage['idbv_' + name]) || 1;
    log('open indexed version ' + version);
    const [err, db] = await open(name, version);
    if (!err) {
      return new DB(db, name, version);
    }
    const [err1, db1] = await open(name, version + 1);
    if (!err1) {
      return new DB(db1, name, version + 1);
    }
    reset(name);
    return DB.open(name);
  }
  public version: number;
  public name: string;
  private db: IDBDatabase;
  constructor(db: IDBDatabase, n: string, v: number) {
    this.version = v;
    localStorage['idbv_' + n] = v;
    this.db = db;
    this.name = n;
  }
  public async get(name: string, key: string) {
    if (!this.exist(name)) {
      return [true];
    }
    try {
      const store = await this.db
        .transaction(name, 'readonly')
        .objectStore(name);
      return await get(store, key);
    } catch (error) {
      return [true, error];
    }
  }
  public async set(name: string, key: string, val: any, options = {}) {
    let store: IDBObjectStore;
    if (!this.exist(name)) {
      log('create collection');
      this.db.close();
      this.version += 1;
      const [err, db] = await open(this.name, this.version);
      localStorage['idbv_' + this.name] = this.version;
      if (err) {
        return [true, '打开失败'];
      }
      this.db = db;
      // this.db.transaction(key, 'versionchange');
      store = this.db.createObjectStore(name, {
        autoIncrement: true,
        ...options
      });
    } else {
      store = this.db.transaction(name, 'readwrite').objectStore(name);
    }
    return await add(store, key, val);
  }
  public close() {
    this.db.close();
  }
  private exist(key: string) {
    return this.db.objectStoreNames.contains(key);
  }
}
