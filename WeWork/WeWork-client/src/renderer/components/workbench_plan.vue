<template>
<div class="main">
<div class="main" v-if="plan!=null">



<el-card class="bodyCard">
<el-page-header @back="goBack" style="margin-bottom:20px"></el-page-header>
<el-descriptions :title="plan.plan.name" :column="2">
    <el-descriptions-item label="状态"><el-tag size="mini" :type="status">{{status=='danger'?'未开始'
    :(status=='warning'?'进行中':'已结束')}}</el-tag></el-descriptions-item>
    
    <el-descriptions-item :label="plan.plan.type=='个人计划'?'执行人':'负责人'">
    <el-tag size="mini" >{{plan.plan.type=='个人计划'?plan.plan.excutor.name:plan.plan.manager.name}}</el-tag>
    </el-descriptions-item>
    

    <el-descriptions-item label="开始时间">{{
        plan.plan.startDate+" "+plan.plan.startTime}}
    </el-descriptions-item>
    <el-descriptions-item label="结束时间">{{
        plan.plan.endDate+" "+plan.plan.endTime}}
    </el-descriptions-item>

</el-descriptions>

   <el-descriptions :column="1" v-if="plan.plan.type=='小组计划'"><el-descriptions-item label="小组成员">
      <div v-if="plan.plan.type=='小组计划'&&plan.plan.member.length>0">
      <el-tag style="margin-right:5px" size="mini" :type="item.host?'':'success'" v-for="(item,i) in plan.plan.member" :key="i">{{item.name}}</el-tag>
      </div>

      <div v-if="plan.plan.type=='小组计划'&&plan.plan.member.length==0">
      <span>无</span>
      </div>      
   </el-descriptions-item>
   </el-descriptions>


 <el-tabs v-model="activeName">
    <el-tab-pane label="计划时间线" name="plan_timeLine"></el-tab-pane>
    <el-tab-pane label="计划描述" name="plan_text"></el-tab-pane>
    <el-tab-pane label="计划任务" name="plan_pro"></el-tab-pane>
    <el-tab-pane label="计划参与人" name="plan_member"></el-tab-pane>
    <el-tab-pane label="更多功能" name="plan_function"></el-tab-pane>
    </el-tabs>
   

   <!-- 计划描述 -->

   <div v-if="activeName=='plan_text'">
   <el-card>
   <el-descriptions  direction="vertical">
   <el-descriptions-item label="描述">
   <span v-if="!planTextEdit">{{plan.plan.desc}}</span>
   <el-input type="textarea" v-if="planTextEdit" v-model="plan.plan.desc"></el-input>
   </el-descriptions-item>
   </el-descriptions>
   <el-button type="text" v-if="!planTextEdit" @click="planTextEdit=!planTextEdit">编辑</el-button>
   <el-button type="text" v-if="planTextEdit" @click="commitPlanTextEdit" >确认</el-button>
   </el-card>
   </div>


   <!-- 计划时间线 -->
   
   <el-card v-if="activeName=='plan_timeLine'">
   <el-timeline >

    <el-timeline-item :timestamp="dateFormat(item.date)" placement="top" v-for="(item,i) in timeLine" :key="i">
      <el-card>

       <el-table
       :show-header="false"
       :data="item.event"
        style="width: 100%">
       <el-table-column  prop="time"></el-table-column>
       <el-table-column  prop="str"></el-table-column>
       <el-table-column >
       <template slot-scope="scope">
       <el-tag v-if="scope.row.status=='开始'" type="success" size="medium">开始</el-tag>
       <el-tag v-if="scope.row.status=='结束'"  type="warning" size="medium">结束</el-tag>
       </template>
       </el-table-column>
       </el-table>

      </el-card>
    </el-timeline-item>

  </el-timeline>
   </el-card>


   
  <!-- 计划任务 -->
  <div v-if="activeName=='plan_pro'">
  <el-card >
  <el-collapse  v-model="plan_pro" accordion >
  <div v-for="(item,i) in plan.process" :key="i">
  <el-collapse-item  :title="'任务'+(i)+'：'+item.pro_name" :name="i+1" v-if="i!=0&&i!=plan.process.length-1" >
    
    <div>任务时间：{{item.startDate+" "+
    item.startTime+" 至 "+item.endDate+" "+item.endTime}}
    </div>
    <br>
    <div>任务描述：{{item.pro_text}}</div>
    <br>
    <div>任务状态：
    <el-tag size="mini" :type="item.status==0?'danger':(item.status==1?'warning':'success')">
    {{item.status==0?'未开始':(item.status==1?'进行中':'已完成')}}
    </el-tag>
    </div>

    <br>
    <el-descriptions :column="1" v-if="plan.plan.type=='小组计划'">
    <el-descriptions-item label="任务参与人">

      <div v-if="plan.plan.type=='小组计划'&&item.hasOwnProperty('proMem')&&item.proMem.length>0">
      <el-tag style="margin-right:5px" size="mini" type="success" v-for="(item,i) in item.proMem" :key="i">
      {{item.name}}
      </el-tag>
      </div>

      <div v-if="!item.hasOwnProperty('proMem')||item.proMem.length==0">
      <span>未指定</span>
      </div>     

    </el-descriptions-item>
    </el-descriptions>

    
    
    <!-- 小组计划可以添加任务参与人 -->
    <br>
    <el-button @click="addProMem(i)" type="text" v-if="plan.plan.type=='小组计划'">编辑任务参与人</el-button>
    
    </el-collapse-item>
    
    </div>
    </el-collapse>
    <el-button type="text" style="font-size:15px" @click="showPlanDetail">添加任务或修改任务</el-button>
    </el-card>
    </div>


  <el-dialog width="650px" title="任务参与人" v-if="plan.plan.member.length!=0"  :visible.sync="addProMemVisible"
           :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" center>

  <el-transfer 
   style="text-align: left; display: inline-block"  
   v-model="proMem" 
   :data="totalMem"
   :titles="['备选', '已选']">
  </el-transfer>
  
  <div slot="footer" class="dialog-footer">
    <el-button @click="cancleAddProMem">取 消</el-button>
    <el-button type="primary" @click="confirmAddProMem">确 定</el-button>
  </div>
  </el-dialog>


  <!-- 计划参与人 -->
  <el-card v-if="activeName=='plan_member'">
  <el-descriptions v-if="plan.plan.type=='小组计划'" direction="vertical">
  <el-descriptions-item label="参与人"> 

  

  <el-tag  
  v-for="(item,i) in plan.plan.member" 
  :key="i"
  :type="item.host?'':'success'" 
  @click.native="memberInfo(i)"  
  class="pointer" 
  style="font-size:15px;margin-right:5px">
  {{item.name}}
  </el-tag>


  <el-tag @click.native="addMember" type="warning" class="pointer" style="font-size:15px">+</el-tag>
  </el-descriptions-item>
  </el-descriptions>
  <el-button  type="primary" @click="openChatRoom">成员交流室</el-button>
  <div v-if="plan.plan.type=='个人计划'">
  <el-empty description="个人计划无计划参与者，转换为小组计划可添加计划参与者">
  <el-button type="primary" @click="confirmTypeChange">转换</el-button>
  </el-empty>
  </div>

  </el-card>


