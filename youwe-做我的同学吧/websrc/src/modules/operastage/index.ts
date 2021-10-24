import { App, defineAsyncComponent } from "vue";
import { addRoute, registEntryType, subscribe } from "../core";
import { signinGuard } from "../user";
import { Mcore } from "./deps";
let installed = false;
export default {
    install(app: App) {
        if (installed) return;
        console.error(`operashow module install`);
        installed = true;
        addRoute({
            path: "/opera/piyin/i/:id",
            component: () => import("./views/ces.vue"),
            beforeEnter: signinGuard
        });
        addRoute({
            path: "/opera/role/add",
            component: () => import("./views/NewRole.vue"),
            beforeEnter: signinGuard
        });
        addRoute({
            path: "/opera/script/add",
            component: () => import("./views/NewScript.vue"),
            beforeEnter: signinGuard
        });
        subscribe("opera-storys")
        subscribe("opera-charactors")
        // addRoute({
        //   path: "/explore/create-plan",
        //   component: () => import("./views/CreatePlan.vue"),
        // });
        // addRoute({
        //   path: "/explore/active-plan/:id",
        //   component: () => import("./views/ActivePlan.vue"),
        // });
        registEntryType(
            ["opera", "piying"].join("/"),
            "故事演绎",
            defineAsyncComponent(() => import("./views/RoomEntry.vue"))
        );
    },
};

// export const ExplorePlanEntry = defineAsyncComponent(() =>
//     import("./views/ExplorePlanEntry.vue")
// );
