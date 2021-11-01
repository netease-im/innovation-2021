import { App } from "vue";
import { addRoute } from "../core";
import { hasLogin, user } from "./state";
export { user, hasLogin } from "./state";
let installed = false;

export default {
  install(app: App) {
    if (installed) return;
    console.error(`user module install`);
    installed = true;
    addRoute({
      path: "/signin",
      component: () => import("./views/signin.vue"),
    });
    addRoute({
      path: "/signup",
      component: () => import("./views/Signup.vue"),
    });
    addRoute({
      path: "/mine",
      component: () => import("./views/mine.vue"),
    });
    app.provide("user", user);
  },
};

export const signinGuard = () => hasLogin.value ? undefined : { path: "/signin" }