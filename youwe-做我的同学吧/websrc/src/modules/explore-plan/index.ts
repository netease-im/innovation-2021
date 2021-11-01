import { App, defineAsyncComponent } from "vue";
import { addRoute } from "../core";
import { signinGuard } from "../user";
import { Mcore } from "./deps";
let installed = false;
export default {
  install(app: App) {
    if (installed) return;
    console.error(`plan module install`);
    Mcore.subscribe("exp-plans.mine");
    Mcore.subscribe("ships.mine");
    Mcore.subscribe("ships.waiting");
    installed = true;
    addRoute({
      path: "/explore",
      component: () => import("./views/BriefList.vue"),
      beforeEnter: signinGuard
    });
    addRoute({
      path: "/explore/create-plan",
      component: () => import("./views/CreatePlan.vue"),
      beforeEnter: signinGuard,

    });
    addRoute({
      path: "/explore/active-plan/:id",
      component: () => import("./views/ActivePlan.vue"),
      beforeEnter: signinGuard,
    });
  },
};

export const ExplorePlanEntry = defineAsyncComponent(() =>
  import("./views/ExplorePlanEntry.vue")
);
