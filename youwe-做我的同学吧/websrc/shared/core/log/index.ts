let reporter: any | null = null;
export function setReporter(f: any) {
  if (typeof f === "function") reporter = f;
}
export const log = (
  data: any,
  namespace?: string,
  level: "log" | "report" = "log"
) => {
  if (!sessionStorage.__forceLog && level === "log") {
    return;
  }
  if (level === "report") {
    // report api
    reporter && reporter({ data, namespace });
  } else {
    console[level] && console[level](data);
  }
};

log.namespace = (name: string, level="log" as any) => {
  return (data: any) => log(data, name, level);
};
