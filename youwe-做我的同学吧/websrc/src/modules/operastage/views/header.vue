<template>
  <div class="frow header">
    <div @click="showInfo">👨‍👨‍👦‍👦： {{ rtcData.users.length }}</div>
    <teleport to="body">
      <Popup ref="pop" title="房间信息">
        <div>
          <h3>
            {{ info.name }}
          </h3>
          <hr />
          <h4>
            {{ info?.desc }}
          </h4>
          <table>
            <thead>
              <tr>
                <td>ID</td>
                <td>创建者</td>
                <td>最大人数</td>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>
                  {{ info?.id }}
                </td>
                <td>{{ info?.creator?.nickname }}</td>
                <td>{{ info?.maxMember }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </Popup>
    </teleport>
    <div class="frow">
      <slot></slot>
      <FruiButton
        size="mini"
        @click="leave"
        style="width: 2rem; height: 2rem; margin-left: 1rem"
        icon="el-icon-d-arrow-left"
        circle=""
      ></FruiButton>
    </div>
  </div>
</template>

<script lang="ts">
// import { rtc } from "@/services/net/rtc";
import { computed, defineComponent } from "vue";
import { roomData, roomService } from "../services";
import Popup from "@/components/Popup.vue";
import FruiButton from "@/components/Button.vue";
export default defineComponent({
  components: {
    Popup,
    FruiButton,
  },
  setup() {
    return {
      rtcData: computed(() => rtc.data),
      info: computed(() => roomData.info),
      leave() {
        roomService.left();
        router.push("/");
      },
    };
  },
  methods: {
    showInfo() {
      (this.$refs.pop as any).open();
    },
  },
});
</script>

<style lang="scss" scoped>
body .header {
  position: fixed;
  z-index: 200;
  background-color: rgba($color: #000000, $alpha: 0.3);
  width: 100vw;
  top: -3rem;
  transition: top 2s;
  padding: 0.25rem;
}
// body[layout="s"]
body.active .header {
  top: 0;
}
.header {
  justify-content: space-between;
  align-items: center;
}
</style>
