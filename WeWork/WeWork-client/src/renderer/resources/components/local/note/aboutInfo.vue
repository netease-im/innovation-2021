<template>
    <div class="about-info">
        <el-alert title="使用说明" type="success" :closable="false"></el-alert>
        <p>1、快速保存：Ctrl+S</p>
        <p>2、保存或删除：右侧内容区域鼠标右键</p>
    </div>
</template>

<script>
    export default {
        name: 'aboutInfo'
    }
</script>

<style lang="less" scoped>
    .about-info {
        padding: 15px;
        background-color: #fefefe;
        line-height: 1.8;
        color: #999999;
        font-size: 14px;
        height: 100%;
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;

        /deep/ .el-alert{
            margin-bottom: 15px;
            font-size: 16px;
        }
        
        p {
            margin: 0;
            color: #999999;
            font-size: 14px;
        }
    }
</style>