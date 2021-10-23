<template>
<div>
<el-upload
  class="upload-demo"
  ref="upload"
  action="http://49.234.97.138/uploadFile"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :file-list="fileList"
  :auto-upload="false"
  :name="'214'">
  <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
  <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
</el-upload>

<el-button @click="downLoad('214','1.jpg')"> 下载文件 </el-button>
<el-button @click="getInfo('file')">获取文件</el-button>
<el-button @click="deleteFile('file','1.jpg')">删除文件</el-button>

</div>
</template>

<script>
  export default {
    data() {
      return {
        fileList: []
      };
    },
    methods: {
      submitUpload() {
        this.$refs.upload.submit();
        console.log(this.$refs.upload)
        // this.$refs.upload.clearFiles()
      },
      handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },

      //下载文件函数
      downLoad(plan_id,fileName){
        let that=this
        this.$http({
          method: 'post',
          url: this.$store.state.contextUrl+"/downloadFile",
          data:this.$qs.stringify({
          id:plan_id,
          fileName:fileName
        }),responseType: 'blob' 
      }).then(function(res){          
          let url=URL.createObjectURL(res.data)
          let download=document.createElement("a")
          download.style.display='none'
          download.href=url
          download.download=fileName
          document.body.appendChild(download)
          download.click()
          document.body.removeChild(download)
        }).catch(function (error) {
          if (error.response) {
            if(error.response.status==404){
              that.$message({
                message:'未找到该文件',
                type:'error'
              })
            }else {
              console.log('Error', error.message);
            }
          }
        });
      },


      //获取计划所有附件信息
      getInfo(plan_id){
        this.$http.post(this.$store.state.contextUrl+"/getFileInfo",this.$qs.stringify({
          id:plan_id
        })).then(function(res){
          alert(JSON.stringify(res.data))
        }).catch(function(error){
          console.log(error.message)
        })
      },


      //删除文件
      deleteFile(plan_id,fileName){
          let that=this
          this.$http.post(this.$store.state.contextUrl+"/deleteFile",this.$qs.stringify({
            id:plan_id,
            fileName:fileName
        })).then(function(res){
          if(res.data=='success'){
            that.$message({
              message:'删除成功',
              type:'success'
            })
          }else{
             that.$message({
              message:'删除失败',
              type:'error'
            })
          }
        }).catch(function(error){
          console.log(error.message)
        })
      }
    }
  }
</script>