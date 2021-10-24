import { ItemBox } from "../ItemBox";

export interface ISocketParsed {
  code?: string | number;
  ext?: string;
  cid?: string;
  scid?: string;
  type?: string;
  body?: any;
}
export interface ISocketSend extends ISocketParsed {
  wait?: any;
  encode?: boolean;
}
export interface ISocket {
  name: string;
  data: { [key: string]: any };
  readonly status: number;
  readonly isOpen: boolean;
  readonly isDead: boolean;
  readonly connections: ItemBox<ISocket>;
  close(code: number): void;
  send(e: ISocketSend): Promise<any>;
  addRoute(code: string, func: SocketHandler): ISocket;
}
export type SocketHandler = (e: ISocketParsed, ws: ISocket) => Promise<any>;
