<template>
  <div class="windowbox">
    <div class="top-menu"><h3>WeWork</h3></div>
    <div class="box-content">
      <el-container @click.native.left="mouseMenuShow = false">
        <el-aside>
          <div class="btn-group">
            <el-button type="primary" size="small" @click="createFile">
            新增任务小记
            </el-button>
            <el-button type="text" size="small" @click="reset">全部</el-button>
            <div class="search">
              <el-input placeholder="搜索..." v-model="searchKey" clearable></el-input>
            </div>
          </div>

          <div class="card-box">
            <el-card
              shadow="never"
              v-for="(i, index) in fileList"
              :key="index"
              @click.native="readFile(i, index)"
              :class="index == activeIndex && 'active'"
              v-show="i.indexOf(searchKey) != -1"
              >{{ dataObj[i] }}</el-card
            >
          </div>
        </el-aside>

        <el-container>
          <el-main>
            <wang-editor
              v-model="fileContent"
              :isClear="isEditorClear"
              @change="editorChange"
              v-if="!!fileName && !createCard"
              @keyup.native.ctrl.83="saveContent"
              @contextmenu.native.prevent="mouseMenu($event)"
            ></wang-editor>
            <about-info v-else></about-info>
          </el-main>
        </el-container>
      </el-container>

      <div class="mouse-menu" :style="mouseMenuStyle" v-show="mouseMenuShow" >
        <el-card>
          <el-button type="primary" size="small" @click="saveContent">保存</el-button
          ><br />
          <el-button type="danger" size="small" @click="removeFile(fileName)"
            >删除</el-button
          >
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import WangEditor from "./wangEditor.vue";
import AboutInfo from "./aboutInfo.vue";
import "element-ui/lib/theme-chalk/index.css";
const fs = require("fs");
const { remote } = require("electron");
let win = remote.getCurrentWindow();
export default {
  name: "indexBook",
  data() {
    return {
      targetDir: "data/book", // 数据所在文件夹
      createCard: false, // 添加笔记弹窗
      searchKey: "", // 搜索框
      fileName: "", // 笔记名
      fileList: [], // 文件列表
      fileContent: "", // 文件内容
      fileSelect: "", // 当前选择的文件
      dataObj: {}, // 所有的数据
      isEditorClear: false,
      activeIndex: -1, // 左侧菜单激活项
      mouseMenuShow: false, // 是否显示右键菜单
      mouseMenuStyle: {
        // 右击菜单定位
        top: 0,
        left: 0,
      },
    };
  },
  watch: {
    searchKey(n, o) {
      this.reset();
    },
  },
  components: {
    WangEditor,
    AboutInfo,
  },
  methods: {
    hideWindow() {
      win = remote.getCurrentWindow();
      win.minimize();
      console.log("x");
    },
    closeWindow() {
      win = remote.getCurrentWindow();
      win.close();
    },
    readFile(fileName, index) {
      // 获取文件内容
      let fileNameTemp = this.targetDir + "/" + fileName + ".txt";
      fs.readFile(fileNameTemp, (err, data) => {
        if (err) {
          this.$notify({
            title: "提示",
            message: "查询列表失败",
            type: "fail",
            duration: 1000,
          });
          return;
        }

        this.fileContent = data;
        this.fileName = fileName;
        this.activeIndex = index;

        this.$notify({
          title: "提示",
          message: "已切换至 " + fileName,
          type: "success",
          duration: 1000,
        });
      });
    },
    writeFile() {
      // 生成文件
      if (this.fileList.indexOf(this.fileName) !== -1) {
        this.$notify({
          title: "提示",
          message: "笔记已经存在",
          type: "warning",
          duration: 1000,
        });

        this.fileName = "";
        this.createCard = false;
        return;
      }

      let fileNameTemp = this.targetDir + "/" + this.fileName + ".txt";
      fs.writeFile(fileNameTemp, "", (err) => {
        if (err) {
          this.$notify({
            title: "提示",
            message: "当前目录没有权限进行此操作，请检查系统权限",
            type: "fail",
            duration: 1500,
          });

          return console.log(err);
        }

        this.$notify({
          title: "提示",
          message: this.fileName + " 创建成功",
          type: "success",
          duration: 1000,
        });

        this.getDataList();
        this.reset();
        this.createCard = false;
      });
    },
    removeFile(fileName) {
      // 删除文件
      this.$confirm("确认删除该笔记", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let tempPath = this.targetDir + "/" + fileName + ".txt";
          fs.unlink(tempPath, (err) => {
            if (err) {
              this.$notify({
                title: "提示",
                message: "当前目录没有权限进行此操作，请检查系统权限",
                type: "fail",
                duration: 1500,
              });

              throw err;
            }

            this.$notify({
              title: "提示",
              message: fileName + " 删除成功",
              type: "success",
              duration: 1000,
            });

            this.getDataList();
            this.reset();
          });
        })
        .catch(() => {});
    },
    getDataList() {
      // 获取文件列表
      let dirList = fs.readdirSync(this.targetDir);
      let fileObj;
      this.fileList = [];
      dirList.forEach((fileName) => {
        fileObj = fileName.slice(0, -4);
        Vue.set(this.dataObj, fileObj, fileObj);
        this.fileList.push(fileObj);
      });
    },
    saveContent() {
      // 保存内容
      if (!this.fileName) {
        return;
      }

      let fileNameTemp = this.targetDir + "/" + this.fileName + ".txt";
      fs.writeFile(fileNameTemp, this.fileContent, (err) => {
        if (err) {
          this.$notify({
            title: "提示",
            message: "当前目录没有权限进行此操作，请检查系统权限",
            type: "fail",
            duration: 1500,
          });

          return console.log(err);
        }

        this.$notify({
          title: "提示",
          message: this.fileName + " 保存成功",
          type: "success",
          duration: 1500,
        });

        this.mouseMenuShow = false;
      });
    },
    createFile() {
      this.$prompt("请输入笔记名称", "新增笔记", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputPattern: /^.{1,15}$/,
        inputErrorMessage: "笔记名长度为1-15位",
      })
        .then(({ value }) => {
          this.fileName = value;
          this.createCard = true;
          this.writeFile();
        })
        .catch(() => {
          this.createCard = false;
          this.reset();
        });
    },
    reset() {
      this.activeIndex = -1;
      this.fileName = "";
      this.fileContent = "";
      this.mouseMenuShow = false;
    },
    editorChange(val) {
      console.log(val);
    },
    mouseMenu(e) {
      this.mouseMenuShow = true;
      this.mouseMenuStyle = {
        top: e.pageY + "px",
        left: e.pageX + "px",
      };
    },
  },
  mounted() {
    // 创建数据文件夹
    if (!fs.existsSync(this.targetDir)) {
      fs.mkdirSync(this.targetDir);
    }

    // init
    this.getDataList();
  },
};
</script>

