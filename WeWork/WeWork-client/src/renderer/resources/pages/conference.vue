<template>
  <div id="frame">
    <div class="brief_intro">
      <el-popover
        placement="top-start"
        title="计划会议室"
        width="200"
        trigger="hover"
        content="欢迎来到计划会议室"
      >
        <i class="el-icon-info" slot="reference"></i>
      </el-popover>
    </div>
    <div class="topLab">
      <el-tooltip class="item" effect="dark" content="参会时长" placement="top-start">
        <div class="timetotall">{{ timetotall }}</div>
      </el-tooltip>
    </div>

    <div v-if="ready" :class="mainBoxType">
      <div :class="Screen_Type" v-for="(item,i) in MmemberOnShow" ref="vedio" :data-uid="item.user_id"  :key="i">
        <div class="video_box" v-if="!item.vedio">
          <div class="touxiang">
            <el-avatar :size="100">{{ item.name }}</el-avatar>
          </div>
          <div class="state">
            <i class="el-icon-user-solid hoster" v-if="item.host">主持人</i>
            <i class="el-icon-success online" v-if="item.online">在线</i>
            <i class="el-icon-error offline" v-if="!item.online">离线</i>
          </div>
        </div>
      </div>
    </div>

    <div class="functionLab">
      <div class="function_ico1">
        <el-card shadow="hover" @click.native="setOrRelieveSilence">
          <el-tooltip class="item" effect="dark" content="音频" placement="top-start">
            <i class="el-icon-microphone"></i>
          </el-tooltip>
        </el-card>
      </div>
      <div class="function_ico2">
        <el-card shadow="hover" @click.native="openCamera(localUid)">
          <el-tooltip class="item" effect="dark" content="摄像头" placement="top-start">
            <i class="el-icon-video-camera"></i>
          </el-tooltip>
        </el-card>
      </div>
      <div class="function_ico3">
        <el-card shadow="hover">
          <el-tooltip class="item" effect="dark" content="成员管理" placement="top-start">
            <i class="el-icon-s-custom"></i>
          </el-tooltip>
        </el-card>
      </div>
      <div class="function_ico4">
        <el-card shadow="hover">
          <el-tooltip class="item" effect="dark" content="聊天" placement="top-start">
            <i class="el-icon-chat-dot-square"></i>
          </el-tooltip>
        </el-card>
      </div>
    </div>
    <div class="close_Confer">
      <el-button type="danger" plain>结束会议</el-button>
    </div>
    <div class="back" v-if="OverNine">
      <el-button
        type="info"
        icon="el-icon-back"
        :disabled="Backable"
        @click="Back"
      ></el-button>
    </div>
    <div class="forward" v-if="OverNine">
      <el-button
        type="info"
        icon="el-icon-right"
        :disabled="Forwardable"
        @click="Forward"
      ></el-button>
    </div>
  </div>
</template>

<script>
const remote = require("electron").remote;
const win = remote.getCurrentWindow();
const ipc=require('electron').ipcRenderer

import * as WebRTC2 from '../../../sdk/NIM_Web_WebRTC2_v4.0.1.js';
import config from '../../config';
import { getToken } from '../../common';


// alert(JSON.stringify(NERTC))

