type speedArr = [number, number, number?, number?, number?, number?];
// vx,vy,vt,vrx,vry,vrz
export interface StageItemConfig {
  id: string;
  key: string; // file key
  name: string; // show name
  src: string;
  flip?: [boolean,boolean];
  x?: number;
  y?: number;
  rotation?: number;
  angle?: number;
  size?: [number, number];
}

export interface StageItemDBoneConfig extends StageItemConfig {
  speed?: speedArr;
  srcPath?: string;
}

export interface StageItemPlayableConfig extends StageItemDBoneConfig {
  uid?: string;
  report?: boolean; // for player's own item
}

export interface CharactorConfig extends StageItemPlayableConfig {}

export interface CharactorInStageData extends StageItemDBoneConfig {
  uid: string;
}
