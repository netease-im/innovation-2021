export type colorDefine =
  | [number, number, number, number]
  | [number, number, number]
  | string;

export class Color {
  public static rgb2hex([r = 0, g = 0, b = 0]) {
    return (
      "#" +
      ([r, g, b] as number[])
        .map((el) => {
          let v = el.toString(16);
          if (v.length < 2) {
            v = "0" + v;
          }
          return v;
        })
        .join("")
        .toUpperCase()
    );
  }
  public static hex2rgb(s: string) {
    s = s.slice(1);
    const ret:number[] = [];
    ret.push(parseInt(`0x${s.slice(0, 2)}`, 10));
    ret.push(parseInt(`0x${s.slice(2, 4)}`, 10));
    ret.push(parseInt(`0x${s.slice(4)}`, 10));
    return ret;
  }
  public static parse(
    def:
      | string
      | [number, number, number]
      | [number, number, number, number]
      | Color,
    alpha?: number
  ): { hex: string; value: [number, number, number]; alpha: number } {
    const obj = {
      hex: "",
      value: [] as any,
      alpha,
    } as any;
    if (def instanceof Color) {
      return def.data;
    } else if (typeof def === "string") {
      obj.hex = def;
      obj.value = this.hex2rgb(def);
    } else if (def instanceof Array) {
      if (def.length === 3) {
        obj.hex = this.rgb2hex(def);
        obj.value = [...def];
      } else if (def.length === 4) {
        obj.hex = this.rgb2hex(def.slice(0, 3) as any);
        obj.value = def.slice(0, 3);
        obj.alpha = def[3];
      }
    }
    if (typeof obj.alpha !== "number") {
      obj.alpha = 1;
    }
    if (!obj.hex) {
      obj.hex = "#000000";
      obj.value = [0, 0, 0];
    }
    return obj;
  }
  private data!: {
    hex: string;
    value: any;
    alpha: number;
  };
  constructor(def: any, alpha?: number) {
    this.setData(def, alpha);
  }
  public setData(def: any, alpha?: number) {
    this.data = Color.parse(def, alpha);
  }
  public get rgba() {
    return `rgba(${this.data.value.join(",")},${this.data.alpha})`;
  }
  public get hex() {
    return this.data.hex;
  }
  public get rgb() {
    return `rgba(${this.data.value.join(",")})`;
  }
  public get alpha() {
    return this.data.alpha;
  }
  public set alpha(v) {
    v = Math.max(0, Math.min(1, v));
    this.setData(this.data.hex, v);
  }
  public toString() {
    return this.hex;
  }
}

export interface ColorUnit {
  background: colorDefine;
  text?: colorDefine;
  border?: colorDefine;
  outlet?: colorDefine;
}
