<template>
<div class="main">
<el-card style="height:100%;overflow-y:auto">
<el-card class="plan-card" >
<h3 style="color:#1E90FF">创建计划</h3>
  <el-form ref="form" :model="form" label-width="auto">
  <el-form-item class="span"  label="计划名称">
    <el-input v-model="form.name"></el-input>
  </el-form-item>
  <el-form-item class="span" label="计划时间">
    <el-col :span="11">
      <el-date-picker  value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择开始日期" v-model="form.startDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择开始时间" v-model="form.startTime" style="width: 100%;"></el-time-picker>
    </el-col>
    <el-col style="margin-top:15px" :span="11">
      <el-date-picker  value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择结束日期" v-model="form.endDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-top:15px;margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择结束时间" v-model="form.endTime" style="width: 100%;"></el-time-picker>
    </el-col>
  </el-form-item>
  <el-form-item  label="计划性质">
    <el-radio-group v-model="form.type">
      <el-radio label="小组计划"></el-radio>
      <el-radio label="个人计划"></el-radio>
    </el-radio-group>
  </el-form-item>
  
  <el-form-item  label="计划执行人" v-if="form.type=='个人计划'">
    <el-tag @click.native="editMem(host)" size="medium" class="pointer" style="font-size:15px;margin-right:5px">{{host.name}}</el-tag>
  </el-form-item>
  
  <el-form-item  label="小组负责人" v-if="form.type=='小组计划'">
  
   <el-tag @click.native="editMem(host)" size="medium" class="pointer" style="font-size:15px;margin-right:5px">{{host.name}}</el-tag>

  </el-form-item>

  <el-form-item  label="小组成员" v-if="form.type=='小组计划'">
   <el-tag :key="i" 
    :type="item.host?'':'success'"
    v-for="(item,i) in form.member" 
    size="medium" 
    class="pointer" 
    style="font-size:15px;margin-right:5px"
    :closable="!item.host"
    @close="removeMem(i)"
    @click.native="editMem(item)">
   {{item.name}}
   </el-tag>
   <el-tag size="medium" type="warning"  @click.native="addMember" class="pointer" style="font-size:15px">+</el-tag>
  </el-form-item>


  <el-form-item label="计划描述">
    <el-input type="textarea" v-model="form.desc"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="success" @click="createPlan">立即创建</el-button>
    <el-button type="warning" @click="openPro">子任务</el-button>
    <el-button type="info" @click="refresh">重置</el-button>
  </el-form-item>
  </el-form>
  </el-card>

  <!-- 指定计划参与人 -->
  <el-dialog title="添加参与人" v-if="addMemberTemp!=null"  :visible.sync="addMemberVisible"
           :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
  <el-form label-width="auto">
    <el-form-item label="名字" >
      <el-input v-model="addMemberTemp.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" >
       <el-input v-model="addMemberTemp.email" autocomplete="off"></el-input>
    </el-form-item>
     <el-form-item label="手机" >
       <el-input v-model="addMemberTemp.phone" autocomplete="off"></el-input>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button type="warning" @click="contactVisible=true">联系人列表</el-button>
    <el-button @click="cancleAddMem">取 消</el-button>
    <el-button type="primary" @click="confirmAddMem">确 定</el-button>
  </div>
  </el-dialog>
  </el-card>


<!-- 修改参与人信息 -->
<el-dialog title="人员信息" v-if="memberSlectedVisible"  :visible.sync="memberSlectedVisible">
  <el-form label-width="auto">
    <el-form-item label="名字" >
      <el-input v-model="memberSlected.name" autocomplete="off" :readonly="memberSlected.host"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" >
       <el-input v-model="memberSlected.email" autocomplete="off" :readonly="memberSlected.host"></el-input>
    </el-form-item>
     <el-form-item label="手机" >
       <el-input v-model="memberSlected.phone" autocomplete="off" :readonly="memberSlected.host"></el-input>
    </el-form-item>
  </el-form>
   <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="memberSlectedVisible=false">确 定</el-button>
  </div>
</el-dialog>


<!-- 子任务窗口 -->
<el-dialog width="950px" top="5vh" title="子任务" v-if="proVisible"  :visible.sync="proVisible">

