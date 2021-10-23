import Vue from 'vue'
import axios from 'axios'
import App from './App'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import qs from 'qs'
import 'element-ui/lib/theme-chalk/index.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

if (!process.env.IS_WEB) Vue.use(require('vue-electron'))
axios.defaults.adapter = require('axios/lib/adapters/http.js')
// axios.defaults.adapter = require('axios/lib/adapters/xhr');
Vue.http = Vue.prototype.$http = axios
Vue.config.productionTip = false
Vue.prototype.$qs=qs
Vue.use(ElementUI)
Vue.use(mavonEditor)
/* eslint-disable no-new */
new Vue({
  components: { App },
  router,
  store,
  template: '<App/>'
}).$mount('#app')
