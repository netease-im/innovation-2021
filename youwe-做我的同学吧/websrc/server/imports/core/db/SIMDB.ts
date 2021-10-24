import { LOCALDB, longDB, tempDB } from "./localstorage";
// 配置
// 说明：创建db实例子时，如果不给与一个域名，则会使用默认域名
// 非严格模式下，创建已经存在的key以及更新不存在的key都可用。
// db.clrSync();
const dbsName = "yuhj_dbs";
if (localStorage[dbsName] === undefined) {
  longDB.clr();
  longDB.set(dbsName, ["APP"]);
}
const emptyObj = Object.create(null);
export class SIMDB {
  public static version = "1.0.0";
  db: LOCALDB;
  public static connect(dbname = "APP", long = true) {
    return new SIMDB(dbname, long);
  }
  public clear() {
    return this.db.clr();
  }
  public static dbs() {
    return longDB.get(dbsName)[1];
  }
  private dbname: string;
  private prefix: string;
  private constructor(dbname: string, long: boolean) {
    this.db = long ? longDB : tempDB;
    this.dbname = dbname;
    this.prefix = dbname + "_";
    this.init();
  }
  public set(key: string, val: any) {
    return this.db.set(this.prefix + key, val);
  }
  public get(key: string, def = emptyObj) {
    const [err, data] = this.db.get(this.prefix + key);
    return err ? def : data;
  }
  public del(key: any) {
    return this.db.del(this.prefix + key);
  }
  public drop() {
    if (this.keys.length === 0) {
      return;
    }
    this.keys.forEach((key) => {
      this.db.del(key);
    });
  }
  private init() {
    if (this.inited) {
      return;
    }
    this.db.set(dbsName, [...SIMDB.dbs(), this.dbname]);
  }
  get inited() {
    const [err, res] = this.db.get(dbsName);
    if (!err) {
      return res.indexOf(this.dbname) !== -1;
    }
    return false;
  }
  get keys() {
    return this.db.keys.filter(
      (ele: string) => ele.slice(0, this.prefix.length) === this.prefix
    );
  }
}
