<template>
    <div class="bodyDiv" >

    <div class="header">
    <h3 class='h3'>MyWork</h3>
    </div>

    <el-card class="card"  shadow="never">
     <div slot="header">
     <span style="color:#1E90FF">今日任务</span>
     <img @click="reload" class="hover" style="height:20px;float:right" src="../../../assets/refresh.svg" />
     </div>

      <el-table
      :data="todayPro"
      style="width: 100%;height:100%"
      class="jobTable"
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
    </div>
</template>

<script>
var database =require('nedb')
var db=[]
const ipc=require('electron').ipcRenderer
export default {
      created(){
          let that=this
          ipc.on('changeDb',function(event,arg){
            db=new database({
              filename:arg,
              autoload:true
          })
          that.reload()
          })
        },
        data(){
            return {
                plan:[],
                todayPro:[],
            }
        },
        methods:{
          showTodayPro(){
            ipc.send('openMainAndShowTodayPro')
          },
          getDateArr(date){
            console.log(date)
            return date.split(/\s*[^0-9]\s*/g)
          },
          reload(){
            this.plan=[]
            this.todayPro=[]
            let that=this
            db.find({type:"planItemTest2"},function(err,Docs){
              if(Docs.length!=0){
                   let today=new Date()
                   let year=today.getFullYear()
                   let month=today.getMonth()+1
                   let day=today.getDate()

                  that.plan=Docs
                  that.plan.forEach(function(item,i){
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
              }
           })
          }
        },
}
</script>



<style scoped>

.header {
    height: 28px;
    padding: 2px;
    background-color:rgb(90,40,250);
    -webkit-app-region: drag;
    text-align: center;
     font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.h3{
  margin-top:5px;
  color:azure
}

.bodyDiv{
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: 0 1px 6px 1px rgba(0, 0, 0, 0.3);
  justify-content: space-between;
  overflow: hidden;
  position: relative;
  background-color:rgb(230, 240, 255);
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
.card{
  height:100%
}
.el-card{
  border-radius: 0;
}

.drag{
  -webkit-app-region: drag;
}

.hover:hover{
  cursor: pointer;
}

.jobTable>>>.el-table__body-wrapper:hover{
    cursor: pointer;
}

</style>