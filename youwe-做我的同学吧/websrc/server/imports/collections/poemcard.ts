
export const PoemCardRecords = new Mongo.Collection<PoemCardRecord>("poemcard-records");
export const Poems = new Mongo.Collection<Poem>("poems");


export interface PoemCardRecord {
    _id: any;
    poemId: string;
    poem: Poem;
    needActions: string[];
    session: string;
    allWords: string[];
    useWords: string[];
    optWords: string[];
    stage: number;
    useScore: number;
    userMap: {
        [key: string]: {
            words: string[],
            score: number
        }
    }
}

export interface Poem {
    _id: string;
    id: string;
    name?: string;
    poet?: string;
    lines: string[],
    createdAt: number;
    updatedAt: number;
    userId:string
}


export interface IWord {
    _id: string;
    code: number;
    text: string;
    skin?: string;
}


