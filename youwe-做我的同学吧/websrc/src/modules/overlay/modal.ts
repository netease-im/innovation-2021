import { IonButton, IonContent, IonPage, modalController } from "@ionic/vue";

export const showModal = (opts: { component: any }) =>
  new Promise(async (resolve) => {
    const modal = await modalController.create({
      component: opts.component,
    });
    modal.onDidDismiss().then((v) => {
      resolve(v);
    });
    modal.present();
  });
