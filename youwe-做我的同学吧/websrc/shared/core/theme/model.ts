import { Color, ColorUnit } from "./color";

export interface ThemeColorset {
  name: string;
  default: ColorUnit;
  primary: ColorUnit;
  lighter: ColorUnit;
  daker: ColorUnit;
  accent: ColorUnit;
  divider: string;
  text: {
    default: string;
    primary: string;
    secodary: string;
  };
}

export class Theme {
  public divider!: Color;
  public default!: { background: Color; text: Color };
  public primary!: { background: Color; text: Color };
  public name!: string;
  public lighter!: { background: Color; text: Color };
  public daker!: { background: Color; text: Color };
  public accent!: { background: Color; text: Color };
  public text!: { default: Color; primary: Color; secodary: Color };
  public meta!: any;
  constructor(e: ThemeColorset) {
    this.setConfig(e);
  }
  public setConfig(e: ThemeColorset) {
    this.meta = e;
    this.divider = new Color(e.divider, 0.12);
    this.name = e.name;
    this.primary = {
      background: new Color(e.primary.background),
      text: new Color(e.primary.text || e.text.default),
    };
    this.default = {
      background: new Color(e.default.background),
      text: new Color(e.default.text || e.text.default),
    };
    this.lighter = {
      background: new Color(e.lighter.background),
      text: new Color(e.lighter.text || e.text.default),
    };
    this.daker = {
      background: new Color(e.daker.background),
      text: new Color(e.daker.text || e.text.default),
    };
    this.accent = {
      background: new Color(e.accent.background),
      text: new Color(e.accent.text || e.text.default),
    };
    this.text = {
      default: new Color(e.text.default),
      primary: new Color(e.text.primary),
      secodary: new Color(e.text.secodary),
    };
  }
}
