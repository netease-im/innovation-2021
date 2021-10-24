<template>
    <div class="bodyDiv">
    <main-head v-if="rk"></main-head>
    <sub-header></sub-header>
    <router-view v-if="refresh" class="mainDiv"></router-view>
    </div>
</template>

<script>
import MainHead from '../header/MainHead.vue'
import SubHeader from '../header/subHeader.vue'

var database =require('nedb')
let db=[]

export default {
  created(){
    db=new database({
          filename:'./data/main/cloud.db',
          autoload:true
        })
  },
  components: { MainHead,SubHeader },
  data(){
    return {
      refresh:true,
      rk:true
    }
  },
  methods:{
    reload(){
      this.refresh=false

      //nextTick: Dom数据更新之后调用
      this.$nextTick(function(){
        this.refresh=true
        
      })
    },
    remake(){
      this.rk=false

      this.$nextTick(function(){
        this.rk=true
      })
    },
    refreshDB(){
      let that=this
      if(this.$store.state.login){

        db=new database({
          filename:'./data/main/cloud.db',
          autoload:true
        })
        
        this.$http.post(that.$store.state.contextUrl+'/downloadPlan',that.$qs.stringify({
          user_id:that.$store.state.user_id
        })).then(function(res){
          db.remove({}, { multi: true }, function (err, numRemoved) {
          let cloudPlanArray=res.data
          let planArr=[]
          let planLog=[]
          for(let i=0;i<cloudPlanArray.length;i++){
             let plan=JSON.parse(cloudPlanArray[i].plan_text)
             plan.id=cloudPlanArray[i].id
             planArr.push(plan)
             planLog.push(JSON.parse(cloudPlanArray[i].planLog_text)) 
          }
          db.insert(planArr)
          db.insert(planLog)
          
          that.$http.post(that.$store.state.contextUrl+'/downloadGoal',that.$qs.stringify({
            user_id:that.$store.state.user_id
          })).then(function(res){
            let cloudGoalArr=res.data
            let goals=[]
            cloudGoalArr.forEach(function(item,index){
              goals.push(JSON.parse(item.goal))
            })
            db.insert(goals)
            
            that.$http.post(that.$store.state.contextUrl+'/downloadCowork',that.$qs.stringify({
              partner:that.$store.state.user_id
              })).then(function(res){
                let cowork=[]
                res.data.forEach(function(item,index){
                  item.type='cowork'
                  item.plan=JSON.parse(item.plan)
                  item.planLog=JSON.parse(item.planLog)
                  cowork.push(item)
                  })
                  db.insert(cowork)
                  that.logining=false
                  that.reload()
                  that.$store.state.syncing=false                             
              })
            })
          })
        })
      }
    }
  },
  provide(){
    return{
      reload:this.reload,
      remake:this.remake,
      refreshDB:this.refreshDB
    }
  }
}
</script>

<style scoped>
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
}
.mainDiv{
  width:100%;
  height: 100%;
  overflow-y: auto;
  word-break: break-all;
  flex-grow: 1;
  position: relative;
  overflow-x: hidden;
  padding-bottom: 1px;
  box-sizing: border-box;
}

</style>