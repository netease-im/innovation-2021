import { timestamp } from "../date";
import { ISocketParsed } from "./type";

export const SOCKET_PART_SEP = "\n";
export const SOCKET_HEAD_SEP = "\t";
export const encodeSocket = (e: ISocketParsed) => {
  const { code, ext, cid, scid, body } = e;
  const coded = typeof body === "string" ? "" : "J";
  const datas: string[] = [
    [code || "", ext || "", cid || "", scid || "", coded].join(SOCKET_HEAD_SEP),
  ];
  datas.push(coded ? JSON.stringify(body) : body);
  const fs = datas.join(SOCKET_PART_SEP);
  return fs;
};
export const decodeSocket = (str: string, options?: any): ISocketParsed => {
  if (typeof str !== "string") {
    return str;
  }
  const si: number = str.indexOf(SOCKET_PART_SEP);
  const hs = si > -1 ? str.slice(0, si) : str;
  const bs = si > -1 ? str.slice(si + 1) : "";
  const [code, ext, cid, scid, coded] = hs.split(SOCKET_HEAD_SEP);
  let body;
  try {
    body = coded ? JSON.parse(bs) : bs;
  } catch (error) {
    body = bs;
  }
  return {
    code,
    ext,
    cid,
    scid,
    body,
  };
};
