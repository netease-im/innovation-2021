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
    content="存在日志记录">
    <img slot='reference' style="width:20px;height:20px;float:left;margin-left:5%" src="../assets/note.svg" />
    </el-popover>
 

   <!-- the mark of changePoint --> 
   <el-popover
    v-if="isChangePoint(data.day)"
    placement="top-start"
    title="计划状态"
    width="150"
    trigger="hover"
    :content="showChangePointContent(data.day)">
    <img slot='reference' style="width:20px;height:20px;float:left;margin-left:5%" src="../assets/changePoint.svg" />
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
    <el-button type="primary" @click="saveNote">保 存</el-button>
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
            dateSelected:'',
            NoteDialogVisible:false,

            changePointMap:new Map()
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
                    this.process.forEach(function(item,index){
                        if(index==0){
                            that.Set(that.changePointMap,that.getDateStanderedFormat(item.startDate),item.pro_name)
                            return
                        }else if(index==that.process.length-1){
                            that.Set(that.changePointMap,that.getDateStanderedFormat(item.startDate),item.pro_name)
                            return
                        }
                        that.Set(that.changePointMap,that.getDateStanderedFormat(item.startDate),'流程'+item.id+'开始')
                        that.Set(that.changePointMap,that.getDateStanderedFormat(item.endDate),'流程'+item.id+'结束')
                    })
    },
    methods:{
        //set the array value of changePointMap
        Set(map,format,process){
            let tmp=[]
            if(map.has(format)){
                tmp=map.get(format)
            }
            tmp.push(process)
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
        }
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