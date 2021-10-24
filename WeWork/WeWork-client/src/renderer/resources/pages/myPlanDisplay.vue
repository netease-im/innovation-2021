<template>
<div v-if="plan!=null" class="main">


<el-card>
<el-page-header @back="goBack" content="计划详情">
</el-page-header>
</el-card>

<el-card>
<el-descriptions title="计划信息">
    <el-descriptions-item label="计划名称">{{plan.plan.name}}</el-descriptions-item>
    <el-descriptions-item label="计划性质">{{plan.plan.type}}</el-descriptions-item>
    
    <el-descriptions-item :label="plan.plan.type=='个人计划'?'执行人':'负责人'">
    <el-tag size="mini">{{plan.plan.manager.name}}</el-tag>
    </el-descriptions-item>

</el-descriptions>

<el-descriptions>
<el-descriptions-item label="计划时间">{{plan.plan.startDate+" "+
    plan.plan.startTime+" 至 "+plan.plan.endDate+" "+plan.plan.endTime}}
</el-descriptions-item>
</el-descriptions>

<el-descriptions :column="1" v-if="plan.plan.type=='小组计划'">
<el-descriptions-item label="小组成员">
      <div v-if="plan.plan.type=='小组计划'&&plan.plan.member.length>0">
      <el-tag style="margin-right:5px" size="mini" :type="item.host?'':'success'" v-for="(item,i) in plan.plan.member" :key="i">{{item.name}}</el-tag>
      </div>

      <div v-if="plan.plan.type=='小组计划'&&plan.plan.member.length==0">
      <span>无</span>
      </div>      
</el-descriptions-item>
</el-descriptions>

<el-descriptions>
<el-descriptions-item label="计划描述">{{plan.plan.desc}}</el-descriptions-item>
</el-descriptions>

</el-card>


<el-collapse  v-model="activeName" accordion v-for="(item,i) in plan.process" :key="i">
  <el-collapse-item :title="'任务'+(i)+'：'+item.pro_name" :name="i+1" v-if="i!=0&&i!=plan.process.length-1">
    
    <div>任务时间：{{item.startDate+" "+
    item.startTime+" 至 "+item.endDate+" "+item.endTime}}
    </div>
    
    <div>任务描述：{{item.pro_text}}</div>

    <div v-if="plan.plan.type=='小组计划'">
    任务参与人：

    <el-tag size="mini" type="info" v-if="!item.hasOwnProperty('proMem')||item.proMem.length==0">
    未指定
    </el-tag>

    <el-tag style="margin-right:5px" size="mini" :type="mem.host?'':'success'" v-for="(mem,i) in item.proMem" :key="i">
    {{mem.name}}
    </el-tag>
    </div>

    <div>任务状态：
    <el-tag v-if="item.status==0" type="danger" size="mini">未开始</el-tag>
    <el-tag v-if="item.status==1" type="warning" size="mini">进行中</el-tag>
    <el-tag v-if="item.status==2" type="success" size="mini">已完成</el-tag>
    </div>

  </el-collapse-item>
</el-collapse>

<el-card style="padding-bottom:2%">

  <div class="block">
    <span style="font-weight:bold" class="demonstration">查看日志</span>
    <br><br>
    <el-date-picker
      v-model="logDate"
      size="large"
      align="right"
      type="date"
      placeholder="选择日期"
      value-format="yyyy-MM-dd"
      :picker-options="pickerOptions"
      @change="chgTime">
    </el-date-picker>
    <br>  <br>
    <img src="../../assets/calendar.svg" style="width:70%"/>
  </div>
 <el-card  class='logText'>
  <el-input
  type="textarea"
  :rows="5"
  placeholder="日志信息为空"
  :readonly="true"
  v-model="planLog">
  </el-input>
 </el-card>

</el-card>

    
  <!-- 计划总结部分 -->
  <el-card>
  
  <span style="font-weight:bold;" class="demonstration">计划总结</span>
  
  <div style="margin-top:1%">
  <div style="height:10%"  v-for="(item,i) in notesTitle" :key="i">
    
    <el-card class="planDisplay" @click.native=" toPlanNote(item)"  shadow='hover'>
    
    <div class="planBasic">

    <el-popover 
    placement="right-end"
    width="200"
    trigger="hover"
    :content="item"
    v-if="item.length>10">
    <span slot="reference" style="font-size:20px;color: rgb(87, 171, 226)"  >{{item.length>10?item.substr(0,10)+'...':item}}</span>
    </el-popover>

     <span v-if="item.length<=10" style="font-size:20px;color: rgb(87, 171, 226)"  >{{item}}</span>

    <img  style="width:30px;height:30px;float:left;margin-right:2%" src="../../assets/note_item.svg" />
    </div>

    </el-card>
    </div>
    </div>
    </el-card>




<el-card>

 <span style="font-weight:bold" class="demonstration">计划时间线</span>
 <br><br>
 <el-timeline>

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

</div>
</template>


<script>
var database =require('nedb')
var db=[]
  export default {
    
    methods:{
        toPlanNote(name){
          let that=this
          let content=that.notes.planNotes[name]
          this.$router.push({
                name:'markdown',
                params:{
                    plan_name:this.plan.plan.name,
                    edit:true,
                    preview:true,
                    plan:this.plan,
                    context:content,
                    title:name
                }
            })
        },
        chgTime(val){
            if(this.notes.dailyNotes.hasOwnProperty(val)){
                this.planLog=this.notes.dailyNotes[val]
            }else{
                this.planLog=''
            }
        },
        goBack(){
            this.$router.push('/myPlan')
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
        dateFormat(date){
             let arr=date.split(/\s*[^0-9]\s*/g)
             return arr[0]+'/'+arr[1]+'/'+arr[2]
        }
    },

    created (){
      this.plan=this.$route.params.plan
      let that=this
      db=new database({
        filename:that.$store.state.dbName,
        autoload:true
      })

      db.find({type:"planItemNotes",plan:that.plan.plan.name},function(err,Docs){
                   if(Docs.length!=0){
                       that.notes=Docs[0]
                   }
                   if(that.notes.hasOwnProperty('planNotes')){
                    that.notesTitle=Object.keys(that.notes.planNotes)
                   }
                })


        //getTimeLine schedule
        this.plan.process.forEach(function(item,i){
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

            this.timeLineMap.forEach(function(value,key){
                      value.sort((a,b)=>{return that.compareTime(a.time,b.time)})
　　　　　　　　　　　　that.timeLine.push({
                            date:key,
                            event:value
                      })
　　　　　　});
            
            this.timeLine.sort((a,b)=>{return that.compareDate(a.date,b.date)})
    },
    data() {
      return {
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周前',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },


        activeName: '1',
        plan:null,
        logDate:'',
        notes:{},
        planLog:'',
        timeLineMap:new Map(),
        timeLine:[],
        notesTitle:[]
      };
    }
  }
</script>

<style scoped>
.main{
    height:100%;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.el-collapse >>> .el-collapse-item__header{
    padding-left:2% ;
}

.el-collapse >>> .el-collapse-item__content{
     padding-left:2% ;
}

.block{
    float:left;
    width:23%;
}

.logText{
    float: left;
    width: 70%;
    margin-left: 5%;
}


.planDisplay{
    width: 99%;
    height: 100%;
    margin: 1px auto;
}

.planDisplay:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.planDisplay >>> .el-card__body{
     padding: 5px;
}


</style>