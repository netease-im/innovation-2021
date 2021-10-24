import { loadingController } from "@ionic/vue";
export const showLoading = async (message = "请稍等...", header = "不好") => {
  const loader = await loadingController.create({
    message,
    cssClass: "warning-pop"
  });
  await loader.present()
};
export const hideLoading = async (header = "警告") => {
  const loader = await loadingController.getTop()
  await loader?.dismiss()
};