export default {
  data() {
    return {
      
      ready:false,

      //频道变量
      isSilence: true,
      isStop: true,
      client: null,
      localUid:-1,
      localStream: null,
      remoteStreams: [],
      channelName:'',
      max: 20,
      isPublish:false,

      //页面变量
      plan:null,
      timetotall: "00:00",
      StartTime: null,
      NowTime: null,
      // 判断是否需要翻页按钮
      OverNine: false,
      Backable: false,
      Forwardable: false,
      MemMap:new Map(),
      MemberInro: [
        // {
        //   id: 1,
        //   name: "刘嘉俊",
        //   state: true,
        //   manager: true,
        //   vedio:false
        //   // 是否为主持人
        // }
      ],
      MmemberOnShow: [],
      membernum: 11,
      // 当前展示的人的最后一个对应总展示数组的下标
      Onstart: 0,
      Screen_Type: "",
      flag: 0,
      WindowStyle: "MIN",
      mainBoxType: "mainBox",
    };
  },
  created(){
    let that=this
    ipc.on("plan",function(event,plan,id){
      that.plan=JSON.parse(JSON.stringify(plan))

      that.localUid=id
      that.channelName=that.plan.plan.name
      that.membernum=that.plan.plan.member.length

      that.MemberInro=JSON.parse(JSON.stringify(that.plan.plan.member))
      
      that.MemberInro.forEach(function(item,index){
        item.online=false
        item.vedio=false
        that.MemMap.set(item.user_id,item)
      })

      that.initPage()
      
      
      that.initChannel()
    })
  },
  destroyed() {
    if(this.localStream!=null){
      this.localStream.destroy()
    }
    WebRTC2.destroy(this.client)
  },
  methods: {

    userOnline(user_id){
      this.MemMap.get(user_id).online=true
    },
    
    userOffline(user_id){
      this.MemMap.get(user_id).online=false
    },

    userVideoOn(user_id){
      this.MemMap.get(user_id).vedio=true
    },

    userVideoOff(user_id){
      this.MemMap.get(user_id).vedio=false
    },

    checkVideo(){
      let that=this
      setTimeout(()=>{
        if(!that.localStream.hasVideo()){
          that.checkVideo()
        }else{
          that.displayLocalVideo()
        }
      },1000)
    },
    
    displayLocalVideo(){
      if(!this.isPublish){
        this.publish()
      }
      const div = [...this.$refs.vedio].find(
        (item) => Number(item.dataset.uid) === Number(this.localUid)
        );
      this.localStream.play(div);
      this.localStream.setLocalRenderMode({
        // 设置视频窗口大小
        width: div.clientWidth,
        height: div.clientHeight,
        cut: true, // 是否裁剪
      });
    },

     getDeviceId(){
       WebRTC2.getDevices().then(data => {
         const {audioIn, audioOut, video} = data
         audioIn.forEach(item=>{
           console.log('mic label: ', item.label, 'deviceId: ', item.deviceId)
        })
        
        video.forEach(item=>{
          console.log('video label: ', item.label, 'deviceId: ', item.deviceId)
        })
      })
     },

     initChannel(){
       
       window.self = this;
       let that=this
       
       this.client = WebRTC2.createClient({
         appkey: config.appkey,
         debug: true,
      })
      
      
      this.client.on('peer-online', (evt) => {
        console.warn(`${evt.uid} 加入房间`);
        that.message(`${evt.uid} 加入房间`,"success")
        that.userOnline(evt.uid)
      });
      
      this.client.on('peer-leave', (evt) => {
        console.warn(`${evt.uid} 离开房间`);
        that.message(`${evt.uid} 离开房间`,"success")
        this.remoteStreams = this.remoteStreams.filter(
          (item) => !!item.getId() && item.getId() !== evt.uid
        );
        that.userOffline(evt.uid)
      });
      
      
      this.client.on('stream-added', async (evt) => {
        const stream = evt.stream;
        const userId = stream.getId();
        if (this.remoteStreams.some(item => item.getId() === userId)) {
          console.warn('收到已订阅的远端发布，需要更新', stream);
          this.remoteStreams = this.remoteStreams.map(item => item.getId() === userId ? stream : item);
          await this.subscribe(stream);
          
        } else if (this.remoteStreams.length < this.max - 1) {
          console.warn('收到新的远端发布消息', stream)
          this.remoteStreams = this.remoteStreams.concat(stream)
          await this.subscribe(stream);
          
        } else {
          console.warn('房间人数已满')
        }
        
      });

      this.client.on('stream-removed', (evt) => {
        const stream = evt.stream
        const userId = stream.getId()
        stream.stop();
        this.remoteStreams = this.remoteStreams.map(item => item.getId() === userId ? stream : item)
        console.warn('远端流停止订阅，需要更新', userId, stream)
        that.userVideoOff(item.getId())
        });

      this.client.on('stream-subscribed', (evt) => {
        console.warn('收到了对端的流，准备播放');
        const remoteStream = evt.stream;
        //用于播放对方视频画面的div节点
        const div = [...this.$refs.vedio].find(
                    (item) => Number(item.dataset.uid) === Number(remoteStream.getId())
                );
        that.userVideoOn(item.getId())
        remoteStream
        .play(div)
        .then(() => {
          console.warn('播放视频');
          remoteStream.setRemoteRenderMode({
            // 设置视频窗口大小
            // width: 160,
            // height: 90,
            width: div.clientWidth,
            height: div.clientHeight,
            cut: false, // 是否裁剪
            });
          }).catch((err) => {
            console.warn('播放对方视频失败了: ', err);
          });
        });

        this.getToken().then(token => {
                this.joinChannel(token)
            }).catch(e => {
                this.message(e,"error")
                console.error(e)
            })
     },

     initPage(){
       
       //初始化页面
      
       this.StartTime = new Date().getTime();
       
       let that = this;
       
       setInterval(function () {
         that.NowTime = new Date().getTime();
         let x = parseInt((that.NowTime - that.StartTime) / 1000);
         let Second = x % 60;
         let Minute = parseInt(x / 60);
         let StringBuffer = "";
         
         if (Minute < 10) {
           StringBuffer = StringBuffer + "0" + Minute + ":";
           
          } else {
           StringBuffer = StringBuffer + Minute + ":";
          }
          
          if (Second < 10) {
            StringBuffer = StringBuffer + "0" + Second;
          } else {
            StringBuffer = StringBuffer + Second;
          }
          
          that.timetotall = StringBuffer;
          
          }, 1000);
          
          // 这里进行初始化页面分框的渲染
          let temp = this.membernum;
          
          switch (temp) {
            case 1:
              this.Screen_Type = "ScreenType1";
              break;
            case 2:
              this.Screen_Type = "ScreenType2";
              break;
            case 3:
              this.Screen_Type = "ScreenType4";
              break;
            case 4:
              this.Screen_Type = "ScreenType4";
              break;
            case 5:
              this.Screen_Type = "ScreenType6";
              break;
            case 6:
              this.Screen_Type = "ScreenType6";
              break;
            case 7:
              this.Screen_Type = "ScreenType9";
              break;
            case 8:
              this.Screen_Type = "ScreenType9";
              break;
            case 9:
              this.Screen_Type = "ScreenType9";
              break;
            default:
              this.tackleOver();
            }
            
            if (temp > 9) {
              this.OverNine = true;
            }
            
            if (temp <= 9) {
              for (let i = 0; i <= this.membernum - 1; i++) {
                this.MmemberOnShow.push(this.MemberInro[i]);
              }
              
              this.Onstart = this.membernum - 1;
            }
            
            // 监听最大化,调整图像大小
            win.on("maximize", function () {
            // this.WindowStyle = "MAX";
            
            that.mainBoxType = "MaxmainBox";
            switch (that.Screen_Type) {
              case "ScreenType1":
                that.Screen_Type = "ScreenType1x";
                break;
                
              case "ScreenType2":
                that.Screen_Type = "ScreenType2x";
                break;
                
              case "ScreenType4":
                that.Screen_Type = "ScreenType4x";
                break;
                
              case "ScreenType6":
                that.Screen_Type = "ScreenType6x";
                break;
                
              case "ScreenType9":
                that.Screen_Type = "ScreenType9x";
                break;
                
            }
          
          });
          
          win.on("unmaximize", function () {
            
            that.mainBoxType = "mainBox";
            
            switch (that.Screen_Type) {
              case "ScreenType1x":
                that.Screen_Type = "ScreenType1";
                break;
                
              case "ScreenType2x":
                that.Screen_Type = "ScreenType2";
                break;
                
              case "ScreenType4x":
                that.Screen_Type = "ScreenType4";
                break;
                
              case "ScreenType6x":
                that.Screen_Type = "ScreenType6";
                break;
                
              case "ScreenType9x":
                that.Screen_Type = "ScreenType9";
                break;
            }
          });
     },

     message(msg,type){
       this.$message({
         message:msg,
         type:type
       })
     },
    // 通讯部分函数
     getToken() {
       let that=this
       console.log(config.appkey)
       return getToken({
         uid: that.localUid,
         appkey: config.appkey,
         appSecret: config.appSecret,
         channelName: that.channelName
         
         }).then(token => {
           return token
          
          }, (e) => {
            throw e;
          });
        },

      joinChannel(token) {
        let that=this
        if (!this.client) {
          that.message('内部错误，请重新加入房间','error');
          return;
        }
          console.info('开始加入房间: ', this.channelName);

          this.client
                    .join({
                        channelName: that.channelName,
                        uid: that.localUid,
                        token,
                    })
                    .then((data) => {
                        console.info('加入房间成功，开始初始化本地音视频流');
                        that.ready=true
                        that.MemMap.get(that.localUid).online=true
                        this.initLocalStream();
                    })
                    .catch((error) => {
                        console.error('加入房间失败：', error)
                        that.message('加入房间失败', 'error')
                        // message(`${error}: 请检查appkey或者token是否正确`);
                        // this.returnJoin();
                    });
            },
        
        initLocalStream() {
                let that=this
                //初始化本地的Stream实例，用于管理本端的音视频流
                this.localStream = WebRTC2.createStream({
                    uid: this.localUid,
                    audio: false, //是否启动mic
                    video: false, //是否启动camera
                    screen: false, //是否启动屏幕共享
                });

                //设置本地视频质量
                this.localStream.setVideoProfile({
                    resolution: WebRTC2.VIDEO_QUALITY_720p, //设置视频分辨率
                    frameRate: WebRTC2.CHAT_VIDEO_FRAME_RATE_15, //设置视频帧率
                });

                //设置本地音频质量
                this.localStream.setAudioProfile('speech_low_quality');

                //启动媒体，打开实例对象中设置的媒体设备
                this.localStream
                    .init()
                    .then(() => {
                        // 发布
                        // this.publish();
                    })
                    .catch((err) => {
                        console.warn('音视频初始化失败: ', err);
                        that.message('音视频初始化失败','error');
                        this.localStream = null;
                    });
            },
            
          publish() {
                let that=this
                console.warn('开始发布视频流');
                //发布本地媒体给房间对端
                this.client
                    .publish(this.localStream)
                    .then(() => {
                        that.isPublish=true
                        console.warn('本地 publish 成功');
                    })
                    .catch((err) => {
                        console.error('本地 publish 失败: ', err);
                        that.message('本地 publish 失败','error');
                    });
            },
          
          subscribe(remoteStream) {
                let that=this
                remoteStream.setSubscribeConfig({
                    audio: true,
                    video: true,
                });
                this.client
                    .subscribe(remoteStream)
                    .then(() => {
                        console.warn('本地 subscribe 成功');
                    })
                    .catch((err) => {
                        console.warn('本地 subscribe 失败: ', err);
                        that.message('订阅对方的流失败','error');
                    });
            },
          
          setOrRelieveSilence() {
                let that=this
                const { isSilence } = this;
                this.isSilence = !isSilence;
                if (this.isSilence) {
                    console.warn('关闭mic');
                    this.localStream
                        .close({
                            type: 'audio',
                        })
                        .then(() => {
                            console.warn('关闭 mic sucess');
                            that.message('关闭mic','success');
                        })
                        .catch((err) => {
                            console.warn('关闭 mic 失败: ', err);
                            that.message('关闭 mic 失败','error');
                        });
                } else {
                    console.warn('打开mic');
                    console.warn('打开mic sucess');
                    that.message('打开mic','success');
                    if (!this.localStream) {
                        that.message('当前不能打开mic','warning');
                        return;
                    }
                    this.localStream
                        .open({
                            type: 'audio',
                        })
                        .then(() => {
                            console.warn('打开mic sucess');
                            that.message('打开mic','success');
                        })
                        .catch((err) => {
                            console.warn('打开mic失败: ', err);
                            that.message('打开mic失败','error');
                        });
                     if(!this.isPublish){
                       this.publish()
                     }
                }
            },
          
          stopOrOpenVideo(user_id) {
                let that=this
                const { isStop } = this;
                this.isStop = !isStop;
                if (this.isStop) {
                    this.MemMap.get(user_id).vedio=false
                    console.warn('关闭摄像头');
                    that.message("关闭摄像头","success")
                    this.localStream
                        .close({
                            type: 'video',
                        })
                        .then(() => {
                            console.warn('关闭摄像头 sucess');
                        })
                        .catch((err) => {
                            console.warn('关闭摄像头失败: ', err);
                            that.message('关闭摄像头失败');
                        });
                } else {
                    this.MemMap.get(user_id).vedio=true
                    console.warn('打开摄像头');
                    that.message("打开摄像头","success")
                    if (!this.localStream) {
                        that.message('当前不能打开camera');
                        return;
                    }

                    this.localStream
                        .open({
                            type: 'video'
                        }).then(()=>{})
                          .catch((err) => {
                            console.warn('打开摄像头失败: ', err);
                            that.message('打开摄像头失败','error');
                        })
                        
                       
                        this.checkVideo()
                        
                }
            },
          
          handleOver() {
                console.warn('离开房间');
                this.client.leave();
                this.returnJoin(1);
          },

          openCamera(user_id){
            this.stopOrOpenVideo(user_id)
          },

        
          
          // 页面函数
          // 处理超过9个人时的初始化主界面的问题
          
          tackleOver: function () {
            this.Screen_Type = "ScreenType9";
            for (let i = 0; i <= 8; i++) {
              this.MmemberOnShow.push(this.MemberInro[i]);
            }
            this.Onstart = 8;
          },
          
          Back: function () {
            
            if (this.flag == 1) {
              this.Onstart = this.Onstart - 9;
            } else {
              this.Onstart = this.Onstart - (this.Onstart % 9) - 1;
            }
            
            this.MmemberOnShow = [];
            
            for (let i = this.Onstart - 8; i <= this.Onstart; i++) {
              this.MmemberOnShow.push(this.MemberInro[i]);
            }
            
            this.Forwardable = false;
          },
          
          Forward: function () {
            
            if (this.membernum - this.Onstart - 1 >= 9) {
              this.MmemberOnShow = [];
              
              for (let i = this.Onstart + 1; i <= this.Onstart + 9; i++) {
                this.MmemberOnShow.push(this.MemberInro[i]);
              }
              
              this.Onstart = this.Onstart + 9;
              if (this.Onstart + 1 == this.membernum) {
                this.Forwardable = true;
              }
              
              this.flag = 1;
              
              } else {
                
                this.MmemberOnShow = [];
                
                for (let i = this.Onstart + 1; i <= this.membernum - 1; i++) {
                  this.MmemberOnShow.push(this.MemberInro[i]);
                }
                
                this.Onstart = this.membernum - 1;
                console.log(this.Onstart);
                this.Forwardable = true;
                this.flag = 0;
                
                }
                
                this.Backable = false;
                },
            },

  watch: {
    Onstart: function (val) {
      if (val < 9) {
        this.Backable = true;
      }
    },

  },
};
</script>

