export function sleep(s: number) {
  return new Promise((res) => {
    setTimeout(res, s * 1000);
  });
}
export function randInt(max: number) {
  return Math.round(Math.random() * max);
}
export function randItem(arr: any[]) {
  const max = arr.length;
  return arr[Math.round(Math.random() * max)];
}
export function num2str(num: number, c = 2) {
  let t = String(num);
  while (t.length < c) {
    t = "0" + t;
  }
  return t;
}
export function deepCopy(e: any) {
  if (typeof e !== "object") {
    return e;
  }
  return JSON.parse(JSON.stringify(e));
}
export function assert(v: any, msg = "unknow") {
  if (typeof v === "function") {
    v = v();
  }
  if (!v && v !== 0) {
    throw Error(msg);
  }
}
export function isPlainObject(obj: any) {
  return Object.prototype.toString.call(obj) === "[object Object]";
}
export async function clearObject(obj: any) {
  if (typeof obj !== "object" || !obj || Object.keys(obj).length === 0) {
    return;
  }
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      const t = obj[key];
      switch (typeof t) {
        case "object":
          if ((Array.isArray(t) && t.length === 0) || !t) {
            delete obj[key];
          } else if (t && typeof t === "object") {
            clearObject(t);
          }
          break;
        case "number":
          if (isNaN(t) || !isFinite(t)) {
            delete obj[key];
          }
          break;
        case "undefined":
        case "function":
        case "symbol":
          delete obj[key];
          break;
      }
    }
  }
}
export function makeArr(a: any) {
  return Array.isArray(a) ? a : [a];
}
export function isDef(v: any) {
  return v !== null && v !== undefined;
}
