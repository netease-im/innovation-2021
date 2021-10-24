export const appConfig = Object.create(null);

export const isDev = process.env.NODE_ENV === "development";
export const mockData = isDev && true;

export const STAGE_WIDTH_PLAYER = 2000;
export const STAGE_WIDTH_WACTHER = 540;
export const STAGE_HEIGHT_PLAYER = 900;
export const STAGE_HEIGHT_WACTHER = 900;

export const STAGE_BG_IMAGE_BASE = "/static";


export const defLogLevel = isDev ? 1 : 99;
