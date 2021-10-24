import { hashString } from "../hash";

export class StorageMessager {
  private static uid = "";
  public onmessage?: (this: StorageMessager, ev: MessageEvent) => any;
  public onmessageerror?: any;
  private key: string;
  constructor(public name: string) {
    if (!StorageMessager.uid) {
      StorageMessager.uid = hashString(Date.now().toString() + location.href);
    }
    this.key = `__storage_msg_${hashString(name)}`;
    window.addEventListener("storage", this.handler);
  }
  public postMessage(data: any) {
    localStorage.setItem(
      this.key,
      JSON.stringify({ uid: StorageMessager.uid, data })
    );
  }
  public close() {
    window.removeEventListener("storage", this.handler);
  }
  private handler() {
    const str = localStorage[this.key];
    try {
      const obj = JSON.parse(str);
      if (
        obj &&
        typeof this.onmessage === "function" &&
        obj.uid !== StorageMessager.uid &&
        obj.data
      ) {
        this.onmessage({ data: obj.data } as any);
      }
    } catch (error) {
      if (typeof this.onmessageerror === "function") {
        this.onmessageerror(error);
      }
    }
  }
}
