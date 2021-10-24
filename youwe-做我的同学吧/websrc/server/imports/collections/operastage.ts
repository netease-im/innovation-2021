

export const OperaCharactors = new Mongo.Collection<OperaCharactor>("opera-charactors");

export const OperaStorys = new Mongo.Collection<OperaStory>("opera_storys")
export const OperaStageCharactorDatas = new Mongo.Collection<OperaStageCharactorData>("opera_stage_charator_data")

export interface OperaCharactor {
    _id: any;
    name: string;
    key: string;
    updatedAt: number;
    size: number[];
    x: number;
    y: number;
    userId: string;
    flip?: number[];
    createdAt: number;
}

export interface OperaStory {
    _id: any;
    name: string;
    desc: string;
    userId?: string;
    charactors: string[]
    items: string[]
    stageImages: string[]
    bgm: string[]
    createdAt: number;
    updatedAt: number;
}


export interface OperaStageCharactorData {
    _id: any;
    key: string; // file key
    src?: string;
    name: string; // show name
    modifiedAt: number;
    charId: string;
    size: number[];
    x: number;
    y: number;
    rotation?: number;
    userId: string;
    angle?: number;
    flip?: any[];
    speed?: number[];
    roomId: string;
    state: number;
    animation?: { name: string, time?: number }
}