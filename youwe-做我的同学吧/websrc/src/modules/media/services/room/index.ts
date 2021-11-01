import { reactive, ref, watch } from "vue";
import { init, join, publish, subscribe } from "../nertc/mertc";
export const joinRoom = ({ appkey, channelName, uid, token }) => {
  roomId.value = channelName;
  roomState.uid = uid;
  roomState.token = token;
  roomState.appKey = appkey;
};
export const initClient = (appkey: string) => {
  init(appkey);
};
export const leftRoom = () => {
  roomId.value = "";
  // client.destroy()
};
const remoteStreams = reactive([]);
const roomId = ref("");
export const roomState = {
  appKey: null,
  localStream: null,
  uid: null,
  token: null,
};

watch(roomId, (nv, ov) => {
  if (nv === ov) {
    return;
  }
  const { uid, appKey } = roomState;
  if (!uid || !appKey) return;
  if (!nv) {
    const client = init();
    client.leave();
    return;
  }
  console.log(roomState)
  join(appKey, nv, uid, null, remoteStreams).then(() =>
    publish(uid).then((v) => (roomState.localStream = v,console.log(v)))
  );
});
