import { createApp } from "vue";
import App from "./App.vue";
import { router } from "./router";

export const app = createApp(App);

export function appUse(comp: any) {
  app.use(comp);
}

app.use(router)

// import "../media/services/nertc/mertc"