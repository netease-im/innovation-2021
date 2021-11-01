import { App, defineAsyncComponent } from "vue";
import { addRoute, registEntryType, subscribe } from "../core";
import { signinGuard } from "../user";

let installed = false;
export default {
  install(app: App) {
    if (installed) return;
    installed = true;
    addRoute({
      path: "/poemcard/room/:id",
      component: () => import("./views/RoomPage.vue"),
      beforeEnter: signinGuard,
    });
    addRoute({
      path: "/poem/lines/create",
      component: () => import("./views/NewPoem.vue"),
      beforeEnter: signinGuard,
    });
    subscribe("poems")
    registEntryType(
      ["poem", "word-card"].join("/"),
      "字卡大战",
      defineAsyncComponent(() => import("./views/RoomEntry.vue"))
    );
  },
};
