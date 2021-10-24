import { ComputedRef, Ref, computed, ref } from "vue";
import { sleep } from "../base";

import { Channel, conBus } from "../event";
import { ItemBox } from "../ItemBox";
import { log as blog } from "../log";
import { PromiseQueue } from "../PromiseQueue";
import { decodeSocket, encodeSocket } from "./coder";
import { ISocket, ISocketSend, SocketHandler } from "./type";

class Socket {
  public static connect(
    url: string,
    name = "default",
    data?: any
  ): Promise<Socket> {
    let t = this.connections.get(name);
    return new Promise(async (resolve, reject) => {
      if (t && !t.isDead) {
        Object.assign(t.data, data);
        while (t.status === WebSocket.CONNECTING) {
          await sleep(1);
        }
        if (t.status === WebSocket.OPEN) {
          return resolve(t);
        }
      }
      const socket = new WebSocket(Socket.patchUrl(url));

      const timer = setTimeout(() => {
        socket.close(1000, "timeout");
        reject("TIMEOUT");
      }, 30000);
      function clearState() {
        clearTimeout(timer);
        socket.removeEventListener("open", onOpen);
        socket.removeEventListener("error", onError);
      }
      function onOpen() {
        if (t) {
          t.transfer(socket, data);
        } else {
          t = new Socket(name, socket, data);
        }
        if (t.cache.size > 0) {
          t.cache.forEach((el) => {
            t?.sendSocket(el);
          });
          t.cache.clear();
        }
        t.channel.emit("open", { name, url });
        t.status = WebSocket.OPEN;
        clearState();
        resolve(t);
      }
      function onError(ev: any) {
        clearState();
        reject(ev);
      }
      socket.addEventListener("open", onOpen);
      socket.addEventListener("error", onError);
    });
  }
  protected static scanTimer?: any = 0;
  protected static scanMark: any = 0;
  protected static connections = new ItemBox<Socket>();
  protected static handlers = new ItemBox<SocketHandler>(() => {});
  protected static addCommonHandle(code: string, func: SocketHandler) {
    this.handlers.add(code, func);
    return this;
  }
  protected static scan() {
    if (this.scanTimer) {
      return;
    }
    this.scanTimer = setInterval(() => {
      if (this.connections.size === 0) {
        return;
      }
      const now = Date.now();
      this.connections.each((el) => {
        if (now - el.lastActive > 30000) {
          el.ping();
        }
      });
    }, 30000);
  }
  protected static stopScan() {
    if (this.scanTimer) {
      clearInterval(this.scanTimer);
      this.scanTimer = 0;
    }
  }
  public static patchUrl(url: string) {
    if (url.startsWith("ws://") || url.startsWith("wss://")) {
      return url;
    }
    if (!url.startsWith("/")) {
      url = "/" + url;
    }
    return "ws" + location.origin.slice(4) + url;
  }
  private logger: (msg: any, type?: number | undefined) => void;
  public name: string;
  public data: { [key: string]: any } = {};
  public status: number = WebSocket.CLOSED;
  public get isOpen() {
    return this.status === WebSocket.OPEN;
  }
  public get isDead() {
    return (
      this.status === WebSocket.CLOSED || this.status === WebSocket.CLOSING
    );
  }
  public channel: Channel;
  private socket: WebSocket;
  private cache: Set<string | Blob | ArrayBuffer> = new Set();
  private lastActive = 0;
  private handlers = new ItemBox<SocketHandler>(() => {});
  private queue = new PromiseQueue(5);
  private constructor(name: string, socket: WebSocket, e?: any) {
    Object.assign(this.data, e);
    this.socket = socket;
    this.channel = conBus(`socket-${name}`);
    this.name = name;
    Socket.connections.add(name, this);
    this.addHandler();
    Socket.scan();
    this.logger = blog.namespace(`socket-${name}`);
  }
  get connections() {
    return Socket.connections;
  }
  public close(code: number) {
    if (this.isOpen) {
      this.status = WebSocket.CLOSED;
      this.socket.close(code);
    }
  }
  public send(e: ISocketSend) {
    if (!e || (!e.code && !e.scid)) {
      this.logger(`无效的socket包:${JSON.stringify(e).slice(0, 40)}`);
      return Promise.reject("VAL_ERR");
    }
    if (typeof e.ext !== "string" || (e.ext || "").indexOf(" ") > -1) {
      this.logger("ext only can be number | string without whitespace");
      return Promise.reject("VAL_ERR");
    }
    this.logger(`try send`);
    this.logger(e);
    return new Promise<any | undefined>((res, rej) => {
      if (e.wait) {
        const cid = this.queue.add(res, rej);
        e.cid = cid;
      }
      this.sendSocket(encodeSocket(e));
      if (!e.wait) {
        res(true);
      }
    });
  }
  public addRoute(code: string | number, func: SocketHandler) {
    this.handlers.add(String(code), func);
    return this;
  }
  private sendSocket(e: string | Blob | ArrayBuffer) {
    if (!this.socket || !this.isOpen) {
      this.logger(`cache:${e}`);
      this.cache.add(e);
    } else {
      this.lastActive = Date.now();
      this.logger("send:" + e.slice(0, 50));
      this.socket.send(e);
    }
    return this;
  }
  private addHandler() {
    if (this.socket instanceof WebSocket) {
      this.socket.onmessage = ({ data }) => {
        this.lastActive = Date.now();
        this.logger(typeof data);
        this.logger(data);
        this.routeData.call(this, data);
      };
      this.socket.onclose = ({ code }) => {
        Socket.connections.del(this.name);
        this.channel.emit("close");
      };
    }
  }
  private async routeData(this: Socket, raw: string) {
    if (!raw || raw === "0") {
      return;
    }
    if (raw === "") {
      return this.sendSocket("0");
    }
    const e = decodeSocket(raw, { decode: 1 });
    this.logger({ raw, e });
    if (e) {
      if (e.cid) {
        const t = this.queue.get(e.cid);
        delete e.cid;
        if (typeof t === "function") {
          t(e);
        }
      }
      if (!e.code) {
        this.sendSocket(
          !!e.scid ? encodeSocket({ code: "", scid: e.scid }) : ""
        );
        return;
      }
      Object.freeze(e);
      // code exist cases
      const hs = this.handlers.get(e.code);
      if (typeof hs !== "function") {
        return this.logger("未注册处理器");
      }
      if (!e.scid) {
        return hs(e, this);
      }
      let ansData = await hs(e, this);
      if (ansData && !ansData.body && !ansData.ext) {
        if (typeof ansData === "string") {
          ansData = { ext: ansData };
        } else {
          ansData = { body: ansData };
        }
      }
      return this.send({ ...ansData, scid: e.scid });
    }
  }
  private ping() {
    this.sendSocket("");
    this.lastActive = Date.now();
  }
  private transfer(socket: WebSocket, data: any) {
    this.socket = socket;
    this.addHandler();
    Object.assign(this.data, data);
  }
}

export const connect = (
  url: string,
  name = "default",
  data?: any
): Promise<ISocket> => Socket.connect(url, name, data);
