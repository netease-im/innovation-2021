<template>
    <div class="main" v-if="cowork!=null">
    <el-card >
    <el-page-header @back="goBack" content="计划详情"></el-page-header>
    </el-card>
    <el-card>
    <el-descriptions title="计划信息">
    <el-descriptions-item label="计划名称">{{cowork.cowork.body.content.plan.name}}</el-descriptions-item>
    <el-descriptions-item label="计划性质">{{cowork.cowork.body.content.plan.type}}</el-descriptions-item>
    
    <el-descriptions-item :label="cowork.cowork.body.content.plan.type=='个人计划'?'执行人':'负责人'">
    <el-tag size="mini">{{cowork.cowork.body.content.plan.type=='个人计划'?cowork.cowork.body.content.plan.excutor.name:
      cowork.cowork.body.content.plan.manager.name}}</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions>
    <el-descriptions-item label="计划时间">{{cowork.cowork.body.content.plan.startDate+" "+
    cowork.cowork.body.content.plan.startTime+" 至 "+cowork.cowork.body.content.plan.endDate
    +" "+cowork.cowork.body.content.plan.endTime}}
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions :column="1" v-if="cowork.cowork.body.content.plan.type=='小组计划'">
    <el-descriptions-item label="小组成员">
      <div v-if="cowork.cowork.body.content.plan.type=='小组计划'&&cowork.cowork.body.content.plan.member.length>0">
      <el-tag style="margin-right:5px" size="mini" :type="item.host?'':(me.email==item.email?'warning':'success')" v-for="(item,i) in cowork.cowork.body.content.plan.member" :key="i">{{item.name}}</el-tag>
      </div>

      <div v-if="cowork.cowork.body.content.plan.type=='小组计划'&&cowork.cowork.body.content.plan.member.length==0">
      <span>无</span>
      </div>      
      
    </el-descriptions-item>

    <el-descriptions-item label="我" v-if="me!=null">
      <el-tag type="warning" size="mini">{{me.name}}</el-tag>      
      
    </el-descriptions-item>
    </el-descriptions>
    
    <el-descriptions>
    <el-descriptions-item label="计划描述">{{cowork.cowork.body.content.plan.desc}}</el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-collapse  v-model="activeName" accordion v-for="(item,i) in cowork.cowork.body.content.pro" :key="i">
    <el-collapse-item :title="'我的任务'+(i+1)+'：'+item.pro_name">
    
    <div>任务时间：{{item.startDate+" "+
    item.startTime+" 至 "+item.endDate+" "+item.endTime}}
    </div>
    
    <div>任务参与人：
    <span v-if="!item.hasOwnProperty('proMem')||item.proMem.length==0">未指定</span>
    <el-tag style="margin-right:5px" size="mini" 
            :type="mem.host?'':(me.email==mem.email?'warning':'success')" 
            v-for="(mem,i) in item.proMem" :key="i">
    {{mem.name}}
    </el-tag>
    </div>
    <div>任务描述：{{item.pro_text}}</div>
    <div>我的任务状态：</div>
    <el-progress style="margin-right:35%" :format="format" :percentage="getProPercentage(proMeMap.get(item.id).status)"></el-progress>
    <br>

    <el-button v-if="proMeMap.get(item.id).status==0" icon="el-icon-message" 
    size="medium"  type="warning" style="margin-top:10px"
    @click="sendInfoFor1(item)">
    进行任务
    </el-button>

    <el-tag v-if="proMeMap.get(item.id).status==0.5" size="medium"  type="warning" style="margin-top:10px" disabled>
    等待计划负责人确认任务状态修改——进行任务
    </el-tag>

    <el-button v-if="proMeMap.get(item.id).status==1" icon="el-icon-message" 
    size="medium"  type="success" style="margin-top:10px"
    @click="sendInfoFor2(item)">
    完成任务
    </el-button>

    <el-tag v-if="proMeMap.get(item.id).status==1.5" size="medium"  type="warning" style="margin-top:10px" disabled>
    等待计划负责人确认任务状态修改——完成任务
    </el-tag>

    <el-tag v-if="proMeMap.get(item.id).status==2" size="medium"  type="primary" style="margin-top:10px" disabled>
    任务已完成
    </el-tag>

   

    </el-collapse-item>
    </el-collapse>
     
    <el-card>
    <el-button type="info"  icon="el-icon-message" plain>发送消息</el-button>
    <el-button @click="deleteCowork" type="danger" icon="el-icon-delete" plain>删除计划</el-button>
    </el-card>

    <el-dialog top="5vh" width="600px" v-if="host!=null&&me!=null&&proSelected!=null" 
    :visible.sync="sendInfoVisibleFor1" title="发送请求">
    <el-card>
    <el-descriptions :column="1">
    <el-descriptions-item label="请求接收方">
    <el-tag size="mini">{{host.name}}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="任务">
    {{proSelected.pro_name}}
    </el-descriptions-item>
    <el-descriptions-item label="请求任务状态转换">
    <el-tag size="mini" type="danger">未开始</el-tag> → <el-tag size="mini" type="warning">进行中</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="report" placeholder="任务准备情况……"></el-input>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="message" placeholder="我的留言……"></el-input>
    </el-card>

    <el-card>
    <el-button @click="confirmSendInfoFor1" type="primary">确认发送请求</el-button>
    </el-card>

    </el-dialog>
    

    <el-dialog top="5vh" width="600px" v-if="host!=null&&me!=null&&proSelected!=null" 
    :visible.sync="sendInfoVisibleFor2" title="发送请求">
    <el-card>
    <el-descriptions :column="1">
    <el-descriptions-item label="请求接收方">
    <el-tag size="mini">{{host.name}}</el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="任务">
    {{proSelected.pro_name}}
    </el-descriptions-item>
    <el-descriptions-item label="请求任务状态转换">
    <el-tag size="mini" type="warning">进行中</el-tag> → <el-tag size="mini" type="success">结束</el-tag>
    </el-descriptions-item>
    </el-descriptions>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="report" placeholder="任务完成情况……"></el-input>
    </el-card>

    <el-card>
    <el-input type="textarea" v-model="message" placeholder="我的留言……"></el-input>
    </el-card>

    <el-card>
    <el-button @click="confirmSendInfoFor2" type="primary">确认发送请求</el-button>
    </el-card>

    </el-dialog>

    </div>
