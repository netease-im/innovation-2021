export interface IUserProfile {
  _id: string;
  name: string;
  avatar: string;
}
export interface IMineProfile {
  username: string;
  profile?: {
    name: string;
    avatar?: string;
  };
}
