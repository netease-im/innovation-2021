import { num2str } from "./base";
import { log } from "./log";

/**
 * @description 返回一个格式化的时间用于计时的格式化 小时：分钟：秒
 * @param time 时间秒 或者毫秒
 */
export function fmtTime(time: number): string {
  if (typeof time !== "number" || time < 0) {
    return "";
  }
  if (time > 9999999999) {
    time = Math.floor(time / 1000);
  }
  const hour = Math.floor(time / 3600);
  time = time % 3600;
  const minute = Math.floor(time / 60);
  time = time % 60;
  const second = time;
  return hour + ":" + num2str(minute, 2) + ":" + num2str(second, 2);
}

export interface IFMTDate {
  ymdSeparator?: string;
  hmsSeparator?: string;
  iYear?: boolean;
  iMonth?: boolean;
  iDay?: boolean;
  iHour?: boolean;
  iMinu?: boolean;
  iSec?: boolean;
  onlyYMD?: boolean;
}
const defIFMT: IFMTDate = {
  ymdSeparator: "-",
  hmsSeparator: ":",
  iYear: true,
  iMonth: true,
  iDay: true,
  iHour: true,
  iMinu: true,
  iSec: true,
  onlyYMD: false,
};
/**
 * 格式化日期
 * @param date 日期字符串、毫秒(秒)或日期对象
 */
export function fmtDate(
  date?: number | Date | string,
  options?: IFMTDate
): string {
  // try {
  const copy = date;
  const t = validDate(date);
  if (!t) {
    return "无效日期:" + copy;
  }
  const opt = { ...defIFMT, ...options };
  const ymdContainer: number[] = [];
  if (opt.iYear) {
    ymdContainer.push(t.getFullYear());
  }
  if (opt.iMonth) {
    ymdContainer.push(t.getMonth() + 1);
  }
  if (opt.iDay) {
    ymdContainer.push(t.getDate());
  }
  const r = ymdContainer.map((_) => num2str(_, 2)).join(opt.ymdSeparator);
  if (!opt.onlyYMD) {
    const hmsContainer: number[] = [];
    if (opt.iHour) {
      hmsContainer.push(t.getHours());
    }
    if (opt.iMinu) {
      hmsContainer.push(t.getMinutes());
    }
    if (opt.iSec) {
      hmsContainer.push(t.getSeconds());
    }
    return (
      r + " " + hmsContainer.map((_) => num2str(_, 2)).join(opt.hmsSeparator)
    );
  } else {
    return r;
  }
  // } catch (e) {
  //   log(e);
  //   return '无效日期';
  // }
}

export function validDate(e: any): Date | null {
  if (typeof e === "number") {
    if (e < 9999999999) {
      e = e * 1000;
    }
  }
  if (!e) {
    e = new Date();
  } else if (!(e instanceof Date)) {
    e = new Date(e);
  }
  return !!e.getTime() ? e : null;
}

const UnitMap = {
  年: 31557600000,
  月: 2629800000,
  天: 86400000,
  小时: 3600000,
  分钟: 60000,
  秒: 1000,
};

export function humanlize(date: any) {
  let pass: number;
  const t = validDate(date);
  if (!t) {
    log("无效日期:" + date);
    return "";
  }
  pass = Date.now() - t.getTime();
  let ret = "";
  for (const key in UnitMap) {
    if (pass >= (UnitMap as any)[key]) {
      ret = Math.floor(pass / (UnitMap as any)[key]) + key + "前";
      break;
    }
  }
  return ret || "刚刚";
}

export const timestamp = () => clampTime();

export const today = () => fmtDate(undefined, { onlyYMD: true });

export const clampTime = (e: any = new Date()) =>
  Math.floor(new Date(e).getTime() / 1000);
