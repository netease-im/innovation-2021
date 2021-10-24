<template>
    <div class="main">
    <el-card class="bodyCard" >
    <el-page-header @back="goBack" content="消息列表" style="margin-bottom:15px"></el-page-header>
    <el-divider></el-divider>
    <el-tabs type="card" v-model="infoShow">
    <el-tab-pane label="进度请求" name="statusInfo"></el-tab-pane>
    <el-tab-pane label="任务通知" name="proInfo"></el-tab-pane>
    <el-tab-pane label="更新许可" name="diyInfo"></el-tab-pane>
    <el-tab-pane label="其他通知" name="otherInfo"></el-tab-pane>
    </el-tabs>
     
    <!-- 展示任务进度请求 type:1 --> 
    <el-table
      v-if="infoShow=='statusInfo'"
      class="jobTable"
      :data="statusInfo"
      style="width: 100%;height:100%"
      @row-click="showRequest">

      <el-table-column>
      <template slot-scope="scope">
       进度请求
      </template>
      </el-table-column>

      <el-table-column
        prop="body.content.plan_name"
        label="所属计划"
        >
      </el-table-column>

      <el-table-column
        prop="body.content.pro.pro_name"
        label="任务名"
        >
      </el-table-column>

      <el-table-column
      prop="sender"
      label="发送人">  
      </el-table-column>

      <el-table-column
      prop="body.content.time"
      label="发送时间">  
      </el-table-column>

      <el-table-column>
      <template slot-scope="scope">
       <el-button @click="sendMsg(scope.row)"  icon="el-icon-message" size="mini" type="info" circle plain></el-button>
       <el-button @click="deleteMsg(scope.row,true)" icon="el-icon-delete" size="mini" type="danger" circle plain></el-button>
      </template>
      </el-table-column>

    </el-table>

    <!-- 展示任务清单通知 type:2 -->
    <el-table
      v-if="infoShow=='proInfo'"
      class="jobTable"
      :data="proInfo"
      style="width: 100%;height:100%"
      @row-click="showMessage">

      <el-table-column>
      <template slot-scope="scope">
       任务清单
      </template>
      </el-table-column>

      <el-table-column
        prop="body.content.plan_name"
        label="所属计划"
        >
      </el-table-column>


      <el-table-column
      prop="sender"
      label="发送人">  
      </el-table-column>

      <el-table-column
      prop="body.content.time"
      label="发送时间">  
      </el-table-column>

      <el-table-column>
      <template slot-scope="scope">
       <el-button @click="sendMsg(scope.row)"  icon="el-icon-message" size="mini" type="info" circle plain></el-button>
       <el-button @click="deleteMsg(scope.row,true)" icon="el-icon-delete" size="mini" type="danger" circle plain></el-button>
      </template>
      </el-table-column>

    </el-table>

    
    <!-- 展示更新许可  type:3 -->
    <el-table
      v-if="infoShow=='diyInfo'"
      class="jobTable"
      :data="diyInfo"
      style="width: 100%;height:100%"
      @row-click="showDiyMessage">

      <el-table-column>
      <template slot-scope="scope">
       <span v-if="scope.row.body.content.type=='acceptRequestInfo'">进度更新许可</span>
      </template>
      </el-table-column>

      <el-table-column
        prop="body.content.plan_name"
        label="所属计划"
        >
      </el-table-column>


      <el-table-column
      prop="sender"
      label="发送人">  
      </el-table-column>

      <el-table-column
      prop="body.content.time"
      label="发送时间">  
      </el-table-column>

      <el-table-column>
      <template slot-scope="scope">
       <el-button @click="sendMsg(scope.row)"  icon="el-icon-message" size="mini" type="info" circle plain></el-button>
       <el-button @click="deleteMsg(scope.row,true)" icon="el-icon-delete" size="mini" type="danger" circle plain></el-button>
      </template>
      </el-table-column>

    </el-table>
    
    <!-- 展示其他通知  type>=4 ;  4：消息通知  5：任务提醒  6：消息回执 -->
    <el-table
      v-if="infoShow=='otherInfo'"
      class="jobTable"
      :data="otherInfo"
      style="width: 100%;height:100%"
      @row-click="showOtherInfo">

      <el-table-column>
      <template slot-scope="scope">
       <span v-if="scope.row.body.type==4">消息通知</span>
       <span v-if="scope.row.body.type==5">任务提醒</span>
       <span v-if="scope.row.body.type==6">消息回执</span>
      </template>
      </el-table-column>

      <el-table-column
        prop="body.content.plan_name"
        label="所属计划"
        >
      </el-table-column>


      <el-table-column
      prop="sender"
      label="发送人">  
      </el-table-column>

      <el-table-column
      prop="body.content.time"
      label="发送时间">  
      </el-table-column>

      <el-table-column
      prop="body.briefMsg"
      label="消息内容">  
      </el-table-column>

      <el-table-column>
      <template slot-scope="scope">
       <el-button @click="sendMsg(scope.row)"  icon="el-icon-message" size="mini" type="info" circle plain></el-button>
       <el-button @click="deleteMsg(scope.row,true)" icon="el-icon-delete" size="mini" type="danger" circle plain></el-button>
      </template>
      </el-table-column>

    </el-table>


    </el-card>
    

     <el-dialog width="700px" top="5vh" :title="messageTitle(messageSlected)" 
                v-if="messageDetailVisible"  
                :visible.sync="messageDetailVisible">
     <el-card>
     <el-descriptions :column="2">
     <el-descriptions-item label="所属计划">{{messageSlected.body.content.plan_name}}</el-descriptions-item>
     <el-descriptions-item label="发送人">{{messageSlected.sender}}</el-descriptions-item>
     <el-descriptions-item label="发送时间">{{messageSlected.body.content.time}}</el-descriptions-item>
     <el-descriptions-item label="任务数量">{{messageSlected.body.content.pro.length}}</el-descriptions-item>
     </el-descriptions>
     </el-card>
     <el-card>
     <div slot="header">
     我的任务
     </div>

     <el-table
      height="200px"
      class="jobTable"
      :data="messageSlected.body.content.pro"
      style="width: 100%"
      @row-click="showProDetail">
      <el-table-column prop="id" label="任务序号"></el-table-column>
      <el-table-column prop="pro_name" label="任务名称"></el-table-column>
      </el-table>

     </el-card>
     <el-card>
     <el-button @click="showExtraMsg" type="info">发送人留言</el-button>
     <el-button @click="showPlanBasic" type="warning">计划信息</el-button>
     <el-button @click="showAcceptConfirm" type="success">接受任务</el-button>
     <el-button type="danger">拒绝任务</el-button>
     </el-card>
     </el-dialog>
    
  <el-dialog  title="计划信息" v-if="planDetailVisible"  :visible.sync="planDetailVisible">
    <el-descriptions :column="1" >
    <el-descriptions-item label="计划名称">{{messageSlected.body.content.plan.name}}</el-descriptions-item>
    <el-descriptions-item label="计划性质">{{messageSlected.body.content.plan.type}}</el-descriptions-item>
    
    <el-descriptions-item :label="messageSlected.body.content.plan.type=='个人计划'?'执行人':'负责人'">
    <el-tag size="mini">
    {{messageSlected.body.content.plan.type=='个人计划'?messageSlected.body.content.plan.excutor.name:messageSlected.body.content.plan.manager.name}}
    </el-tag>
    </el-descriptions-item>

    </el-descriptions>

    <el-descriptions>
    <el-descriptions-item label="计划时间">{{messageSlected.body.content.plan.startDate+" "+
       messageSlected.body.content.plan.startTime+" 至 "+messageSlected.body.content.plan.endDate+" "+messageSlected.body.content.plan.endTime}}
    </el-descriptions-item>
    </el-descriptions>

    <el-descriptions :column="1" v-if="messageSlected.body.content.plan.type=='小组计划'">
    <el-descriptions-item label="小组成员">
      <div v-if="messageSlected.body.content.plan.type=='小组计划'&&messageSlected.body.content.plan.member.length>0">
      <el-tag style="margin-right:5px" size="mini" :type="item.host?'':'success'" v-for="(item,i) in messageSlected.body.content.plan.member" :key="i">{{item.name}}</el-tag>
      </div>

      <div v-if="messageSlected.body.content.plan.type=='小组计划'&&messageSlected.body.content.plan.member.length==0">
      <span>无</span>
      </div>      
    </el-descriptions-item>
    </el-descriptions>

     <el-descriptions>
     <el-descriptions-item label="计划描述">{{messageSlected.body.content.plan.desc}}</el-descriptions-item>
     </el-descriptions>
    </el-dialog>

    <el-dialog  title="任务信息" v-if="proDetailVisible"  :visible.sync="proDetailVisible">
    <el-descriptions :column="1">
    <el-descriptions-item label="任务序号">{{proSlected.id}}</el-descriptions-item>
    <el-descriptions-item label="任务名称">{{proSlected.pro_name}}</el-descriptions-item>
    
    <el-descriptions-item label="参与人">
    <el-tag style="margin-right:5px" size="mini" type="success" v-for="(item,i) in proSlected.proMem" :key="i">
      {{item.name}}
    </el-tag>
    </el-descriptions-item>

    </el-descriptions>

    <el-descriptions>
    <el-descriptions-item label="任务时间">{{proSlected.startDate+" "+
       proSlected.startTime+" 至 "+proSlected.endDate+" "+proSlected.endTime}}
    </el-descriptions-item>
    </el-descriptions>

     <el-descriptions>
     <el-descriptions-item label="任务描述">{{proSlected.pro_text}}</el-descriptions-item>
     </el-descriptions>
    </el-dialog>


     <!-- 任务进度请求part -->
     <el-dialog width="700px" top="5vh" :title="messageTitle(messageSlected)" 
                v-if="requestDetailVisible"  
                :visible.sync="requestDetailVisible">
     <el-card>
     <el-descriptions :column="1">
     <el-descriptions-item label="发送人">
     {{messageSlected.sender}}
     </el-descriptions-item>
     <el-descriptions-item label="所属计划">
     {{messageSlected.body.content.plan_name}}
     </el-descriptions-item>
     <el-descriptions-item label="任务名称">
     {{messageSlected.body.content.pro.pro_name}}
     </el-descriptions-item>
     <el-descriptions-item label="任务进度请求">
     <div v-if="messageSlected.body.content.me.status==0.5">
     <el-tag size="mini" type="danger">未开始</el-tag>
     →
     <el-tag size="mini" type="warning">进行中</el-tag>
     </div>

     <div v-if="messageSlected.body.content.me.status==1.5">
     <el-tag size="mini" type="warning">进行中</el-tag>
     →
     <el-tag size="mini" type="success">已完成</el-tag>
     </div>

     </el-descriptions-item>
     </el-descriptions>
     </el-card>
     <el-card>
     <el-descriptions title="任务汇报" :colon="false">
     </el-descriptions>
     <el-input v-model="messageSlected.body.content.report" type="textarea" :readonly="true"></el-input>
     </el-card>
     <el-card>
     <el-button @click="showExtraMsg" type="info">发送人留言</el-button>
     <el-button @click="acceptRequest" type="primary">接受请求</el-button>
     <el-button type="danger" >拒绝请求</el-button>
     </el-card>
     </el-dialog>
    
     <!-- 任务进度转换弹窗 -->
      <el-dialog width="700px" top="5vh" title="任务进度转换" 
                v-if="acceptRequestVisible"  
                :visible.sync="acceptRequestVisible">

      <el-card>

      <el-descriptions :column="1" title="我的合作">
      <el-descriptions-item label="发送人">
      {{messageSlected.sender}}
      </el-descriptions-item>

      <el-descriptions-item label="计划名称">
      {{messageSlected.body.content.plan_name}}
      </el-descriptions-item>

      <el-descriptions-item label="任务名称">
      {{messageSlected.body.content.pro_name}}
      </el-descriptions-item>

      <el-descriptions-item label="进度转换">
      <div v-if="messageSlected.body.content.status==0.5">
      <el-tag size="mini" type="danger">未开始</el-tag>
        →
      <el-tag size="mini" type="warning">进行中</el-tag>
      </div>
      
      <div v-if="messageSlected.body.content.status==1.5">
      <el-tag size="mini" type="warning">进行中</el-tag>
        →
      <el-tag size="mini" type="success">已完成</el-tag>
      </div>
      
      </el-descriptions-item>

      </el-descriptions>

      </el-card>

      <el-card >
      <el-button @click="confirmCoworkProStatusAccept" type="primary">更新任务进度</el-button>
      </el-card>

      </el-dialog>

       
      <!-- 消息通知的弹框 -->
      <el-dialog width="700px" top="5vh" title="消息通知"  
       v-if="otherInfoVisible"  :visible.sync="otherInfoVisible">
      <el-card>
      <el-descriptions :column="1">
      <el-descriptions-item label="发送人">
      {{messageSlected.sender}}
      </el-descriptions-item>
       <el-descriptions-item label="发送时间">
      {{messageSlected.body.content.time}}
      </el-descriptions-item>
       <el-descriptions-item label="所属计划">
      {{messageSlected.body.content.plan_name}}
      </el-descriptions-item>
      </el-descriptions>
      </el-card>
      <el-card>
      <el-input type="textarea" v-model="messageSlected.body.content.extraMsg" :readonly="true"></el-input>
      </el-card>
      <el-card>
      <el-button @click="showPlanBasic" v-if="messageSlected.body.type!=6" type="warning">计划信息</el-button>
      <el-button @click="showProDetail(messageSlected.body.content.pro)" v-if="messageSlected.body.type==5" type="warning">任务详情</el-button>
      <el-button @click="otherInfoVisible=false" type="primary">确定</el-button>
      </el-card>
      </el-dialog>

      <!-- 消息回执的弹窗 -->
      <el-dialog width="700px" top="5vh" title="消息回执"  
       v-if="callbackInfoVisible"  :visible.sync="callbackInfoVisible">
       <el-card> 
       <el-descriptions :column="1">
       <el-descriptions-item label="接收人">{{messageSlected.sender}}</el-descriptions-item>
       </el-descriptions>
       <el-input type="textarea" v-model="callbackText" placeholder="在此处输入消息内容....."></el-input>
       </el-card>
       <el-card>
       <el-button @click="sendCallbackInfo" type="primary">确认发送</el-button>
       </el-card>
      </el-dialog>

    </div>
