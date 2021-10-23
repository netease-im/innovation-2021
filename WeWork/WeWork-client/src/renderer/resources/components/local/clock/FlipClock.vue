<template>
  <div class="outer">
    <div class="inner">
      <div class="FlipClock" @click="defineTimeVisible=true">
        <Flipper ref="flipperHour1" />
        <Flipper ref="flipperHour2" />
        <em>:</em>
        <Flipper ref="flipperMinute1" />
        <Flipper ref="flipperMinute2" />
        <em>:</em>
        <Flipper ref="flipperSecond1" />
        <Flipper ref="flipperSecond2" />
      </div>
      <div class="rightsome">
        <div class="subjectTitle">
          <div class="topti">待办事项</div>
          <div class="content">
            <el-card @click.native="defineWorkVisible=true" shadow="always" class="box-card"> {{work}}</el-card>
          </div>
        </div>
        <div class="handler">
          <el-button
            type="warning"
            icon="el-icon-video-pause"
            circle
            style="font-size: 20px"
            @click="Suspend"
          ></el-button>
          <el-button
            type="primary"
            icon="el-icon-video-play"
            circle
            style="font-size: 20px"
            @click="Continue"
          ></el-button>
          <el-button
            type="danger"
            icon="el-icon-refresh"
            circle
            style="font-size: 20px"
            @click="Reload"
          ></el-button>
        </div>
      </div>
    </div>
    <div class="clockbottom">WeWork</div>
    
    <el-dialog :visible.sync="defineWorkVisible" :show-close="false">
    <el-descriptions>

    <el-descriptions-item  label="待办事项">
    <el-input size="mini" v-model="work"> </el-input>
    </el-descriptions-item>
    
    </el-descriptions>
    </el-dialog>

    <el-dialog :visible.sync="defineTimeVisible" :show-close="false">
    <el-descriptions>

    <el-descriptions-item class="pick"  label="持续时间">
     <el-time-picker
     size="mini"
    v-model="setTime"
    value-format="HHmmss"
    placeholder="任务持续时间">
   </el-time-picker>
    </el-descriptions-item>
    
    </el-descriptions>
    </el-dialog>

  </div>
</template>

