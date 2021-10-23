<template>
<div style="overflow-y:hidden">
<el-card style="height:100%;overflow-y:auto">
<el-page-header @back="goBack" content="计划日志">
</el-page-header>
<el-divider></el-divider>

<el-calendar v-model="value" v-if="typeOf(notes.dailyNotes)!='undefined'">
  <template
    slot="dateCell"
    slot-scope="{date, data}">
    <p :class="getDateColor(data.day)" >
       {{ data.day.split('-').slice(1).join('-') }}<br> 
       <!-- {{ hasNote(data.day) ? '✔️' : ''}} -->
    </p>

    <el-popover
    v-if="hasNote(data.day)"
    placement="top-start"
    title="日志提示"
    width="150"
    trigger="hover"
    >
    <span>{{noteText_brief}}</span>
    <img slot='reference'  @mouseover="Update_noteText(data.day)" style="width:20px;height:20px;float:left;margin-left:5%" src="../assets/note.svg" />
    </el-popover>
 <!-- 已修改 -->

   <!-- the mark of changePoint --> 
   <el-popover
    v-if="isChangePoint(data.day)"
    placement="left"
    width="240"
    trigger="hover">
    <!-- :content="showChangePointContent(data.day)"> -->
     <el-table :data="gridData" stripe>
    <el-table-column width="80" property="status" label="状态"></el-table-column>
    <el-table-column width="60" property="time" label="时间"></el-table-column>
    <el-table-column width="100" property="task" label="任务"></el-table-column>
  </el-table>
    <img slot='reference' @mouseover=" getGridData(data.day)" style="width:20px;height:20px;float:left;margin-left:5%" src="../assets/changePoint.svg" />
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

<el-dialog title="日志信息" :visible.sync="NoteDialogVisible"  top="15%">
    <span>{{'日期：'+dateSelected}}</span>
    <el-input
    style="margin-top:3%"
    type="textarea"
    placeholder="请输入日志内容"
    v-model="noteText"
    maxlength="300"
    show-word-limit
    rows='8'
    >
    </el-input>
    <div slot="footer" class="dialog-footer">
    <el-button @click="NoteDialogVisible = false">返 回</el-button>
    <el-button type="primary" @click="saveNote()">保 存</el-button>
  </div>
</el-dialog>

</div>
</template>
<script>
var database =require('nedb')
let db=[]
export default {
    data () {
        return {
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

            notes:[],
            noteText:'',
            noteText_brief:'',
            dateSelected:'',
            NoteDialogVisible:false,

            changePointMap:new Map(),
            ProjectMap:new Map(),
            gridData: []
        }
    },
    created (){
             this.plan=this.$route.params.plan
                    this.process=this.$route.params.process
                    let that=this
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
                                plan:that.plan.name,
                                dailyNotes:{}
                            }
                            db.insert(that.notes)
                        }
                    })
                  
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
                    that=this
                    // getDateStanderedFormat return 20xx-xx-xx format date
                    /*
                    第一个要获取项目的名称，获取项目开始的日期，项目结束的日期
                    这里可以另外设立一个map数据结构
                    第二个就是要存储如图的数据结构
                     gridData: [{
                      status:'END',
                      time:'8:00',
                      task:'作业1'
                    },{
                      status:'END',
                      time:'8:00',
                      task:'作业1'
                    }]
                    可以先用map 用日期作为键，上图对象数组为value
                    再从后面提取
                    */
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
                   
    },
    methods:{
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
        async saveNote(){
            let that=this
            this.notes.dailyNotes[this.dateSelected]=this.noteText
            await db.update({type:'planItemNotes',plan:that.plan.name},this.notes,function(err,numReplaced){
            })
             this.$message({
                     message:'保存成功',
                     type:'success'
                     })

       
       //云同步代码
       if(this.$store.state.dbName=='./cloud.db'){
           
          that.$store.state.syncing=true
          that.$http.post(that.$store.state.contextUrl+'/uploadPlan',that.$qs.stringify({

          user_id:that.$store.state.user_id,
          plan:JSON.stringify([{
              type:"planItemTest2",
              plan:that.plan,
              process:that.process
          }]),
          planLog:JSON.stringify([that.notes])

        })).then(function(res){
          if(res.data=='fail'){
            that.$message({
              message:'云端同步失败，请检查网络设置',
              type:'error'
            })
          }
          that.$store.state.syncing=false
        })
        this.reload()
    }
  
  },
        hasNote(date){
            if(this.notes.dailyNotes.hasOwnProperty(date)){
                return true
            }else{
                return false
            }
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
            'value':{
                handler:function(newValue, oldValue){
                 this.noteText=''   
                 if(JSON.stringify(newValue)!=JSON.stringify(oldValue)){
                     return
                 }
                 let year=newValue.getFullYear()
                 let month=newValue.getMonth()+1
                 let day=newValue.getDate()
                 this.dateSelected=year+'-'+(month<=9?('0'+month):month)+'-'+(day<=9?('0'+day):day)
                 let DateArray=this.dateSelected.split(/\s*[^0-9]\s*/g)
                 if(!this.isOutside(DateArray)){
                     this.$message({
                         message:'不在计划时间范围内',
                         type:'warning'
                     })
                     return
                 }
                 
                 let date=new Date()
                 let today_year=date.getFullYear()
                 let today_month=date.getMonth()+1
                 let today_day=date.getDate()

                 if(!(DateArray[0]<=today_year&&DateArray[1]<=today_month&&DateArray[2]<=today_day)){
                      this.$message({
                         message:'不能操作未来日期',
                         type:'warning'
                     })
                     return
                 }


                 if(this.notes.dailyNotes.hasOwnProperty(this.dateSelected)){
                     this.noteText=this.notes.dailyNotes[this.dateSelected]
                 }
                 this.NoteDialogVisible=true
                }
            }
        },
        inject:['reload']
    }
</script>
<style>
  .inside {
    color: #23ac01;
  }
  .outside {
    color: #b6b6b6;
  }
  .today{
    color: rgb(48, 141, 247);
  }
 
</style> 