<el-dialog title="添加参与人" v-if="plan.plan.member.length!=0"  :visible.sync="addMemberVisible"
           :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
  <el-form label-width="auto">
    <el-form-item label="名字" >
      <el-input v-model="addMemberTemp.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" >
       <el-input v-model="addMemberTemp.email" autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="手机" >
       <el-input v-model="addMemberTemp.phone" autocomplete="off"></el-input>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="cancleAddMem">取 消</el-button>
    <el-button type="primary" @click="confirmAddMem">确 定</el-button>
  </div>
</el-dialog>

<!-- 参与人信息 -->
<el-dialog top="4vh" :title="plan.plan.member[memberSlected].name" v-if="memberSlected!=-1"  :visible.sync="memberInfoVisible">
<el-card>
<div>
<el-tag  type="primary" style="margin-right:5px">{{plan.plan.member[memberSlected].name}}
</el-tag>
<el-tag  type="warning" v-if="plan.plan.member[memberSlected].host">负责人
</el-tag>
</div>
<br>
<el-descriptions :column="1">
    <el-descriptions-item label="邮箱">   
    {{plan.plan.member[memberSlected].email}}
    </el-descriptions-item>

    <el-descriptions-item label="手机">   
    {{plan.plan.member[memberSlected].phone.match(/^\s*$/)?'未设置':plan.plan.member[memberSlected].phone}}
    </el-descriptions-item>
