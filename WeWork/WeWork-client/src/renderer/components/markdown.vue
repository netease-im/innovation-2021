<template>
<div class="main">
   <el-card style="height:100%;overflow-y:auto">
    <el-page-header @back="goBack" content="编辑计划总结">
    </el-page-header>
    <el-divider></el-divider>
    <mavon-editor  v-if="preview"  @save="save"  v-model="context" 
        :subfield="false"
        :defaultOpen="'preview'"
        :toolbarsFlag="false"
        :editable="false"
        :ishljs="true"/>
    
      <mavon-editor v-if="!preview"   @save="save" v-model="context" :toolbars="toolbars"/>
   </el-card>

    <el-dialog :visible.sync="saveDialog"  title="保存">
    <el-form label-width="auto">
    <el-form-item label="请输入标题">
    <el-input v-model="title"></el-input>
    </el-form-item>
    <el-button type="success" @click="doSave">确认保存</el-button>
    </el-form>
    </el-dialog>
</div>
</template>

<script>
var database =require('nedb')
let db=[]
export default {
    created(){
         let that=this
         this.plan_name=this.$route.params.plan_name
         if(this.$route.params.edit){
             this.context=this.$route.params.context
             this.title=this.$route.params.title
         }
         this.preview=this.$route.params.preview
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
                })
    },
    methods:{

        //保存md文章内容
        doSave(){
            let that=this
            if(this.context==''){
                this.$message({
                    message:'计划内容不能为空',
                    type:'error'
                })
            }
            if(!this.notes.hasOwnProperty('planNotes')){
                this.notes.planNotes={}
            }
            delete this.notes.planNotes[this.title]
            this.notes.planNotes[this.title]=this.context
            db.update({type:'planItemNotes',plan:that.plan_name},that.notes,function(err,numReplaced){
                that.$message({
                    message:'保存成功',
                    type:'success'
                })
            })

               //云同步代码
        if(this.$store.state.dbName=='./cloud.db'){
          
          let plan=[]

          db.find({type:'planItemTest2',"plan.name":that.plan_name},function(err,Docs){
              if(Docs.length!=0){
                  plan=Docs[0]
              }else{
                  that.$message({
                      message:'云端同步失败，请检查网络设置',
                      type:'error'
                      })
                  that.$store.state.syncing=false
                  return
              }
          
           
          that.$store.state.syncing=true
          that.$http.post(that.$store.state.contextUrl+'/uploadPlan',that.$qs.stringify({

          user_id:that.$store.state.user_id,
          plan:JSON.stringify([plan]),
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
      })
    }
            

        },
        save(){
            this.saveDialog=true
        },
        goBack(){
            if(this.$route.params.hasOwnProperty('plan')){
                 this.$router.push({
                     name:'myPlanDisplay',
                     params:{
                         plan:this.$route.params.plan
                     }
           })
           return 
        }
            let that=this
            this.$router.push({
                name:'planNote',
                params:{
                    plan_name:that.plan_name
                }
            })

        }
    },
    data(){
        return {
            context:'',//输入的数据
            title:'',
            toolbars: {
              bold: true, // 粗体
              italic: true, // 斜体
              header: true, // 标题
              underline: true, // 下划线
              mark: true, // 标记
              superscript: true, // 上角标
              quote: true, // 引用
              ol: true, // 有序列表
              code: true, // code
              /* 1.3.5 */
              undo: true, // 上一步
              trash: true, // 清空
              save:true
            },

            saveDialog:false,
            notes:[],
            plan_name:'',
            preview:false
        }
    }
}
</script>

<style scoped>
.main{
   font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
   height:100%;
   overflow-y: hidden;
}
</style>