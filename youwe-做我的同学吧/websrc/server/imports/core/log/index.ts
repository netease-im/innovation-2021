let reporter: any | null = null;
export function setReporter(f: any) {
  if (typeof f === "function") reporter = f;
}

const map: any = {
  0: "log",
  1: "info",
  2: "warn",
  3: "error",

  11: "log"
}
export const log = (
  data: any,
  namespace?: string,
  level = 0,
  opts = {
    level: 1
  }
) => {
  if (!sessionStorage.__forceLog && level < opts.level) {
    return;
  }
  const funName = map[level] || 'log'
  if (level === 11) {
    // report api
    reporter && reporter({ data, namespace });
  } else {
    const fun = (console as any)[funName];
    fun && fun(data);
  }
};

log.namespace = (name: string, level = 0, opts?: { level: number }) => {
  return (data: any) => log(data, name, level, opts);
};
