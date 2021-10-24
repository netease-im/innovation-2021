<template>
<div class="main">
    <el-card class="button_area" v-if="showBtn">
    <el-button  type="success" size="mini" @click="toPDF" style="margin-left:0" >导出成pdf</el-button>
    <el-button  type="warning" size="mini" @click="changeType" style="margin-left:0;margin-top:10%">切换样式</el-button>
     <el-input-number controls-position="right" v-if="type!=0" size="mini" v-model="columns"  :min="1" :max="3" label="描述文字"></el-input-number>
    </el-card>

    <table v-if="type==0" border="1" height="600" width="1000" cellspacing="0" align="center">
    
     <tr align="center" height="70">
        <td colspan="5">计划基本信息</td>
    </tr>

    <tr align="center">
        <td colspan="2">计划名称</td>
        <td colspan="3">{{plan.name}}</td>
    </tr>

    <tr align="center">
        <td colspan="2">计划性质</td>
        <td colspan="3">{{plan.type}}</td>
    </tr>

    <tr align="center" v-if="plan.type=='小组计划'">
        <td colspan="2">计划负责人</td>
        <td colspan="3">{{plan.manager.name}}</td>
    </tr>


    <tr align="center" v-if="plan.type=='个人计划'">
        <td colspan="2">计划执行人</td>
        <td colspan="3">{{plan.excutor.name}}</td>
    </tr>

    <tr align="center">
        <td colspan="2">计划开始时间</td>
        <td colspan="3">{{plan.startDate+plan.startTime}}</td>
    </tr>

    <tr align="center">
        <td colspan="2">计划结束时间</td>
        <td colspan="3">{{plan.endDate+plan.endTime}}</td>
    </tr>

     <tr align="center" height="60">
        <td colspan="2">计划描述</td>
        <td colspan="3">{{plan.desc}}</td>
    </tr>
    
    <tr align="center"  height="70" v-if="plan.type=='小组计划'">
        <td colspan="5">小组成员</td>
    </tr>

    <tr align="center" v-for="(item,i) in plan.member" :key="i">
        <td colspan="2">小组成员{{i+1}}</td>
        <td colspan="3">{{item.name}}</td>
    </tr>

    <tr align="center"  height="70">
        <td colspan="5">任务列表</td>
    </tr>
	
    <tr align="center">
    	<td>任务序号</td>
        <td>任务名称</td>
        <td>开始时间</td>
        <td>结束时间</td>
        <td width="400">任务描述</td>
    </tr>
    
     <tr align="center" v-for="(item,i) in process" :key="i">
    	<td>{{item.id}}</td>
        <td>{{item.pro_name}}</td>
        <td>{{item.startDate+item.startTime}}</td>
        <td>{{item.endDate+item.endTime}}</td>
        <td>{{item.pro_text}}</td>
     </tr>
</table>

  <el-card shadow="never" v-if="type==1" class="typeCard">
  <el-descriptions label-class-name="bold"  :column="columns" title="计划基本信息">
  <el-descriptions-item label="计划名称">{{plan.name}}</el-descriptions-item>
  <el-descriptions-item label="计划性质">{{plan.type}}</el-descriptions-item>
  <el-descriptions-item label="计划执行人" v-if="plan.type=='个人计划'">{{plan.excutor.name}}</el-descriptions-item>
  <el-descriptions-item label="计划负责人" v-if="plan.type=='小组计划'">{{plan.manager.name}}</el-descriptions-item>
  <el-descriptions-item label="计划开始时间">{{plan.startDate+plan.startTime}}</el-descriptions-item>
  <el-descriptions-item label="计划结束时间">{{plan.endDate+plan.endTime}}</el-descriptions-item>
  <el-descriptions-item label="计划描述">{{plan.desc}}</el-descriptions-item>
  </el-descriptions>
<br>

<el-descriptions label-class-name="bold" :column="1" title="小组成员"  v-if="plan.type=='小组计划'">
     <el-descriptions-item  :label="'小组成员'+(i+1)" v-for="(item,i) in plan.member" :key="i">
     {{item.name}}
     </el-descriptions-item>
</el-descriptions>
<br>

<div  v-for="(item,i) in process" :key="i">
  <el-descriptions label-class-name="bold" :column="columns" :title="'任务'+(i+1)">
  <el-descriptions-item label="任务名称">{{item.pro_name}}</el-descriptions-item>
  <el-descriptions-item label="开始时间">{{item.startDate+item.startTime}}</el-descriptions-item>
  <el-descriptions-item label="结束时间">{{item.endDate+item.endTime}}</el-descriptions-item>
  <el-descriptions-item label="任务描述">{{item.pro_text}}</el-descriptions-item>
  </el-descriptions>
  <br>
</div>

     <el-descriptions label-class-name="bold" :column="1" title="日志" direction="vertical" v-if="logDate.length!=0">
      <el-descriptions-item v-for="(item,i) in logDate" :key="i" :label="item">
      {{notes.dailyNotes[item]}}
      </el-descriptions-item>
      </el-descriptions>


</el-card>



 <el-card shadow="never" v-if="type==2" class="typeCard">
  <el-descriptions :column="columns" title="计划基本信息" border>
  <el-descriptions-item label="计划名称">{{plan.name}}</el-descriptions-item>
  <el-descriptions-item label="计划性质">{{plan.type}}</el-descriptions-item>
  <el-descriptions-item label="计划执行人" v-if="plan.type=='个人计划'">{{plan.excutor.name}}</el-descriptions-item>
  <el-descriptions-item label="计划负责人" v-if="plan.type=='小组计划'">{{plan.manager.name}}</el-descriptions-item>
  <el-descriptions-item label="计划开始时间">{{plan.startDate+plan.startTime}}</el-descriptions-item>
  <el-descriptions-item label="计划结束时间">{{plan.endDate+plan.endTime}}</el-descriptions-item>
  <el-descriptions-item label="计划描述">{{plan.desc}}</el-descriptions-item>
  </el-descriptions>
