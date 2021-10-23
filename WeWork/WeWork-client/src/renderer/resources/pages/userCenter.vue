<template>

    <div class="main">
    <el-card style="height:100%">
    <el-card class="basic-card">
    <img @click="changeUserHead"  src="../../assets/userCenterHead1.svg" v-if="$store.state.userCenterHeadIndex==0" class="userHead"/>
    <img @click="changeUserHead" src="../../assets/userCenterHead2.svg" v-else-if="$store.state.userCenterHeadIndex==1" class="userHead"/>
    <img @click="changeUserHead" src="../../assets/userCenterHead3.svg" v-else-if="$store.state.userCenterHeadIndex==2" class="userHead" />
    <img @click="changeUserHead" src="../../assets/userCenterHead4.svg" v-else-if="$store.state.userCenterHeadIndex==3" class="userHead" />
    
    <div class="userIdInfo">
    
    <el-popover
         placement="right-end"
         width="200"
         trigger="hover"
         :content="$store.state.user">
    <span slot="reference" style="float:left" class="userID">{{'用户：'+($store.state.user.length>=15?$store.state.user.substr(0,15)+"...":$store.state.user)}}</span>
    </el-popover>
    
    
    <span style="float:left" class="userID_">{{$store.state.user=='HelloWorld'?'本地用户':'云端用户'}}</span>
    </div>
    
    </el-card>

    <el-card class="function-card" @click.native="toMessage">
    <el-badge :is-dot="hasNewMsg()"  class="item">
      <span style="float:left" class="function">我的消息</span>
    </el-badge>
    </el-card>

    <el-card class="function-card" @click.native="toContacts">
      <span style="float:left" class="function">我的联系人</span>
    </el-card>

    
    <el-card class="function-card" @click.native="SyncToCloud">
      <span style="float:left" class="function">同步云端</span>
    </el-card>

    <el-card class="function-card" @click.native="DownToLocal">
      <span style="float:left" class="function">传送本地</span>
    </el-card>


    <el-card class="function-card" @click.native="clickModifyUserInfoPart">
      <span style="float:left" class="function">修改账户信息</span>
    </el-card>


    <el-card class="function-card" @click.native="clickDeleteCard" >
      <span style="float:left" class="function">删除用户计划</span>
    </el-card>

   
  <el-dialog title="选择本地计划" 
   :visible.sync="syncToCloud"
   top='10vh'
   width='80%'>

  <el-table :data="plan" height="350px">
    <el-table-column property="name" label="计划名称" width="150"></el-table-column>
    <el-table-column property="type" label="计划性质" width="100"></el-table-column>
    <el-table-column property="time" label="计划时间" width="400"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
       <el-button
          size="mini"
          type="success"
          @click="toCloudSlect(scope.$index,scope.row)" v-if="!plan[scope.$index].selected">选择</el-button>
       <el-button
          size="mini"
          type="warning"
          @click="CancleToCloudSlect(scope.$index,scope.row)" v-if="plan[scope.$index].selected">取消</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-button type="primary" @click="syncToCloudConfirm=true" style="margin-left:40%;margin-top:2%">同步到云端</el-button>
  </el-dialog>


  <el-dialog title="选择云端计划" 
   :visible.sync="downToLocal"
   top='10vh'
   width='80%'>

  <el-table :data="cloudPlan" height="350px">
    <el-table-column property="name" label="计划名称" width="150"></el-table-column>
    <el-table-column property="type" label="计划性质" width="100"></el-table-column>
    <el-table-column property="time" label="计划时间" width="400"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
       <el-button
          size="mini"
          type="success"
          @click="toLocalSlect(scope.$index,scope.row)" v-if="!cloudPlan[scope.$index].selected">选择</el-button>
       <el-button
          size="mini"
          type="warning"
          @click="CancleToLocalSlect(scope.$index,scope.row)" v-if="cloudPlan[scope.$index].selected">取消</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-button type="primary" @click="downToLocalConfirm=true" style="margin-left:40%;margin-top:2%">传送到本地</el-button>
  </el-dialog>




  <el-dialog
  title="同步云端"
  :visible.sync="syncToCloudConfirm"
  width="30%">
  <span>所选计划同步到云端之后将会覆盖云端原有同名计划，同时会清空计划状态以及计划成员，确认进行同步操作吗</span>
  <span slot="footer" class="dialog-footer">
    <el-button @click="syncToCloudConfirm = false">取 消</el-button>
    <el-button type="primary" @click="doCloudSync" :loading="syncToCloudConfirm_loading">同 步</el-button>
  </span>
  </el-dialog>   


  <el-dialog
  title="传送本地"
  :visible.sync="downToLocalConfirm"
  width="30%">
  <span>云端计划传送到本地之后将会覆盖本地原有同名计划，确认进行传送操作吗</span>
  <span slot="footer" class="dialog-footer">
    <el-button @click="syncToLocal = false">取 消</el-button>
    <el-button type="primary" @click="doLocalDown">传 送</el-button>
  </span>
  </el-dialog>   

  


  <!-- 计划同步通知功能part -->
   
  <el-dialog title="选择云端计划" 
   :visible.sync="syncInformedPlanChoose"
   top='10vh'
   width='90%'>

  <el-table :data="cloudPlan" height="350px">
    <el-table-column property="name" label="计划名称" width="150"></el-table-column>
    <el-table-column property="type" label="计划性质" width="100"></el-table-column>
    <el-table-column property="time" label="计划时间" width="400"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
       <el-button
          size="mini"
          type="success"
          @click="defineContent(scope.$index,scope.row)" >选择</el-button>

      </template>
    </el-table-column>
  </el-table>
  </el-dialog>

  <el-dialog
  title="通知方式"
  :visible.sync="syncInformedTypeChoose"
  width="30%">

  <div class="typeChoose" @click="doEmailInform">
  <img class="typeImg" src="../../assets/sync_email.svg" />
  <span>邮箱通知</span>
  </div>

  <div class="typeChoose" @click="$message({
                                    message:'暂未开放',
                                    type:'warning'
                                  })">

  <img class="typeImg" src="../../assets/sync_message.svg" />
  <span>短信通知</span>
  </div>

  <div style="clear:both"></div>
  </el-dialog>
   
  <el-dialog
  :lock-scroll="false"
  title="自定义通知内容"
  :visible.sync="defineInfomedContent"
  width="50%"
  top="5vh"
  v-if="thisCloudPlan!=null">

    <el-descriptions title="计划开始" v-if="pro_index==0">
    <el-descriptions-item label="开始时间">
    {{thisCloudPlan.process[pro_index].startDate+thisCloudPlan.process[pro_index].startTime}}
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions title="任务信息" :column="1"  v-if="pro_index!=0&&pro_index!=thisCloudPlan.process.length-1">
    
    <el-descriptions-item label="任务名称">{{thisCloudPlan.process[pro_index].pro_name}}</el-descriptions-item>
    
    <el-descriptions-item label="开始时间">
    {{thisCloudPlan.process[pro_index].startDate+thisCloudPlan.process[pro_index].startTime}}
    </el-descriptions-item>
    
    <el-descriptions-item label="结束时间">
    {{thisCloudPlan.process[pro_index].endDate+thisCloudPlan.process[pro_index].endTime}}
    </el-descriptions-item>
    
    <el-descriptions-item label="描述">
    <el-input type="textarea" v-model="thisCloudPlan.process[pro_index].pro_text" :readonly="true"></el-input>
    </el-descriptions-item>

    </el-descriptions>

    <el-descriptions title="计划结束" v-if="pro_index==thisCloudPlan.process.length-1">
    <el-descriptions-item label="结束时间">
    {{thisCloudPlan.process[pro_index].startDate+thisCloudPlan.process[pro_index].startTime}}
    </el-descriptions-item>
    </el-descriptions>

    <div v-if="pro_index==0">
    <span >计划开始通知内容</span>
    <el-input type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    </div>


    <div v-if="pro_index!=0&&pro_index!=thisCloudPlan.process.length-1">
    <span>任务开始通知内容</span>
    <el-input  type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    <span >任务结束通知内容</span>
    <el-input type="textarea" v-model="schedule[pro_index].endContent" placeholder="请输入通知内容"></el-input>
    </div>

    <div v-if="pro_index==thisCloudPlan.process.length-1">
    <span >计划结束通知内容</span>
    <el-input type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    </div>


  <el-pagination
  style="margin-top:3%"
  v-if="thisCloudPlan!=null"
  background
  layout="prev, pager, next"
  :page-size="1"
  :total="thisCloudPlan.process.length"
  @current-change="informedContent_index">
  </el-pagination>
   
  <el-button type="primary" @click="syncInformedTypeChoose=true" style="margin-top:3%">确认提交</el-button>
  </el-dialog>


 
 <!-- 修改账户信息part -->
 
  <el-dialog
  :visible.sync="modifyUserInfo"
  width="600px"
  :show-close="false"
  :close-on-click-modal="false"
  :close-on-press-escape="false"
  title="用户信息" >

  <el-descriptions :column="1" >
  <el-descriptions-item label="邮箱">
  <span v-if="!change">{{$store.state.user}}</span>
  <el-input v-if="change" placeholder="请输入新邮箱" v-model="user" size="mini"></el-input>
  </el-descriptions-item>
  <br>
   <el-descriptions-item label="手机">
  <span v-if="!change">{{$store.state.phone}}</span>
  <el-input v-if="change" placeholder="请输入新手机号" v-model="phone" size="mini"></el-input>
  </el-descriptions-item>
  <br>
   <el-descriptions-item label="姓名">
  <span v-if="!change">{{$store.state.user_name}}</span>
  <el-input v-if="change" placeholder="请输入新姓名" v-model="user_name" size="mini"></el-input>
  </el-descriptions-item>
  <br>
  <el-descriptions-item label="密码">
  <span v-if="!change">{{$store.state.password}}</span>
  <el-input v-if="change" placeholder="请输入新密码" v-model="password" size="mini"></el-input>
  </el-descriptions-item>
  </el-descriptions>
  
  <el-button type="success" v-if="!change" @click="change=!change" >修改</el-button>
  <el-button type="warning"  @click="modifyUserInfo=false;change=false" >取消</el-button>
  <el-button type="success" v-if="change"  @click="submitChange()"  :loading="modifying">确认修改</el-button>

  </el-dialog>
   

  
  <!-- 删除用户计划part -->

  <el-dialog title="删除计划" 
   :visible.sync="deleteUserPlan"
   top='10vh'
   width='90%'>

   <el-tabs  v-model="delete_Db" v-if="$store.state.login">
    <el-tab-pane label="本地计划" name="local"></el-tab-pane>
    <el-tab-pane label="云端计划" name="cloud"></el-tab-pane>
   </el-tabs>

  
  <el-table  :data="cloudPlan" height="350px" v-if="delete_Db=='cloud'&&$store.state.login" >
    <el-table-column property="name" label="计划名称" width="150"></el-table-column>
    <el-table-column property="type" label="计划性质" width="100"></el-table-column>
    <el-table-column property="time" label="计划时间" width="400"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
       <el-button
          size="mini"
          type="danger"
          @click="doDeleteUserPlan(scope.$index,scope.row)" >删除</el-button>
    </template>
    </el-table-column>
  </el-table>

  <el-table  :data="plan" height="350px" v-if="delete_Db=='local'||!$store.state.login">
    <el-table-column property="name" label="计划名称" width="150"></el-table-column>
    <el-table-column property="type" label="计划性质" width="100"></el-table-column>
    <el-table-column property="time" label="计划时间" width="400"></el-table-column>
    <el-table-column>
    <template slot-scope="scope">
       <el-button
          size="mini"
          type="danger"
          @click="doDeleteUserPlan(scope.$index,scope.row)">删除</el-button>

    </template>
    </el-table-column>
  </el-table>



  </el-dialog>
  
    </el-card>

  </div>