<script>
import Flipper from "./Flipper";
const { Notification } = require("electron").remote;
export default {
  name: "FlipClock",
  data() {
    return {
      timer: null,
      flipObjs: [],
      setTime: "",
      timeLeave: "",
      defineWorkVisible:false,
      defineTimeVisible:false,
      work:'未指定',
    };
  },
  components: {
    Flipper,
  },
  methods: {
    // 初始化数字
    init() {
      // let now = new Date()
      // let nowTimeStr = this.formatDate(new Date(now.getTime()), 'hhiiss')
      let nowTimeStr = this.setTime;
      for (let i = 0; i < this.flipObjs.length; i++) {
        this.flipObjs[i].setFront(nowTimeStr[i]);
      }
    },
    // 开始计时
    run() {
      let that=this
      this.timer = setInterval(() => {
        // 获取当前时间
        // let now = new Date();
        // let nowTimeStr = this.formatDate(new Date(now.getTime() - 1000), "hhiiss");
        // let nextTimeStr = this.formatDate(now, "hhiiss");
        let nowTimeStr = this.timeLeave;
        let nextTimeStr = this.timeminus(this.timeLeave);
        this.timeLeave = this.timeminus(this.timeLeave);
        for (let i = 0; i < this.flipObjs.length; i++) {
          if (nowTimeStr[i] === nextTimeStr[i]) {
            continue;
          }
          this.flipObjs[i].flipDown(nowTimeStr[i], nextTimeStr[i]);
        }
        if (nextTimeStr == "000000") {
          let notification = new Notification({
            title: that.work,
            body: "任务完成",
          });
          notification.show();
          that.$message({
            message:'任务完成',
            type:'success'
          })
          clearInterval(this.timer);
        }
      }, 1000);
    },
    timeminus(Time) {
      // if (Time == "000000") {
      //   return "-1";
      // }
      let Hour1 = Time[0];
      let Hour2 = Time[1];
      let Min1 = Time[2];
      let Min2 = Time[3];
      let Sec1 = Time[4];
      let Sec2 = Time[5];
      if (Sec2 != "0") {
        Sec2 = this.Charminus(Sec2);
      } else if (Sec1 != "0") {
        Sec2 = "9";
        Sec1 = this.Charminus(Sec1);
      } else if (Min2 != "0") {
        // 此处已经是xx-xx-00的状态了
        Sec1 = "5";
        Sec2 = "9";
        Min2 = this.Charminus(Min2);
      } else if (Min1 != "0") {
        Sec1 = "5";
        Sec2 = "9";
        Min2 = "9";
        Min1 = this.Charminus(Min1);
      } else if (Hour2 != "0") {
        Sec1 = "5";
        Sec2 = "9";
        Min2 = "9";
        Min1 = "5";
        Hour2 = this.Charminus(Hour2);
      } else if (Hour1 != "0") {
        Sec1 = "5";
        Sec2 = "9";
        Min2 = "9";
        Min1 = "5";
        Hour2 = "9";
        Hour1 = this.Charminus(Hour1);
      }
      return "" + Hour1 + Hour2 + Min1 + Min2 + Sec1 + Sec2;
    },
    Charminus(x) {
      return parseInt(x) - 1 + "";
    },
    Suspend() {
      clearInterval(this.timer);
    },
    Continue() {
      this.run();
    },
    Reload() {
      clearInterval(this.timer);
      this.timeLeave = this.setTime;
      this.init();
    },
    // 正则格式化日期,
    formatDate(date, dateFormat) {
      /* 单独格式化年份，根据y的字符数量输出年份
     * 例如：yyyy => 2019
            yy => 19
            y => 9
     */
      if (/(y+)/.test(dateFormat)) {
        dateFormat = dateFormat.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      // 格式化月、日、时、分、秒
      let o = {
        "m+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "i+": date.getMinutes(),
        "s+": date.getSeconds(),
      };
      for (let k in o) {
        if (new RegExp(`(${k})`).test(dateFormat)) {
          // 取出对应的值
          let str = o[k] + "";
          /* 根据设置的格式，输出对应的字符
           * 例如: 早上8时，hh => 08，h => 8
           * 但是，当数字>=10时，无论格式为一位还是多位，不做截取，这是与年份格式化不一致的地方
           * 例如: 下午15时，hh => 15, h => 15
           */
          dateFormat = dateFormat.replace(
            RegExp.$1,
            RegExp.$1.length === 1 ? str : this.padLeftZero(str)
          );
        }
      }
      return dateFormat;
    },
    // 日期时间补零
    padLeftZero(str) {
      return ("00" + str).substr(str.length);
    },
  },
  mounted() {
    this.flipObjs = [
      this.$refs.flipperHour1,
      this.$refs.flipperHour2,
      this.$refs.flipperMinute1,
      this.$refs.flipperMinute2,
      this.$refs.flipperSecond1,
      this.$refs.flipperSecond2,
    ];
    this.setTime = "000000";
    this.timeLeave = this.setTime;
    this.init();
    // this.run();
  },
  watch:{
    'defineTimeVisible':{
      handler:function(val){
        if(!val){
          this.Suspend()
          this.timeLeave = this.setTime;
          this.init()
        }
      }
    }
  }
};
</script>

<style scoped>
.outer {
  font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
  border-top: 3px solid #5a28fa;
  border-left: 3px solid #5a28fa;
  border-right: 3px solid #5a28fa;
  width: 750px;
  box-sizing: border-box;
  background-color: #5a28fa;
  cursor: pointer;
}
#app {
  overflow: hidden;
}
.inner {
  box-sizing: border-box;
  width: 744px;
  display: flex;
}
.rightsome {
  /* width: 174px; */
  box-sizing: border-box;
  background-color: #5a28fa;
  border-top: 5px solid black;
  border-bottom: 5px solid black;
  border-right: 5px solid black;
  border-left: 5px solid black;
  border-radius: 30px;
}
.topti {
  box-sizing: border-box;
  font-size: larger;
  font-weight: bolder;
  line-height: 40px;
  color: #388edf;
  text-align: center;
  width: 190px;
  height: 40px;
}
.content {
  display: flex;
  justify-content: center;
}
.handler {
  height: 74px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}
.box-card {
  width: 150px;
  height: 40px;
  font-size: large;
  font-weight: bolder;
  display: flex;
  justify-content: center;
  align-items: center;
}
.FlipClock {
  box-sizing: border-box;
  border: 5px solid black;
  border-radius: 30px;
  padding: 20px 0;
  width: 560px;
  background-color: #fefefe;
  text-align: center;
}
.FlipClock .M-Flipper {
  margin: 0 3px;
}
.FlipClock em {
  display: inline-block;
  line-height: 102px;
  font-size: 66px;
  font-style: normal;
  vertical-align: top;
}
.clockbottom {
  background-color: #5a28fa;
  line-height: 30px;
  width: 744px;
  height: 30px;
  text-align: center;

  font-size: larger;
  font-weight: bolder;
  -webkit-app-region: drag;
  color:azure

}

 .el-time-panel /deep/ .el-time-panel__footer{
  display: none;
}
</style>

<style>
 .el-time-panel /deep/ .el-time-panel__footer{
  display: none;
}
</style>
