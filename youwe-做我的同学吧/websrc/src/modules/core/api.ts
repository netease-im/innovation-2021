import { MTracker } from "@/meteor";
import { onUnmounted, reactive } from "vue";
import { Mcore } from "./deps";

interface RevokeOption {
  showLoading?: boolean;
  hideAlert?: boolean;
  wait?: boolean | undefined;
  onResultReceived?: ((error: any, result?: any) => void) | undefined;
  returnStubValue?: boolean;
  throwStubExceptions?: boolean;
  noRetry?: boolean;
}

export async function handleResponse(
  resolve: any,
  reject: any,
  data: any[],
  config?: any
) {
  console.log(data);
  if (!data[0] && Array.isArray(data[1])) data = data[1];
  if (data[0] && !config?.hideAlert) {
    const err = data[0];
    const tips = typeof err === "string" ? err : err.message || "未知错误";
    alert("出现错误:" + tips);
  }
  resolve(data);
}
export const revoke = (name: string, arg1?: any, ...args: any[]) => {
  const options: RevokeOption = {
    showLoading: true,
    noRetry: true,
    wait: true,
  };
  if (typeof arg1 !== "function") args.unshift(arg1);
  else Object.assign(options, arg1());
  return new Promise<[any, any?]>((resolve, reject) => {
    return Mcore.apply(name, args, options, (err, result) =>
      handleResponse(resolve, reject, [err, result], options)
    );
  });
};

const subs = new Map<string, { stop: () => void }>();
export function subscribe(key: string, ...args: any[]) {
  const os = subs.get(key);
  os && os.stop();
  let resolved = false;
  return new Promise((resolve) => {
    subs.set(
      key,
      Mcore.subscribe(key, ...args, {
        onReady() {
          resolve([0, subs.get(key)]);
          resolved = true;
        },
        onStop(err: typeof Mcore.Error) {
          if (!resolved) {
            resolve([err, subs.get(key)]);
          }
          subs.delete(key)
        },
      })
    );
  });
}
export function unsubscribe(key: string) {

}
export function useMongo<T = any, U = T>(
  collection: Mongo.Collection<T, U>,
  selector: Mongo.Selector<T> = {},
  options?: Mongo.Options<T>
) {
  const result = reactive<U[]>([]);
  const query = collection.find(selector, options).observe({
    added(doc) {
      result.push(doc);
    },
    removed(doc) {
      const index = result.findIndex((el) => (el as any)._id === doc._id);
      result.splice(index, 1);
    },
    changed(nd, od) {
      const index = result.findIndex((el) => (el as any)._id === od._id);
      result.splice(index, 1, nd);
    },
  });
  onUnmounted(() => query.stop());
  return result;
}

export function loadJson(path: string, def?: any) {
  return fetch(path)
    .then((res) => res.json())
    .catch((err) => (def ? def : null));
}

export const serverState: DDP.DDPStatus = reactive({}) as any;

Meteor.startup(() => {
  MTracker.autorun(() => {
    const state = Mcore.status();
    for (const key in state) {
      serverState[key] = state[key];
    }
    console.log(serverState);
  });
});