</template>

<script>
var database =require('nedb')
var db=[]
var cloudDb=[]
export default {
  inject:['reload'],
  async created(){
    let that=this

    //获取本地仓库中的数据
      db=new database({
        filename:'./data/main/local.db',
        autoload:true
        })

      cloudDb=new database({
        filename:'./data/main/cloud.db',
        autoload:true
        })

        await db.find({type:"planItemTest2"},function(err,Docs){
                   if(Docs.length!=0){
                      for(let i=0;i<Docs.length;i++){
                        that.plan.push({
                          name:Docs[i].plan.name,
                          type:Docs[i].plan.type,
                          time:Docs[i].plan.startDate+Docs[i].plan.startTime+" 至 "
                                 +Docs[i].plan.endDate+Docs[i].plan.endTime,
                          selected:false
                        })
                      }
                      that.planFullData=JSON.parse(JSON.stringify(Docs))
                   }
                })

        await db.find({type:'planItemNotes'},function(err,Docs){
                   for(let i=0;i<Docs.length;i++){
                     delete Docs[i]._id
                     that.planLogFullData.set(Docs[i].plan,Docs[i])
                   }
       })     



       await cloudDb.find({type:"planItemTest2"},function(err,Docs){
                   if(Docs.length!=0){
                      for(let i=0;i<Docs.length;i++){
                        that.cloudPlan.push({
                          name:Docs[i].plan.name,
                          type:Docs[i].plan.type,
                          time:Docs[i].plan.startDate+Docs[i].plan.startTime+" 至 "
                                 +Docs[i].plan.endDate+Docs[i].plan.endTime,
                          selected:false
                        })
                      }
                      that.cloudPlanFullData=JSON.parse(JSON.stringify(Docs))
                   }
                })

        await cloudDb.find({type:'planItemNotes'},function(err,Docs){
                   for(let i=0;i<Docs.length;i++){
                     delete Docs[i]._id
                     that.cloudPlanLogFullData.set(Docs[i].plan,Docs[i])
                   }
        })     
        
    
  },

  data(){
      return {

          //云端同步以及传送本地
          plan:[],
          planFullData:[],
          planLogFullData:new Map(),

          cloudPlan:[],
          cloudPlanFullData:[],
          cloudPlanLogFullData:new Map(),

          syncToCloud:false,
          downToLocal:false,

          syncToCloudConfirm:false,
          syncToCloudConfirm_loading:false,
          
          downToLocalConfirm:false,
          downToLocalConfirm_loading:false,

           
          //云端同步通知功能
          syncInformedTypeChoose:false,
          syncInformedPlanChoose:false,
          defineInfomedContent:false,
          pro_index:0,
          thisCloudPlan:null,
          schedule:[],


          //修改账户信息
          modifyUserInfo:false,
          change:false,
          user:this.$store.state.user,
          phone:this.$store.state.phone,
          password:this.$store.state.password,
          user_name:this.$store.state.user_name,
          modifying:false,

          //删除用户计划
          deleteUserPlan:false,
          delete_Db:'local'
      }
  },
  methods:{

    toContacts(){
      if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }
      this.$router.push('/contacts')
    },

    //同步上传
    async doCloudSync(){
        if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }
        
        let that=this
        let uploadPlan=[]
        let uploadPlanLog=[]
        this.syncToCloudConfirm_loading=true

        for(let i=0;i<this.plan.length;i++){
          if(this.plan[i].selected){
            delete this.planFullData[i]._id
            uploadPlan.push(JSON.parse(JSON.stringify(this.planFullData[i])))
            if(that.planLogFullData.has(this.plan[i].name)){
              uploadPlanLog.push(JSON.parse(JSON.stringify(that.planLogFullData.get(this.plan[i].name))))
            }else{
              uploadPlanLog.push({
                 type:'planItemNotes',
                 plan:that.plan[i].name,
                 dailyNotes:{}
              })
            }
          }
        }

        uploadPlan.forEach(function(item,index){
          item.plan.member=[]
          item.plan.manager={
            name:that.$store.state.user_name,
            email:that.$store.state.user,
            phone:that.$store.state.phone,
            host:true,
            id:0,
            user_id:that.$store.state.user_id
          }
          item.plan.member.push(item.plan.manager)
          item.process.forEach(function(pro,i){
            if(i!=0&&i!=item.process.length-1){
              pro.proMem=[]
              pro.status=0
            }
          })
        })

        let params={
          plan:JSON.stringify(uploadPlan),
          planLog:JSON.stringify(uploadPlanLog),
          user_id:that.$store.state.user_id
        }

       

        this.$http.post(that.$store.state.contextUrl+'/uploadPlan',that.$qs.stringify(params))
        .then(function(res){

        if(that.$store.state.login){
          that.$http.post(that.$store.state.contextUrl+'/downloadPlan',that.$qs.stringify({
          user_id:that.$store.state.user_id
        })).then(function(res){
          cloudDb.remove({}, { multi: true }, function (err, numRemoved) {})
          let cloudPlanArray=res.data
          for(let i=0;i<cloudPlanArray.length;i++){
             cloudDb.insert(JSON.parse(cloudPlanArray[i].plan_text))
             cloudDb.insert(JSON.parse(cloudPlanArray[i].planLog_text)) 
          }
          
          that.$message({
              message:'同步成功',
              type:'success'
            })

          that.syncToCloud=false
          that.syncToCloudConfirm=false
          that.syncToCloudConfirm_loading=false
          that.plan.forEach(function(item){
          item.selected=false
          })

        })
      }
    })
  },

      
      //传送本地
      doLocalDown(){
          
          if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }

          for(let i=0;i<this.cloudPlan.length;i++){
          let that=this
 
          if(this.cloudPlan[i].selected){
            
            delete this.cloudPlanFullData[i]._id
            db.remove({type:"planItemTest2","plan.name":that.cloudPlan[i].name},{},function(err, numRemoved){})
            db.remove({type:"planItemNotes",plan:that.cloudPlan[i].name},{},function(err, numRemoved){})
            
            db.insert(this.cloudPlanFullData[i])
            
            if(this.cloudPlanLogFullData.has(this.cloudPlan[i].name)){
              db.insert(this.cloudPlanLogFullData.get(this.cloudPlan[i].name))
            }
          }
        }

        this.$message({
          message:'成功传送到本地',
          type:'sucess'
        })
      },

      SyncToCloud(){
        if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }
        this.syncToCloud=true
      },

      DownToLocal(){
         if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }
        this.downToLocal=true
      },

      changeUserHead(){
          this.$store.state.userCenterHeadIndex=
          this.$store.state.userCenterHeadIndex==3?
          0:this.$store.state.userCenterHeadIndex+1
      },
      toCloudSlect(select,row){
        this.plan[select].selected=true
      },
      toLocalSlect(select,row){
        this.cloudPlan[select].selected=true
      },
      CancleToCloudSlect(select,row){
        this.plan[select].selected=false
      },
      CancleToLocalSlect(select,row){
        this.cloudPlan[select].selected=false
      },


      //云端同步通知功能part
      
      clickSyncInformPart(){
        if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return
        }
        this.syncInformedPlanChoose=true
      },

      informedContent_index(page){
        this.pro_index=page-1
      },
      defineContent(select,row){
        this.schedule=[]
        this.thisCloudPlan=this.cloudPlanFullData[select]
        

        let that=this

        this.thisCloudPlan.process.forEach(function(item,i){
          if(i==0){
            that.schedule.push({
              start:item.startDate+item.startTime,
              startContent:'您的计划“'+that.thisCloudPlan.plan.name+"”"+"已到预定开始时间——"+
              item.startDate+item.startTime,
              end:null,
              endContent:null
            })
          }else if(i==that.thisCloudPlan.process.length-1){
             that.schedule.push({
              start:item.startDate+item.startTime,
              startContent:'您的计划“'+that.thisCloudPlan.plan.name+"”"+"已到预定结束时间——"+
              item.startDate+item.startTime,
              end:null,
              endContent:null
            })
          }else{
             that.schedule.push({
              start:item.startDate+item.startTime,
              
              startContent:'您的计划“'+that.thisCloudPlan.plan.name+"”"+"已到"+"任务"+
              item.id+":“"+item.pro_name+"”的开始时间——"+item.startDate+item.startTime,
              
              end:item.endDate+item.endTime,
              
              endContent:'您的计划“'+that.thisCloudPlan.plan.name+"”"+"已到"+"任务"+
              item.id+":“"+item.pro_name+"”的结束时间——"+item.endDate+item.endTime,

            })
          }
        })
        
        if( this.thisCloudPlan.plan.informed){
          this.$message({
            message:'该计划的同步通知已开启！',
            type:'error'
          })
          return 
        }
        this.defineInfomedContent=true
      },
      
      doEmailInform(){

         if(this.thisCloudPlan.plan.informed){
          this.$message({
            message:'该计划的同步通知已开启！',
            type:'error'
          })
          return 
        }else{
          this.thisCloudPlan.plan.informed=true
        }

        let that=this
        this.$http.post(this.$store.state.contextUrl+"/emailSync",that.$qs.stringify({
          user_id:that.$store.state.user_id,
          user_name:that.$store.state.user_name,
          schedule:JSON.stringify(that.schedule)
        })).then(function(res){
          if(res.data=='success'){
            that.$message({
              message:'已成功开启邮箱同步通知功能，请注意查收邮箱信息',
              type:'success'
            })


          let uploadPlanLog=[]
          if(that.planLogFullData.has(that.thisCloudPlan.plan.name)){
              uploadPlanLog.push(that.planLogFullData.get(that.thisCloudPlan.plan.name))
            }else{
              uploadPlanLog.push({
                 type:'planItemNotes',
                 plan:that.thisCloudPlan.plan.name,
                 dailyNotes:{}
            })
          }
           

       
        cloudDb.update({type:"planItemTest2","plan.name":that.thisCloudPlan.plan.name},
          that.thisCloudPlan,
          function(err, numReplaced){
                if(numReplaced==1){
                that.$message({
                    message:'保存成功',
                    type:'success'
                 })
                }
             })

       that.$http.post(that.$store.state.contextUrl+'/uploadPlan',that.$qs.stringify({
          user_id:that.$store.state.user_id,
          plan:JSON.stringify([that.thisCloudPlan]),
          planLog:JSON.stringify(uploadPlanLog)
        })).then(function(res){
          that.reload()
          if(res.data=='fail'){
            that.$message({
              message:'云端同步失败，请检查网络设置',
              type:'error'
            })
          }
          that.$store.state.syncing=false
        })

          }else{
            that.$message({
              message:'出现错误',
              type:'error'
            })
          }
        })
      },

      //修改账户 part
      clickModifyUserInfoPart(){
        if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return
        }
        this.modifyUserInfo=true
      },
      
      submitChange(){
        let that=this
        this.modifying=true
        this.$http.post(that.$store.state.contextUrl+'/changeUserInfo',that.$qs.stringify({
          id:that.$store.state.user_id,
          email:that.user,
          phone:that.phone,
          password:that.password,
          user_name:that.user_name
        })).then(function(res){
          if(res.data=='success'){
            that.$message({
              message:'修改成功',
              type:'success'
            })
            that.reload()
          }else{
             that.$message({
              message:'修改失败',
              type:'error'
            })
          }
          that.modifying=false
          that.change=!that.change

          that.$store.state.user=that.user
          that.$store.state.phone=that.phone
          that.$store.state.user_name=that.user_name
          that.$store.state.password=that.password
        })
      },

      //删除用户计划
      doDeleteUserPlan(select,row){
        let that=this
        if(!this.$store.state.login||this.delete_Db=='local'){
          db.remove({type:'planItemTest2',"plan.name":that.plan[select].name},{},function(error,num){
            db.remove({type:'planItemNotes',plan:that.plan[select].name},{},function(error,num){
              that.reload()
              that.$message({
                message:'删除成功',
                type:'error'
              })
            })
          })
        }else{
            cloudDb.remove({type:'planItemTest2',"plan.name":that.cloudPlan[select].name},{},function(error,num){
            cloudDb.remove({type:'planItemNotes',plan:that.cloudPlan[select].name},{},function(error,num){
              that.$http.post(that.$store.state.contextUrl+'/deletePlan',that.$qs.stringify({
                user_id:that.$store.state.user_id,
                plan_name:that.cloudPlan[select].name
              })).then(function(res){
                that.reload()
                if(res.data=='success'){
                  that.$message({
                    message:'删除成功',
                    type:'error'
                  })
                }
              })
              
            })
          })
        }
      },
      clickDeleteCard(){
        this.deleteUserPlan=true
      },
      hasNewMsg(){
        let hasNew=false
        this.$store.state.message.forEach(function(item,index){
          if(hasNew){
            return
          }
          if(item.read==0){
            hasNew=true
          }
        })
        return hasNew
      },

      //查看消息详情
      toMessage(){
        if(!this.$store.state.login){
          this.$message({
            message:'请先进行登录',
            type:'warning'
          })
          return 
        }
        this.$router.push('/message')
      }
  }
}
</script>
<style>

