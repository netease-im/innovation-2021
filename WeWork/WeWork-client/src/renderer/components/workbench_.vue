
<template>
    <div class="main">

    <el-card class="planDiv" shadow="never">
    <div slot="header">
    <span style="color:#1E90FF">我的计划</span>
    
    

    <img src="../assets/add.svg" class="addIcon" @click="addPlan"/>


    <el-dropdown class="sort" >
    <span >
    分类 <i class="el-icon-arrow-down el-icon--right"></i>
    </span>
    <el-dropdown-menu slot="dropdown">
    <el-dropdown-item @click.native="chgData(data_endDate)" >截止日期</el-dropdown-item>
    <el-dropdown-item @click.native="chgData(data_status)">计划状态</el-dropdown-item>
    </el-dropdown-menu>
    </el-dropdown>

    </div>

    <el-tree 
    node-key="id"
    :data="data" 
    :props="defaultProps" 
    class="planList"
    @node-click="goToPlan"
    :default-expanded-keys="[1,2,3]" >
      <span  class="custom-tree-node" style="width:100%" slot-scope="{ node, data }">
        <span  :class="data.hasOwnProperty('id')?'label':''">{{ data.label }}</span>

         <span>
          <el-tag
            type="info"
            size="mini"
            v-if="!data.hasOwnProperty('id')"
            style="float:right;margin-left:4px"
           >
           {{data.endDate}}
          </el-tag>
        </span>

        <span>
          <el-tag
            size="mini"
            :type="data.status"
            v-if="!data.hasOwnProperty('id')"
            style="float:left;margin-right:2%"
           >
           {{data.status=='warning'?'进行中':(data.status=='success'?'已结束':'未开始')}}
          </el-tag>
        </span>
      </span>
    </el-tree>
    </el-card>

    <div class="rightCol">
    <el-card class="taskList" shadow="never">
     <div slot="header">
     <span style="color:#1E90FF">今日任务</span>
     </div>

      <el-table
      class="jobTable"
      :data="todayPro"
      style="width: 100%;height:100%"
      @row-click="showTodayPro">

      <el-table-column
        prop="proName"
        label="任务名称"
        >
      </el-table-column>
      <el-table-column
        prop="planName"
        label="所属计划"
        >
      </el-table-column>
 
       <el-table-column
        label="任务状态"
       >
        <template slot-scope="scope">
        <el-tag size="mini" :type="scope.row.status==0?'danger':(scope.row.status==1?'warning':'success')">
        {{scope.row.status==0?'待开始':(scope.row.status==1?'进行中':'已完成')}}
        </el-tag>
        </template>
      </el-table-column>
      
    </el-table>
    </el-card>

    <!-- 我的目标part -->
    <el-card class="targetCard" shadow="never">
     <div slot="header">
     <span style="color:#1E90FF">我的目标</span>
     <img @click="addGoal" src="../assets/add.svg" class="addIcon" />
     </div>

     <el-table
      class="jobTable"
      :data="goal"
      style="width: 100%;height:100%"
      @row-click="showGoalDetail"
      :show-header="false">

      <el-table-column
        prop="goal.goal_name"
        >
      </el-table-column> 

      <el-table-column>

       <template slot-scope="scope">
       <el-progress :percentage="goalPercentage(scope.row)"></el-progress>
       </template>

      </el-table-column>     
      </el-table>

    </el-card>
    </div>

   
    <!-- 今日任务详情弹窗 -->
   <el-dialog top="5vh" width="900px" :title="todayProSelected.pro_name" v-if="todayProSelected!=null"  :visible.sync="todayProVisible">
   <el-card>
   <el-descriptions :column="2">
    <el-descriptions-item label="开始时间">
    {{todayProSelected.startDate+" "+todayProSelected.startTime}}
    </el-descriptions-item>

    <el-descriptions-item label="结束时间">
    {{todayProSelected.endDate+" "+todayProSelected.endTime}}
    </el-descriptions-item>

    <el-descriptions-item label="所属计划">
    {{todayProSelected.planName}}
    </el-descriptions-item>

    <el-descriptions-item label="任务状态">
    <el-tag size="mini" :type="todayProSelected.status==0?'danger':(todayProSelected.status==1?'warning':'success')">
        {{todayProSelected.status==0?'待开始':(todayProSelected.status==1?'进行中':'已完成')}}
        </el-tag>
    </el-descriptions-item>
    
    <el-descriptions-item label="参与人">
    <div v-if="todayProSelected.hasOwnProperty('proMem')&&todayProSelected.proMem.length!=0">
    <el-tag style="margin-right:3px" size="mini" type="success" :key="i" v-for="(item,i) in todayProSelected.proMem">
    {{item.name}}
    </el-tag>
    </div>
    <div v-if="!todayProSelected.hasOwnProperty('proMem')||todayProSelected.proMem.length==0">
    未指定
    </div>
    </el-descriptions-item>
   </el-descriptions>
   </el-card>
   
   <div v-if="plan[todayProSelected.planIndex].plan.type!='个人计划'&&$store.state.dbName!='./local.db'">
   <el-card  v-for="(item,index) in todayProSelected.proMem" :key="index">
   <el-tag type="success" >{{item.name}}</el-tag>
   <el-button style="margin-left:5px" @click="remindMemProgress(item)" size="mini" type="warning" icon="el-icon-message"  plain>任务进度提醒</el-button>
   <el-progress :percentage="MemPer(item)" :format="MemStatus" ></el-progress>
   </el-card>

   <el-card v-if="todayProHost!=null">
   <div slot="header">我的进度</div>
   <el-button type="warning" @click="changeMemStatus(1)" v-if="todayProHost.status==0">进行任务</el-button>
   <el-button type="success" @click="changeMemStatus(2)" v-if="todayProHost.status==1">完成任务</el-button>
   <el-button type="primary" v-if="todayProHost.status==2" disabled>任务已完成</el-button>
   </el-card>
   </div>
   

   <el-card v-if="plan[todayProSelected.planIndex].plan.type=='个人计划'||$store.state.dbName=='./local.db'">
   <el-button type="warning" @click="changeProStatus(1)" v-if="todayProSelected.status==0">进行任务</el-button>
   <el-button type="success" @click="changeProStatus(2)" v-if="todayProSelected.status==1">完成任务</el-button>
   <el-button type="primary" v-if="todayProSelected.status==2" disabled>任务已完成</el-button>
   </el-card>
  
  </el-dialog>
     
  
  <!-- 查看目标详情 -->
 <el-dialog width="900px" top="5vh" :title="goalDetail.goal.goal_name" v-if="goalDetail!=null"  :visible.sync="goalDetailVisible">
 <el-card>
 <el-descriptions direction="vertical" :column="1">
 <el-descriptions-item label="目标描述">
  <el-input type="textarea" :readonly="true" :value="goalDetail.goal.goal_text.desc"></el-input>
 </el-descriptions-item>
  <el-descriptions-item label="预期结果">
  <el-input type="textarea" :readonly="true" :value="goalDetail.goal.goal_text.res"></el-input>
 </el-descriptions-item>
 </el-descriptions>
 </el-card>


 <el-card>
 <el-descriptions direction="vertical" :column="1">
  <el-descriptions-item label="关联计划">
 <el-tag style="margin-right:4px" v-for="(item,index) in goalDetail.goal.goal_plans" :key="index">
 {{item}}
 </el-tag>
 </el-descriptions-item>
 
 <el-descriptions-item label="目标进度">
 <el-progress :percentage="goalPercentage(goalDetail)"></el-progress>
 </el-descriptions-item>
 
 </el-descriptions>
 </el-card>
 <el-card>
 <el-button type="primary" @click="editGoal" plain>编辑</el-button>
 <el-button type="danger"  @click="removeGoal" plain>删除</el-button>
 </el-card>

 </el-dialog>


  <!-- 目标添加弹窗 -->

  <el-dialog width="900px" top="5vh" :title="title" v-if="goalAddTemp!=null"  :visible.sync="goalAddVisible">

  <el-card>
  <el-form label-width="auto">
  <el-form-item label="目标名" >
  <el-input  v-model="goalAddTemp.goal_name" :readonly="nameEditForbidden"></el-input>
  </el-form-item>
  
  <el-form-item label="目标描述">
  <el-input type="textarea" v-model="goalAddTemp.goal_text.desc"></el-input>
  </el-form-item>
  
  <el-form-item label="预期结果">
  <el-input type="textarea" v-model="goalAddTemp.goal_text.res"></el-input>
  </el-form-item>

  <el-form-item label="关联计划">
   

  <el-tag
  style="margin-right:5px"
  v-for="tag in tags"
  :key="tag.name"
  :closable="true"
  :type="tag.type"
   @close="removeSelectedPlan(tag)">
  {{tag.name}}
   </el-tag>
  <el-tag @click.native="chooseRelatedPlan" class="pointer" type="warning">+ 添加</el-tag>
 
  </el-form-item>

  </el-form>
  </el-card>
  
  <el-card>
  <el-button @click="commitGoal" type="primary">确认</el-button>
  <el-button @click="goalAddVisible=false" type="info">取消</el-button>
  </el-card>
  </el-dialog>

  <el-dialog width="700px" top="10vh" title="选择关联计划" v-if="goalAddTemp!=null"  :visible.sync="relatedPlanVisible">
   <el-table
      :data="data_endDate[3].children"
      class="jobTable">

      <el-table-column
        prop="label"
        label="计划名称"
        >
      </el-table-column>

      <el-table-column
        prop="host"
        label="发起人"
        >
      </el-table-column>

       <el-table-column
        prop="type"
        label="计划性质"
        >
      </el-table-column>

      <el-table-column
        label="计划状态"
       >
      <template slot-scope="scope">
        <el-tag size="mini" :type="scope.row.status==0?'danger':(scope.row.status==1?'warning':'success')">
        {{scope.row.status==0?'未开始':(scope.row.status==1?'进行中':'已结束')}}
        </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column>
      <template slot-scope="scope">
        <el-button @click="chooseThisPlan(scope.row)" size="mini" type="warning" v-if="!scope.row.selected">
        选择
        </el-button>
      </template>
      </el-table-column>

    </el-table>

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
        db.find({type:"planItemTest2"},function(err,Docs){
                   if(Docs.length!=0){
                     that.plan=JSON.parse(JSON.stringify(Docs))
                     that.planShow=JSON.parse(JSON.stringify(Docs))
                   }
                   that.peekDayPlan()
                   that.plan.forEach(function(item,index){
                     that.map.set(item.plan.name,item)
                     })
                })
       
       db.find({type:"goal"},function(err,Docs){
                  if(Docs.length!=0){
                    that.goal=JSON.parse(JSON.stringify(Docs))
                  }
                  
       })
       
    },
     data() {
      return {
        //页面数据
        plan:[],
        planShow:[],
        goal:[],

        data_endDate: [
        {
          id:1,
          label: '今日',
          children: []
        }, 
        {
          id:2,
          label: '本周',
          children: []
        }, 
        {
          id:3,
          label: '本月',
          children: []
        },
        {
            id:4,
            label: '全部计划',
            children: []
        }],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        data_status: [
        {
          id:1,
          label: '进行中',
          children: []
        }, 
        {
          id:2,
          label: '未开始',
          children: []
        }, 
        {
          id:3,
          label: '已完成',
          children: []
        },
        {
            id:4,
            label: '全部计划',
            children: []
        }],
        data:[],
        defaultProps: {
          children: 'children',
          label: 'label'
        },

        todayPro: [],
        todayProSelected:null,
        todayProVisible:false,
        todayProHost:null,


        //目标添加弹窗
        goalAddVisible:false,
        goalAddTemp:null,
        relatedPlanVisible:false,
        title:'添加目标',
        nameEditForbidden:false,

         tags: [
          // { name: '标签一', type:'success' },
        ],

        //目标关联计划映射
        map:new Map(),
        
        //查看任务详情
        goalDetail:null,
        goalDetailVisible:false
        
      };
    },
    methods:{
        goToPlan(data){
          if(data.hasOwnProperty('id')){
            return 
          }
          this.$router.push({
            name:'workbenchPlan',
            params:{
              name:data.label
            }
          })
        },
        addPlan(){
            this.$router.push('/createPlan')
        },
        getDateArr(date){
           return date.split(/\s*[^0-9]\s*/g)
        },
        chgData(data){
          this.data=data
        },
        peekDayPlan(){
          //变量初始化
          this.todayPro=[]
          this.data_endDate=[
        {
          id:1,
          label: '今日',
          children: []
        }, 
        {
          id:2,
          label: '本周',
          children: []
        }, 
        {
          id:3,
          label: '本月',
          children: []
        },
        {
            id:4,
            label: '全部计划',
            children: []
        }],
      
        this.data_status=[
        {
          id:1,
          label: '进行中',
          children: []
        }, 
        {
          id:2,
          label: '未开始',
          children: []
        }, 
        {
          id:3,
          label: '已结束',
          children: []
        },
        {
            id:4,
            label: '全部计划',
            children: []
        }]


          let that=this
          let today=new Date()
          let year=today.getFullYear()
          let month=today.getMonth()+1
          let day=today.getDate()
          this.plan.forEach(function(item,i){
            // let arr_start=that.getDateArr(item.plan.startDate)
            let arr_end=that.getDateArr(item.plan.endDate)
            let planDate=new Date(arr_end[0],arr_end[1]-1,arr_end[2])
            let endDate=arr_end[1].replace(/^[0]*/g,'')+'月'+arr_end[2].replace(/^[0]*/g,'')+'日'
            let status=that.planStatus(item.process)

            let planItem={
                label:item.plan.name,
                endDate:endDate,
                status:status,

                //this is for 关联计划选择
                host:item.plan.type=='小组计划'?item.plan.manager:item.plan.excutor,
                type:item.plan.type,
                selected:false
            }

            //比较两天是否是同一天
            if(year==arr_end[0]&&month==arr_end[1]&&day==arr_end[2]){
              that.data_endDate[0].children.push(planItem)
            }

            //比较两天是否是同一周
            let daySpan=1000*60*60*24;   
            let day1=Math.floor(today.getTime()/daySpan) 
            let day2=Math.floor(planDate.getTime()/daySpan)
            
          
            
            if(parseInt((day1+3)/7)==parseInt((day2+3)/7)){
                that.data_endDate[1].children.push(planItem)
            } 

            if(arr_end[0]==year&&arr_end[1]==month){
                that.data_endDate[2].children.push(planItem)
            }

            if(planItem.status=='warning'){
               that.data_status[0].children.push(planItem)
            }

            if(planItem.status=='danger'){
              that.data_status[1].children.push(planItem)
            }

            if(planItem.status=='success'){
              that.data_status[2].children.push(planItem)
            }

            that.data_endDate[3].children.push(planItem)
            that.data_status[3].children.push(planItem)

            item.process.forEach(function(item_,index){
              if(index!=0&&index!=item.process.length-1){
                let proDate_start=that.getDateArr(item_.startDate)
                let proDate_end=that.getDateArr(item_.endDate)
                if(proDate_start[0]<=year&&proDate_end[0]>=year&&
                   proDate_start[1]<=month&&proDate_end[1]>=month&&
                   proDate_start[2]<=day&&proDate_end[2]>=day&&!item_.hasOwnProperty('hidden')){
                     that.todayPro.push({
                       proName:item_.pro_name,
                       planName:item.plan.name,
                       status:item_.status,
                       planIndex:i,
                       proIndex:index
                     })
                   }
              }
            })

          })
          this.data=this.data_endDate
        },

        planStatus(pro){
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
        peekWeekPlan(){
           let that=this
           let today=new Date()
           let daySpan=1000*60*60*24;   
           let day1=Math.floor(today.getTime()/daySpan)       

        },

        //操作今日计划
        showTodayPro(row, column, event){
          let that=this
          this.todayProHost=null
          this.todayProVisible=true
          this.todayProSelected=JSON.parse(JSON.stringify(this.plan[row.planIndex].process[row.proIndex]))
          this.todayProSelected.planName=row.planName
          this.todayProSelected.status=row.status
          this.todayProSelected.planIndex=row.planIndex
          this.todayProSelected.proIndex=row.proIndex
          this.todayProSelected.row=row

          this.todayProSelected.proMem.forEach(function(item,index){
            if(that.todayProHost!=null){
              return 
            }
            if(item.host){
              that.todayProHost=item
            }
          })
        },

        //修改任务状态
        changeMemStatus(i){
          let that=this
          let pro=this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex]
          let host=null
          pro.proMem.forEach(function(item,index){
            if(host!=null){
              return 
            }
            if(item.host){
              host=item
              host.status=i
              that.todayProSelected.proMem[index].status=i
            }
          })
          this.refreshProStatus(pro)
          this.todayProSelected.status=pro.status
          if(i==1){
            this.save('您正在进行该任务',this.plan[this.todayProSelected.planIndex])
          }else if(i==2){
            this.save('您已完成该任务',this.plan[this.todayProSelected.planIndex])
          }
          this.peekDayPlan()
        },

        changeProStatus(i){
          let pro=this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex]
          pro.status=i
          this.todayProSelected.status=pro.status
          if(i==1){
            this.save('正在进行该任务',this.plan[this.todayProSelected.planIndex])
          }else if(i==2){
            this.save('已完成该任务',this.plan[this.todayProSelected.planIndex])
          }
          this.peekDayPlan()
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

        //保存数据
        save(msg,plan){
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
        saveGoal(){

        },
       //添加目标
        addGoal(){
          this.nameEditForbidden=false
          this.title='添加目标'
          this.goalAddTemp={
            goal_name:'',
            goal_plans:[],
            goal_text:{
              desc:'',
              res:''
            }
          }
          this.goalAddVisible=true
        },
        commitGoal(){
          if(this.goalAddTemp.goal_name.match(/^\s*$/)||
             this.goalAddTemp.goal_text.desc.match(/^\s*$/)||
             this.goalAddTemp.goal_text.res.match(/^\s*$/)){
               this.$message({
                 message:'目标信息不能为空',
                 type:'error'
               })
               return
             }
          if(this.tags.length==0){
               this.$message({
                 message:'目标至少关联一个计划',
                 type:'error'
               })
               return
          }

          let that=this
          that.goalAddTemp.goal_plans=[]
          this.tags.forEach(function(item,index){
            that.goalAddTemp.goal_plans.push(item.name)
          })

          db.remove({type:"goal","goal.goal_name":that.goalAddTemp.goal_name},{multi:true},function (err, numRemoved){})

          db.insert({type:"goal",goal:that.goalAddTemp},function (err, newDoc){
            that.$message({
              message:'创建成功',
              type:'success'
            })
          })

            //云同步代码
            if(that.$store.state.dbName=='./cloud.db'){
                that.$store.state.syncing=true
                that.$http.post(that.$store.state.contextUrl+'/uploadGoal',that.$qs.stringify({
                  user_id:that.$store.state.user_id,
                  goal_name:that.goalAddTemp.goal_name,
                  goal:JSON.stringify({type:"goal",goal:that.goalAddTemp})
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
        that.reload()
        },
        
        editGoal(){
          this.nameEditForbidden=true
          let that=this
          this.title='编辑目标'
          this.goalAddTemp=this.goalDetail.goal
          this.goalDetail.goal.goal_plans.forEach(function(item,index){
            that.tags.push({
              name:item,
              type:'success'
            })
          let l=that.data[3].children.length
          for(let i=0;i<l;i++){
            if(item==that.data[3].children[i].label){
              that.data[3].children[i].selected=true
            }
          }
        })
          this.goalAddVisible=true
      },

        //删除目标
        removeGoal(){
          let that=this
          db.remove({type:"goal","goal.goal_name":that.goalDetail.goal.goal_name},{multi:true},
          function (err, numRemoved){
            that.$message({
              message:'删除成功',
              type:'error'
            })

            if(that.$store.state.dbName=='./cloud.db'){
              that.$store.state.syncing=true
                that.$http.post(that.$store.state.contextUrl+'/deleteGoal',that.$qs.stringify({
                  user_id:that.$store.state.user_id,
                  goal_name:that.goalDetail.goal.goal_name,
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
            that.reload()
          })
        },

        removeSelectedPlan(tag){
          this.tags.splice(this.tags.indexOf(tag),1)
          let l=this.data[3].children.length
          for(let i=0;i<l;i++){
            if(tag.name==this.data[3].children[i].label){
              this.data[3].children[i].selected=false
            }
          }
        },
        chooseRelatedPlan(){
          this.relatedPlanVisible=true
        },
        chooseThisPlan(plan){
          plan.selected=true
          this.tags.push({
            name:plan.label,
            type:'success'
          })
        },

        //查看目标详情
        showGoalDetail(row, column, event){
          this.goalDetail=row
          this.goalDetailVisible=true
        },
        //目标完成度
        goalPercentage(goal){
           let that=this
           let total=0
           let finished=0
           goal.goal.goal_plans.forEach(function(item,index){
             if(that.map.has(item)){
               let plan=that.map.get(item)
               plan.process.forEach(function(pro,i){
                 if(i==0||i==plan.process.length-1){
                   return 
                 }
                 total++
                 if(pro.status==2){
                   finished++
                 }
               })  
             }
           })
           return parseInt(finished/total*100)

        },
        //返回成员任务完成度
        MemStatus(num){
          if(num===0){
            return '未开始'
          }else if(num===50){
            return '进行中'
          }else{
            return '已结束'
          }
          
        },
        MemPer(mem){
           if(mem.status==0){
            return 0
          }else if(mem.status==1){
            return 50
          }else{
            return 100
          }
        },
        isShowMyProgress(){
          let hasHost=false
          this.todayProSelected.proMem.forEach(function(item,index){
            if(hasHost){
              return 
            }
            if(item.host){
              hasHost=true
            }
          })
          return hasHost
        },

        //通知成员进行任务
        remindMemProgress(mem){
          let that=this
          let acceptor_id=-1
          let acceptor_name=null
          this.$http.post(this.$store.state.contextUrl+'/getUserId',this.$qs.stringify({
                    email:mem.email
                })).then(function(res){
                    if(res.data!=-1){
                        acceptor_id=res.data.id
                        acceptor_name=res.data.user_name
                        
                        if(mem.name!=acceptor_name){
                            that.$message({
                                message:'参与人姓名与账户姓名（'+acceptor_name+'）不匹配,请重新设置参与人信息',
                                type:'error'
                            })
                            return 
                          }
                            let content={}
                            let date=new Date()
                            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
                            content.time=timeStr

                            content.plan_name=that.plan[that.todayProSelected.planIndex].plan.name

                            content.plan=that.plan[that.todayProSelected.planIndex].plan

                            content.pro=that.plan[that.todayProSelected.planIndex].
                                             process[that.todayProSelected.proIndex]

                            content.extraMsg='这是一条任务进度提醒，请您尽快完成指定任务，点击下方任务详情查看任务内容'

                            content.accepted=false

                            that.$http.post(that.$store.state.contextUrl+'/uploadNotice',that.$qs.stringify({
                                acceptor:acceptor_id,
                                sender:that.$store.state.user_id,
                                content:JSON.stringify(content),

                                //任务进度提醒
                                type:5,
                                read:0,
                            })).then(function(res_){
                                if(res_.data=='success'){
                                    that.$message({
                                        message:'任务进度提醒发送成功',
                                        type:'success'
                                    })
                                    
                                    //webSocket part
                                    // let socket=that.$store.state.socket
                                    that.send(acceptor_id)                   
                                }else{
                                     that.$message({
                                        message:'发送失败',
                                        type:'error'
                                    })
                                }
                            })      
                        
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
    },
    watch:{
      'goalAddVisible':{
        handler:function(val){
          if(!val){
             this.data[3].children.forEach(function(item,index){
               item.selected=false
             })
             this.goalAddTemp={
            goal_name:'',
            goal_plans:[],
            goal_text:{
              desc:'',
              res:''
            }
          }
            this.tags=[]
          }
          
        }
      },
      // 'todayProSelected.proMem':{
      //   handler:function(proMem){
      //     let count=0
      //     proMem.forEach(function(item,index){
      //       count+=item.status
      //     })
      //     let preStatus=this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex].status
      //     if(count==0){
      //       this.todayProSelected.status=0
      //       this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex].status=0
      //     }else if(count==proMem.length*2){
      //       this.todayProSelected.status=2
      //       this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex].status=2
      //     }else{
      //       this.todayProSelected.status=1
      //       this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex].status=1
      //     }
      //     if(preStatus!=this.plan[this.todayProSelected.planIndex].process[this.todayProSelected.proIndex].status){
      //       this.save('已保存任务状态',this.plan[this.todayProSelected.planIndex])
      //     }
      //   },
      //   deep:true
      // }   
    },
    inject:['reload']
}
</script>

<style scoped>
.pointer:hover{
  cursor: pointer;
}
.sort{
  color:#a2a9b1;
  float:right
}


.sort:hover{
  cursor: pointer;
}

.label{
   color:#a2a9b1
}
.main{
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   height:100%;
   overflow-y: hidden;
}
.planDiv{
    width: 55%;
    height: 100%;
    overflow-y: auto;
    margin-left: 2px;
    float: left;
    padding: 10px;
    box-sizing: border-box;
}

.taskList{
   width: 44.6%;
   height: 60%;
   overflow-y: auto;
   box-sizing: border-box;
   padding: 10px;
   float: left;
}
.targetCard{
   width: 44.6%;
   height: 40%;
   overflow-y: auto;
   float: left;
   box-sizing: border-box;
}

.addIcon{
    height:20px;
    float: right; 
    padding: 0;
    margin-left:4%
}

.addIcon:hover{
    cursor: pointer;
}

.planList >>> .el-tree-node__content{
    height: 50px;
}

.jobTable>>>.el-table__body-wrapper:hover{
    cursor: pointer;
}
.rightCol{
     height: 100%;
}
</style>