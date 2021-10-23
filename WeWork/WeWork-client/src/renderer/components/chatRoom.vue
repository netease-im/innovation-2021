<template>
      <!-- 成员聊天交流室 -->
      <el-dialog 
       top="5vh" 
       title="成员交流室" 
       width="900px" 
       v-if="visible"  
       :visible.sync="visible">
        <el-row :gutter="2">
        <el-col :span="6">
        <el-card style="height:500px;overflow-y:auto">
        <div slot="header">成员列表</div>
        <el-table :data="plan.plan.member"
                  :show-header="false"
                  class="jobTable">
        <el-table-column >
        <template slot-scope="scope">
        <el-tag :type="scope.row.host?'':'success'" size="medium">{{scope.row.name}}</el-tag>
        </template>
        </el-table-column>
        </el-table>
        </el-card>
        </el-col>
        <el-col :span="18" >
        <el-card style="height:500px">

        <el-card style="height:320px;overflow-y:auto" shadow="never">
        
        <!--
        {
          host:arr[1],
          sender:arr[2],
          msg:arr[3],
          time:timeStr,
          position:position
        }
         -->

        <div v-for="(item,i) in IM" :key="i" style="margin-bottom:20px">
        <div style="margin-bottom:5px;width:100%">
        <span :style="item.position">{{item.time}}</span>
        <div style="clear:both"></div>
        </div>
        <div class="icon" :style="item.position"> <el-tag type="success" size="medium">{{getSenderName(item.sender)}}</el-tag> </div>
        <div class="box" :style="item.position">{{item.msg}}</div>
        <div style="clear:both"></div>
        </div>
        

        </el-card>
        

        <el-card style="height:130px" shadow="never">
        <el-input v-model="IM_text" type="textarea" style="margin-bottom:5px"></el-input>
        <el-button @click="sendIM" plain type="primary" style="float:right">发送</el-button>
        <el-button plain type="warning" style="float:right;margin-right:5px">清空</el-button>
        </el-card>
        </el-card>
        </el-col>
      </el-row>

      </el-dialog>
</template>

<script>
export default {
    methods:{
        sendIM(){
                let that=this
                this.$http.post(this.$store.state.contextUrl+'/addIM',that.$qs.stringify({
                    user_id:that.$store.state.user_id,
                    msg:that.IM_text
                })).then(function(res){
                    if(res.data!='success'){
                        that.$message({
                            message:'发送失败',
                            type:'error'
                        })
                    }
                })
                let arg_str='IM?'
                this.plan.plan.member.forEach(function(item,index){
                    arg_str+=(item.user_id+'&')
                })
                arg_str+=(that.plan.plan.name+'&')
                arg_str+=(that.plan.plan.manager.user_id+'&')
                arg_str+=(that.$store.state.user_id+'&')
                arg_str+=that.IM_text
                this.$store.state.socket.send(arg_str)
            },
            getSenderName(id){
                let intId=parseInt(id)
                let name=null
                this.plan.plan.member.forEach(function(item,i){
                    if(name!=null){
                        return 
                    }
                    
                    if(item.user_id===intId){
                        name=item.name
                    }
                })
                return name
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
            type:Array,
            default(){
                return []
            }
        }
    },
    data(){
        return {
            IM_text:''
        }
    },
    watch:{
        "visible":{
            handler:function(val){
                this.$emit("update:visible",val)
            }
        }
    }
}
</script>

<style scoped>
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

</style>