import { hashString } from "./hash";
import { log } from "./log";

export function removeScript(id: string, raw = false) {
  document.querySelector(`script#${raw ? "script__" : ""}${id}`)?.remove();
}
export function removeScripts(ids: string[]) {
  ids.forEach((e) => removeScript(e));
}
/**
 *
 * @param src
 * @param id
 * @returns [fail,id]
 */
export async function loadScript(
  src: string,
  id = ""
): Promise<[boolean, string]> {
  id = "script__" + (id || hashString(src));
  if (document.querySelector(`script#${id}`)) {
    log(`exist ------ ${id} `);
    return [false, id];
  }
  return new Promise((resolve) => {
    const el = document.createElement("script");
    el.id = id;
    el.src = src;
    document.head.append(el);
    el.onload = () => {
      log(`success ${src} ------ `);
      resolve([false, id]);
    };
    el.onerror = (err) => {
      log("fail ------ ");
      console.error(err);
      resolve([true, id]);
    };
  });
}
export async function loadScripts(
  arr: Array<{ id?: string; src: string } | string>,
  seq = false
) {
  if (!Array.isArray(arr)) {
    return () => {
      console.warn("empty scripts");
    };
  }
  let ids: Array<[boolean, string]> = [];
  if (seq) {
    for (const el of arr) {
      ids.push(
        await (typeof el === "string"
          ? loadScript(el)
          : loadScript(el.src, el.id))
      );
    }
  } else
    ids = await Promise.all<[boolean, string]>(
      arr.map((el) =>
        typeof el === "string" ? loadScript(el) : loadScript(el.src, el.id)
      )
    );
  function dispose(index = -1) {
    const sids = ids.map((el) => el[1]);
    if (typeof index === "string") {
      removeScript(index, true);
    } else if (!sids[index]) {
      removeScripts(sids);
    } else {
      removeScript(sids[index]);
    }
  }
  if (ids.some((el) => el[0])) {
    log(ids);
    dispose();
    throw Error("core js load fail");
  }
  return dispose;
}
