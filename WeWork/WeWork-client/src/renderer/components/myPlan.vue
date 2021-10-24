<template>
    <div class="main">
     <img v-if="empty" src="../assets/empty.svg" style="height:90%;width:90%" />
    <div style="height:100%" v-if="plan.length!=0">

    <div style="height:110px"  v-for="(item,i) in plan" :key="i">
    <el-card class="planDisplay" @click.native="PlanDisplay(item)" shadow='hover'>
    
    <div class="planBasic">

    <el-popover 
    placement="right-end"
    width="200"
    trigger="hover"
    :content="item.plan.name"
    v-if="item.plan.name.length>7">
    <span slot="reference" style="font-size:20px;color: rgb(87, 171, 226)"  >{{item.plan.name.length>7?item.plan.name.substr(0,7)+'...':item.plan.name}}</span>
    </el-popover>

     <span v-if="item.plan.name.length<=7" style="font-size:20px;color: rgb(87, 171, 226)"  >{{item.plan.name}}</span>
    
    <span style="font-size:15px;width:100%;float:left"  >{{item.plan.type}}</span>
    <img  style="width:30px;height:30px;float:left;margin-top:5%" src="../assets/myPlanItem.svg" />
    </div>
    
    <div class="planExcutor">

    <span style="font-size:15px; color: rgb(105, 192, 148)"  >{{item.plan.type=='个人计划'?'执行人:':'负责人:'}}</span>
    <span style="font-size:15px"  >{{item.plan.type=='个人计划'?item.plan.excutor.name:item.plan.manager.name}}</span>
    <br>
    <span style="font-size:15px; color: rgb(105, 192, 148)">计划时间:</span>
    <span style="font-size:15px"  >{{item.plan.startDate+" "+
    item.plan.startTime+" 至 "+item.plan.endDate+" "+item.plan.endTime}}</span>
    </div>
    
    <el-popover 
    placement="right-end"
    width="200"
    trigger="hover"
    :content="planStatusShow(item.process)">
    <el-progress slot="reference"  style="float:right;margin-right:5%" type="circle" :width="80" :status="planStatus(item.process)" :percentage="progress(item.process)"></el-progress>
    </el-popover>

    </el-card>
    </div>


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
                   }else{
                       that.empty=true
                   }
                })
   },
   methods:{
       PlanDisplay(item){
           this.$router.push({
               name:'myPlanDisplay',
               params:{
                   plan:item
               }
           })
       },
       progress(pro){
           let total=pro.length-2
           let finished=0
           for(let i=1;i<pro.length-1;i++){
               if(pro[i].status==2){
                   finished++
               }
           }
           return parseInt(finished/total*100)
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
                   return 
               }else{
                   return 'exception'
               }
           }
       },
       planStatusShow(pro){
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
               return '计划已完成'
           }else {
               if(isStart){
                   return '计划进行中'
               }else{
                   return '计划未开始'
               }
           }

       }
   },
   data(){
       return{
           plan:[],
           empty:false
       }
   }
}
</script>

<style scoped>
.main{
    width:100%;
    height: 100%;
    padding-top: 3px;
    box-sizing: border-box;
}

.planDisplay{
    width: 99%;
    height: 100%;
    margin: auto;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.planDisplay >>> .el-card__body{
  padding: 15px;
}

.planDisplay:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.planBasic{
    width: 17%;
    float: left;
}

.planExcutor{
    width: 60%;
    margin-top: 2%;
    margin-left: 7%;
    float: left;
}

.searchArea{
    width: 99%;
    margin: auto;
}

</style>