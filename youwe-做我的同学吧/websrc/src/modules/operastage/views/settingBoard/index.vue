<template>
  <Button
    size="mini"
    style="width: 2rem; height: 2rem"
    icon="el-icon-setting"
    @click="open"
    circle=""
  ></Button>
</template>

<script lang="ts">
import { computed, defineComponent, readonly, ref, defineEmits } from "vue";
import MediaVolSetting from "./MediaVolSetting.vue";
import FullscreenSetting from "./FullscreenSetting.vue";
import PopupPicker from "@/components/PopupPicker.vue";
import { roomData } from "../../services";
import Button from "@/components/Button.vue";
export default defineComponent({
  emits: ["action"],
  setup() {
    const show = ref(false);
    const playerList = computed(() => roomData.players);
    const idleUsers = computed(() =>
      roomData.users.filter((el: string) => !playerList.value.includes(el))
    );
    const isPlayer = readonly(
      computed(() => playerList.value.includes(roomData.uid))
    );
    const isAdmin = computed(() => roomData.uid === roomData.info?.creator.uid);
    const info = computed(() => roomData.info);
    return {
      isPlayer,
      isAdmin,
      playerList,
      show,
      close() {
        show.value = false;
      },
      open() {
        show.value = true;
      },
      idleUsers,
      info
    };
  },
  components: {
    MediaVolSetting,
    FullscreenSetting,
    PopupPicker,
    Button,
  },
  methods: {
    drop(e: MouseEvent | TouchEvent) {
      let el = e.target as HTMLElement;
      if (el.tagName.toUpperCase() === "SPAN") {
        el = el.parentElement!;
      }
      const { id } = el.dataset;
      if (!id) {
        return;
      }

      this.$emit("action", {
        name: "drop",
        data: id,
      });
    },
    dropMe() {
      this.$emit("action", {
        name: "drop",
        data: roomData.uid,
      });
    },
    pick(e: MouseEvent | TouchEvent) {
      let el = e.target as HTMLElement;
      if (el.tagName.toUpperCase() === "SPAN") {
        el = el.parentElement!;
      }
      const { id } = el.dataset;
      if (!id) {
        return;
      }
      this.$emit("action", {
        name: "pick",
        data: id,
      });
    },
  },
});
</script>

<style lang="scss" scoped>
.drawer-body {
  color: #332;
  padding: 0 1rem 1rem;
  width: 100%;
  max-width: 480px;
}
</style>
