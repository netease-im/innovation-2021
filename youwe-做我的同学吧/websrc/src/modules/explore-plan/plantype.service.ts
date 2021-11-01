import { reactive, ref } from "vue";

export const subList = reactive<any[]>([]);

export const mainList = [
  {
    key: "poem",
    name: "诗词",
  },
  {
    key: "opera",
    name: "杂剧演绎",
  },
];
const map = {
  poem: [
    {
      name: "字卡大战",
      key: "word-card",
    },
  ],
  opera: [
    {
      name: "剧本演绎",
      key: "piying",
    },
  ],
};

export const choice = reactive({
  main: "poem",
  sub: "",
});

export const pickMain = (item) => {
  choice.main = item;
  subList.splice(0, subList.length, ...map[item.key]);
  console.log(subList);
};
export const confirmPick = (sub) => {
  resolver?.call(null, { main: choice.main, sub });
  resetChoice();
  showModal.value = false;
};
const resetChoice = () => {
  choice.main = "poem";
  choice.sub = "";
};

export const showModal = ref(false);

let resolver;
export const startPickType = () =>
  new Promise<{ main: string; sub: string } | null>((resolve) => {
    showModal.value = true;
    resolver = resolve;
  });

export const cancelPick = () => {
  resolver?.call(null, null);
  showModal.value = false;
};