</style>
<style scoped>

.main{
    height:90%;
    overflow-y:hidden;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.basic-card{
    height:100px;
    width: 99%;
    margin: auto;
    margin-top:0.4%
}
.basic-card >>> .el-card__body{
  padding: 0.6%;
}
.userHead{
    height:70px;
    margin-top:5px;
    margin-left:5px;
    float:left
}
.userHead:hover{
    cursor: pointer;
}
.userID{
    font-size: 20px;
    color: rgb(33, 140, 211);
    margin-left:2%;
}

.userID:hover{
  cursor: pointer;
}

.userID_{
    width:100%;
    font-size: 15px;
    color: rgb(5, 5, 5);
    margin-left:2%;
}

.userIdInfo{
    float: left;
    width: 30%;
    margin-left:2%;
}

.userIdInfo_btn{
    margin-top: 6%;
}

.sync{
    height:7%;
    width:7%;
    margin-right:1%;
    float:right
}

.sync:hover{
    cursor: pointer;
}

.function-card{
    height:70px;
    width: 99%;
    margin: auto;
}

.function-card:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.function{
    font-size: 15px;
    color: rgb(33, 140, 211);
}

.typeChoose{
  float:left;
  width: 25%;
  margin-left: 15%;
  text-align: center;
}

.typeImg{
  width: 100%;
}

.typeChoose:hover{
  cursor: pointer;
  background-color: rgb(230, 246, 255);
}


</style>