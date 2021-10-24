import { log } from "../log";
import { DB as idb } from "./indexdb";
import { DB as ldb } from "./localstorage";
export interface IAPIBase {
  get(): any;
  upd(): boolean;
  clear(): boolean;
  del(): boolean;
}
let _DB: any;
let fallback: boolean = false;
if (typeof indexedDB !== undefined) {
  log("support indexedDB");
  _DB = idb;
} else {
  _DB = ldb;
  fallback = true;
}
class DB {
  public static async open(name = "app") {
    // if (!_DB) {
    //   await init();
    // }
    const db = await _DB.open(name);
    if (!db) {
      log("error while connect db");
      return null;
    }
    return new DB(db);
  }
  private db: any;
  constructor(db: any) {
    this.db = db;
  }
  get fallback() {
    return fallback;
  }
  public async get(name: string, key: string) {
    const [err, data] = await this.db.get(name, key);
    return err ? null : data;
  }
  public async upd(name: string, key: string, value: any) {
    const [err] = await this.db.upd(name, key, value);
    return !err;
  }
  public async close() {
    this.db.close();
  }
}
import { SIMDB } from "./SIMDB";
export const conIDB = (name?: string) => DB.open(name);
export const conMDB = (name?: string, long = true) => SIMDB.connect(name, long);
