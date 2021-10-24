import { computed, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { IUserProfile, Mcore, MTracker } from "./deps";

export const user = reactive<IUserProfile>({} as any);
export const hasLogin = computed(()=>!!user._id)
MTracker.autorun(async () => {
  console.log("meteor autorun up--------");
  if (Mcore.userId()) {
    const u = Mcore.user();
    user._id = Mcore.userId()!;
    if (u) {
      user.avatar = u.profile?.avatar;
      user.name = u.profile?.name;
      console.log(`updated user profile`);
    }
  } else if (user._id) {
    location.reload();
  }
});
