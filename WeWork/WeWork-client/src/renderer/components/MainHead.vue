<template>
  <div class="header">
    <h3 class='h3'>MyWork</h3>
    <div class="rightBox">
      <img @click="closeWindow" class="icon rightIcon" style="width:30px;height:30px;float:right" src="../assets/close.svg" />
      <img @click="fullWindow" class="icon rightIcon" style="width:20px;height:30px;float:right;margin-right:0.5%" src="../assets/full.svg" />
      <img @click="hideWindow" class="icon rightIcon" style="width:20px;height:30px;float:right;margin-right:1%" src="../assets/min.svg" />
      
      
      <el-popover
         placement="bottom-end"
         :title="userType"
         width="200"
         trigger="hover">
      <img class="icon rightIcon" slot="reference" style="width:25px;height:25px;float:right;margin-top:0.2%;margin-right:2%" src="../assets/userHead.svg" />
      <img class="icon" style="width:50px;height:50px;float:left" src="../assets/userHead.svg" />
      <div><span style="margin-top:0.6%;margin-left:6%">{{user.length>=15?user.substr(0,15)+"...":user}}</span></div>
      <el-button @click="loginVisible=true" type="success" size="mini" style="margin-left:6%" v-if="userType=='本地用户'">登录</el-button>
      <el-button @click="cancleLogin" type="warning" size="mini" style="margin-left:6%" v-if="userType=='云端用户'">退出</el-button>
      </el-popover>

   <span style="color:white;float:right;margin-top:0.6%;margin-right:1%">{{user}}</span>

   <div v-if="$store.state.syncing">
   <img class="icon" style="width:25px;height:25px;float:right;margin-top:0.2%;margin-right:2%" src="../assets/loading.svg" />
   <span style="color:white;font-size:10px;float:right;margin-top:1%;margin-right:0.5%">{{'同步中...'}}</span>
   </div>

 <el-dialog title="用户登录" :visible.sync="loginVisible" >
 <el-form :model="userForm" label-width="auto">
 <el-form-item label="登录邮箱">
 <el-input v-model="userForm.email" placeholder="请输入登录邮箱"></el-input>
 </el-form-item>
 <el-form-item label="登录密码">
 <el-input v-model="userForm.password" placeholder="请输入登录密码" show-password></el-input>
 </el-form-item>
 </el-form>
 <el-button @click="login" type="success" :loading="logining">登录</el-button>
 <el-button @click="registerVisible=true" type="warning" :loading="logining">注册</el-button>
 </el-dialog>


 <el-dialog title="用户注册" :visible.sync="registerVisible" >
 <el-form :model="userRigister" label-width="auto">
 <el-form-item label="注册邮箱">
 <el-input v-model="userRigister.email" placeholder="请输入登录邮箱"></el-input>
 </el-form-item>

 <el-form-item label="手机号码">
 <el-input v-model="userRigister.phone" placeholder="请输入手机号码"></el-input>
 </el-form-item>

 
 <el-form-item label="姓名">
 <el-input v-model="userRigister.user_name" placeholder="请输入用户姓名"></el-input>
 </el-form-item>

 <el-form-item label="输入密码">
 <el-input v-model="userRigister.pwd" placeholder="请输入登录密码" show-password></el-input>
 </el-form-item>

 <el-form-item label="重复密码">
 <el-input v-model="userRigister.pwd_repeat" placeholder="请重复登录密码" show-password></el-input>
 </el-form-item>

 </el-form>
 <el-button @click="register" type="warning" :loading="logining">确认注册</el-button>
 </el-dialog>
     

    </div>
  </div>
</template>

<script>
import { remote } from 'electron'
var database =require('nedb')
var db=new database({
        filename:'./cloud.db',
        autoload:true
    })



        
