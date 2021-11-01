import { computed, reactive, ref } from "vue";

const cards = [];

export const STAGE = {
  PENDING: 0, // no poemId
  WAITING: 1,
  PICKING: 2,
  PICKED: 3,
  DROPING: 4,
  DROPPED: 5,
  COUNTING: 6,
  COUNTED: 7,
};

export interface IWordGoing {
  id: string;
  txt: string;
  actived?: boolean;
}


export const execControl = reactive({
  cantExc: true,
  cantUse: true,
  cantPick: true,
});

export const picked = ref<any>(null);

export const pickedWords: any[] = reactive([])
export function updateExecControl(opts, hands) {
  execControl.cantUse = pickedWords.length !== 1 || hands.length < 7;
  execControl.cantExc =
    pickedWords.length !== 2 || pickedWords[0][0] === pickedWords[1][0];
  execControl.cantPick = !picked.value || hands.length > 11 || opts.length < 1;
}

export const onHandWordClick = (id: string, hands: any[], opts: any[]) => {
  if (!id) return;
  console.log(`click ${id}`)
  const card = hands.find((el) => el.id === id);
  if (!card) return;
  const oi = pickedWords.findIndex(el => el === card.r)
  if (oi < 0) {
    pickedWords.push(card.r)
  } else {
    pickedWords.splice(oi, 1)
  }
  updateExecControl(opts, hands);
};

export function pickOpt(opts, card: any, hands: any[]) {
  if (picked.value?.id === card.id)
    picked.value = null
  else
    picked.value = card
  updateExecControl(opts, hands)
}
export function execCmd(cmd: string, session) {
  switch (cmd) {
    case "push":
      break;
    case "pull":
      break;
    case "exch":
      break;

    default:
      break;
  }
}