</template>

<script>
var database =require('nedb')
var db=[]
export default {
    created(){
        let that=this
        db=new database({
        filename:that.$store.state.dbName,
        autoload:true
        })
        let messages=this.$store.state.message
    
        messages.forEach(function(item,index){
            if(item.type==1){
                //任务进度通知
                 that.statusInfo.push({
                    sender:item.sender_name+"("+item.sender_email+")",
                    body:item
                })
            }else if(item.type==2){
                //任务清单通知
                 that.proInfo.push({
                    sender:item.sender_name+"("+item.sender_email+")",
                    body:item
                })
            }else if(item.type==3){
                 that.diyInfo.push({
                    sender:item.sender_name+"("+item.sender_email+")",
                    body:item
                })
            }else if(item.type>=4){
                 item.briefMsg=item.content.extraMsg.substr(0,14)
                 if(item.content.extraMsg.length>14){
                     item.briefMsg+='...'
                 }
                 that.otherInfo.push({
                    sender:item.sender_name+"("+item.sender_email+")",
                    body:item
                })
            }
        })
        
    },
    methods:{
        goBack(){
            this.$router.push('/userCenter')
        },

        //展示任务清单通知计划详情
        showMessage(row, column, event){
            this.messageSlected=row
            if(this.deleteMode||this.sendMode){
                this.deleteMode=false
                this.sendMode=false
                return
            }
            this.messageDetailVisible=true
        },

       //展示任务进度请求
       showRequest(row, column, event){
              this.messageSlected=row
              if(this.deleteMode||this.sendMode){
                this.deleteMode=false
                this.sendMode=false
                return
            }
            this.requestDetailVisible=true
       },

       //展示任务进度更新许可
       showDiyMessage(row, column, event){
            this.messageSlected=row
            if(this.deleteMode||this.sendMode){
                this.deleteMode=false
                this.sendMode=false
                return
            }
            if(row.body.content.type=='acceptRequestInfo'){
                this.acceptRequestVisible=true
            }
       },
       

       //展示消息通知
       showOtherInfo(row, column, event){
           this.messageSlected=row
           if(this.deleteMode||this.sendMode){
                this.deleteMode=false
                this.sendMode=false
                return
            }
           this.otherInfoVisible=true
           if(this.messageSlected.body.read==0){
               this.messageSlected.body.read=1
               this.saveNotice()
           }
       },

       messageTitle(message){
            
            if(message.body.type==1){
                return '进度请求'
            }else if(message.body.type==2){
                return '任务清单'
            }else{
                return '通知详情'

            }
        },
        showExtraMsg(){
           const h=this.$createElement
           let that=this
           this.$msgbox({
             title: '留言',
             message: h('p', null, [
               h('span', null,that.messageSlected.body.content.extraMsg)
               ]),
            });
        },
        showPlanBasic(){
            this.planDetailVisible=true
        },
        showProDetail(row, column, event){
            this.proSlected=row
            this.proDetailVisible=true
        },
        showAcceptConfirm(){
            let that=this
            if(!this.messageSlected.body.content.accepted){
                 this.messageSlected.body.content.accepted=true
                 this.save('接受成功，请在我的合作中查看')
                 this.deleteMsg(this.messageSlected,false)
                 this.messageDetailVisible=false
                //  this.$http.post(this.$store.state.contextUrl+'/updateNotice',this.$qs.stringify({
                //      id:that.messageSlected.body.id,
                //      content:JSON.stringify(that.messageSlected.body.content)
                //  })).then(function(res){
                //      if(res.data=='fail'){
                //          that.$message({
                //              message:'出现错误',
                //              type:'error'
                //          })
                //      }
                //  })
            }else{
                this.$message({
                    message:'该任务已接受，请在我的合作中查看',
                    type:'warning'
                })
            }
        },
        //保存notice
        saveNotice(msg){
            let that=this
            if(that.$store.state.dbName=='./cloud.db'){
                 that.$store.state.syncing=true
                 that.$http.post(that.$store.state.contextUrl+'/updateNoticeRead',that.$qs.stringify({
                     id:that.messageSlected.body.id,
                     read:that.messageSlected.body.read
                 })).then(function(res){
                     if(res.data=='success'){
                        //  that.$message({
                        //      message:msg,
                        //      type:'success'
                        //  })
                         that.$store.state.syncing=false
                     }
                 })
             }
        },
        //保存cowork
        save(msg){
             let that=this
            //  db.insert({type:'cowork',cowork:that.messageSlected},function(err,newDoc){
            //     that.$message({
            //         message:msg,
            //         type:'success'
            //     })
            //  })

             //云同步代码
            if(that.$store.state.dbName=='./cloud.db'){
                    
                    that.$store.state.syncing=true
                    that.$http.post(that.$store.state.contextUrl+'/uploadCowork',that.$qs.stringify({
                        user_id:that.$store.state.user_id,
                        content:JSON.stringify({
                            type:'cowork',
                            cowork:that.messageSlected
                        })
                    })).then(function(res){
                        if(res.data=='fail'){
                            that.$message({
                                message:'云端同步失败，请检查网络设置',
                                type:'error'
                            })
                        }
                        that.$http.post(that.$store.state.contextUrl+'/downloadCowork',that.$qs.stringify({
                            user_id:that.$store.state.user_id
                        })).then(function(res){
                            db.remove({type:'cowork'},{multi: true },function (err, numRemoved) {})
                            let cowork=[]
                            res.data.forEach(function(item,index){
                                item.content=JSON.parse(item.content)
                                item.content.id=item.id
                                cowork.push(item.content)
                            
                            })
                            db.insert(cowork)
                            that.logining=false
                            that.reload()
                            that.$store.state.syncing=false
                            that.$message({
                                message:msg,
                                type:'success'
                            })
                        })
                })      
            }
        },
        deleteMsg(msg,tip){
            this.deleteMode=true
            if(this.infoShow=='proInfo'){
                this.proInfo.splice(this.proInfo.indexOf(msg),1)
            }else if(this.infoShow=='statusInfo'){
                this.statusInfo.splice(this.statusInfo.indexOf(msg),1)
            }else if(this.infoShow=='diyInfo'){
                this.diyInfo.splice(this.diyInfo.indexOf(msg),1)
            }else if(this.infoShow=='otherInfo'){
                this.otherInfo.splice(this.otherInfo.indexOf(msg),1)
            }
            let del=false
            let that=this
            this.$store.state.message.forEach(function(item,index){
                if(del){
                    return 
                }
                if(msg.body.id==item.id){
                    del=true
                    that.$store.state.message.splice(index,1)
                }
            })

            if(tip){
                this.$message({
                    message:'删除成功',
                    type:'error'
                })
            }

            if(that.$store.state.dbName=='./cloud.db'){
                that.$store.state.syncing=true
                    that.$http.post(that.$store.state.contextUrl+'/deleteNotice',that.$qs.stringify({
                        id:msg.body.id,
                    })).then(function(res){
                        if(res.data=='fail'){
                            that.$message({
                                message:'云端同步失败，请检查网络设置',
                                type:'error'
                            })
                        }
                    that.$store.state.syncing=false
                })    
            }
        },

        //消息回执弹窗
        sendMsg(msg){
            this.sendMode=true
            this.callbackInfoVisible=true
        },
       
        //发送消息回执
        sendCallbackInfo(){
             let that=this
             let date=new Date()
             let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()

                    //发送人账户中计划任务状态转换指令发送
                    that.$http.post(that.$store.state.contextUrl+'/uploadNotice',that.$qs.stringify({
                        acceptor:that.messageSlected.body.sender,
                        sender:that.messageSlected.body.acceptor,
                        content:JSON.stringify({
                            extraMsg:that.callbackText,
                            time:timeStr,
                            plan_name:that.messageSlected.body.content.plan_name
                        }),

                        //type:6 消息回执
                        type:6,
                        read:0
                    })).then(function(res){
                        if(res.data=='success'){
                            that.$store.state.syncing=false
                            that.callbackInfoVisible=false
                            that.$message({
                                message:'消息回执发送成功',
                                type:'success'
                            })
                            
                            that.refreshMsg(that.messageSlected.body.sender)
                        }else{
                            that.$message({
                                message:'消息回执发送失败',
                                type:'error'
                            })
                      }
                })
        },

        //刷新计划任务的状态
        refreshProStatus(pro){
             let count=0
             pro.proMem.forEach(function(item,index){
                 count+=item.status
             })
             if(count==0){
                 pro.status=0
            }else if(count==pro.proMem.length*2){
                 pro.status=2
            }else{
                 pro.status=1
            }
        },

        //负责人接受任务进度转换请求
        acceptRequest(){
            let that=this
            db.find({type:'planItemTest2',"plan.name":that.messageSlected.body.content.plan_name},function(err,docs){
                if(docs.length==0){
                    that.$message({
                        message:'错误！！，计划不存在',
                        type:'error'
                    })
                    return 
                }else{
                    let plan=docs[0]
                    let sender=that.messageSlected.body.content.me
                    let pro_id=that.messageSlected.body.content.pro_id
                    let pro=plan.process[pro_id]

                    //计划负责人账户中计划任务状态的转换
                    if(that.$store.state.dbName=='./cloud.db'){
                        that.$store.state.syncing=true
                        //本地+线上保存
                        let found=false
                        pro.proMem.forEach(function(item,index){
                            if(found){
                                return 
                            }
                            if(item.email==sender.email){
                                if(sender.status==0.5){
                                    item.status=1
                                }else if(sender.status==1.5){
                                    item.status=2
                                }
                                found=true
                            }
                        })
                        that.refreshProStatus(pro)
                        that.savePlan("保存成功",plan)
                        that.requestDetailVisible=false
                    }else{
                        this.$message({
                            message:'出现错误,账户未登录',
                            type:'error'
                        })
                    }
                    let date=new Date()
                    let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
                    //发送人账户中计划任务状态转换指令发送
                    that.$http.post(that.$store.state.contextUrl+'/uploadNotice',that.$qs.stringify({
                        acceptor:that.messageSlected.body.sender,
                        sender:that.messageSlected.body.acceptor,
                        content:JSON.stringify({
                            cowork_id:that.messageSlected.body.content.cowork_id,
                            proId:that.messageSlected.body.content.pro_id,
                            type:'acceptRequestInfo',
                            status:sender.status,
                            plan_name:that.messageSlected.body.content.plan_name,
                            pro_name:that.messageSlected.body.content.pro.pro_name,
                            time:timeStr
                        }),

                        //type:3 其他通知
                        type:3,
                        read:0
                    })).then(function(res){
                        if(res.data=='success'){
                            that.refreshMsg(that.messageSlected.body.sender)
                            that.$store.state.syncing=false
                            that.deleteMsg(that.messageSlected,false)
                        }else{
                            that.$message({
                                message:'计划状态转换许可发送失败',
                                type:'error'
                            })
                        }
                    })
                }
            }) 
        },

        //参与人接受计划状态转换许可
        confirmCoworkProStatusAccept(){
            let that=this
            let cowork=null
            db.find({type:'cowork',id:that.messageSlected.body.content.cowork_id},function(err,docs){
                if(docs.length!=0){
                    cowork=docs[0]
                    let finish=false
                    cowork.cowork.body.content.pro.forEach(function(item,index){
                        if(finish){
                            return 
                        }
                        if(item.id=that.messageSlected.body.content.proId){
                            let end=false
                            item.proMem.forEach(function(mem,i){
                                if(end){
                                    return 
                                }
                                if(mem.email==that.$store.state.user){
                                    if(mem.status==0.5){
                                        mem.status=1
                                    }else if(mem.status==1.5){
                                        mem.status=2
                                    }
                                    end=true
                                }
                            })
                            finish=true
                        }
                    })
                    db.update({type:'cowork',id:that.messageSlected.body.content.cowork_id},cowork,function(err, numReplaced){
                        that.$message({
                            message:'转换成功',
                            type:'success'
                        })
                    })
                    if(that.$store.state.dbName=='./cloud.db'){
                         that.$store.state.syncing=true
                         that.$http.post(that.$store.state.contextUrl+'/updateCowork',that.$qs.stringify({
                             id:that.messageSlected.body.content.cowork_id,
                             content:JSON.stringify(cowork)
                         })).then(function(res){
                             if(res.data!='success'){
                                 that.$message({
                                     message:'云同步保存计划状态转换出现错误',
                                     type:'error'
                                 })
                             }
                             that.$store.state.syncing=false
                             that.deleteMsg(that.messageSlected,false)
                         })
                    }
                }else{
                    that.$message({
                        message:'计划状态转换出现错误',
                        type:'error'
                    })
                }
            })
            this.acceptRequestVisible=false
        },

        
    
        //保存数据
        savePlan(msg,plan){
            let that=this
            db.update({type:"planItemTest2","plan.name":plan.plan.name},plan,function(err, numReplaced){
                if(numReplaced==1){
                that.$message({
                    message:msg,
                    type:'success'
                 })
                }
            })
            
            
            //云同步代码
            if(that.$store.state.dbName=='./cloud.db'){
                let promise=new Promise(function(resolve,reject){
                    let notes=[]
                    db.find({type:'planItemNotes',plan:plan.plan.name},function(err,Docs){
                        if(Docs.length!=0){
                            notes.push(Docs[0])
                        }else{
                            notes=[{
                                type:'planItemNotes',
                                plan:plan.plan.name,
                                dailyNotes:{}
                            }]
                        }
                        resolve(notes)
                    })
                })
                
                promise.then(function(notes){
                    if(notes==null){
                        that.$message({
                            message:'云同步失败',
                            type:'error'
                        })
                    }
                    
                    that.$store.state.syncing=true
                    that.$http.post(that.$store.state.contextUrl+'/uploadPlan',that.$qs.stringify({
                        user_id:that.$store.state.user_id,
                        plan:JSON.stringify([{
                            type:"planItemTest2",
                            plan:plan.plan,
                            process:plan.process
                        }]),
                        planLog:JSON.stringify(notes)
                    })).then(function(res){
                        if(res.data=='fail'){
                            that.$message({
                                message:'云端同步失败，请检查网络设置',
                                type:'error'
                            })
                        }
                    that.$store.state.syncing=false
                })
            })
          }
       },

       //websocket通信：刷新msg
       refreshMsg(uid){
           if(this.$store.state.socket.readyState==1){
                    this.$store.state.socket.send("refresh?"+uid)
          }
       }
    },
    watch:{
        "callbackInfoVisible":{
            handler:function(val){
                if(!val){
                    this.callbackText=''
                }
            }
        }
    },
    inject:['reload'],
    data(){
        return {
            infoShow:'statusInfo',

            //message.type=1
            statusInfo:[],

            //message.type=2
            proInfo:[],

            //message.type=3
            diyInfo:[],

            //message.type=4
            otherInfo:[],

            messageDetailVisible:false,
            messageSlected:null,

            deleteMode:false,
            sendMode:false,

            //计划详情
            planDetailVisible:false,

            //任务详情
            proDetailVisible:false,
            proSlected:null,


            //任务进度请求详情
            requestDetailVisible:false,

            //任务进度转换许可
            acceptRequestVisible:false,

            //其他通知
            otherInfoVisible:false,

            //消息回执弹框
            callbackInfoVisible:false,
            callbackText:'',
        }
    },
}
</script>

<style scoped>
.main{
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   height:100%;
   overflow-y: hidden;
}
.bodyCard{
   height:100%;
   overflow-y: auto;
}

.jobTable>>>.el-table__body-wrapper:hover{
    cursor: pointer;
}
</style>