<template>
    <div class="main" v-if="cowork!=null">
    <el-card >
    <el-page-header @back="goBack" content="计划详情"></el-page-header>
    </el-card>
    <el-card>
    <el-descriptions title="计划信息">
    <el-descriptions-item label="计划名称">{{cowork.plan.plan.name}}</el-descriptions-item>
    <el-descriptions-item label="计划性质">{{cowork.plan.plan.type}}</el-descriptions-item>
    
    <el-descriptions-item :label="cowork.plan.plan.type=='个人计划'?'执行人':'负责人'">
    <el-tag size="mini">{{cowork.plan.plan.manager.name}}</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions>
    <el-descriptions-item label="计划时间">{{cowork.plan.plan.startDate+" "+
    cowork.plan.plan.startTime+" 至 "+cowork.plan.plan.endDate
    +" "+cowork.plan.plan.endTime}}
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions :column="1" v-if="cowork.plan.plan.type=='小组计划'">
    <el-descriptions-item label="小组成员">
      <div v-if="cowork.plan.plan.type=='小组计划'&&cowork.plan.plan.member.length>0">
      <el-tag class="hover_noColor" style="margin-right:5px" 
              size="mini" :type="item.host?''
              :(me.email==item.email?'warning':'success')" 
              v-for="(item,i) in cowork.plan.plan.member" :key="i"
              @click.native="showMemInfo(item)">
      {{item.name}}
      </el-tag>
      </div>

      <div v-if="cowork.plan.plan.type=='小组计划'&&cowork.plan.plan.member.length==0">
      <span>无</span>
      </div>      
      
    </el-descriptions-item>

    <el-descriptions-item label="我" v-if="me!=null">
      <el-tag type="warning" size="mini">{{me.name}}</el-tag>      
      
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions>
    <el-descriptions-item label="计划描述">{{cowork.plan.plan.desc}}</el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-collapse  v-model="activeName" accordion v-for="(item,i) in proList" :key="i">
    <el-collapse-item :title="'我的任务'+(i+1)+'：'+item.pro_name">
    
    <div>任务时间：{{item.startDate+" "+
    item.startTime+" 至 "+item.endDate+" "+item.endTime}}
    </div>
    
    <div>任务参与人：
    <span v-if="!item.hasOwnProperty('proMem')||item.proMem.length==0">未指定</span>
    <el-tag style="margin-right:5px" size="mini" 
            :type="mem.host?'':(me.email==mem.email?'warning':'success')" 
            v-for="(mem,i) in item.proMem" :key="i">
    {{mem.name}}
    </el-tag>
    </div>
    <div>任务描述：{{item.pro_text}}</div>
    <div>我的任务状态：</div>
    <el-progress style="margin-right:35%" :format="format" :percentage="getProPercentage(proMeMap.get(item.id).status)"></el-progress>
    <br>

    <el-button v-if="proMeMap.get(item.id).status==0" icon="el-icon-message" 
    size="medium"  type="warning" style="margin-top:10px"
    @click="sendInfoFor1(item)">
    进行任务
    </el-button>

    <el-tag v-if="proMeMap.get(item.id).status==0.5" size="medium"  type="warning" style="margin-top:10px" disabled>
    等待计划负责人确认任务状态修改——进行任务
    </el-tag>

    <el-button v-if="proMeMap.get(item.id).status==1" icon="el-icon-message" 
    size="medium"  type="success" style="margin-top:10px"
    @click="sendInfoFor2(item)">
    完成任务
    </el-button>

    <el-tag v-if="proMeMap.get(item.id).status==1.5" size="medium"  type="warning" style="margin-top:10px" disabled>
    等待计划负责人确认任务状态修改——完成任务
    </el-tag>

    <el-tag v-if="proMeMap.get(item.id).status==2" size="medium"  type="primary" style="margin-top:10px" disabled>
    任务已完成
    </el-tag>

   

    </el-collapse-item>
    </el-collapse>
     
    <el-card @click.native="openChatRoom" class="hover">
     <i class="el-icon-s-comment"></i>
     <el-badge :is-dot="hasNewMsg()"  class="item">
     <span style="font-weight:bold;font-size:20px;color:rgb(87, 171, 226)"  >
     计划交流室
     </span>
     </el-badge>
   </el-card>
   
   <el-card @click.native="openFileCenter" class="hover">
     <i class="el-icon-upload"></i>
     <span style="font-weight:bold;font-size:20px;color:rgb(87, 171, 226)"  >
     计划资料库
     </span>
   </el-card>

   <el-card @click.native="openConference" class="hover">
     <i class="el-icon-video-camera-solid"></i>
     <span style="font-weight:bold;font-size:20px;color:rgb(87, 171, 226)"  >
     计划会议室
     </span>
   </el-card>

    <el-card>
    <el-button type="info"  icon="el-icon-message" plain>发送消息</el-button>
    <el-button @click="deleteCowork" type="danger" icon="el-icon-delete" plain>删除计划</el-button>
    </el-card>

    <el-dialog top="5vh" width="600px" v-if="host!=null&&me!=null&&proSelected!=null" 
    :visible.sync="sendInfoVisibleFor1" title="发送请求">
    <el-card>
    <el-descriptions :column="1">
    <el-descriptions-item label="请求接收方">
    <el-tag size="mini">{{host.name}}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="任务">
    {{proSelected.pro_name}}
    </el-descriptions-item>
    <el-descriptions-item label="请求任务状态转换">
    <el-tag size="mini" type="danger">未开始</el-tag> → <el-tag size="mini" type="warning">进行中</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="report" placeholder="任务准备情况……"></el-input>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="message" placeholder="我的留言……"></el-input>
    </el-card>

    <el-card>
    <el-button @click="confirmSendInfoFor1" type="primary">确认发送请求</el-button>
    </el-card>

    </el-dialog>
    

    <el-dialog top="5vh" width="600px" v-if="host!=null&&me!=null&&proSelected!=null" 
    :visible.sync="sendInfoVisibleFor2" title="发送请求">
    <el-card>
    <el-descriptions :column="1">
    <el-descriptions-item label="请求接收方">
    <el-tag size="mini">{{host.name}}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="任务">
    {{proSelected.pro_name}}
    </el-descriptions-item>
    <el-descriptions-item label="请求任务状态转换">
    <el-tag size="mini" type="warning">进行中</el-tag> → <el-tag size="mini" type="success">结束</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="report" placeholder="任务完成情况……"></el-input>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="message" placeholder="我的留言……"></el-input>
    </el-card>

    <el-card>
    <el-button @click="confirmSendInfoFor2" type="primary">确认发送请求</el-button>
    </el-card>

    </el-dialog>

    <chat-room v-if="chatRoomVisible" :visible.sync="chatRoomVisible" :IM="IM"  :plan="cowork.plan.plan"></chat-room> 
    
     <!-- 参与人信息 -->
      <el-dialog  :title="memberSlected.name" v-if="memberInfoVisible"  
                 :visible.sync="memberInfoVisible">
      <el-card>
      <div>
      <el-tag  type="primary" style="margin-right:5px">{{memberSlected.name}}
      </el-tag>
      <el-tag  type="warning" v-if="memberSlected.host">负责人
      </el-tag>
      </div>
      <br>
      <el-descriptions :column="1">
      <el-descriptions-item label="邮箱">   
      {{memberSlected.email}}
      </el-descriptions-item>

      <el-descriptions-item label="手机">   
      {{memberSlected.phone.match(/^\s*$/)?'未设置':memberSlected.phone}}
      </el-descriptions-item>
      </el-descriptions>
      </el-card>     
      </el-dialog>


    </div>
