export type EventCallbackFunc = (...args: any[]) => Promise<any> | any;

export interface EventCallbackConf {
  conf: EventConfig;
  func: EventCallbackFunc;
  freeTime?: number;
}
export interface EventConfig {
  uniq?: boolean | string;
  boom?: number;
  ttl?: number;
  top?: boolean;
  name?: string;
  hook?: string;
  once?: boolean;
  interval?: number;
}
export interface EventOptions {
  uniq?: boolean | string;
  boom?: number;
  ttl?: number;
  top?: boolean;
  name?: string;
  hook?: string;
  once?: boolean;
  interval?: number;
}
