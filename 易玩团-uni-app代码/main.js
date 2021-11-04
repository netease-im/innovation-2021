import App from './App'
import uView from "uview-ui";
import Vue from 'vue';
import request from './config/request.js';
import { getUserInfo } from 'utils/getUserInfo.js';
import { initYunXin } from 'utils/initYunXin.js';
import { issueFormat, timeFormat, mapIssueFormat } from 'utils/format.js'

// 全局引用uView
Vue.use(uView);

Vue.prototype.$issueFormat = issueFormat
Vue.prototype.$timeFormat = timeFormat
Vue.prototype.$mapIssueFormat = mapIssueFormat


//将封装请求js文件挂载在vue原型上
Vue.prototype.request = request
Vue.prototype.initYunXin = initYunXin
Vue.prototype.$getUserInfo = getUserInfo

Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()