export default {
   inject:['reload'],
   data () {
     return {
        isMaximized:false,
        user:'HelloWorld',
        userType:'本地用户',

        loginVisible:false,
        logining:false,

        registerVisible:false,
        userRigister:{
          email:'',
          phone:'',
          pwd:'',
          pwd_repeat:'',
          user_name:''
        },

        userForm:{
          email:'',
          password:''
        },

        noticePromise:Promise.resolve()
     }
   },
  name: 'mainHead',
  created(){ 
    let that=this

    //网络状态发生改变才会触发，打开页面不会立刻触发
    window.addEventListener('online',function(){
      that.$store.state.online=true
      that.$message({
        message:'已连接互联网',
        type:'success'
      })
    })
    window.addEventListener('offline',function(){
      that.$store.state.online=false
      that.$message({
        message:'未连接互联网',
        type:'error'
      })
    })

    if(navigator.onLine==true){
      that.$store.state.online=true
    }


  },
  methods: {

    //检查注册和登录时的邮箱格式
    emailFormatMatch(str){
      if(!str.match(/^[0-9,A-Z,a-z]+@[0-9,A-Z,a-z]+\.com|cn|net $/g)){
        return false
      }else{
        return true
      }
    },

    check(){
      alert(this.$store.state.dbName)
    },
    closeWindow () {
      remote.app.quit()
    },

    //退出登录
    cancleLogin(){
      db=new database({
        filename:'./cloud.db',
        autoload:true
      })
      let that=this
      db.remove({}, { multi: true }, function (err, numRemoved) {
      that.userType="本地用户"
      that.user='HelloWorld'
      that.$store.state.dbName='./local.db'
      that.$store.state.login=false
      that.$store.state.user='HelloWorld'
      that.$store.state.password=''
      that.$store.state.phone=''
      that.$store.state.user_name=''
      that.$store.state.user_id=-1
      that.reload()
       });
    },

    //进行登录
    async login(){
      if(!this.$store.state.online){
        this.$message({
          message:'未连接互联网，无法登录',
          type:'error'
        })
        return 
      }
      let that=this
      if(!this.emailFormatMatch(that.userForm.email)){
        this.$message({
          message:'请输入正确邮箱格式',
          type:'error'
        })
        return
      }

      db=new database({
        filename:'./cloud.db',
        autoload:true
      })
      this.logining=true
      let params={
        email:that.userForm.email,
        password:that.userForm.password
      }
      this.$http.defaults.adapter = require('axios/lib/adapters/http')
      await this.$http.post(that.$store.state.contextUrl+'/login',that.$qs.stringify(params)).then(function(res){
        if(res.data!="fail"){
          that.$message({
            message:'登录成功',
            type:'success'
          })
          that.userType="云端用户"
          that.user=that.userForm.email
          that.loginVisible=false
          that.$store.state.dbName='./cloud.db'

          that.$store.state.user=that.userForm.email
          that.$store.state.password=that.userForm.password
          that.$store.state.phone=res.data.phone
          that.$store.state.user_name=res.data.user_name
          that.$store.state.user_id=res.data.id

          that.$store.state.login=true
        }else{
           that.$message({
            message:'登录失败',
            type:'error'
          })
          that.logining=false
        }
      })

      if(this.$store.state.login){
        this.$http.post(that.$store.state.contextUrl+'/downloadPlan',that.$qs.stringify({
          user_id:that.$store.state.user_id
        })).then(function(res){
          db.remove({}, { multi: true }, function (err, numRemoved) {
          let cloudPlanArray=res.data
          let planArr=[]
          let planLog=[]
          for(let i=0;i<cloudPlanArray.length;i++){
             planArr.push(JSON.parse(cloudPlanArray[i].plan_text))
             planLog.push(JSON.parse(cloudPlanArray[i].planLog_text)) 
          }
          db.insert(planArr)
          db.insert(planLog)
          
          that.$http.post(that.$store.state.contextUrl+'/downloadGoal',that.$qs.stringify({
            user_id:that.$store.state.user_id
          })).then(function(res){
            let cloudGoalArr=res.data
            let goals=[]
            cloudGoalArr.forEach(function(item,index){
              goals.push(JSON.parse(item.goal))
            })
            db.insert(goals)
            
            that.$http.post(that.$store.state.contextUrl+'/downloadCowork',that.$qs.stringify({
              user_id:that.$store.state.user_id
              })).then(function(res){
                let cowork=[]
                res.data.forEach(function(item,index){
                  item.content=JSON.parse(item.content)
                  item.content.id=item.id
                  cowork.push(item.content)
                })
                db.insert(cowork)
                that.logining=false
                that.reload()
            })
          })

          })
        })
      }
    },

    //注册用户
    register(){
      if(this.userRigister.email.match(/^\s*$/)||
      this.userRigister.phone.match(/^\s*$/)||
      this.userRigister.user_name.match(/^\s*$/)){
        this.$message({
          message:'注册信息不能为空',
          type:'error'
        })
        return 
      }
      if(this.userRigister.pwd!=this.userRigister.pwd_repeat){
        this.$message({
          message:'两次密码输入不一致',
          type:'error'
        })
        return
      }
      let that=this
      this.$http.post(that.$store.state.contextUrl+'/register',that.$qs.stringify({
        email:that.userRigister.email,
        phone:that.userRigister.phone,
        user_name:that.userRigister.user_name,
        password:that.userRigister.pwd
      })).then(function(res){
        if(res.data=='success'){
          that.$message({
            message:'注册成功',
            type:'success'
          })
          that.registerVisible=false
          that.userRigister={
            email:'',
            pwd:'',
            pwd_repeat:''
          }
          that.reload()
        }else if(res.data=='exists'){
           that.$message({
            message:'账户已存在，请更改账户邮箱',
            type:'error'
          })
        }else{
          that.$message({
            message:'注册失败',
            type:'error'
          })
        }
      })
    },


    fullWindow () {
      let win = remote.getCurrentWindow()
       if(this.isMaximized){ 
          this.isMaximized=false
          win.unmaximize()
        }else{ 
          this.isMaximized=true
          win.maximize(); 
      } 
    },


    hideWindow () {
      let win = remote.getCurrentWindow()
      if (win.isMinimized()) {
        win.restore()
      } else {
        win.minimize()
      }
    },
    gotoHome () {
      this.$store.dispatch('changeTransition', 'flip')
      this.$router.push('/login')
    },


    //websocket function
    onmessage(event){
      if(event.data==='refresh'){
        //refresh the messageList
        console.log('accepted')
        this.getMessage()
      }else if(event.data.match(/^IM\&/g)){
        let content=event.data.substr(3,event.data.length-3)
        let arr=content.split(/\&/g)
        let map=this.$store.state.IM

        let key=arr[0]+"&"+arr[1]
        if(!map.has(key)){
          map.set(key,[])
        }

        let date=new Date()
        let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                      date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()

        //arr[0]:plan_name ; arr[1]:host_id ; arr[2]:sender_id; arr[3]:msg content 

        let position='left'
        if(arr[2]==this.$store.state.user){
          position='right'
        }
        
        // console.log(JSON.stringify(arr))
        map.get(key).push({
          host:arr[1],
          sender:arr[2],
          msg:arr[3],
          time:timeStr,
          position:position
        })
        

        // console.log(JSON.stringify(map.get(key)))
       
      }
    },

    send(){
      if(this.$store.state.socket.readyState==1){
          this.$store.state.socket.send("test")
        }
    },

    onclose(){
       this.$store.state.socket.close()
    },
    
    //获取消息通知
    getMessage(){
       let that=this
       this.$http.post(this.$store.state.contextUrl+'/downloadNotice',this.$qs.stringify({
         user_id:that.$store.state.user_id
       })).then(function(res){
         res.data.forEach(function(item,index){
           let content=JSON.parse(item.content)
           item.content=content
         })
         that.$store.state.message=JSON.parse(JSON.stringify(res.data))
        //  console.log(JSON.stringify(that.$store.state.message))
       })
    }

  },
  watch:{
    "$store.state.online":{
      handler:function(val){
        if(!val&&this.$store.state.login){
          this.cancleLogin()
        }
      }
    },
    "$store.state.login":{
      handler:function(val){
        if(val){
          this.$store.state.socket=new WebSocket(this.$store.state.WebSocketUrl+"?uid="+this.$store.state.user_id)
          this.$store.state.socket.onmessage=this.onmessage
          this.$store.state.socket.onclose=this.onclose
          console.log(this.$store.state.socket)
          this.getMessage()
        }else{
          this.$store.state.message=[]
          this.$store.state.socket.close()
        }
      }
    },
    "$store.state.message":{
      handler:function(message){
        let that=this
        message.forEach(function(item,index){
          that.noticePromise=that.noticePromise.then(function(){
            if(item.read==0){
                           
            if(item.type==2){
                //任务清单信息
                that.$notify({
                  title:'任务清单',
                  message:'用户 '+item.sender_name+'('+item.sender_email+')'+' 给您发送了任务清单',
                  type:'info'
              })
            }else if(item.type==1){
                //任务状态请求转换信息
                that.$notify({
                  title:'任务进度更新请求',
                  message:'用户 '+item.sender_name+'('+item.sender_email+')'+' 向您请求更新任务进度',
                  type:'info'
              })
            }else if(item.type==3){
              if(item.content.type=='acceptRequestInfo'){
                that.$notify({
                    title:'任务进度更新请求',
                    message:'用户 '+item.sender_name+'('+item.sender_email+')'+
                    '接受了您的任务更新请求,您可将任务状态更新为：'+(item.content.status==0.5?'进行中':'已完成'),
                    type:'info'
                  })
                }
             }else if(item.type==4||item.type==6){
                 that.$notify({
                    title:'任务进度更新请求',
                    message:'用户 '+item.sender_name+'('+item.sender_email+')'+' 给您发送了一条消息',
                    type:'info'
                })
             }else if(item.type==5){
                   that.$notify({
                    title:'任务进度更新请求',
                    message:'用户 '+item.sender_name+'('+item.sender_email+')'+' 给您任务提醒，请尽快完成指定任务',
                    type:'info'
                })
             }
           }
        })
      })
    },
      deep:true
    }
  }
}
</script>


<style scoped>
  .subHead{
    margin-top:10px
  }
  .header {
    height: 28px;
    padding: 2px;
    background-color:rgb(90,40,250);
    -webkit-app-region: drag
}
.rightIcon {
    -webkit-app-region: no-drag;
}

.icon:hover {
    color: #ff001a;
    cursor: pointer;
}


.rightBox{
  width: 100%;
}

.h3{
  float:left;
  margin-top:5px;
  margin-left:45%;
  color:azure
}
</style>