<el-table height="470px" :data="process">
<el-table-column prop="id" label="任务序号"></el-table-column>
<el-table-column prop="pro_name" label="任务名"></el-table-column>
<el-table-column>
<template slot-scope="scope">
<el-button type="warning" size="small" @click="editPro(scope.row)">编辑</el-button>
<el-button type="danger" size="small" @click="deletePro(scope.row)">删除</el-button>
</template>
<template slot="header" slot-scope="scope">
  <el-button @click="addPro" type="primary">添加子任务</el-button>
</template>
</el-table-column>
</el-table>

</el-dialog>


  <!-- 添加子任务窗口 -->
  <el-dialog width="650px" top="10vh" title="新建子任务" v-if="addProVisible"  :visible.sync="addProVisible">

  <el-form ref="process" :model="proTmp" label-width="auto">

  <el-form-item   label="任务名称">
    <el-input  v-model="proTmp.pro_name"></el-input>
  </el-form-item>

  <el-form-item  label="任务时间">
    <el-col :span="11">
      <el-date-picker value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择开始日期" v-model="proTmp.startDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择开始时间" v-model="proTmp.startTime" style="width: 100%;"></el-time-picker>
    </el-col>
    <el-col style="margin-top:15px" :span="11">
      <el-date-picker value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择结束日期" v-model="proTmp.endDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-top:15px;margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择结束时间" v-model="proTmp.endTime" style="width: 100%;"></el-time-picker>
    </el-col>
  </el-form-item>

  <el-form-item label="任务描述">
   <el-input type="textarea" v-model="proTmp.pro_text"></el-input>
  </el-form-item>


  <el-form-item label="任务参与人" v-if="form.type=='小组计划'">

  <el-tag class="hover" size="mini" 
          :type="item.host?'':'success'" 
          v-for="(item,i) in proTmp.proMem"
          :key="i"
          style="font-size:15px;margin-right:5px">{{item.name}}</el-tag>

  <el-tag @click.native="addProMem" class="hover" size="mini" type="warning">+添加</el-tag>
  </el-form-item>

  <el-form-item>
  <el-button @click="confirmAddPro" type="primary">确认创建</el-button>
  <el-button @click="addProVisible=false" type="warning">取消</el-button>
  </el-form-item>

  </el-form>

  </el-dialog>



  <!-- 编辑子任务窗口 -->
  <el-dialog width="650px" top="10vh" title="编辑子任务" v-if="editProVisible"  :visible.sync="editProVisible">

  <el-form ref="process" :model="editProTmp" label-width="auto">

  <el-form-item   label="任务名称">
    <el-input  v-model="editProTmp.pro_name"></el-input>
  </el-form-item>

  <el-form-item  label="任务时间">
    <el-col :span="11">
      <el-date-picker value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择开始日期" v-model="editProTmp.startDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择开始时间" v-model="editProTmp.startTime" style="width: 100%;"></el-time-picker>
    </el-col>
    <el-col style="margin-top:15px" :span="11">
      <el-date-picker value-format="yyyy 年 MM 月 dd 日" type="date" placeholder="选择结束日期" v-model="editProTmp.endDate" style="width: 100%;"></el-date-picker>
    </el-col>
    <el-col style="margin-top:15px;margin-left:27px" :span="11">
      <el-time-picker value-format="HH:mm:ss" placeholder="选择结束时间" v-model="editProTmp.endTime" style="width: 100%;"></el-time-picker>
    </el-col>
  </el-form-item>

  <el-form-item label="任务描述">
   <el-input type="textarea" v-model="editProTmp.pro_text"></el-input>
  </el-form-item>


  <el-form-item label="任务参与人" v-if="form.type=='小组计划'">

  <el-tag class="hover" size="mini" 
          :type="item.host?'':'success'" 
          v-for="(item,i) in editProTmp.proMem"
          style="font-size:15px;margin-right:5px">{{item.name}}</el-tag>

  <el-tag @click.native="editProMem" class="hover" size="mini" type="warning">+添加</el-tag>
  </el-form-item>

  <el-form-item>
  <el-button @click="confirmEditPro" type="primary">确认修改</el-button>
  <el-button @click="editProVisible=false" type="warning">取消</el-button>
  </el-form-item>

  </el-form>

  </el-dialog>


   <!-- 选择任务参与人弹框 -->
  <el-dialog width="650px" title="任务参与人" v-if="addProMemVisible"  :visible.sync="addProMemVisible"
           :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false" center>

  <el-transfer 
   style="text-align: left; display: inline-block"  
   v-model="proMem" 
   :data="totalMem"
   :titles="['备选', '已选']">
  </el-transfer>
  
  <div slot="footer" class="dialog-footer">
    <el-button @click="cancleAddProMem">取 消</el-button>
    <el-button type="primary" @click="confirmAddProMem">确 定</el-button>
  </div>
  </el-dialog>
   
  <peek-contacts :visible.sync="contactVisible" :memInfo.sync="contact" ></peek-contacts>

  </div>

  