<style lang="less" scoped>
.windowbox{
     font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB",
    "Microsoft YaHei","微软雅黑",Arial,sans-serif;
}
i {
  display: inline-block;
  width: 20px;
  height: 20px;
  -webkit-app-region: no-drag;
}
.el-icon-minus::before {
  -webkit-app-region: no-drag;
}
.el-icon-close::before {
  -webkit-app-region: no-drag;
}
.el-icon-minus {
  position: fixed;
  top: 10px;
  right: 50px;
  font-size: larger;
  color: #fff;
  font-weight: bolder;
  cursor: pointer;
}
.el-icon-close {
  position: fixed;
  top: 10px;
  right: 10px;
  font-size: larger;
  font-weight: bolder;
  cursor: pointer;
  color: #fff;
}
.top-menu {
  height: 28px;
  padding: 2px;
  background-color: rgb(90, 40, 250);
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  -webkit-app-region: drag;
  cursor: pointer;
  text-align: center;
}
html,
body {
  margin: 0;
}
#app > .windowbox > .box-content > .el-container {
  position: fixed;
  top: 40px;
}
#app > .windowbox > .box-content > .el-container > .el-aside {
  position: fixed;
  top: 40px;
}
html,
body,
.el-container,
.el-aside,
#app {
  height: 100%;
  overflow-y: auto;
}

.box-content {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

.el-aside {
  position: fixed;
  top: 0;
  left: 0;
  width: 300px;
  border-top: 1px solid #f2f2f2;
  border-right: 1px solid #f2f2f2;

  .btn-group {
    position: fixed;
    z-index: 99;
    padding: 10px;
    background-color: #ffffff;
    width: 299px;
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;

    .search {
      margin-top: 10px;
    }

    &-bottom {
      bottom: 0;
    }
  }

  .card-box {
    padding: 105px 0 55px;
    /*border-bottom: 1px solid #f2f2f2;*/

    .el-card {
      border: none;
      border-top: 1px solid #f2f2f2;
      cursor: pointer;
      -webkit-border-radius: 0;
      border-radius: 0;
      -webkit-transition: 0.2s;
      transition: 0.2s;

      &:hover,
      &.active {
        padding-left: 10px;
        background-color: #f8f8f8;
        -webkit-transition: 0.2s;
        transition: 0.2s;
      }
    }

    .el-card__body {
      padding: 15px;
      color: #666;
    }
  }
}

.el-container {
  > .el-container {
    margin-left: 300px;
  }

  .el-main {
    padding: 2px 0 0 0;
    height: 100%;
    border-top: 1px solid #f2f2f2;
  }
}

.mouse-menu {
  position: fixed;
  /deep/ .el-card__body {
    padding: 10px 10px 0;
  }

  /deep/ .el-button {
    margin-bottom: 10px;
    padding: 8px 30px;
  }
}


::-webkit-scrollbar {
  width: 6px;
  height: 1px;
}

::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 10px;
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #999999;
}

::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 10px;
  background: #ededed;
}
</style>
