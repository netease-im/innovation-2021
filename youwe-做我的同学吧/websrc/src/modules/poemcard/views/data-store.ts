import { reactive, computed } from "vue";

const STEP = {
  IDLE: 0,
  VOTE: 1,
  WAITING: 2,
  PICK: 3,
  PUSH: 4,
  PULL: 5,
  CACL: 6,
};
export const roomData = reactive({
  pick: [] as any[],
  used: [] as any[],
  hand: [
    { actived: false, id: "1" },
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {},
    {},
  ],
  push: [] as any[],
  step: STEP.PICK,
  actived: [] as any[],
});

export async function pushCard() {
  const card = roomData.actived.pop();
  if (!card) return;
  card.actived = false;
  const i = roomData.hand.indexOf(card);
  roomData.hand.splice(i, 1);
  roomData.push.push(card);
}

export function togglePick(card: any) {
  if (card.actived) {
    card.actived = false;
    const i = roomData.actived.indexOf(card);
    roomData.actived.splice(i, 1);
    return;
  }
  if (roomData.actived.length > 6) {
    const c = roomData.actived.pop();
    c && (c.actived = false);
  }
  card.actived = true;
  roomData.actived.unshift(card);
}

// subscribe(ServerAction.SYNC, () => {
//   switch (roomData.step) {
//     case STEP.VOTE:
//       // 开始 list初始化
//       break;
//     case STEP.PICK:
//       // 并且会送来pick list
//       break;
//     case STEP.PUSH:
//       // 同步pushed list
//       break;
//     case STEP.PULL:
//       // 同步push list
//       break;
//   }
// });
