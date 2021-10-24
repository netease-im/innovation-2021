<template>
    <div class="main"> 

    <el-card class="btnCard">
    <el-button type="success" @click="goBack"  >返回</el-button>
    <el-button type="warning" @click="toMarkdown"  >新增总结</el-button>
    </el-card>
    
    <img v-if="empty" src="../../assets/empty.svg" style="height:70%;width:70%" />

    <div style="height:100%" v-if="notesTitle.length!=0">
    
    <div style="height:10%"  v-for="(item,i) in notesTitle" :key="i">
    
    <el-card class="planDisplay"  shadow='hover'>
    
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
    
    <el-button @click="MarkdownPreview(item)" style="float:right" type="primary" size="mini">查看</el-button>
    <el-button @click="MarkdownEdit(item)" style="float:right;margin-right:1%" type="primary" size="mini">编辑</el-button>

    </el-card>
    </div>
    </div>

    </div>
</template>

<script>
var database =require('nedb')
var db=[]

export default {
   created(){
         let that=this
         this.plan_name=this.$route.params.plan_name
         db=new database({
             filename:that.$store.state.dbName,
             autoload:true
             })
         db.find({type:'planItemNotes',plan:that.plan_name},function(err,Docs){
             if(Docs.length!=0){
                 that.notes=Docs[0]
                 }else{
                     that.notes={
                         type:'planItemNotes',
                         plan:that.plan_name,
                         dailyNotes:{},
                         planNotes:{}
                      }
                      db.insert(that.notes)
                }
                if(that.notes.hasOwnProperty('planNotes')){
                    that.notesTitle=Object.keys(that.notes.planNotes)
                }
                
                if(that.notesTitle.length==0){
                    that.empty=true
                }
            })
    },
    methods:{
        goBack(){
             let that=this
             this.$router.push({
            name:'workbenchPlan',
            params:{
              name:that.plan_name
            }
          })
        },

        //新增计划总结
        toMarkdown(){
            let that=this
            this.$router.push({
                name:'markdown',
                params:{
                    plan_name:that.plan_name,
                    edit:false,
                    preview:false
                }
            })
        },

        //编辑计划总结
        MarkdownEdit(name){
             let that=this
             let content=that.notes.planNotes[name]
              this.$router.push({
                name:'markdown',
                params:{
                    plan_name:that.plan_name,
                    context:content,
                    title:name,
                    edit:true,
                    preview:false
                }
            })
        },
        MarkdownPreview(name){
             let that=this
             let content=that.notes.planNotes[name]
              this.$router.push({
                name:'markdown',
                params:{
                    plan_name:that.plan_name,
                    context:content,
                    title:name,
                    edit:true,
                    preview:true
                }
            })
        }
    },
    data(){
        return{
            notes:[],
            notesTitle:[],
            plan_name:'',
            empty:false
        }
    }
}
</script>

<style scoped>
.main{
    width:100%;
    height: 100%;
    box-sizing: border-box;
}

.planDisplay{
    width: 99%;
    height: 100%;
    margin: auto;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}

.planDisplay >>> .el-card__body{
  padding: 15px;
}

.planDisplay:hover{
    cursor: pointer;
    background-color: rgb(230, 246, 255);
}

.planBasic{
    width: 80%;
    float: left;
}

.planExcutor{
    width: 52%;
    margin-top: 2%;
    margin-left: 7%;
    float: left;
}
.btnCard {
     width: 99%;
     margin:auto
}
.btnCard >>> .el-card__body{
  padding: 10px;
}
</style>