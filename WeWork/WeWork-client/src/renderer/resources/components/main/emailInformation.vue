<template>
<div>
<el-dialog
  :lock-scroll="false"
  :title="'接收人：'+acceptor.email+'（'+acceptor.name+'）'"
  :visible.sync="visible"
  width="50%"
  top="4vh"
  v-if="visible">

     <el-card>

    <el-descriptions title="计划开始" v-if="pro_index==0&&acceptor.host">
    <el-descriptions-item label="开始时间">
    {{plan.process[pro_index].startDate+plan.process[pro_index].startTime}}
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions title="任务信息" :column="1"  v-if="pro_index!=0&&pro_index!=plan.process.length-1||!acceptor.host">
    
    <el-descriptions-item label="任务名称">{{plan.process[pro_index].pro_name}}</el-descriptions-item>
    
    <el-descriptions-item label="开始时间">
    {{plan.process[pro_index].startDate+plan.process[pro_index].startTime}}
    </el-descriptions-item>
    
    <el-descriptions-item label="结束时间">
    {{plan.process[pro_index].endDate+plan.process[pro_index].endTime}}
    </el-descriptions-item>
    
    <el-descriptions-item label="描述">
    {{plan.process[pro_index].pro_text}}
    </el-descriptions-item>

    </el-descriptions>
    

    <el-descriptions title="计划结束" v-if="pro_index==plan.process.length-1&&acceptor.host">
    <el-descriptions-item label="结束时间">
    {{plan.process[pro_index].startDate+plan.process[pro_index].startTime}}
    </el-descriptions-item>
    </el-descriptions>

    </el-card>
    
  
    <el-card>
    <div v-if="pro_index==0&&acceptor.host">
    <el-tag size="mini" style="margin-bottom:3px" type="success"><span >计划开始通知内容</span></el-tag>
    <el-input type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    </div>


    <div v-if="pro_index!=0&&pro_index!=plan.process.length-1||!acceptor.host">
    <div style="margin-bottom:10px">
     <el-tag size="mini" style="margin-bottom:3px" type="warning"><span>任务开始通知内容</span></el-tag>
    <el-input   type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    </div>
    <div>
    <el-tag size="mini" style="margin-bottom:3px" type="success"><span >任务结束通知内容</span></el-tag>
    <el-input type="textarea" v-model="schedule[pro_index].endContent" placeholder="请输入通知内容"></el-input>
    </div>
    </div>

    <div v-if="pro_index==plan.process.length-1&&acceptor.host">
    <el-tag size="mini" style="margin-bottom:3px" type="warning"><span >计划结束通知内容</span></el-tag>
    <el-input type="textarea" v-model="schedule[pro_index].startContent" placeholder="请输入通知内容"></el-input>
    </div>

    </el-card>




  <el-pagination
  style="margin-top:3%"
  v-if="visible"
  background
  layout="prev, pager, next"
  :page-size="1"
  :total="schedule.length"
  @current-change="informedContent_index">
  </el-pagination>
   
  <el-button type="primary" @click="doEmailInform" style="margin-top:3%">确认发送</el-button>
  </el-dialog>

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
    },
    data(){
        return {
            schedule:[],
            pro_index:0,
        }
    },
    props:{
        visible:{
            type:Boolean,
            default:false
        },
        plan:{
            type:Object,
            default(){
                return {}
            }
        },
        acceptor:{
            type:Object,
            default(){
                return {}
            }
        },
    },
    methods:{
        informedContent_index(page){
            this.pro_index=page-1
        },
        
        doEmailInform(){
         
         let that=this

         
        if(this.acceptor.informed){
            this.$message({
                message:'该计划的同步通知已开启！',
                type:'error'
            })
            return 
            
            }else{
                this.acceptor.informed=true
            }
         


        this.$http.post(this.$store.state.contextUrl+"/emailSync",that.$qs.stringify({
          user_id:that.acceptor.user_id,
          email:that.acceptor.email,
          user_name:that.acceptor.name,
          schedule:JSON.stringify(that.schedule)
        })).then(function(res){
          if(res.data=='success'){
            that.$message({
              message:'已成功开启邮箱同步通知功能，请注意查收邮箱信息',
              type:'success'
            })


    
        
       
        db.update({type:"planItemTest2","plan.name":that.plan.plan.name},
          that.plan,
          function(err, numReplaced){
                if(numReplaced==1){
                that.$message({
                    message:'保存成功',
                    type:'success'
                 })
                }
             })

       that.$http.post(that.$store.state.contextUrl+'/updatePlan',that.$qs.stringify({
          id:that.plan.id,
          plan_text:JSON.stringify(that.plan),
        })).then(function(res){
          that.$emit('update:visible',false)
          if(res.data=='fail'){
            that.$message({
              message:'云端同步失败，请检查网络设置',
              type:'error'
            })
          }
          that.$store.state.syncing=false
        })

          }else{
            that.$message({
              message:'出现错误',
              type:'error'
            })
          }
        })
      },

    },
    watch:{
        "visible":{
            handler:function(val){
                this.$emit("update:visible",val)
                if(val){
                    let that=this
                    this.schedule=[]
                if(this.acceptor.host){
                    that.plan.process.forEach(function(item,i){
                    if(i==0){
                        that.schedule.push({
                            start:item.startDate+item.startTime,
                            startContent:'您的计划“'+that.plan.plan.name+"”"+"已到预定开始时间——"+
                            item.startDate+item.startTime,
                            end:null,
                            endContent:null
                        })
                    }else if(i==that.plan.process.length-1){
                        that.schedule.push({
                            start:item.startDate+item.startTime,
                            startContent:'您的计划“'+that.plan.plan.name+"”"+"已到预定结束时间——"+
                            item.startDate+item.startTime,
                            end:null,
                            endContent:null
                        })
                    }else{
                        that.schedule.push({
                            start:item.startDate+item.startTime,
                            startContent:'您的计划“'+that.plan.plan.name+"”"+"已到"+"任务"+
                                          item.id+":“"+item.pro_name+"”的开始时间——"+item.startDate+item.startTime,
                            end:item.endDate+item.endTime,
                            endContent:'您的计划“'+that.plan.plan.name+"”"+"已到"+"任务"+
                                          item.id+":“"+item.pro_name+"”的结束时间——"+item.endDate+item.endTime,
                            })
                        }
                    })
                }else{
                    that.plan.process.forEach(function(item,i){
                         if(i!=0&&i!=that.plan.process.length-1){
                             let l=item.proMem.length
                             for(let i=0;i<l;i++){
                                 if(that.acceptor.user_id==item.proMem[i].user_id){
                                     that.schedule.push({
                                          start:item.startDate+item.startTime,
                                          startContent:'您的合作计划“'+that.plan.plan.name+"”"+"已到"+"任务"+
                                                        item.id+":“"+item.pro_name+"”的开始时间——"+item.startDate+item.startTime,
                                          end:item.endDate+item.endTime,
                                          endContent:'您的合作计划“'+that.plan.plan.name+"”"+"已到"+"任务"+
                                                        item.id+":“"+item.pro_name+"”的结束时间——"+item.endDate+item.endTime,
                                     })
                                     break
                                 }
                             }
                         }
                     })
                } 
                }
            },
            immediate:true
        }
    }
}
</script>
