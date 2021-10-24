<template>
  <div class="main" >
  
  <img v-if="empty" src="../assets/empty.svg" style="height:90%;width:90%" />
  
  <div style="height:100%" v-if="plan.length!=0">
    <div style="height:100%;width:15%;float:left">
          <el-card style="height:99%; text-align: center;">
           <img style="width:15px;height:15px;" @click="turnPage(-1,plan.length)" class="turnPage" src="../assets/turnLeft.svg" />
          <span style="font-size:20px;color:#1E90FF;font-weight:bold;margin-left:10px;margin-right:10px">计划</span>
           <img style="width:15px;height:15px;" @click="turnPage(1,plan.length)" class="turnPage" src="../assets/turnRight.svg" />
          
          <div v-for="(item,i) in plan" :key="i">
          
          <el-card  class="item" shadow="hover" @click.native="changePlan(i)" 
                    v-if="i>=(thisPage-1)*pageSize&&i<thisPage*pageSize" >

           <el-popover
             placement="right-end"
             width="200"
             trigger="hover"
            :content="plan[i].plan.name"
            v-if="plan[i].plan.name.length>4">
              <span slot="reference" style="font-size:20px" >{{plan[i].plan.name.substr(0,4)+'..'}}</span>
           </el-popover>
           <span style="font-size:20px" v-if="plan[i].plan.name.length<=4">{{plan[i].plan.name}}</span>

          </el-card>
          </div>
          
          
          </el-card>
    </div>
    <el-card class="basicInfo">
    
    <el-popover
    placement="bottom-start"
    width="200"
    trigger="hover"
    :content="this.plan[index].plan.name">
    <span slot="reference" class="planBasicInfo" v-if="this.plan[index].plan.name.length<=4">{{plan[index].plan.name}}</span>
    <span slot="reference" class="planBasicInfo" v-if="this.plan[index].plan.name.length>4">{{plan[index].plan.name.substr(0,4)+'..'}}</span>
    </el-popover>

    
    <span class="planBasicInfo" style="margin-left:30px">计划性质：{{this.plan[index].plan.type}}</span>
    <span class="planBasicInfo" style="margin-left:30px">时间：{{this.plan[index].plan.startDate+" "+
    this.plan[index].plan.startTime+" — "+this.plan[index].plan.endDate+" "+
    this.plan[index].plan.endTime}}</span>
    </el-card>

    <el-card class="moreInfo">
    <div>
    <img @click="boy=!boy" v-if="boy" class="head" style="width:30px;height:30px;float:left;margin-top:-5px" src="../assets/boy.svg" />
    <img @click="boy=!boy" v-if="!boy" class="head" style="width:30px;height:30px;float:left;margin-top:-5px" src="../assets/girl.svg" />
    
    <span class="planBasicInfo">
    {{this.plan[index].plan.type=='小组计划'?this.plan[index].plan.manager:
    this.plan[index].plan.excutor}}
    </span>
    </div>

    <el-card @click.native="showCurrentWork" class="planButton" shadow="hover">
    <span style="font-size:20px">当前任务</span>
    </el-card>
    
    <el-card @click.native="showPlanDetail" class="planButton" shadow="hover">
    <span style="font-size:20px">编辑计划</span>
    </el-card>

    <el-card @click.native="showPlanLog" class="planButton" shadow="hover">
    <span style="font-size:20px">计划日志</span>
    </el-card>

    <el-card @click.native="toPlanNotes" class="planButton" shadow="hover">
    <span style="font-size:20px">计划总结</span>
    </el-card>

     <el-card @click.native="toPDF" class="planButton" shadow="hover">
    <span style="font-size:20px">导出计划</span>
    </el-card>
    
    </el-card>

    <!-- the card that shows the process information -->
    <el-card class="processInfo">
  
  <el-card style="margin-bottom:5px">
  <el-table
    :data="plan[index].process"
    style="width: 100%"
    height="200px"
    @row-click="getThisPro"
     :row-class-name="tableRowClassName">
    <el-table-column
      prop="pro_name"
      label="计划事务">
    </el-table-column>
  </el-table>
  </el-card>

    

    <el-card class="proDip">
    <span class="planBasicInfo" style="color:#E6A23C;font-size:23px">{{plan[index].process[thisPro].pro_name}}</span>
    <hr  style="width:100%"  align="left" />
    <div class="processPart" v-if="plan[index].process[thisPro].pro_name!='计划开始'&&
    plan[index].process[thisPro].pro_name!='计划结束'">
    <div style="width:60%;float:left">
    <br>
     <span class="planBasicInfo"  style="color:#409EFF">
    {{'任务序号：'+plan[index].process[thisPro].id}}
    </span>
    <br>
    <br>
    <span class="planBasicInfo" style="color:#409EFF" >
    {{'开始时间：'+plan[index].process[thisPro].startDate+plan[index].process[thisPro].startTime}}
    </span>
    <br>
    <br>
    <span class="planBasicInfo" style="color:#409EFF" >
    {{'结束时间：'+plan[index].process[thisPro].endDate+plan[index].process[thisPro].endTime}}
    </span>
    </div>
     <el-steps :space="50" 
    :active="plan[index].process[thisPro].status" 
    finish-status="success"
    direction="vertical"
    style="float:left">
    <el-step title="未开始"></el-step>
    <el-step title="进行中"></el-step>
    <el-step title="已完成"></el-step>
    </el-steps>

    <el-button size="mini" @click="chgStatus" style="float:left;margin-left:5%" v-if="plan[index].process[thisPro].status==0">开始流程</el-button>
    <el-button size="mini" @click="chgStatus" style="float:left;margin-left:5%" v-if="plan[index].process[thisPro].status==1">完成流程 </el-button>
    <el-button size="mini" @click="chgStatus" style="float:left;margin-left:5%" v-if="plan[index].process[thisPro].status==2" disabled>流程结束</el-button>

    <el-button size="mini" @click="ShowProText" type="warning" style="float:left;margin-left:5%;margin-top:3%" >任务描述</el-button>
    </div>

    <div class="startOrEndPart" v-if="plan[index].process[thisPro].pro_name=='计划开始'||
    plan[index].process[thisPro].pro_name=='计划结束'">
    <br>
    <span class="planBasicInfo" style="color:#409EFF" >
    {{'时间：'+plan[index].process[thisPro].startDate+plan[index].process[thisPro].startTime}}
    </span>
    <br><br><br><br>
   
    <span class="planBasicInfo"
    v-if="plan[index].process[thisPro].pro_name=='计划开始'">
    {{plan[index].process[thisPro].pro_text}}
    </span>
    
    <span class="planBasicInfo" 
    v-if="plan[index].process[thisPro].pro_name=='计划结束'">
    {{plan[index].process[thisPro].pro_text}}
    </span>
    </div>

    </el-card>
    </el-card>
  
  
  <el-dialog title="当前任务" :visible.sync="curworkDialogVisible">
  <el-form label-width="auto" v-if="curProcess.length!=0">
  <el-form-item label="任务序号">
  <span>{{curProcess[curPage-1].id}}</span>
  </el-form-item>
   <el-form-item label="任务名称">
  <span>{{curProcess[curPage-1].name}}</span>
  </el-form-item>
   <el-form-item label="开始时间">
  <span>{{curProcess[curPage-1].startTime}}</span>
  </el-form-item>
   <el-form-item label="结束时间">
  <span>{{curProcess[curPage-1].endTime}}</span>
  </el-form-item>
  </el-form>
  <el-pagination
    @current-change="handleCurrentChange"
    layout="prev, pager, next"
    :page-size="1"
    :total="curProcess.length">
  </el-pagination>
  </el-dialog>
  
  </div>
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
                   }else{
                     that.empty=true
                   }
                })
    },
    mounted () {
      this.findCurrentPro()
    },
    data() {
      return{
        plan:[],
        planShow:[],
        index:0,
        boy:true,
        thisPro:0,
        status:0,
        curworkDialogVisible:false,
        curProcess:[],
        curPage:1,
        
        thisPage:1,
        pageSize:8,

        empty:false



      }
    },
    methods: {
      tableRowClassName({row, rowIndex}){
         if (rowIndex == 0||rowIndex == this.plan[this.index].process.length-1) {
          return 'warning-row';
        }

        return '';

      },
      getThisPro(row){
        if(row.hasOwnProperty("id")){
          this.thisPro=row.id
        }else if(row.pro_name=='计划开始'){
          this.thisPro=0
        }else{
          this.thisPro=this.plan[this.index].process.length-1
        }
      },
      turnPage(val,len){
        if(this.thisPage==1&&val==-1){
          return 
        }else if(this.thisPage*this.pageSize>=len&&val==1){
          return 
        }

        this.thisPage=this.thisPage+val

      },

      toPDF(){
          const ipc = require('electron').ipcRenderer
          ipc.send('createPDF',this.plan[this.index])
      },

      toPlanNotes(){
        let that=this
        this.$router.push({
          name:'planNote',
          params:{
            plan_name:that.plan[that.index].plan.name
          }
        })
      },

      handleCurrentChange(val){
        this.curPage=val
      },
      findCurrentPro(){
        this.curProcess=[]
        let l=this.plan[this.index].process.length
        for(let i=0;i<l;i++){
          if(this.plan[this.index].process[i].status==1){
            this.curProcess.push({
              id:this.plan[this.index].process[i].id,
              name:this.plan[this.index].process[i].pro_name,
              startTime:this.plan[this.index].process[i].startDate+this.plan[this.index].process[i].startTime,
              endTime:this.plan[this.index].process[i].endDate+this.plan[this.index].process[i].endTime
            })
          }
        }
      },
      showCurrentWork(){
         const h=this.$createElement
         if(this.curProcess.length==0){
           this.$msgbox({
             title: '当前任务',
             message: h('p', null, [
               h('span', null,'暂无当前任务')
               ]),
               });
            return 
          }
        this.curworkDialogVisible=true
      },
      showPlanDetail(){
        let that=this
        this.$router.push({
          name:'showPlan',
          params:{
            plan:that.plan[that.index].plan,
            process:that.plan[that.index].process
          }
        })
      },
      showPlanLog(){
        let that=this
         this.$router.push({
          name:'planLog',
          params:{
            plan:that.plan[that.index].plan,
            process:that.plan[that.index].process
          }
        })
      },
      ShowProText(){
      const h = this.$createElement
      this.$msgbox({
          title: '流程详情',
          message: h('p', null, [
            h('span', null,this.plan[this.index].process[this.thisPro].pro_text)
          ]),
        });
      },

      chgStatus(){
        let that=this
        this.plan[this.index].process[this.thisPro].status++
        db.update({
          type:"planItemTest2",
          "plan.name":that.plan[that.index].plan.name
        },this.plan[this.index],{},function (err, numReplaced) {})
        this.curWorkIndex=this.findCurrentPro()

       

       //云同步代码
       if(this.$store.state.dbName=='./cloud.db'){
        
        let promise=new Promise(function(resolve,reject){
          let notes=[]
          db.find({type:'planItemNotes',plan:that.plan[that.index].plan.name},function(err,Docs){
                        if(Docs.length!=0){
                            notes.push(Docs[0])
                        }else{
                            notes=[{
                                type:'planItemNotes',
                                plan:that.plan[that.index].plan.name,
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
          plan:JSON.stringify([that.plan[that.index]]),
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

      changePlan(i){
        this.index=i
      },

      changeSex(){
        if(this.src==this.b_src){
          this.src==this.g_src
        }else{
          this.src=this.b_src
        }
      }
    },

    watch:{
        'index':{
          handler:function(val){
             this.findCurrentPro()
          },
          immediate:true,
          deep:true
        }
      }
  }
</script>

<style>

 .el-table .warning-row {
    background: oldlace;
  }
  .el-table .success-row {
    background: #f0f9eb;
  }
</style>

<style scoped>

 .el-table .warning-row {
    background: rgb(207, 161, 75);
  }



  .transition-box {
    margin-bottom: 10px;
    width: 200px;
    height: 100px;
    border-radius: 4px;
    background-color: #409EFF;
    text-align: center;
    color: #fff;
    padding: 40px 20px;
    box-sizing: border-box;
    margin-right: 20px;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }
  
  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }
  
  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }


.turnPage:hover{
  cursor: pointer;
}

.processChart{
  width:100%;
  height:100%
}

.basicInfo{
  float: left;
  margin-left: 1%;
  margin-top: 5px;
  height: 50px;
  width: 83%;
}

.item:hover{
  cursor: pointer;
}
.item{
  margin-top: 15%;
  height:30px;
  width:115%;
  margin-left:-8%;
  padding: 0;
  text-align: center;
  padding-top: 7px;
}
.item >>> .el-card__body{
  padding: 0;
}

.planButton{
  margin-top: 15%;
  height:30px;
  width:110%;
  margin-left: -6%;
  padding: 0;
  text-align: center;
  padding-top: 7px;
}
.planButton >>> .el-card__body{
  padding: 0;
}

.planButton:hover{
  cursor: pointer;
}


.basicInfo >>> .el-card__body{
  padding: 12px;
}

.planBasicInfo{
  margin-left:5px;
  font-size:16px;
  color:#23507e;
  font-weight:bold
}

.planBasicInfo:hover{
  cursor: pointer;
}

.moreInfo{
  float: left;
  margin-left: 1%;
  margin-top: 5px;
  height: 87%;
  width: 20%;
}

.processInfo{
  float: left;
  margin-left: 1%;
  margin-top: 5px;
  height: 87%;
  width: 61.7%;
}

.head:hover{
  cursor: pointer;
}

.main{
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   height:100%
}



</style>