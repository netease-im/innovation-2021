import { log } from "../log";

function setItem(key: string, val: any): [0] {
  localStorage.setItem(key, btoa(JSON.stringify(val)));
  return [0];
}
function getItem(key: string): [0 | 1, any?] {
  const t = localStorage[key];
  try {
    return [0, JSON.parse(atob(localStorage[key]) as string)];
  } catch (error) {
    delete localStorage[key];
    return [1, error.message];
  }
}
function setTempItem(key: string, val: any): [0] {
  sessionStorage.setItem(key, btoa(JSON.stringify(val)));
  return [0];
}
function getTempItem(key: string): [0 | 1, any?] {
  const t = sessionStorage.getItem(key);
  try {
    return [0, JSON.parse(atob(sessionStorage[key]) as string)];
  } catch (error) {
    delete sessionStorage[key];
    return [1, error.message];
  }
}
export class LOCALDB {
  private db: Storage;
  constructor(private long = true) {
    this.db = long ? localStorage : sessionStorage;
  }
  public get keys() {
    const t: string[] = [];
    for (let i = 0; i < this.db.length; i++) {
      if (this.db.key(i) !== null) {
        t.push(this.db.key(i) as string);
      }
    }
    return t;
  }
  public get(key: string) {
    return (this.long ? getItem : getTempItem)(key);
  }
  public set(key: string, value: any) {
    return (this.long ? setItem : setTempItem)(key, value);
  }
  public del(key: string) {
    this.db.removeItem(key);
  }
  public clr() {
    log("clear");
    if (typeof this.db.clear === "function") {
      return this.db.clear();
    }
    for (let i = 0; i < this.db.length; i++) {
      this.db.removeItem(this.db.key(i) as string);
    }
  }
}

export const longDB = new LOCALDB();
export const tempDB = new LOCALDB(false);

// tslint:disable-next-line: max-classes-per-file
export class DB {
  public static async open(name: string, version = 1) {
    DB.validateKey(name);
    return new DB(name, version);
  }
  private static validateKey(e: string) {
    if (typeof e !== "string" || e.indexOf("_") > -1) {
      throw Error("unvliad name with");
    }
  }
  public name: string;
  constructor(n: string, v: number) {
    this.name = n;
  }

  public async get(name: string, key: string) {
    return longDB.get(this.genkey(name, key));
  }
  public async set(name: string, key: string, val: any) {
    return longDB.set(this.genkey(name, key), val);
  }
  public close() {
    // empty
  }
  private genkey(cname: string, key: string) {
    DB.validateKey(cname);
    DB.validateKey(key);
    return this.name + "_" + cname + "_" + key;
  }
}