</el-descriptions>
</el-card>

<el-card>
<div slot="header">
已分配任务
</div>
<el-table
      height="150px"
      :show-header="false"
      :data="memPro"
      style="width: 100%">
      <el-table-column prop="id" label="任务序号"></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>
</el-table>
</el-card>
<el-card>
<el-button v-if="!plan.plan.member[memberSlected].host" @click="deleteMember" type="danger">删除</el-button>
<el-button @click="editMem" type="warning">修改</el-button>
<el-button v-if="!plan.plan.member[memberSlected].host" @click="information" type="success">发送通知</el-button>
<el-button v-if="!plan.plan.member[memberSlected].host" @click="transPlan" type="primary">邮箱同步通知</el-button>
</el-card>
</el-dialog>

<el-dialog title="修改信息" v-if="plan.plan.member.length!=0"  :visible.sync="editMemberVisible"
           :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
  <el-form label-width="auto">
    <el-form-item label="名字" >
      <el-input v-model="editMemberTemp.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" >
       <el-input v-model="editMemberTemp.email" autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="手机" >
       <el-input v-model="editMemberTemp.phone" autocomplete="off"></el-input>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="cancleEditMem">取 消</el-button>
    <el-button type="primary" @click="confirmEditMem">确 定</el-button>
  </div>
</el-dialog>

  
  <!-- 更多功能 -->
  <div v-if="activeName=='plan_function'">
  <el-card>
  <el-row :gutter="20">
  <el-col :span="6">
  <el-card class="pointer" shadow="hover" style="text-align:center" @click.native="showPlanDetail">
  <div style="width:50%;margin-left:25%">
  <img src="../assets/planEdit.svg" style="width:100%" />
  <span>计划编辑</span>
  </div>
  </el-card>
  </el-col>

  <el-col :span="6">
  <el-card class="pointer" shadow="hover" style="text-align:center" @click.native="showPlanLog">
  <div style="width:50%;margin-left:25%">
  <img src="../assets/planLog.svg" style="width:100%" />
  <span >计划日志</span>
  </div>
  </el-card>
  </el-col>

  <el-col :span="6">
  <el-card class="pointer" shadow="hover" style="text-align:center" @click.native="toPlanNotes">
  <div style="width:50%;margin-left:25%">
  <img src="../assets/planNote.svg" style="width:100%" />
  <span >计划总结</span>
  </div>
  </el-card>
  </el-col>

  <el-col :span="6">
  <el-card class="pointer" shadow="hover" style="text-align:center" @click.native="toPDF">
  <div style="width:50%;margin-left:25%">
  <img src="../assets/planOutput.svg" style="width:100%" />
  <span >计划导出</span>
  </div>
  </el-card>
  </el-col>
  </el-row>

  </el-card>
  </div>

</el-card>
</div>

<el-dialog top="5vh" title="发送通知" width="600px" v-if="informationVisible"  :visible.sync="informationVisible">
 <el-card>
 <el-tabs v-model="infoMode">
    <el-tab-pane label="任务通知" name="proInfo"></el-tab-pane>
    <el-tab-pane label="自定义通知" name="otherInfo"></el-tab-pane>
 </el-tabs>
 <div v-if="infoMode=='proInfo'">
 <el-table
      height="150px"
      :show-header="false"
      :data="memPro"
      style="width: 100%">
      <el-table-column prop="id" label="任务序号"></el-table-column>
      <el-table-column prop="name" label="任务名称"></el-table-column>

      <el-table-column>
      <template slot-scope="scope">
        <el-button @click="selectInfoPro(scope.row)" size="mini" type="success" v-if="!scope.row.selected">选择</el-button>
        <el-button @click="removeInfoPro(scope.row)" size="mini" type="warning" v-if="scope.row.selected">取消</el-button>
      </template>
      </el-table-column>

</el-table>
 </div>
 </el-card>
 <el-card>
 <el-input placeholder="发送人留言..." type="textarea" v-model="extraMsg"></el-input>
 </el-card>
 <el-card>
 <el-button type="primary" @click="confirmSendInfo">发 送</el-button>
 </el-card> 
