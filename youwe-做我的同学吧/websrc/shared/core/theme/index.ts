import { Color } from "./color";
import { ThemeColorset, Theme } from "./model";

const light: ThemeColorset = {
  name: "orange",
  default: {
    background: [255, 228, 196],
    text: "#333333",
  },
  primary: {
    background: "#FF5722",
  },
  lighter: {
    background: "#FFCCBC",
    text: "#212121",
  },
  daker: {
    background: "#E64A19",
  },
  accent: {
    background: "#FFC107",
  },
  divider: "#BDBDBD",
  text: {
    default: "#FFFFFF",
    primary: "#212121",
    secodary: "#757575",
  },
};

enum ITextCase {
  Primary,
  Secondary,
  DiableHintIcon,
}

class ThemeManager {
  private themeSets: ThemeColorset[] = [light];
  private dark!: boolean;
  private currentSet!: Theme;
  constructor(dark?: boolean) {
    this.currentSet = new Theme(light);
    this.setDarkMode(dark);
    this.updateStyleVars();
  }
  public setDarkMode(v?: boolean) {
    this.dark = !!v;
    if (v) {
      document.body.setAttribute("mode", "dark");
    } else {
      document.body.setAttribute("mode", "light");
    }
    this.currentSet.divider.alpha = 0.12;
  }
  public setDefaultThemes(e: ThemeColorset[]) {
    this.themeSets = e.concat(light);
  }
  public setTheme(name?: string) {
    const index = this.themeSets.findIndex((e) => e.name === name) || 0;
    const el = this.themeSets[index];
    if (el !== this.currentSet.meta) {
      this.currentSet.setConfig(el);
      this.updateStyleVars();
    }
  }
  private updateStyleVars() {
    let el = document.querySelector("style#hj-theme-var") as HTMLStyleElement;
    if (!el) {
      el = document.createElement("style");
      el.id = "hj-theme-var";
      document.querySelector("head")?.appendChild(el);
    }
    const pcolor = new Color(
      this.currentSet.text.primary,
      this.dark ? 1 : 0.87
    );
    const scolor = new Color(
      this.currentSet.text.secodary,
      this.dark ? 0.7 : 0.54
    );
    const dcolor = new Color(
      this.currentSet.text.primary,
      this.dark ? 0.5 : 0.38
    );
    el.innerText =
      ":root{ " +
      "--primary-bgc:" +
      this.currentSet.primary.background +
      ";--primary-txt:" +
      this.currentSet.primary.text +
      ";--def-bgc:" +
      this.currentSet.default.background +
      ";--def-txt:" +
      this.currentSet.default.text +
      ";--accent-bgc:" +
      this.currentSet.accent.background +
      ";--accent-txt:" +
      this.currentSet.accent.text +
      ";--light-bgc:" +
      this.currentSet.lighter.background +
      ";--light-txt:" +
      this.currentSet.lighter.text +
      ";--dark-bgc:" +
      this.currentSet.daker.background +
      ";--dark-text:" +
      this.currentSet.daker.text +
      ";--primary-color:" +
      pcolor.rgba +
      ";--secodary-color:" +
      scolor.rgba +
      ";--disable-color:" +
      dcolor.rgba +
      ";--icon-color:" +
      dcolor.rgba +
      ";--hint-color:" +
      dcolor.rgba +
      ";--divider-color:" +
      this.currentSet.divider.rgba +
      ";}";
  }
}

export const themeManager = new ThemeManager(true);