</template>

<script>
import peekContacts from '../components/main/peekContacts.vue'
var database =require('nedb')
let db=[]
export default {
  components:{
    peekContacts
  },
  inject:['reload','refreshDB'],
  created:function(){
      let that=this
      db=new database({
        filename:that.$store.state.dbName,
        autoload:true
        })
      this.form.member.push(this.host)

},
  data () {
    return {
      form: {
        name: '',
        startDate: '',
        startTime: '',
        endDate:'',
        endTime:'',
        type: '小组计划',
        desc: '',
        manager:{},
        memCount:0,
        member:[],
        informed:false
      },
      process:[],
      
      addMemberVisible:false,
      addMemberTemp:null,
      host:{
        name:this.$store.state.user=='HelloWorld'?'本地用户':this.$store.state.user_name,
        email:this.$store.state.user=='HelloWorld'?'':this.$store.state.user,
        phone:this.$store.state.phone,
        host:true,
        id:0,
        user_id:this.$store.state.user_id
      },
      memberSlected:null,
      memberSlectedVisible:false,


      //添加子任务
      proVisible:false,
      addProVisible:false,
      proTmp:null,

      
      //添加任务参与人
      addProMemVisible:false,
      totalMem:[],
      proMem:[],
      proSelected:-1,

      //编辑子任务
      editProVisible:false,
      editProTmp:{},
      editProSelected:{},

      //计划联系人
      contactVisible:false,
      contact:{}
    }
  },
  methods: {
    //比价日期前后
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
    
    //比较时间前后
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
    
    compare(date1,time1,date2,time2){
      let dateCompare=this.compareDate(date1,date2)
      if(dateCompare==0){
        return this.compareTime(time1,time2)
      }else{
        return dateCompare
      }
    },

    refresh(){
      this.process=[]
        this.form={
        name: '',
        startDate: '',
        startTime: '',
        endDate:'',
        endTime:'',
        type: '小组计划',
        desc: '',
        excutor:{},
        manager:{},
        memCount:0,
        member:[]
      }
    },

    checkPlanIsEmpty(plan){
      if(plan.name==''||
         plan.startDate==''||
         plan.startTime==''||
         plan.endDate==''||
         plan.endTime==''||
         plan.type==''||
         plan.desc==''){
           return false
         }
      if(plan.type=='小组计划'&&plan.manager==''||
         plan.type=='个人计划'&&plan.excutor==''){
           return false
      }
      return true
    },

    checkProcessIsEmpty(step){
      let l=step.length
      for(let i=0;i<l;i++){
        let tmp=step[i]
        if(tmp.pro_name==''||
           tmp.pro_text==''||
           tmp.startDate==''||
           tmp.startTime==''||
           tmp.endDate==''||
           tmp.endTime==''){
             return false
           }
      }
      return true
    },

    createPlan(){
      let that=this
      if(!this.checkPlanIsEmpty(this.form)){
        this.$message({
          message:'计划属性不能为空',
          offset:100,
          type:'error',
          showClose: true
        })
      return 
    }

      if(!this.checkProcessIsEmpty(this.process)){
         this.$message({
          message:'任务属性不能为空',
          offset:100,
          type:'error',
          showClose: true
      })
      return 
    }
      
      that.form.name=that.form.name.replace(/^\s*|\s*$/g,"")

      db.find({type:"planItemTest2",'plan.name':that.form.name},function(err,Docs){
        if(Docs.length!=0){
           that.$message({
             message:'不能存在同名计划',
             offset:100,
             type:'error',
             showClose: true
             })
            return 
        }else{
          let startDate=that.form.startDate
          let startTime=that.form.startTime
          let endDate=that.form.endDate
          let endTime=that.form.endTime
          
          if(that.compare(startDate,startTime,endDate,endTime)>=0){
            that.$message({
              message:'计划结束时间必须在计划开始时间之后',
              type:'error'
              })
              return
            }
      

      for(let i=0;i<that.process.length;i++){
        let pro_startDate=that.process[i].startDate
        let pro_startTime=that.process[i].startTime
        let pro_endDate=that.process[i].endDate
        let pro_endTime=that.process[i].endTime

        if(that.compare(pro_startDate,pro_startTime,pro_endDate,pro_endTime)>=0){
          that.$message({
            message:'任务'+(i+1)+'的结束时间必须在开始时间之后',
            type:'error'
          })
          return 
        }else if(that.compare(startDate,startTime,pro_startDate,pro_startTime)>0){
          that.$message({
            message:'任务'+(i+1)+'的开始时间不能在计划开始时间之前',
            type:'error'
          })
          return 
        }else if(that.compare(pro_endDate,pro_endTime,endDate,endTime)>0){
           that.$message({
            message:'任务'+(i+1)+'的结束时间不能在计划结束时间之后',
            type:'error'
          })
          return 
        }
      }

     //确定任务序号
     that.resetProId()

     that.process.unshift({
            pro_name:'计划开始',
            pro_text:'好的开始是成功的一半！',
            startDate:startDate,
            startTime:startTime,
      })
     that.process.push({
            pro_name:'计划结束',
            pro_text:'坚持就是胜利！',
            startDate:endDate,
            startTime:endTime,
      })
      

      that.form.manager=that.host
      if(that.host.name.match(/^\s*$/g)){
           that.$message({
            message:'小组成员名字不能为空',
            type:'error'
          })
          return 
        }

      if(that.form.type=='小组计划'){
        for(let i=0;i<that.form.member.length;i++){
        if(that.form.member[i].name.match(/^\s$/g)){
          that.$message({
            message:'小组成员名字不能为空',
            type:'error'
          })
          return 
        }
      } 
    }


     
     if(!that.$store.state.login){
       db.insert({
         type:"planItemTest2",
         plan:that.form,
         process:that.process,
        },function (err, newDoc){
          that.$message({
            message:'创建成功',
            type:'success'
            })
          })
       }

    

      
     
       //云同步代码
      if(that.$store.state.login){
        
        let promise=new Promise(function(resolve,reject){
          let notes=[]
          db.find({type:'planItemNotes',plan:that.form.name},function(err,Docs){
                        if(Docs.length!=0){
                            notes.push(Docs[0])
                        }else{
                            notes=[{
                                type:'planItemNotes',
                                plan:that.form.name,
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
             plan:that.form,
             process:that.process
          }]),
          planLog:JSON.stringify(notes)

        })).then(function(res){
          if(res.data=='fail'){
            that.$message({
              message:'云端同步失败，请检查网络设置',
              type:'error'
            })
          }else{
            that.$message({
              message:'创建成功',
              type:'success'
            })
          }
          that.$store.state.syncing=false
          that.refreshDB()
        })

        })
      }
      that.reload()
    }
  })
},
    
    //添加计划成员人
    addMember(){
      let id=this.form.member[this.form.member.length-1].id
      this.addMemberTemp={
        name:'',
        email:'',
        phone:'',
        host:false,
        id:id+1
      }
      this.addMemberVisible=true
    },
    confirmAddMem(){
      let that=this

      //对于线上模式中的计划，需要保证参与人的账户存在
      if(this.$store.state.login){
        this.$http.post(this.$store.state.contextUrl+'/getUserId',this.$qs.stringify({
          email:that.addMemberTemp.email
        })).then(function(res){
          if(res.data==-1){
            that.$message({
              message:'该用户不存在',
              type:'error'
            })
            return 
          }else{

          if(that.addMemberTemp.name!=res.data.user_name){
             that.$message({
               message:'新增参与人名字与该账户名字（'+res.data.user_name+"）不匹配",
               type:'error'
            })
            return 
          }else{
              that.addMemberTemp.user_id=res.data.id
              that.form.member.push(JSON.parse(JSON.stringify(that.addMemberTemp)))
              let id=that.form.member[that.form.member.length-1].id
              that.addMemberTemp={
                name:'',
                email:'',
                phone:'',
                host:false,
                id:id+1,
            }
            that.addMemberVisible=false
            }
          }
        })
      }else{
        that.form.member.push(JSON.parse(JSON.stringify(that.addMemberTemp)))
              let id=that.form.member[that.form.member.length-1].id
              that.addMemberTemp={
                name:'',
                email:'',
                phone:'',
                host:false,
                id:id+1,
            }
         that.addMemberVisible=false
      }
    },
    cancleAddMem(){
      this.addMemberTemp=null
      this.addMemberVisible=false
    },
    refreshPlan(){
      db.find({"plan.name":"test"},function(err,Docs){
        alert(JSON.stringify(Docs))
      })
    } ,
    removeMem(i){
      this.form.member.splice(i,1)
    },

    //修改和查看已添加的计划参与人
    editMem(item){
      this.memberSlected=item
      this.memberSlectedVisible=true
    },

    //定义子任务
    openPro(){
        this.proVisible=true
    },
    
    resetProId(){
      this.process.forEach(function(item,index){
        item.id=index+1
      })
    },

    addPro(){
      this.addProVisible=true
    },

    confirmAddPro(){
      this.process.push(JSON.parse(JSON.stringify(this.proTmp)))
      this.resetProId()
      this.addProVisible=false
    },


    //添加子任务的成员

    addProMem(){
      this.addProMemVisible=true
    },

    cancleAddProMem(){
      this.addProMemVisible=false
    },
    confirmAddProMem(){
      let that=this
      if(that.proTmp!=null){
        that.proTmp.proMem=[]
        this.proMem.forEach(function(item,index){
          let tmp=JSON.parse(JSON.stringify(that.form.member[item]))
          tmp.status=0
          that.proTmp.proMem.push(tmp)
        })
      }else{
        that.editProTmp.proMem=[]
        this.proMem.forEach(function(item,index){
          let tmp=JSON.parse(JSON.stringify(that.form.member[item]))
          tmp.status=0
          that.editProTmp.proMem.push(tmp)
        })
      }
      this.addProMemVisible=false
    },

    
    //编辑子任务
    editPro(pro){
      this.editProTmp=JSON.parse(JSON.stringify(pro))
      this.editProSelected=pro
      this.editProVisible=true
    },
    deletePro(pro){
      this.process.splice(this.process.indexOf(pro),1)
      this.resetProId()
    },
    editProMem(){
      let that=this
      this.proMem=[]
      let chkSet=new Set()
      this.editProTmp.proMem.forEach(function(item,index){
        chkSet.add(item.user_id)
      })
      this.totalMem.forEach(function(item,index){
        if(chkSet.has(item.user_id)){
          that.proMem.push(item.key)
        }
      })
      this.addProMemVisible=true
    },
    confirmEditPro(){
      this.process.splice(this.process.indexOf(this.editProSelected),1,
                           JSON.parse(JSON.stringify(this.editProTmp)))    
      this.editProVisible=false
      this.proMem=[]
    }
  },
  
  watch:{
    "form.member":{
       handler:function(mem){
             let that=this
             this.totalMem=[]
             mem.forEach(function(item,index){
                 that.totalMem.push({
                     key:index,
                     label:item.name,
                     user_id:item.user_id
                 })
             })
        },
        deep:true
    },
    "addProVisible":{
      handler:function(val){
        if(val){
           this.proTmp={
             id:0,
             status:0,
             pro_name:'',
             pro_text:'',
             startDate:'',
             startTime:'',
             endDate:'',
             endTime:'',
             proMem:[]
          }
        }else{
          this.proTmp=null
          this.proMem=[]
        }
      }
    },
    "memberSlectedVisible":{
      handler:function(val){
        if(!val){
          this.memberSlected=null
        }
      }
    },
    "contact":{
      handler:function(val){
        if(val!=null){
          this.addMemberTemp.name=val.name
          this.addMemberTemp.email=val.email
          this.addMemberTemp.phone=val.phone
          this.addMemberTemp.user_id=val.user_id
        }
      }
    }
  }
}
</script>

<style scoped>
.hover:hover{
  cursor: pointer;
}
.plan-card{
    overflow-y: auto;
}
.el-card >>> .el-card__body{
  padding: 12px;
}
.process{
    margin-left: 15px;
    height: 97%;
    overflow-y: hidden;
}
.main{
   height:100%;
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
    overflow-y: hidden;
}
.pointer:hover{
  cursor: pointer;
}
</style>