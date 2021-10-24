import { loadJson } from "../core/api";

export const listPoems = () => loadJson("/poemcard/poems/index");