</template>

<script>
var database =require('nedb')
var db=[]
import chatRoom from '../components/main/chatRoom.vue'

export default {
    components: { chatRoom },
    created(){
        this.cowork=this.$route.params.cowork
        let that=this
        db=new database({
        filename:that.$store.state.dbName,
        autoload:true
        })
        
        

        //找出计划成员中的账户本人
        this.cowork.plan.plan.member.forEach(function(item,index){
            if(that.me!=null&&that.host!=null){
                return 
            }
            if(that.$store.state.user==item.email){
                that.me=item
            }
            if(item.host){
                that.host=item
            }
        })
        
        let proLen=this.cowork.plan.process.length
        //确认参与人所参与的任务,找出每个任务中参与人的完成状态
         this.cowork.plan.process.forEach(function(item,index){
            if(index==0||index==proLen-1){
                return 
            }
            let l=item.proMem.length
            for(let i=0;i<l;i++){
                if(that.me.user_id==item.proMem[i].user_id){
                    that.proList.push(item)
                    that.proMeMap.set(item.id,item.proMem[i])
                    break
                }
            }
         })

         //IM init
         let key=this.cowork.plan.plan.name+"&"+this.cowork.plan.plan.manager.user_id
                if(!this.$store.state.IM.has(key)){
                    this.$store.state.IM.set(key,{
                        msg:[],
                        memStatus:new Map()
                    })
                }
         this.IM=this.$store.state.IM.get(key)
    },
    data(){
        return {
            cowork:null,
            activeName:'',
            me:null,
            host:null,

            //获取所有进程的账户成员状态
            proMeMap:new Map(),

            //向任务负责人提交任务状态修改窗口
            sendInfoVisibleFor1:false,
            sendInfoVisibleFor2:false, 
            message:'',
            report:'',
            
            proSelected:null,

            //用户所参与的计划
            proList:[],

            //计划交流室
            chatRoomVisible:false,
            IM:{},

            //展示小组成员信息
            memberInfoVisible:false,
            memberSlected:null

        }
    },
    methods:{
            //展示小组成员信息
            showMemInfo(mem){
                this.memberSlected=mem
                this.memberInfoVisible=true
            },

            //打开计划交流室
            openChatRoom(){
                this.chatRoomVisible=true
            },

            //打开计划资料库
            openFileCenter(){
                if (!this.$store.state.login) {
                    this.$message.warning("请先登陆");
                    return;
                }
                
                let that = this;
                
                this.$router.push({
                    name: "planFile",
                    params: {
                        planName: that.cowork.plan.plan.name,
                        planId: that.cowork.plan_id,
                        cowork:that.cowork
                    },
                });
            },

            //视频会议窗口
            openConference(){
                const ipc = require('electron').ipcRenderer
                ipc.send('createConference',this.cowork.plan,this.$store.state.user_id)
            },
            

            hasNewMsg(){
                let l=this.IM.msg.length
                if(l==0){
                    return false
                }
                return !this.IM.msg[l-1].read&&!this.chatRoomVisible
            },

        sendInfoFor1(pro){
            this.message=''
            this.report=''
            this.sendInfoVisibleFor1=true
            this.proSelected=pro
        },
        confirmSendInfoFor1(){
            let that=this
            this.proMeMap.get(this.proSelected.id).status=0.5
            this.save()
            
            let date=new Date()
            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
            //发送message部分
            this.$http.post(this.$store.state.contextUrl+'/uploadNotice',this.$qs.stringify({
                acceptor:that.cowork.host,
                sender:that.cowork.partner,
                content:JSON.stringify({
                    plan_name:that.cowork.plan.plan.name,
                    pro_id:that.proSelected.id,
                    time:timeStr,
                    pro:that.proSelected,
                    me:that.proMeMap.get(that.proSelected.id),
                    cowork_id:that.cowork.id,

                    //文字内容
                    extraMsg:that.message,
                    report:that.report
                }),
                type:1,
                read:0
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'请求发送成功',
                        type:'success'
                    })
                }
                that.send(that.cowork.host)
                that.sendInfoVisibleFor1=false
            })
        },
        sendInfoFor2(pro){
            this.message=''
            this.report=''
            this.sendInfoVisibleFor2=true
            this.proSelected=pro
        },
        confirmSendInfoFor2(){
            let that=this
            this.proMeMap.get(this.proSelected.id).status=1.5
            this.save()
            
            let date=new Date()
            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
            //发送message部分
            this.$http.post(this.$store.state.contextUrl+'/uploadNotice',this.$qs.stringify({
                acceptor:that.cowork.host,
                sender:that.cowork.partner,
                content:JSON.stringify({
                    plan_name:that.cowork.plan.plan.name,
                    pro_id:that.proSelected.id,
                    time:timeStr,
                    pro:that.proSelected,
                    me:that.proMeMap.get(that.proSelected.id),
                    cowork_id:that.cowork.id,
                      
                    //文字内容
                    extraMsg:that.message,
                    report:that.report
                }),
                type:1,
                read:0
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'请求发送成功',
                        type:'success'
                    })
                }
                that.send(that.cowork.host)
                that.sendInfoVisibleFor2=false
            })
        },

        goBack(){
            this.$router.push("/cowork")
        },
        deleteCowork(){
            let that=this
            that.$store.state.syncing=true
            db.remove({
                type:'cowork',
                id:that.cowork.id
            },{},function(err,num){
                that.$message({
                    message:'删除成功',
                    type:'error'
                })
            })
            
            if(this.$store.state.login){
                this.$http.post(this.$store.state.contextUrl+'/deleteCowork',this.$qs.stringify({
                    id:that.cowork.id
                })).then(function(res){
                    if(res.data=='fail'){
                        that.$message({
                            message:'云端同步失败',
                            type:'error'
                        })
                    }
                    that.$store.state.syncing=false
                    that.goBack()
                })
            }
        },

        //获取进度条的percentage
        getProPercentage(status){
            if(status>=0&&status<1){
                return 0
            }else if(status>=1&&status<2){
                return 50
            }else{
                return 100
            }
        },

        //websocket: send the refresh message
        send(uid){
            if(this.$store.state.socket.readyState==1){
                this.$store.state.socket.send("refresh?"+uid)
                }
            },

        format(num){
            if(num==0){
                return '未开始'
            }else if(num==50){
                return '进行中'
            }else{
                return '已完成'
            }
        },

        save(){
            let that=this
            this.$store.state.syncing=true
            if(this.$store.state.login){
                this.$http.post(this.$store.state.contextUrl+'/updatePlan',this.$qs.stringify({
                    id:that.cowork.plan_id,
                    plan_text:JSON.stringify(that.cowork.plan)
                })).then(function(res){
                    that.$store.state.syncing=false
                    if(res.data!='success'){
                        that.$message({
                            message:'计划状态存储出现错误',
                            type:'error'
                        })
                    }
                })
            }else{
                this.$message({
                    message:'出现错误，该用户未登录',
                    type:'error'
                })
            }
        }
    }
}
</script>

<style scoped>
.main{
   height:100%;
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   overflow-y: auto;
}
.el-collapse >>> .el-collapse-item__header{
    padding-left:2% ;
}

.el-collapse >>> .el-collapse-item__content{
     padding-left:2% ;
}


.hover:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.hover_noColor:hover{
    cursor: pointer;
}

</style>