</el-dialog>

      
      <!-- 成员聊天交流室 -->
      <!--<el-dialog 
       top="5vh" 
       title="成员交流室" 
       width="900px" 
       v-if="chatRoomVisible"  
       :visible.sync="chatRoomVisible">
      <el-row :gutter="2">
        <el-col :span="6">
        <el-card style="height:500px;overflow-y:auto">
        <div slot="header">成员列表</div>
        <el-table :data="plan.plan.member"
                  :show-header="false"
                  class="jobTable">
        <el-table-column >
        <template slot-scope="scope">
        <el-tag :type="scope.row.host?'':'success'" size="medium">{{scope.row.name}}</el-tag>
        </template>
        </el-table-column>
        </el-table>
        </el-card>
        </el-col>
        <el-col :span="18" >
        <el-card style="height:500px">

        <el-card style="height:320px;overflow-y:auto" shadow="never">
        <div style="margin-bottom:20px">
        <div style="margin-bottom:5px">
        2021/10/19 10:21:20
        </div>
        <div style="display:inline-block;float:left"> <el-tag type="success" size="medium">曹逸</el-tag> </div>
        <div class="box" style="float:left">fuck off</div>
        <div style="clear:both"></div>
        </div>
        
        <div style="margin-bottom:20px">
        <div style="margin-bottom:5px;width:100%">
        <span style="float:right">2021/10/19 10:21:20</span>
        <div style="clear:both"></div>
        </div>
        <div style="display:inline-block;float:right"> <el-tag type="success" size="medium">曹逸</el-tag> </div>
        <div class="box" style="float:right">fuck off</div>
        <div style="clear:both"></div>
        </div>

        </el-card>
        

        <el-card style="height:130px" shadow="never">
        <el-input v-model="IM_text" type="textarea" style="margin-bottom:5px"></el-input>
        <el-button @click="sendIM" plain type="primary" style="float:right">发送</el-button>
        <el-button plain type="warning" style="float:right;margin-right:5px">清空</el-button>
        </el-card>
        </el-card>
        </el-col>
      </el-row>

      </el-dialog>-->

      <chat-room :visible.sync="chatRoomVisible" :IM="IM"  :plan="plan"></chat-room>


</div>
</template>

