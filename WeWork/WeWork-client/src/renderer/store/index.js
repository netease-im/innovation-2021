import Vue from 'vue'
import Vuex from 'vuex'

import { createPersistedState, createSharedMutations } from 'vuex-electron'

import modules from './modules'

let state={
  dbName:'./data/main/local.db',
  login:false,

  //-1:本地用户  >0:云端用户
  user_id:-1,


  user:'HelloWorld',
  phone:'',
  password:'',
  user_name:'',
  userCenterHeadIndex:0,
  contacts:[],

  contextUrl:'http://49.234.97.138',
  // contextUrl:'http://localhost:81',
  // WebSocketUrl:'ws://localhost:81',
  WebSocketUrl:'ws://49.234.97.138',

  syncing:false,

  //检测是否脱机状态
  online:false,


  socket:null,

  //消息通知
  message:[],

  //即时信息
  IM:new Map(),

  //保存已推送的信息
  acceptedIM:new Set()

}

Vue.use(Vuex)

export default new Vuex.Store({
  modules,
  state,
  plugins: [
    // createPersistedState()
  //   // createSharedMutations()
   ],
  strict: false
})
