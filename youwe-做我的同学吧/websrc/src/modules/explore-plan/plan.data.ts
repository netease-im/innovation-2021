import { onUnmounted, reactive } from "vue";
import { makeCollectionReactive } from "../core/mapCollection";
import { MineShips, } from "../user/deps";
import { ExplorePlans, fmtDate, timestamp } from "./deps";

const now = timestamp();


export const futureList = reactive<any[]>([]);
export const historyList = reactive<any[]>([]);
const transform = (doc) => {
  doc.test = 1;
  return {
    ...doc,
    startDate: fmtDate(doc.startAt),
    closeDate: fmtDate(doc.closeAt),
  };
};

export function getGoingList() {
  const now = timestamp();
  const goingList = reactive<any[]>([]);
  const handler = makeCollectionReactive(
    goingList,
    ExplorePlans,
    {
      startAt: {
        $lt: now,
      },
      closeAt: {
        $gt: now,
      },
    },
    transform
  );
  onUnmounted(() => handler.stop())
  return goingList
}

makeCollectionReactive(
  futureList,
  ExplorePlans,
  {
    startAt: {
      $gt: now,
    },
  },
  transform
);

makeCollectionReactive(
  historyList,
  ExplorePlans,
  {
    closeAt: {
      $lte: now,
    },
  },
  transform
);


export const mineShips: any[] = reactive([])

makeCollectionReactive(mineShips, MineShips, {})
