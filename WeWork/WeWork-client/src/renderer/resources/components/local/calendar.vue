<template>
<div class="bodyDiv">

<div class="header">
    <h3 class='h3'>WeWork</h3>
    
    <el-dropdown>
     <span  class="el-dropdown-link hover">
        {{planSlected}}<i class="el-icon-arrow-down el-icon--right"></i>
      </span>

    <el-dropdown-menu class="hover" v-if="allPlanName.length!=0" slot="dropdown">
    <el-dropdown-item @click.native="select(item)" v-for="(item,i) in allPlanName" :key="i">{{item}}</el-dropdown-item>
    </el-dropdown-menu>
    </el-dropdown>

</div>

<el-card style="height:100%;overflow-y:auto">

<el-calendar v-model="value" class="calendar" :key="rf">
  <template
    slot="dateCell"
    slot-scope="{date, data}">
    <p :class="getDateColor(data.day)" >
       {{ data.day.split('-').slice(1).join('-') }}<br> 
    </p>

 <!-- 已修改 -->

   <!-- the mark of changePoint --> 
   <el-popover
    v-if="isChangePoint(data.day)"
    placement="left"
    width="240"
    trigger="hover">
     <el-table :data="gridData" stripe>
    <el-table-column width="80" property="status" label="状态"></el-table-column>
    <el-table-column width="60" property="time" label="时间"></el-table-column>
    <el-table-column width="100" property="task" label="任务"></el-table-column>
  </el-table>
    <img slot='reference' @mouseover=" getGridData(data.day)" style="width:20px;height:20px;float:left;margin-left:5%" src="../../../assets/changePoint.svg" />
  </el-popover>
  <el-popover
    v-if="isStart(data.day)"
    placement="top-start"
    title="项目状态"
    width="70"
    trigger="hover"
    content="项目开始"
    >
    <i slot="reference" class="el-icon-s-flag" style="color:blue"></i>
    </el-popover>
  
   <el-popover
    v-if="isEnd(data.day)"
    placement="top-start"
    title="项目状态"
    width="70"
    trigger="hover"
    content="项目结束"
    >
     <i  slot="reference" class="el-icon-s-flag" style="color:red"></i>
    </el-popover>
  
  </template>
</el-calendar>
</el-card>


