
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <base href="<%=basePath %>" />
    <title>工作中心列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="layuiadmin/style/admin.css" media="all">
</head>

<body>
<div class="layui-card layadmin-header" style="display:block;">
    <div class="layui-breadcrumb" lay-filter="breadcrumb">
        <a>主页</a>
    </div>
</div>

<div class="layui-fluid">
    <div class="layui-card" style="overflow: hidden">
        <div id="container"  style=" z-index: 9999;height: 250px;width: 60%;float: left; padding-top: 15px;margin-bottom: 30px"></div>
        <div id="container1"  style="z-index: 9999;height: 280px;width: 38%;float: left; padding-right: 15px;padding-top: 15px;margin-bottom: 30px"></div>

    </div>
</div>
<script src="layuiadmin/layui/layui.js"></script>
<script src="layuiadmin/jquery.min.js"></script>



<script>
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;
        //普通图片上传
        var uploadInst = upload.render({
            elem: '#test11'
            ,url: 'shAcLe/upload'
            ,before: function(obj){
                //预读本地文件示例，不支持ie8
                obj.preview(function(index, file, result){
                    $('#demo').attr('src', result); //图片链接（base64）
                });
            }
            ,done: function(res){
                //如果上传失败
                if(res.code > 0){
                    return layer.msg('上传失败');
                }
                //上传成功
                document.getElementById("accLevelLogo1").value=res.value1;
            }
            ,error: function(){
                //演示失败状态，并实现重传
                var demoText = $('#demoText1');
                demoText.html('<span style="color: #FF5722;">上传照片失败</span> <br><a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function(){
                    uploadInst.upload();
                });
            }
        });
    });
</script>
<script>
</script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/dist/echarts.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts-gl/dist/echarts-gl.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts-stat/dist/ecStat.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/dist/extension/dataTool.min.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/map/js/china.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/map/js/world.js"></script>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/echarts/dist/extension/bmap.min.js"></script>
<script type="text/javascript">
    var $ = layui.jquery;
    var n="";
    ace(n);
    function ace(n1) {
        var shu=new Array();
        var zc;
        var wx=new Array();
        var bf=new Array();
        var count1=100,count2=11,count3=36;//1 图片 2 音频 3 视频
        if(n1!=null && n1!=''){
            zc={deviceInfoXh:n1.deviceInfoXh,workCenterId:n1.workCenterId,status:n1.status,deviceWorkDate:n1.deviceWorkDate,purchaseTime:n1.purchaseTime};
        }else{
            zc={};
        }


    }

</script>
</body>
</html>
