export const Users = Meteor.users;


export const ExplorePlans = new Mongo.Collection<{
  _id:string;
  name: string;
  startAt: number;
  closeAt: number;
  types: string[];
  userId: string;
  createdAt: number;
}>("explore_plans");

export const Ship = new Mongo.Collection<IShip>("ships");

export const ShipRecords = new Mongo.Collection<ShipRecord>("ship-records");

export interface IShip {
  _id: string;
  name: string;
  desc: string;
  activedAt: number;
  planId: string;
  leaderId: string;
  members: string[];
}

export interface ShipRecord {
  _id: string;
  name: string;
  desc: string;
  startAt: number;
  state: number;
  closeAt?: number;
  shipId: string;
  planId: string;
  leaderId: string;
  members: string[];
  type: string;
  data?:any
}