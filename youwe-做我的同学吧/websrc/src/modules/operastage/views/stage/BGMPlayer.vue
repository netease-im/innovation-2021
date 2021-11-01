<template>
  <audio autoplay controls style="display: none" class="room-audio bgm" loop ref="item" :src="src"></audio>
</template>

<script lang="ts">
// @ is an alias to /src

import { defineComponent } from "vue";

export default defineComponent({
  name: "BGMPlayer",
  data() {
    return {
      src: ""
    }
  },
  methods: {
    setVal(v: number) {
      (this.$refs.item as HTMLAudioElement).volume = v;
    },
    play(src: string) {
      this.src = src.startsWith("/static") ? src : '/static' + src
      this.$nextTick(() => {
        this.refs.item.play()
      })
    }
  },
});
</script>
