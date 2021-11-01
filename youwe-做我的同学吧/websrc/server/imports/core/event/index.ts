import { EventEmiter } from "./EventEmiter";

export * from "./types";
export * from "./EventEmiter";
export const conBus = function <T = any>(
  name: string,
  options: { interval?: number; local?: boolean } = {}
) {
  return new EventEmiter(name)
};