</div>
</template>
<script>
var database =require('nedb')
let db=[]
const ipc=require('electron').ipcRenderer
export default {
    created(){
        let that=this
        ipc.on('changeDb',function(event,arg){
            db=new database({
                filename:arg,
                autoload:true
            })
            that.load()
        })
    },
    data () {
        return {
            allPlan:new Map(),
            allPlanName:[],

            value:new Date(),
            plan:{},
            process:[],

            syear:0,
            smonth:0,
            sday:0,
            eyear:0,
            emonth:0,
            eday:0,
            thisYear:0,
            thisMonth:0,
            thisDay:0,


            changePointMap:new Map(),
            ProjectMap:new Map(),
            gridData: [],

            planSlected:'选择计划',
            rf:0
        }
    },
   
    methods:{
     select(name){
         this.planSlected=name
     },
     load(){
        let that=this
        this.allPlan=new Map()
        this.allPlanName=[]
        this.planSlected='选择计划'

        db.find({type:"planItemTest2"},function(err,Docs){
            if(Docs.length!=0){
                Docs.forEach(function(item,index){
                    that.allPlan.set(item.plan.name,item)
                    that.allPlanName.push(item.plan.name)
                })
            }
        })
    },
      loadSelected (name){
                     
                    this.gridData=[] 
                    this.ProjectMap=new Map()
                    this.changePointMap=new Map()

                    if(this.allPlan.has(name)){
                         this.plan=this.allPlan.get(name).plan
                         this.process=this.allPlan.get(name).process
                    }else{
                        this.plan={}
                        this.process={}
                        return 
                    }
                    let that=this
        
                    let DateArray=this.plan.startDate.split(/\s*[^0-9]\s*/g)
                    this.syear=parseInt(DateArray[0])
                    this.smonth=parseInt(DateArray[1])
                    this.sday=parseInt(DateArray[2])

                    DateArray=this.plan.endDate.split(/\s*[^0-9]\s*/g)
                    this.eyear=parseInt(DateArray[0])
                    this.emonth=parseInt(DateArray[1])
                    this.eday=parseInt(DateArray[2])

                    let today=new Date()
                    this.thisYear=today.getFullYear()
                    this.thisMonth=today.getMonth()+1
                    this.thisDay=today.getDate()

                    //store all the processTimePoint
                    this.process.forEach(function(item,index){
                        if(index==0){
                            that.SetProject(that.ProjectMap,that.getDateStanderedFormat(item.startDate),1)
                            return
                        }else if(index==that.process.length-1){
                            that.SetProject(that.ProjectMap,that.getDateStanderedFormat(item.startDate),2)
                            return
                        }
                        that.Set(that.changePointMap,that.getDateStanderedFormat(item.startDate),item,1)
                        that.Set(that.changePointMap,that.getDateStanderedFormat(item.endDate),item,2)
                    })

                    this.rf=(this.rf+1)%5
                   
        },
        //set the array value of changePointMap
        Set(map,format,process,flag){
           let tmp=[]
           let grdata = {}
          if(flag==1){
            grdata['status'] = 'START'
            grdata['time'] = process.startTime.substring(0,5)
            grdata['task'] = process.pro_text.substring(0,10)
          
          }else{
            grdata['status'] = 'END'
            grdata['time'] = process.endTime.substring(0,5)
            grdata['task'] = process.pro_text.substring(0,10)

          }
          if(map.has(format)){
              tmp=map.get(format)
          }
          tmp.push(grdata)
          map.set(format,tmp)
        }, 
        SetProject(map,format,flag){
             let grdata = {}
             let tmp=[]
             if(flag==1){
                grdata['status'] = 'START'
             }else{
                 grdata['status'] = 'END'
             }
            if(map.has(format)){
              tmp=map.get(format)
          }
          tmp.push(grdata)
          map.set(format,tmp)  
        },
        typeOf(obj){
            return typeof(obj)
        },
        goBack(){
            let that=this
             this.$router.push({
            name:'workbenchPlan',
            params:{
              name:that.plan.name
            }
          })
        },
        isOutside(DateArray){
            let year=parseInt(DateArray[0])
            let month=parseInt(DateArray[1])
            let day=parseInt(DateArray[2])
            if(year>=this.syear&&year<=this.eyear&&
            month>=this.smonth&&month<=this.emonth&&
            day>=this.sday&&day<=this.eday){
                return  true
            }
            return false
        },
        isChangePoint(date){
            if(this.changePointMap.has(date)){
                return true
            }else{
                return false            
            }
        },
        showChangePointContent(date){
            let tmp=this.changePointMap.get(date)
            let str=''
            for(let i=0;i<tmp.length;i++){
                str+=(tmp[i]+'，')
            }
            return str.substr(0,str.length-1)
        },
        getDateColor(date){
            let DateArray=date.split(/\s*[^0-9]\s*/g)
            if(DateArray[0]==this.thisYear&&
               DateArray[1]==this.thisMonth&&
               DateArray[2]==this.thisDay){
                   return 'today'
               }
            if(this.isOutside(DateArray)){
                return 'inside'
            }
            return 'outside'
        },

        getDateStanderedFormat(date){
            let DateArray=date.split(/\s*[^0-9]\s*/g)
            let year=parseInt(DateArray[0])
            let month=parseInt(DateArray[1])
            let day=parseInt(DateArray[2])
            return year+'-'+(month<=9?('0'+month):month)+'-'+(day<=9?('0'+day):day)
        },
        Update_noteText(date){
            let temText = this.notes.dailyNotes[date+""];
            if(temText.length > 15){
                    temText = temText.substr(0,14);
                    this.noteText_brief = temText + "...";
            }else{
                this.noteText_brief = temText;
            }
        },
        getGridData(date){
            let temp = this.changePointMap.get(date+'')
            let exchange = null
            for(let i = 0;i < temp.length -1;i++){
               for(let j = 0;j < temp.length - i-1;j++){
                      if(temp[j].time > temp[j+1].time){
                        exchange = temp[j]
                        temp[j] = temp[j+1]
                        temp[j+1] = exchange
                      }
               }
            }
            this.gridData = temp
        },
        isStart(date){
          let tmp = null
          let flag = false
          tmp = this.ProjectMap.get(date+'')
            if(tmp==undefined){
            return false
          }
          tmp.forEach(function(item){
            console.log('en')
              if(item.status == "START"){
                  flag = true
              }
          })
          return flag
        },
        isEnd(date){
          let tmp = null
           let flag = false
          tmp = this.ProjectMap.get(date+'')
          if(tmp==undefined){
            return false
          }
          tmp.forEach(function(item){
              if(item.status == "END"){
                flag = true
              }
          })
          return flag
        }
    },
    watch: {
        "planSlected":{
            handler:function(val){
                this.loadSelected(val)
            }
        }
    },

}
</script>
<style>

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
  color:azure;
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


  .inside {
    color: #23ac01;
  }
  .outside {
    color: #b6b6b6;
  }
  .today{
    color: rgb(48, 141, 247);
  }
  
  .hover:hover{
    cursor: pointer;
  }

</style> 