<br>

<el-descriptions :column="1" title="小组成员"  v-if="plan.type=='小组计划'" border>
     <el-descriptions-item :label="'小组成员'+(i+1)" v-for="(item,i) in plan.member" :key="i">
     {{item.name}}
     </el-descriptions-item>
</el-descriptions>
<br>
<div  v-for="(item,i) in process" :key="i">
  <el-descriptions :column="columns" :title="'任务'+(i+1)" border>
  <el-descriptions-item label="任务名称">{{item.pro_name}}</el-descriptions-item>
  <el-descriptions-item label="开始时间">{{item.startDate+item.startTime}}</el-descriptions-item>
  <el-descriptions-item label="结束时间">{{item.endDate+item.endTime}}</el-descriptions-item>
  <el-descriptions-item label="任务描述">{{item.pro_text}}</el-descriptions-item>
  </el-descriptions>
  <br>
</div>


    <el-descriptions :column="1" title="日志" direction="vertical" border v-if="logDate.length!=0">
      <el-descriptions-item v-for="(item,i) in logDate" :key="i" :label="item">
      {{notes.dailyNotes[item]}}
      </el-descriptions-item>
      </el-descriptions>

 </el-card>

 <el-card shadow="never" v-if="type==3" class="typeCard">
  <el-descriptions :column="columns" title="计划基本信息" direction="vertical" border>
  <el-descriptions-item label="计划名称">{{plan.name}}</el-descriptions-item>
  <el-descriptions-item label="计划性质">{{plan.type}}</el-descriptions-item>
  <el-descriptions-item label="计划执行人" v-if="plan.type=='个人计划'">{{plan.excutor.name}}</el-descriptions-item>
  <el-descriptions-item label="计划负责人" v-if="plan.type=='小组计划'">{{plan.manager.name}}</el-descriptions-item>
  <el-descriptions-item label="计划开始时间">{{plan.startDate+plan.startTime}}</el-descriptions-item>
  <el-descriptions-item label="计划结束时间">{{plan.endDate+plan.endTime}}</el-descriptions-item>
  <el-descriptions-item label="计划描述">{{plan.desc}}</el-descriptions-item>
  </el-descriptions>
<br>

<el-descriptions :column="1" title="小组成员" direction="vertical" v-if="plan.type=='小组计划'" border>
     <el-descriptions-item :label="'小组成员'+(i+1)" v-for="(item,i) in plan.member" :key="i">
     {{item.name}}
     </el-descriptions-item>
</el-descriptions>
<br>
<div  v-for="(item,i) in process" :key="i">
  <el-descriptions :column="columns" :title="'任务'+(i+1)" direction="vertical" border>
  <el-descriptions-item label="任务名称">{{item.pro_name}}</el-descriptions-item>
  <el-descriptions-item label="开始时间">{{item.startDate+item.startTime}}</el-descriptions-item>
  <el-descriptions-item label="结束时间">{{item.endDate+item.endTime}}</el-descriptions-item>
  <el-descriptions-item label="任务描述">{{item.pro_text}}</el-descriptions-item>
  </el-descriptions>
  <br>
</div>
     <el-descriptions :column="1" title="日志" direction="vertical" border v-if="logDate.length!=0">
      <el-descriptions-item v-for="(item,i) in logDate" :key="i" :label="item">
      {{notes.dailyNotes[item]}}
      </el-descriptions-item>
      </el-descriptions>
</el-card>



</div>
</template>

<script>
var database =require('nedb')
let db=[]
const ipc=require('electron').ipcRenderer
export default {
    data(){
        return{
            showBtn:true,
            plan:{},
            process:{},
            type:0,
            columns:2,
            notes:[],
            notesTitle:[],
            logDate:[]
        }
    },
     created:function(){
        let that=this
        ipc.on('plan',function(event,arg){
           
        that.plan=JSON.parse(JSON.stringify(arg.plan))
        that.process=JSON.parse(JSON.stringify(arg.process))
        that.process.shift()
        that.process.pop()
        db=new database({
            filename:that.$store.state.dbName,
            autoload:true
        })
        db.find({type:'planItemNotes',plan:that.plan.name},function(err,Docs){
             if(Docs.length!=0){
                 that.notes=Docs[0]
                 }else{
                     that.notes={
                         type:'planItemNotes',
                         plan:that.plan_name,
                         dailyNotes:{},
                         planNotes:{}
                      }
                      db.insert(that.notes)
                }
                if(that.notes.hasOwnProperty('planNotes')){
                    that.notesTitle=Object.keys(that.notes.planNotes)
                }
                 if(that.notes.hasOwnProperty('dailyNotes')){
                    that.logDate=Object.keys(that.notes.dailyNotes)
                }
          })
        })
    
    },
    methods:{
        changeType(){
            this.type=(this.type+1)%4
        },
        toPDF(){
            this.showBtn=false
            ipc.send('print-to-pdf')
            let that=this
            ipc.on('reply',function(event, arg){
                that.$message({
                    message:'导出成功',
                    type:"success"
                })
                that.showBtn=true
            })
        }
    }
}
</script>
<style>
 .bold{
    font-weight:bold;
 }
</style>
<style scoped>
 .button_area{
 position: fixed;
 margin-top:40%;
 opacity: 0.5;
 }
 .button_area >>> .el-card__body{
  padding: 8px;
 }
 .typeCard{
     width:1000;
     margin:auto
 }
 .main{
     height:100%;
     font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
 }
 .button_area:hover{
     cursor: pointer;
     opacity: 1;
 }
</style>