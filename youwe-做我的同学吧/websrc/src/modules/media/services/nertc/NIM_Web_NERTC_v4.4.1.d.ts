export const createStream: (opts: IStreamOptions) => ILocalStream;

export const createClient: (opts: { appkey: string; debug?: any }) => Client;

interface ILocalStream {
  setVideoProfile: (
    resolution: any, //设置视频分辨率
    frameRate: any
  ) => void;
  adjustAudioMixingVolume;
  adjustResolution;
  cleanMediaRecording;
  close;
  destroy;
  downloadMediaRecording;
  getAudioLevel;
  getAudioMixingCurrentPosition;
  getAudioMixingDuration;
  getAudioStream;
  getAudioTrack;
  getEffectsVolume;
  getId;
  getVideoTrack;
  hasAudio;
  hasScreen;
  hasVideo;
  init;
  isPlaying;
  listMediaRecording;
  muteAudio;
  muteScreen;
  muteVideo;
  on;
  open;
  pauseAllEffects;
  pauseAudioMixing;
  pauseEffect;
  play;
  playEffect;
  playMediaRecording;
  preloadEffect;
  resumeAllEffects;
  resumeAudioMixing;
  resumeEffect;
  setAudioMixingPosition;
  setAudioOutput;
  setAudioProfile;
  setAudioVolume;
  setCanvasWatermarkConfigs;
  setCaptureVolume;
  setEffectsVolume;
  setLocalRenderMode;
  setRemoteRenderMode;
  setScreenProfile;
  setSubscribeConfig;
  setVideoProfile;
  setVolumeOfEffect;
  startAudioMixing;
  startMediaRecording;
  stop;
  stopAllEffects;
  stopAudioMixing;
  stopEffect;
  stopMediaRecording;
  switchDevice;
  takeSnapshot;
  unloadEffect;
  unmuteAudio;
  unmuteScreen;
  unmuteVideo;
}

export interface Client {
  addTasks: Function;
  deleteTasks: Function;
  destroy: Function;
  getChannelInfo: Function;
  getConnectionState: Function;
  getLocalAudioStats: Function;
  getLocalVideoStats: Function;
  getRemoteAudioStats: Function;
  getRemoteVideoStats: Function;
  getSessionStats: Function;
  getSystemStats: Function;
  getTransportStats: Function;
  getUid: Function;
  join: Function;
  leave: Function;
  on: Function;
  publish: Function;
  setChannelProfile: Function;
  setClientRole: Function;
  setEncryptionMode: Function;
  setEncryptionSecret: Function;
  setLocalMediaPriority: Function;
  setRemoteVideoStreamType: Function;
  subscribe: Function;
  unpublish: Function;
  unsubscribe: Function;
  updateTasks: Function;
}

interface JoinOptions {
  channelName: string;
  token: string;
  uid: string;
  joinChannelLiveConfig: {
    isHostSpeaker;
    recordAudio;
    recordType: 0 | 1 | 2;
    recordVideo;
  };
  joinChannelRecordConfig?: any;
  neRtcServerAddresses?: any;
}

interface StreamOptions {
  audio?: string;
  audioProcessing?: { AEC?: boolean; AGC?: boolean; ANS?: boolean };
  audioSource?: MediaStreamTrack;
  cameraId?: string;
  client?: Client;
  facingMode?: String;
  microphoneId?: microphoneId; //NERTC.getMicrophones
  screen?: boolean;
  screenAudio?: boolean;
  sourceId?: string;
  uid: number;
  video: boolean;
  videoSource?: MediaStreamTrack;
}
