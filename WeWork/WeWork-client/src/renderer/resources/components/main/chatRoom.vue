<template>
<div class="main">
      <!-- 成员聊天交流室 -->
      <el-dialog 
       top="5vh" 
       title="成员交流室" 
       width="900px" 
       :visible.sync="visible">
        <el-row :gutter="2">
        <el-col :span="6">

        <el-card class="memberList" style="height:500px;overflow-y:auto" :key="refreshMemCount">

        <div slot="header">
        成员列表
        <i @click="refreshMemCard" class="el-icon-refresh-right hover" style="float:right;margin-top:4px"></i>
        </div>

        <el-table :data="plan.member"
                  :show-header="false"
                  class="jobTable"
                  size="mini"
                  @row-click="showMemInfo">
        
        <el-table-column width="32" >
        <template slot-scope="scope">
        <i class="el-icon-user-solid"></i>
        </template>
        </el-table-column>

        <el-table-column >
        <template slot-scope="scope">
        {{scope.row.name}}
        </template>
        </el-table-column>
        
        <el-table-column>
        <template slot-scope="scope">
        <span style="color:#67C23A"  v-if="getMemStatus(scope.row.user_id)">在线</span>
        <span style="color:##909399" v-if="!getMemStatus(scope.row.user_id)">离线</span>
        </template>
        </el-table-column>

        </el-table>
        </el-card>
        </el-col>
        <el-col :span="18" >
        <el-card style="height:500px">

        <el-card class="chat" style="height:320px;overflow-y:auto" >
       

        <div v-for="(item,i) in IM.msg" :key="i" style="margin-bottom:20px">
        <div style="margin-bottom:5px;width:100%">
        <span :style="'float:'+item.position">{{item.time}}</span>
        <div style="clear:both"></div>
        </div>
        <div class="icon" :style="'float:'+item.position"> <el-tag :type="getColor(item)" size="medium">{{getSenderName(item.sender)}}</el-tag> </div>
        <div class="box"  :style="'float:'+item.position">{{item.msg}}</div>
        <div style="clear:both"></div>
        </div>
         
        </el-card>
   
        
        <el-card style="height:130px" >
        <el-input v-model="IM_text" type="textarea" style="margin-bottom:5px"></el-input>
        <el-button @click="sendIM" plain type="primary" style="float:right">发送</el-button>
        <el-button @click="IM_text=''" plain type="warning" style="float:right;margin-right:5px">清空</el-button>
        </el-card>
        </el-card>
        </el-col>
        </el-row>

      </el-dialog>


      <!-- 参与人信息 -->
      <el-dialog  :title="memberSlected.name" v-if="memberInfoVisible"  
                 :visible.sync="memberInfoVisible">
      <el-card>
      <div>
      <el-tag  type="primary" style="margin-right:5px">{{memberSlected.name}}
      </el-tag>
      <el-tag  type="warning" v-if="memberSlected.host">负责人
      </el-tag>
      </div>
      <br>
      <el-descriptions :column="1">
      <el-descriptions-item label="邮箱">   
      {{memberSlected.email}}
      </el-descriptions-item>

      <el-descriptions-item label="手机">   
      {{memberSlected.phone.match(/^\s*$/)?'未设置':memberSlected.phone}}
      </el-descriptions-item>
      </el-descriptions>
      </el-card>     
      </el-dialog>

</div>
</template>

<script>
export default {
    created(){
         let l=this.IM.msg.length
         if(l!=0){
             this.IM.msg[l-1].read=true
         }
         let that=this
         this.plan.member.forEach(function(item,index){
             that.memberKey.push(item.user_id)
         })
         this.checkOnline(0)
    },
    updated(){
        let x=document.getElementsByClassName("chat")[0]
        if(x==undefined){
            return 
        }
        if(this.chatBeforeUpdateScrollHeight==0){
            this.chatBeforeUpdateScrollHeight=x.offsetHeight
        }
        if(x.scrollTop+x.offsetHeight>=this.chatBeforeUpdateScrollHeight){
            x.scrollTop=x.scrollHeight
        }
        this.chatBeforeUpdateScrollHeight=x.scrollHeight
    },
    methods:{
        getMemStatus(user_id){
            return this.memStatus[user_id]==1
        },
        sendIM(){
                if(this.IM_text==''){
                    this.$message({
                        message:'请输入内容',
                        type:'warning'
                    })
                    return 
                }
                let that=this
                let arg_str='IM?'
                this.plan.member.forEach(function(item,index){
                    arg_str+=(item.user_id+'&')
                })
                arg_str+=(that.plan.name+'&')
                arg_str+=(that.plan.manager.user_id+'&')
                arg_str+=(that.$store.state.user_id+'&')
                arg_str+=that.IM_text
                this.$store.state.socket.send(arg_str)
                this.IM_text=''
            },
            getSenderName(id){
                let intId=parseInt(id)
                let name=null
                this.plan.member.forEach(function(item,i){
                    if(name!=null){
                        return 
                    }
                    
                    if(item.user_id===intId){
                        name=item.name
                    }
                })
                return name
            },
            getColor(msg){
                if(msg.host){
                    return ''
                }else{
                    if(msg.position=='right'){
                        return 'warning'
                    }else{
                        return 'success'
                    }
                }
            },
            showMemInfo(row, column, event){
                this.memberSlected=row
                this.memberInfoVisible=true
            },
            checkOnline(preTimer){
                let that=this
                if(this.visible){
                    that.$http.post(that.$store.state.contextUrl+'/chkOnline',that.$qs.stringify({
                        userArr:JSON.stringify(that.memberKey)
                    })).then(function(res){
                        that.memStatus=res.data
                        that.refreshMemCard()
                        if(that.visible){
                            let timer=setTimeout(() => {
                                that.checkOnline(timer)
                            }, 15000);
                        }else{
                            clearTimeout(preTimer)
                        }
                    })
                }else{
                    clearTimeout(preTimer)
                }
            },
            refreshMemCard(){
                this.refreshMemCount=(this.refreshMemCount+1)%5
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
        IM:{
            type:Object,
            default(){
                return {}
            }
        }
    },
    data(){
        return {
            IM_text:'',

            chatBeforeUpdateScrollHeight:0,

            //查看成员信息
            memberSlected:null,
            memberInfoVisible:false,
            memPro:[],
            memberKey:[],

            refreshMemCount:0,
            memStatus:{}
        }
    },
    watch:{
        "visible":{
            handler:function(val){
                this.$emit("update:visible",val)
            }
        },
        "IM":{
            handler:function(val){
                if(this.visible){
                    let l=this.IM.msg.length
                    if(l!=0){
                        this.IM.msg[l-1].read=true
                    }
                }
            },
            deep:true,
            immediate:true
        }
    }
}
</script>



<style scoped>

.main{
     font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

/* 聊天气泡 */
.box{
    margin-left:10px;
    margin-right:10px;
    width: 200px;
    
    background-color: rgb(240, 240, 240);
    border-bottom-color: rgb(240, 240, 240);
    color: rgb(92, 88, 88);
    font-size: 12px;
    line-height: 18px;
    padding: 5px 12px 5px 12px;
    box-sizing: border-box;
    border-radius: 6px;
    position: relative;
    word-break: break-all;
}

.icon{
    display:inline-block
}

.memberList >>> .el-card__body{
  padding: 0;
}

.jobTable>>>.el-table__body-wrapper:hover{
    cursor: pointer;
}

.hover:hover{
    cursor: pointer;
}
</style>