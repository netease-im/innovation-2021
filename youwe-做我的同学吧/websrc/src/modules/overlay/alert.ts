import { alertController } from "@ionic/vue";
export const showWarn = (message = "似乎不太对", header = "不好") =>
  new Promise(async (resolve) => {
    const buttons = ["确认"];
    const alert = await alertController.create({
      header,
      message,
      cssClass: "warning-pop",
      buttons,
    });
    await alert.present();
  });
export const showConfirm = (message = "似乎不太对", header = "警告") =>
  new Promise(async (resolve) => {
    const alert = await alertController.create({
      header,
      message,
      cssClass: "confirm-pop",
      buttons: [
        {
          text: "取消",
          handler() {
            resolve(false);
          },
        },
        {
          text: "确认",
          handler() {
            resolve(true);
          },
        },
      ],
    });
    await alert.present();
  });
