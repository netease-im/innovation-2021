import { createRouter, createWebHashHistory } from "@ionic/vue-router";
import { RouteRecordRaw } from "vue-router";
import { signinGuard } from "../user";
import { app } from "./app";

const routes: Array<RouteRecordRaw> = [
  {
    path: "",
    component: () => import("./views/home.vue"),
  },
  {
    path: "/favor",
    component: () => import("./views/Favor.vue"),
    beforeEnter:signinGuard
  },
  {
    path: "/about",
    component: () => import("./views/About.vue"),
  },
  // { path: "/:pathMatch(.*)*", name: "NotFound", redirect: "/" },
];

export const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes,
});

router.isReady().then(() => {
  app.mount("#app");
  console.error(`router ready...`)
});

export const addRoute = async (route: RouteRecordRaw, name = "") => {
  router.addRoute(name, route);
  // console.error(router)
};
