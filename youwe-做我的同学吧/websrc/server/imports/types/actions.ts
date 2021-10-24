export enum ClientAction {
  JOIN = 1000,
  LEFT,
  BGIN,
  DROP,
  PICK,
  REPT,
}
export enum ServerAction {
  STEP,
  JOIN,
  LEFT,
  DROP,
  PICK,
  SYNC,
  MESG,
}