<script>
import chatRoom from './chatRoom.vue'
var database =require('nedb')
var db=[]
export default {
  components: { chatRoom },
    created(){
        let that=this
        db=new database({
        filename:that.$store.state.dbName,
        autoload:true
        })
        db.find({type:"planItemTest2",'plan.name':that.$route.params.name},function(err,Docs){
                   if(Docs.length!=0){
                     that.plan=JSON.parse(JSON.stringify(Docs[0]))
                     that.status=that.planStatus()


                     //getTimeLine schedule
                     that.plan.process.forEach(function(item,i){
                         if(!that.timeLineMap.has(item.startDate)){
                             that.timeLineMap.set(item.startDate,[])
                             }
                             
                          if(i!=0&&i!=that.plan.process.length-1&&!that.timeLineMap.has(item.endDate)){
                              that.timeLineMap.set(item.endDate,[])
                              }
                              
                          if(i==0){
                              that.timeLineMap.get(item.startDate).push({
                                  time:item.startTime,
                                  str:'计划',
                                  status:'开始'
                                })
                        }else if(i==that.plan.process.length-1){
                            that.timeLineMap.get(item.startDate).push({
                                time:item.startTime,
                                str:'计划',
                                status:'结束'
                                })
                            }else{
                                that.timeLineMap.get(item.startDate).push({
                                    time:item.startTime,
                                    str:'任务'+i+"："+item.pro_name,
                                    status:'开始'
                                    })
                                    that.timeLineMap.get(item.endDate).push({
                                        time:item.endTime,
                                        str:'任务'+i+"："+item.pro_name,
                                        status:'结束'         
                                        })
                                    }
                                })
                                
                                that.timeLineMap.forEach(function(value,key){
                                    value.sort((a,b)=>{return that.compareTime(a.time,b.time)})
                                    that.timeLine.push({
                                        date:key,
                                        event:value
                                    })
                                });
                                
                                that.timeLine.sort((a,b)=>{return that.compareDate(a.date,b.date)})}
                })

        
    },
    methods:{

        
        //保存修改后的计划
        save(){
            let that=this
            db.update({type:"planItemTest2","plan.name":that.plan.plan.name},that.plan,function(err, numReplaced){
                if(numReplaced==1){
                that.$message({
                    message:'保存成功',
                    type:'success'
                 })
                }
            })
            
            
            //云同步代码
            if(that.$store.state.dbName=='./cloud.db'){
                let promise=new Promise(function(resolve,reject){
                    let notes=[]
                    db.find({type:'planItemNotes',plan:that.plan.plan.name},function(err,Docs){
                        if(Docs.length!=0){
                            notes.push(Docs[0])
                        }else{
                            notes=[{
                                type:'planItemNotes',
                                plan:that.plan.plan.name,
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
                            plan:that.plan.plan,
                            process:that.plan.process
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

        goBack(){
            this.$router.push('/workbench')
        },
        
        //计划功能
        toPlanNotes(){
        let that=this
        this.$router.push({
          name:'planNote',
          params:{
            plan_name:that.plan.plan.name
          }
        })
        },

        toPDF(){
          const ipc = require('electron').ipcRenderer
          ipc.send('createPDF',this.plan)
        },

        showPlanDetail(){
        let that=this
        this.$router.push({
          name:'showPlan',
          params:{
            plan:that.plan.plan,
            process:that.plan.process
          }
        })
      },

        showPlanLog(){
            let that=this
            this.$router.push({
                name:'planLog',
                params:{
                    plan:that.plan.plan,
                    process:that.plan.process
                    }
                })
            },
        
        //添加参与人
        addMember(){
            let id=this.plan.plan.member[this.plan.plan.member.length-1].id
            this.addMemberTemp={
                name:'',
                email:'',
                phone:'',
                id:id+1
            },
            this.addMemberVisible=true
        },
        cancleAddMem(){
            this.addMemberVisible=false
        },

        confirmAddMem(){
            let that=this
            if(this.addMemberTemp.name.match(/^\s*$/g)||
               this.addMemberTemp.email.match(/^\s*$/g)){
                   this.$message({
                       message:'邮箱和名字不能为空',
                       type:'error'
                   })
                return 
            }
            //对于线上模式中的计划，需要保证参与人的账户存在
            if(this.$store.state.login){
                this.$http.post(this.$store.state.contextUrl+'/getUserId',this.$qs.stringify({
                    email:that.addMemberTemp.email
                })).then(function(res){
                    if(res.data==-1){
                        that.$message({
                            message:'该用户不存在',
                            type:'error'
                        })
                    return 
                }else{
                    
                    if(that.addMemberTemp.name!=res.data.user_name){
                        that.$message({
                            message:'新增参与人名字与该账户名字（'+res.data.user_name+"）不匹配",
                            type:'error'
                        })
                    return 
                }else{
                    that.addMemberTemp.user_id=res.data.id
                    that.plan.plan.member.push(JSON.parse(JSON.stringify(that.addMemberTemp)))
                    let id=that.plan.plan.member[that.plan.plan.member.length-1].id
                    that.addMemberTemp={
                        name:'',
                        email:'',
                        phone:'',
                        host:false,
                        id:id+1,
                    }
                    that.addMemberVisible=false
                    that.save()
                }
            }
        })
    }else{
            that.plan.plan.member.push(JSON.parse(JSON.stringify(that.addMemberTemp)))
            let id=that.plan.plan.member[that.plan.plan.member.length-1].id
            that.addMemberTemp={
                name:'',
                email:'',
                phone:'',
                host:false,
                id:id+1,
            }
            that.addMemberVisible=false
            that.save()
        }
    },
        
        memberInfo(index){
            this.memberSlected=index
            this.memberInfoVisible=true
        },

        //添加任务参与人

        addProMem(index){
            if(this.plan.plan.member.length==0){
                this.$message({
                    message:'无可选参与人',
                    type:'warning'
                })
                return
            }
            
            let that=this
            if(this.plan.process[index].hasOwnProperty('proMem')){
                let added=new Set()
                this.plan.process[index].proMem.forEach(function(item,index){
                    let l=that.totalMem.length
                    for(let i=0;i<l;i++){
                        if(added.has(that.plan.plan.member[i])){
                            continue
                        }
                        if(item.id==that.plan.plan.member[i].id){
                            that.proMem.push(i)
                            added.add(item)
                            break
                        }
                    }
                })
            }
            this.proSelected=index
            this.addProMemVisible=true
        },
        cancleAddProMem(){
            this.proMem=[]
            this.addProMemVisible=false
        },
        confirmAddProMem(){
            let map=new Map()
            this.plan.process[this.proSelected].proMem.forEach(function(item,index){
                map.set(item.id,item.status)
            })
            this.plan.process[this.proSelected].proMem=[]
            let that=this
            this.proMem.forEach(function(item,index){
                let tmp=JSON.parse(JSON.stringify(that.plan.plan.member[item]))
                if(map.has(tmp.id)){
                    tmp.status=map.get(tmp.id)
                }else{
                    tmp.status=0
                }
                that.plan.process[that.proSelected].proMem.push(tmp)
            })
            
            this.checkProStatus()
            this.addProMemVisible=false
            this.memberSlected=-1
        },

        //提交计划描述修改
        commitPlanTextEdit(){
            this.planTextEdit=!this.planTextEdit
            this.save()
        },

        planStatus(){
           let pro=this.plan.process 
           let total=pro.length-2
           let finished=0
           let isStart=false
           for(let i=1;i<pro.length-1;i++){
               if(pro[i].status==2){
                   finished++
               }
               if(pro[i].status>0){
                   isStart=true
               }
           }
           if(finished==total){
               return 'success'
           }else {
               if(isStart){
                   return 'warning'
               }else{
                   return 'danger'
               }
           }
       },
        dateFormat(date){
             let arr=date.split(/\s*[^0-9]\s*/g)
             return arr[0]+'/'+arr[1]+'/'+arr[2]
        },
        compareDate(date1,date2){
            let arr1=date1.split(/\s*[^0-9]\s*/g)
            let arr2=date2.split(/\s*[^0-9]\s*/g)
            
            let year1=parseInt(arr1[0])
            let year2=parseInt(arr2[0])

            if(year1==year2){
                let month1=parseInt(arr1[1])
                let month2=parseInt(arr2[1])
                
                if(month1==month2){
                    let day1=parseInt(arr1[2])
                    let day2=parseInt(arr2[2])

                    return day1-day2

                }else{
                    return month1-month2
                }
            }else{
                return year1-year2
            }
        },      
        compareTime(time1,time2){
            let arr1=time1.split(/\s*:\s*/g)
            let arr2=time2.split(/\s*:\s*/g)
            
            let hour1=parseInt(arr1[0])
            let hour2=parseInt(arr2[0])

            if(hour1==hour2){
                let minute1=parseInt(arr1[1])
                let minute2=parseInt(arr2[1])

                if(minute1==minute2){
                    let second1=parseInt(arr1[2])
                    let second2=parseInt(arr2[2])

                    return second1-second2
                }else{
                    return minute1-minute2
                }
            }else{
                return hour1-hour2
            }
        },

        //转换计划类型
        confirmTypeChange(){
            this.$confirm('此操作将更改计划类型为小组计划, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
                }).then(() => {
                    this.plan.plan.type="小组计划"
                    this.plan.plan.manager=this.plan.plan.excutor
                    this.save()
                }).catch(() => {
                    this.$message({
                        type: 'warning',
                        message: '已取消转换'
                });          
            });
        },

        //参与人相关操作（删除、信息修改、计划通知）
        deleteMember(){
            if(this.plan.plan.member[this.memberSlected].host){
                this.$message({
                    message:'不能删除项目发起人',
                    type:'error'
                })
                return
            }
            let mem=this.plan.plan.member[this.memberSlected]
            this.plan.plan.member.splice(this.memberSlected,1)
            this.plan.process.forEach(function(item,index){
                if(!item.hasOwnProperty('proMem')){
                    return 
                }
                let l=item.proMem.length
                for(let i=0;i<l;i++){
                    if(mem.id==item.proMem[i].id){
                        item.proMem.splice(i,1)
                        break
                    }
                }
            })
            this.save()
            this.memberSlected=-1
        },

        editMem(){
            this.editMemberTemp=JSON.parse(JSON.stringify(this.plan.plan.member[this.memberSlected]))
            this.editMemberVisible=true
        },
        cancleEditMem(){
            this.editMemberVisible=false
        },
        confirmEditMem(){
            let preMem=this.plan.plan.member[this.memberSlected]
            let thisMemStr=JSON.stringify(this.editMemberTemp)
            if(preMem.host){
                this.plan.plan.excutor=JSON.parse(thisMemStr)
                this.plan.plan.manager=JSON.parse(thisMemStr)
            }
            this.plan.plan.member[this.memberSlected]=JSON.parse(thisMemStr)
            this.totalMem[this.memberSlected]={
                key:this.memberSlected,
                label:this.plan.plan.member[this.memberSlected].name
            }
            this.plan.process.forEach(function(item,index){
                if(item.hasOwnProperty('proMem')){
                    let l=item.proMem.length
                    for(let i=0;i<l;i++){
                        if(preMem.id==item.proMem[i].id){
                            let preStatus=item.proMem[i].status
                            item.proMem[i]=JSON.parse(thisMemStr)
                            item.proMem[i].status=preStatus
                            break
                        }
                    }
                }
            })
            this.save()
            this.editMemberVisible=false
        },
         //向小组成员发送计划任务
            transPlan(){
                if(this.$store.state.user=='HelloWorld'){
                    this.$message({
                        message:'本地计划不提供计划发送功能',
                        type:'warning'
                    })
                    return
                }

            },

            information(){
                // if(!this.$store.state.login){
                //     this.$message({
                //         message:'本地计划不提供通知功能',
                //         type:'warning'
                //     })
                //     return
                // }
                this.informationVisible=true
            },
            selectInfoPro(pro){
                this.info_proSelected.push(this.plan.process[pro.id])
                pro.selected=true
            },
            removeInfoPro(pro){
                let l= this.info_proSelected.length
                for(let i=0;i<l;i++){
                    if(this.info_proSelected[i].id==pro.id){
                        this.info_proSelected.splice(i,1)
                        break
                    }
                }
                pro.selected=false
            },
            confirmSendInfo(){
                let that=this
                let acceptor_id=-1
                let acceptor_name=null
                this.$http.post(this.$store.state.contextUrl+'/getUserId',this.$qs.stringify({
                    email:that.plan.plan.member[that.memberSlected].email
                })).then(function(res){
                    if(res.data!=-1){
                        acceptor_id=res.data.id
                        acceptor_name=res.data.user_name
                        
                        if(that.plan.plan.member[that.memberSlected].name!=acceptor_name){
                            that.$message({
                                message:'参与人姓名与账户姓名（'+acceptor_name+'）不匹配,请重新设置参与人信息',
                                type:'error'
                            })
                            return 
                        }
                       
                        if(that.infoMode=='proInfo'){
                            let content={}
                            let date=new Date()
                            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
                            content.time=timeStr
                            content.pro=that.info_proSelected
                            content.plan_name=that.plan.plan.name
                            content.extraMsg=that.extraMsg
                            content.plan=that.plan.plan
                            content.accepted=false
                            that.$http.post(that.$store.state.contextUrl+'/uploadNotice',that.$qs.stringify({
                                acceptor:acceptor_id,
                                sender:that.$store.state.user_id,
                                content:JSON.stringify(content),
                                type:2,
                                read:0,
                            })).then(function(res_){
                                if(res_.data=='success'){
                                    that.$message({
                                        message:'发送成功',
                                        type:'success'
                                    })
                                    
                                    //webSocket part
                                    // let socket=that.$store.state.socket
                                    that.send(acceptor_id)   
                                    that.informationVisible=false                                   
                                }else{
                                     that.$message({
                                        message:'发送失败',
                                        type:'error'
                                    })
                                }
                            })
                        }else if(that.infoMode=='otherInfo'){
                            let content={}
                            let date=new Date()
                            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
                            content.time=timeStr
                            content.plan_name=that.plan.plan.name
                            content.extraMsg=that.extraMsg
                            content.plan=that.plan.plan
                            content.accepted=false

                            that.$http.post(that.$store.state.contextUrl+'/uploadNotice',that.$qs.stringify({
                                acceptor:acceptor_id,
                                sender:that.$store.state.user_id,
                                content:JSON.stringify(content),
                                type:4,
                                read:0,
                            })).then(function(res_){
                                if(res_.data=='success'){
                                    that.$message({
                                        message:'发送成功',
                                        type:'success'
                                    })
                                    
                                    //webSocket part
                                    // let socket=that.$store.state.socket
                                    that.send(acceptor_id)   
                                    that.informationVisible=false                
                                }else{
                                     that.$message({
                                        message:'发送失败',
                                        type:'error'
                                    })
                                }
                            })      
                        }
                    }else{
                        that.$message({
                            message:'该参与人账户不存在，请先进行注册',
                            type:'error'
                        })
                        return 
                    }
                })
            },

            //websocket: send the refresh message
            send(uid){
                if(this.$store.state.socket.readyState==1){
                    this.$store.state.socket.send("refresh?"+uid)
               }
            },
            checkProStatus(){
                let count=0
                this.plan.process[this.proSelected].proMem.forEach(function(item,index){
                    count+=item.status
                })
                if(count==0){
                    this.plan.process[this.proSelected].status=0
                }else if(count==this.plan.process[this.proSelected].proMem.length*2){
                    this.plan.process[this.proSelected].status=2
                }else{
                    this.plan.process[this.proSelected].status=1
                }
                this.save()
            },


            //成员聊天交流室
            openChatRoom(){
                let key=this.plan.plan.name+"&"+this.plan.plan.manager.user_id
                if(!this.$store.state.IM.has(key)){
                    this.$store.state.IM.set(key,[])
                }
                this.IM=this.$store.state.IM.get(key)
                this.chatRoomVisible=true
            },
    },
    data(){
        return {
            plan:null,
            status:'success',
            activeName:'plan_timeLine',

            timeLineMap:new Map(),
            timeLine:[],

            //控制计划描述编辑模式
            planTextEdit:false,

            //计划任务
            plan_pro:'1',

            //参与人
            addMemberVisible:false,
            addMemberTemp:{
                name:'',
                email:'',
                phone:''
            },
            memberInfoVisible:false,
            memberSlected:-1,
            memberInfoEditMode:false,
            memPro:[],


            //添加任务参与人
            addProMemVisible:false,
            totalMem:[],
            proMem:[],
            proSelected:-1,

            //修改参与人信息
            editMemberVisible:false,
            editMemberTemp:{
                name:'',
                email:'',
                phone:''
            },

            //发送计划通知
            informationVisible:false,
            infoMode:'proInfo',
            info_proSelected:[],
            extraMsg:'',

            //成员聊天交流室
            chatRoomVisible:false,
            IM:[]
        }
    },
    watch:{
        "plan.plan.member":{
            handler:function(mem){
             let that=this
             this.totalMem=[]
             mem.forEach(function(item,index){
                 that.totalMem.push({
                     key:index,
                     label:item.name
                 })
             })
        },
          deep:true
        },
        "memberSlected":{
            handler:function(index){
                let that=this
                this.memPro=[]
               
                let mem=this.plan.plan.member[index]
                this.plan.process.forEach(function(item,index){
                    if(item.hasOwnProperty('proMem')){
                        let l=item.proMem.length
                        for(let i=0;i<l;i++){
                            if(mem.id==item.proMem[i].id){
                                that.memPro.push({
                                    id:item.id,
                                    name:item.pro_name,

                                    //关于任务通知的选择
                                    selected:false
                                })
                            }
                        }
                    }
                })
            },
            deep:true
        },
        "informationVisible":{
            handler:function(val){
                if(!val){
                    this.memPro.forEach(function(item,index){
                        item.selected=false
                    })
                    this.info_proSelected=[]
                    this.infoMode='proInfo'
                    this.extraMsg=''
                }
            },
            deep:true
        }
    }
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
   height: 100%;
   overflow-y: auto;
}

.addIcon{
    height:20px;
    float: left; 
    padding: 0;
}

.hover:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.pointer:hover{
    cursor: pointer;
}

.jobTable>>>.el-table__body-wrapper:hover{
    cursor: pointer;
}





</style>