</template>

<script>
var database =require('nedb')
var db=[]
export default {
    created(){
        this.cowork=this.$route.params.cowork
        let that=this
        db=new database({
        filename:that.$store.state.dbName,
        autoload:true
        })
        
        

        //找出计划成员中的账户本人
        this.cowork.cowork.body.content.plan.member.forEach(function(item,index){
            if(that.me!=null&&that.host!=null){
                return 
            }
            if(that.$store.state.user==item.email){
                that.me=item
            }
            if(item.host){
                that.host=item
            }
        })

        //确定每个任务的账户本人完成状态
        this.cowork.cowork.body.content.pro.forEach(function(item,index){
            let l=item.proMem.length
            for(let i=0;i<l;i++){
                if(item.proMem[i].email==that.me.email){
                    that.proMeMap.set(item.id,item.proMem[i])
                    break
                }
            }
        })
    },
    data(){
        return {
            cowork:null,
            activeName:'',
            me:null,
            host:null,

            //获取所有进程的账户成员状态
            proMeMap:new Map(),

            //向任务负责人提交任务状态修改窗口
            sendInfoVisibleFor1:false,
            sendInfoVisibleFor2:false, 
            message:'',
            report:'',
            
            proSelected:null
        }
    },
    methods:{
        sendInfoFor1(pro){
            this.message=''
            this.report=''
            this.sendInfoVisibleFor1=true
            this.proSelected=pro
        },
        confirmSendInfoFor1(){
            let that=this
            this.proMeMap.get(this.proSelected.id).status=0.5
            this.save()
            
            let date=new Date()
            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
            //发送message部分
            this.$http.post(this.$store.state.contextUrl+'/uploadNotice',this.$qs.stringify({
                acceptor:that.cowork.cowork.body.sender,
                sender:that.cowork.cowork.body.acceptor,
                content:JSON.stringify({
                    plan_name:that.cowork.cowork.body.content.plan.name,
                    pro_id:that.proSelected.id,
                    time:timeStr,
                    pro:that.proSelected,
                    me:that.proMeMap.get(that.proSelected.id),
                    cowork_id:that.cowork.id,

                    //文字内容
                    extraMsg:that.message,
                    report:that.report
                }),
                type:1,
                read:0
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'请求发送成功',
                        type:'success'
                    })
                }
                that.sendInfoVisibleFor1=false
            })
        },
        sendInfoFor2(pro){
            this.message=''
            this.report=''
            this.sendInfoVisibleFor2=true
            this.proSelected=pro
        },
        confirmSendInfoFor2(){
            let that=this
            this.proMeMap.get(this.proSelected.id).status=1.5
            this.save()

            let date=new Date()
            let timeStr=date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+
                                        date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()
            //发送message部分
            this.$http.post(this.$store.state.contextUrl+'/uploadNotice',this.$qs.stringify({
                acceptor:that.cowork.cowork.body.sender,
                sender:that.cowork.cowork.body.acceptor,
                content:JSON.stringify({
                    plan_name:that.cowork.cowork.body.content.plan.name,
                    pro_id:that.proSelected.id,
                    time:timeStr,
                    pro:that.proSelected,
                    me:that.proMeMap.get(that.proSelected.id),
                    cowork_id:that.cowork.id,
                      
                    //文字内容
                    extraMsg:that.message,
                    report:that.report
                }),
                type:1,
                read:0
            })).then(function(res){
                if(res.data=='success'){
                    that.$message({
                        message:'请求发送成功',
                        type:'success'
                    })
                }
                that.sendInfoVisibleFor2=false
            })
        },

        goBack(){
            this.$router.push("/cowork")
        },
        deleteCowork(){
            let that=this
            that.$store.state.syncing=true
            db.remove({
                type:'cowork',
                id:that.cowork.id
            },{},function(err,num){
                that.$message({
                    message:'删除成功',
                    type:'error'
                })
            })
            
            if(this.$store.state.dbName=='./cloud.db'){
                this.$http.post(this.$store.state.contextUrl+'/deleteCowork',this.$qs.stringify({
                    id:that.cowork.id
                })).then(function(res){
                    if(res.data=='fail'){
                        that.$message({
                            message:'云端同步失败',
                            type:'error'
                        })
                    }
                    that.$store.state.syncing=false
                    that.goBack()
                })
            }
        },

        //获取进度条的percentage
        getProPercentage(status){
            if(status>=0&&status<1){
                return 0
            }else if(status>=1&&status<2){
                return 50
            }else{
                return 100
            }
        },

        format(num){
            if(num==0){
                return '未开始'
            }else if(num==50){
                return '进行中'
            }else{
                return '已完成'
            }
        },
       save(){
            let that=this
            db.update({type:"cowork",id:that.cowork.id},that.cowork,function(err, numReplaced){
                if(numReplaced==1){
                that.$message({
                    message:'保存成功',
                    type:'success'
                 })
                }
            })
            
            
            //云同步代码
            if(that.$store.state.dbName=='./cloud.db'){
                    that.$store.state.syncing=true
                    that.$http.post(that.$store.state.contextUrl+'/updateCowork',that.$qs.stringify({
                        id:that.cowork.id,
                        content:JSON.stringify(that.cowork)
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
       }
    }
}
</script>

<style scoped>
.main{
   height:100%;
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   overflow-y: auto;
}
.el-collapse >>> .el-collapse-item__header{
    padding-left:2% ;
}

.el-collapse >>> .el-collapse-item__content{
     padding-left:2% ;
}
</style>