<style scoped>
#app {
  background-color: #fafafa;
}
#frame {
  /* display: flex; */
  flex-direction: column;
  overflow: hidden;
  background-color: #fafafa;
  width: 100%;
  display: flex;
  justify-content: space-between;
  height: 100%;
}
.brief_intro {
  position: absolute;
  left: 20px;
  top: 8px;
  font-size: medium;
}
.topLab {
  height: 30px;
  background-color: #f8f9fb;
  border-top: 1px solid #d8d8db;
  border-bottom: 1px solid #d8d8db;
  display: flex;
  justify-content: center;
  align-items: center;
}
.mainBox {
  height: 610px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}
.MaxmainBox {
  height: 786px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}
.functionLab {
  height: 60px;
  /* background-color: cyan; */
  display: flex;
  justify-content: center;
  align-items: center;
}
.timetotall {
  height: 20px;
  width: 50px;
  text-align: center;
  line-height: 20px;
  /* background-color: #f9fdff; */
  background-color: #d8d8db;
  cursor: pointer;
}
.function_ico1 {
  box-sizing: border-box;
  width: 60px;
  height: 60px;
  /* background-color: darkgoldenrod; */
}
.function_ico2 {
  box-sizing: border-box;
  width: 60px;
  height: 60px;
  /* padding: 10px;
  background-color: darkgoldenrod; */
}
.function_ico3 {
  box-sizing: border-box;
  width: 60px;
  height: 60px;
  /* padding: 10px;
  background-color: darkgoldenrod; */
}
.function_ico4 {
  box-sizing: border-box;
  width: 60px;
  height: 60px;
  /* padding: 10px;
  background-color: darkgoldenrod; */
}
.el-card {
  width: 60px;
  height: 60px;
  cursor: pointer;
}
.el-card:hover {
  background-color: #e0e2e5;
}
.el-card i {
  font-size: 22px;
}
.close_Confer {
  position: absolute;
  bottom: 5px;
  right: 20px;
}
.close_Confer .el-button {
  font-size: medium;
  font-weight: bold;
}
.el-avatar {
  font-size: 20px;
  cursor: pointer;
}
.state {
  display: flex;
  justify-content: center;
  align-items: center;
}
.state i {
  margin-left: 3px;
  font-size: medium;
  cursor: pointer;
}
.el-icon-user-solid.hoster {
  color: #ff801b;
}
.el-icon-success.online {
  color: #01cc66;
}
.el-icon-error.offline {
  color: #dd001b;
}
.touxiang {
  display: flex;
  justify-content: center;
  margin-bottom: 5px;
}
.ScreenType1 {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 1000px;
  height: 610px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType2 {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 500px;
  height: 610px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType4 {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 500px;
  height: 305px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType6 {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 333px;
  height: 305px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType9 {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 333px;
  height: 203px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType1x {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 1420px;
  height: 786px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType2x {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 710px;
  height: 786px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType4x {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 710px;
  height: 393px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType6x {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 473px;
  height: 393px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.ScreenType9x {
  box-sizing: border-box;
  border: 1px solid #e0e2e5;
  width: 32%;
  height: 32%;
  display: flex;
  justify-content: center;
  align-items: center;
}
.back {
  position: absolute;
  bottom: 5px;
  left: 20px;
}
.forward {
  position: absolute;
  bottom: 5px;
  left: 80px;
}
</style>
