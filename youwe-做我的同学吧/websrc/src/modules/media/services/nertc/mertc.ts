import { reactive } from "vue";
import * as NERTC from "./NIM_Web_NERTC_v4.4.1";
import { AndroidPermissions } from "@ionic-native/android-permissions";
import { showWarn } from "@/modules/overlay/alert";

let client: NERTC.Client = null as any;
let appkey = "";
export const init = (key?: string) => {
  if ((key && key === appkey) || !key) return client;
  return (client = NERTC.createClient({ appkey: key }));
};

let localStream: any = null;
export const getLocalStream = async (uid: string, type: string) => {
  // const client = init();
  const stream = NERTC.createStream({
    uid,
    audio: true, //是否启动mic
    video: false, //是否启动camera
    screen: false, //是否启动屏幕共享
  });
  //设置本地音频质量
  stream.setAudioProfile("speech_low_quality");
  //启动媒体，打开实例对象中设置的媒体设备
  try {
    await stream.init();
    localStream = stream;
  } catch (err) {
    console.warn("音频初始化失败: ", err);
    localStream = null;
  }
};

export const join = async (
  appkey: string,
  channelName: string,
  uid: any,
  token = null,
  streams: any[]
) => {
  console.log(appkey);
  const client = init(appkey);
  console.log(client);
  client.setChannelProfile({
    mode: "rtc",
  });
  client.on("peer-online", (evt) => {
    console.warn(`${evt.uid} 加入房间`);
  });
  client.on("peer-leave", (evt) => {
    console.warn(`${evt.uid} 离开房间`);
    const oldIndex = streams.findIndex((item) => item.getId() === evt.uid);
    if (oldIndex > -1) {
      streams.splice(oldIndex, 1);
    }
  });

  client.on("stream-added", async (evt) => {
    const stream = evt.stream;
    const userId = stream.getId();
    const oldIndex = streams.findIndex((item) => item.getId() === userId);
    if (oldIndex > -1) {
      console.warn("收到已订阅的远端发布，需要更新", stream);
      streams.splice(oldIndex, 1, stream);
      await subscribe(stream);
    } else {
      console.warn("收到新的远端发布消息", stream);
      streams.push(stream);
      await subscribe(stream);
    }
  });
  client.on("stream-removed", (evt) => {
    const stream = evt.stream;
    const userId = stream.getId();
    stream.stop();
    const oldIndex = streams.findIndex((item) => item.getId() === userId);
    if (oldIndex > -1) {
      console.warn("收到已订阅的远端发布，需要更新", stream);
      streams.splice(oldIndex, 1);
    }
    client.unpublish(stream);
    console.warn("远端流停止订阅，需要更新", userId, stream);
  });
  client.on("stream-subscribed", (evt) => {
    console.warn("收到了对端的流，准备播放");
    const remoteStream = evt.stream;
    remoteStream
      .play()
      .then(() => {
        console.warn("播放音频");
      })
      .catch((err) => {
        console.warn("播放对方音频失败了: ", err);
      });
  });
  try {
    await client.join({
      channelName,
      uid,
      token,
    });
  } catch (error) { }
};

export async function publish(uid: string) {
  const client = await init();
  console.log(client)
  await getLocalStream(uid, "");
  await client.publish(localStream);
  return localStream;
}

export async function subscribe(stream: any) {
  let cantCheck = false
  let hasRight = false
  try {
    const res = await AndroidPermissions.checkPermission(AndroidPermissions.PERMISSION.RECORD_AUDIO)
    hasRight = res.hasPermission
  } catch (error) {
    cantCheck = true
  }
  if (!cantCheck && !hasRight) {
    try {
      const res = await AndroidPermissions.requestPermission(AndroidPermissions.PERMISSION.RECORD_AUDIO)
      hasRight = res.hasPermission
    } catch (error) {

    }
    if (!hasRight) {
      return showWarn("无法获取麦克风")
    }
  }
  const client = await init();
  stream.setSubscribeConfig({
    audio: true,
    video: false,
  });
  await client.subscribe(stream);
}
