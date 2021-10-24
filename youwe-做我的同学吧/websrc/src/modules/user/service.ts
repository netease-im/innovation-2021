import { handleResponse } from "../core/api";
import { Mcore } from "./deps";

export const signin = (data: {
  username: string;
  password: string;
}): Promise<[any, any?]> =>
  new Promise((resolve, reject) => {
    Mcore.loginWithPassword(data.username, data.password, (err) =>
      handleResponse(resolve, reject, [err])
    );
  });

export const signup = (data: {
  username: string;
  password: string;
}): Promise<[any, any?]> =>
  new Promise((resolve, reject) => {
    Accounts.createUser(
      {
        username: data.username,
        password: data.password,
        profile: { name: "用户" + "username" },
      },
      (err) => handleResponse(resolve, reject, [err])
    );
  });
