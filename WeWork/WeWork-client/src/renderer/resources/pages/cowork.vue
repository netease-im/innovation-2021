<template>
    <div class="main">
     <img v-if="empty" src="../../assets/empty.svg" style="height:90%;width:90%" />

    <div style="height:100%" v-if="cowork!=null">

    <el-card style="width:99%;margin:auto"> 
     <span style="font-weight:bold;font-size:20px;color:rgb(87, 171, 226)"  >
     合作计划
     </span>
    <el-button @click="refreshCowork" style="float:right" 
               type="primary" icon="el-icon-refresh" size="small" plain>
    刷新
    </el-button>
    </el-card>
    
    <div style="height:110px"  v-for="(item,i) in cowork" :key="i">
    <el-card class="planDisplay" @click.native="coworkDisplay(item)" shadow='hover'>
    
    <div class="planBasic">

    <el-popover 
    placement="right-end"
    width="200"
    trigger="hover"
    :content="item.plan.plan.name"
    v-if="item.plan.plan.name.length>8">
    <span slot="reference" style="font-size:20px;color: rgb(87, 171, 226)"  >
    {{item.plan.plan.name>8?
    item.plan.plan.name.substr(0,8)+'...':
    item.plan.plan.name}}</span>
    </el-popover>

     <span v-if="item.plan.plan.name.length<=8" 
     style="font-size:20px;color: rgb(87, 171, 226)"  >
     {{item.plan.plan.name}}</span>
    
    <span style="font-size:15px;width:100%;float:left"  >合作计划</span>
    <img  style="width:30px;height:30px;float:left;margin-top:5%" src="../../assets/myPlanItem.svg" />
    </div>
    
    <div class="planExcutor">

    <span style="font-size:15px; color: rgb(105, 192, 148)"  >计划负责人:</span>
    <span style="font-size:15px"  >
    {{item.plan.plan.manager.email+"("+item.plan.plan.manager.name+")"}}
    </span>
    <br>
    </div>


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

        db.find({type:"cowork"},function(err,Docs){
                   if(Docs.length!=0){
                     that.cowork=JSON.parse(JSON.stringify(Docs))
                   }else{
                       that.empty=true
                   }
                })
   },
   methods:{
       coworkDisplay(cowork){
           this.$router.push({
               name:'coworkDisplay',
               params:{
                   cowork:cowork
               }
           })
       },
       refreshCowork(){
           let that=this
           that.$http.post(that.$store.state.contextUrl+'/downloadCowork',that.$qs.stringify({
               partner:that.$store.state.user_id
               })).then(function(res){
           db.remove({type:'cowork'},{multi: true },
           function (err, numRemoved) {
               let cowork=[]
               res.data.forEach(function(item,index){
                   item.type='cowork'
                   item.plan=JSON.parse(item.plan)
                   item.planLog=JSON.parse(item.planLog)
                   cowork.push(item)
                })
                   db.insert(cowork)
                   that.$message({
                       message:'刷新成功',
                       type:'success'
                   })
                   that.logining=false
                   that.reload()
                   that.$store.state.syncing=false
                })
             })
          },
   },
   data(){
       return{

           // structure:{
           //    type:'cowork',
           //    plan
           //    plan_name
           //    partner
           //    host
           //    planLog
           // }
           cowork:null,
           empty:false
       }
   },
   inject:['reload']
}
</script>

<style scoped>
.main{
    width:100%;
    height: 100%;
    padding-top: 3px;
    box-sizing: border-box;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.planDisplay{
    width: 99%;
    height: 100%;
    margin: auto;
}

.planDisplay >>> .el-card__body{
  padding: 15px;
}

.planDisplay:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.planBasic{
    width: 20%;
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