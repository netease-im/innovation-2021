import { IShip, ShipRecord } from "server/imports/collections";

export * from "../shared/meteor";


export const MineShips = new Mongo.Collection<IShip>("ships_mine")
export const ShipsWaiting = new Mongo.Collection<ShipRecord>("ships_waiting")