<template>
  <div style="overflow-y: hidden">
    <el-card style="height: 100%; overflow-y: auto">
      <el-page-header @back="goBack" content="计划资料"> </el-page-header>
      <el-divider></el-divider>
      <span>文件列表</span>
      <el-table
        :data="fileList"
        style="width: 100%; margin-top: 5px"
        v-if="fileList !== []"
      >
        <el-table-column prop="name" label="文件名" width="250">
        </el-table-column>
        <el-table-column prop="size" label="文件大小 byte"> </el-table-column>
        <el-table-column label="下载" width="60">
          <template slot-scope="scope">
            <div class="download">
              <i
                class="el-icon-download"
                @click="downloadFile(planId, scope.row.name)"
              ></i>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="删除" width="60">
          <template slot-scope="scope">
            <div class="delete">
              <i
                class="el-icon-delete"
                @click="deleteFile(planId, scope.row.name)"
              ></i>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <div class="upload-container">
        <!-- upload -->
        <el-button
          @click="showUploadFile"
          class="uploadBtn"
          style="margin-top=20px"
          >上传文件</el-button
        >
        <el-dialog
          class="dialog-container"
          title="上传文件"
          :visible.sync="dialogVisible"
          @closed="uploadDialogClosed"
          width="50%"
        >
          <!-- upload -->
          <el-upload
            class="upload-demo"
            drag
            :action="uploadUrl"
            :name="planId"
            :on-success="uploadSuccess"
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
          </el-upload>
          <!--  -->
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false"
              >完成</el-button
            >
          </span>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      planName: "",
      planId: "",
      fileList: [],
      dialogVisible: false,
      uploadUrl: this.$store.state.contextUrl + "/uploadFile",
      cowork:null
    };
  },
  created() {
    this.planName = this.$route.params.planName;
    this.planId = this.$route.params.planId + "";
    
    if(this.$route.params.hasOwnProperty("cowork")){
      this.cowork=this.$route.params.cowork
    }

    this.getFileList();
  },

  methods: {
    // 请求文件列表
    getFileList() {
      let that = this;
      this.$http
        .post(
          this.$store.state.contextUrl + "/getFileInfo",
          this.$qs.stringify({
            id: that.planId,
          })
        )
        .then(function (res) {
          that.fileList = res.data;
        })
        .catch(function (error) {
          console.log(error.message);
        });
    },
    // 显示上传文件对话框
    showUploadFile() {
      this.dialogVisible = true;
      console.log("上传组件中此plan的id", this.planId);
    },
    // 关闭上传文件对话框前
    uploadDialogClosed() {
      this.getFileList();
    },
    // 返回
    goBack() {
      let that = this
      
      if(this.cowork!=null){
        this.$router.push({
          name:'coworkDisplay',
          params:{
            cowork:that.cowork
          }
      })
      return 
    }

      this.$router.push({
        name: "workbenchPlan",
        params: {
          name: that.planName,
        },
      });
    },
    //上传成功
    uploadSuccess(response, file, fileList) {
      console.log("上传文件的响应", response);
      if (response.status === -1) {
        this.errorResults = response.data;
        if (this.errorResults) {
          this.importFlag = 2;
        } else {
          this.dialogImportVisible = false;
          this.$message.error(response.errorMsg);
        }
      } else {
        this.importFlag = 3;
        this.dialogImportVisible = false;
        this.$message.info("导入成功");
        this.doSearch();
      }
    },
    // 请求下载文件
    async downloadFile(plan_id, fileName) {
      let that = this;
      this.$http({
        method: "post",
        url: this.$store.state.contextUrl + "/downloadFile",
        data: this.$qs.stringify({
          id: plan_id,
          fileName: fileName,
        }),
        responseType: "blob",
      })
        .then(function (res) {
          let url = URL.createObjectURL(res.data);
          let download = document.createElement("a");
          download.style.display = "none";
          download.href = url;
          download.download = fileName;
          document.body.appendChild(download);
          download.click();
          document.body.removeChild(download);
        })
        .catch(function (error) {
          if (error.response) {
            if (error.response.status == 404) {
              that.$message({
                message: "未找到该文件",
                type: "error",
              });
            } else {
              console.log("Error", error.message);
            }
          }
        });
    },
    // downloadFile(plan_id, fileName) {
    //   let that = this;
    //   console.log("测试download传递数据是否有问题", plan_id, fileName);
    //   this.$http({
    //     method: "post",
    //     url: this.$store.state.contextUrl + "/downloadFile",
    //     data: this.$qs.stringify({
    //       id: plan_id,
    //       fileName: fileName,
    //     }),
    //     responseType: "blob",
    //   })
    //     .then(function (res) {
    //       let url = URL.createObjectURL(res.data);
    //       let download = document.createElement("a");
    //       download.style.display = "none";
    //       download.href = url;
    //       download.download = fileName;
    //       document.body.appendChild(download);
    //       download.click();
    //       document.body.removeChild(download);
    //     })
    //     .catch(function (error) {
    //       if (error.response) {
    //         if (error.response.status == 404) {
    //           that.$message({
    //             message: "未找到该文件",
    //             type: "error",
    //           });
    //         } else {
    //           console.log("Error", error.message);
    //         }
    //       }
    //     });
    // },
    // 请求删除文件
    deleteFile(plan_id, fileName) {
      let that = this;
      this.$confirm("确认删除该文件", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$http
            .post(
              this.$store.state.contextUrl + "/deleteFile",
              this.$qs.stringify({
                id: plan_id,
                fileName: fileName,
              })
            )
            .then(function (res) {
              if (res.data == "success") {
                that.getFileList();
                that.$message({
                  message: "删除成功",
                  type: "success",
                });
              } else {
                that.$message({
                  message: "删除失败",
                  type: "error",
                });
              }
            })
            .catch(function (error) {
              console.log(error.message);
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style scoped>
.download,
.delete {
  margin: 0 12px;
  display: inline-block;
}
.download :hover {
  color: #489eff;
}
.delete :hover {
  color: red;
}
.uploadBtn {
  margin: 20px 0 0 10px;
}
.upload-container {
  display: flex;
  text-align: center;
  align-items: center;
}
</style>