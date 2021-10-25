<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>帖子详情</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../layuiadmin/style/admin.css" media="all">
</head>
<body>


<br>
<fieldset class="layui-elem-field layui-field-title">
    <legend>标题：${issue.iTitle}</legend>
</fieldset>

<div class="layui-field-box">
    内容：${issue.iDetails}
</div>

<div class="layui-field-box">
    性别限制：
    <c:if test="${issue.iSex == 1}">
        <a>男</a>
    </c:if>
    <c:if test="${issue.iSex == 2}">
        <a>女</a>
    </c:if>
    <c:if test="${issue.iSex == 3}">
        <a>无限制</a>
    </c:if>
</div>

<div class="layui-field-box">
    拼团所需人数：${issue.iPeopleNumber}
</div>
<%--
<fieldset class="layui-elem-field layui-field-title">
    <legend>评论</legend>
</fieldset>
<div class="layui-field-box">


    <c:forEach items="${comment}" var="i">
    <c:if test="${not empty i}">
        <ul>
            <li>用户:${i.from_name}(${i.from_user_id})
                <c:if test="${i.to_user_id!=0}">
                    对${i.to_name}(${i.to_user_id})
                </c:if>

                说:${i.reply_msg}</li>
        </ul>
    </c:if>
    <c:if test="${empty i}">
        <ul>
            <li>查询无数据</li>
        </ul>
    </c:if>
    </c:forEach>
</div>
--%>
<fieldset class="layui-elem-field">
    <legend>发帖用户</legend>
    <div class="layui-field-box">
        <blockquote class="layui-elem-quote">
            ID: ${issue.fuId} 用户名: ${issue.frontUser.fuNickname}
        </blockquote>
    </div>
</fieldset>


<fieldset class="layui-elem-field">
    <legend>发帖时间</legend>
    <div class="layui-field-box">
        <blockquote class="layui-elem-quote">发帖时间：${issue.iCreateTime.toLocaleString()}</blockquote>
    </div>
</fieldset>

<fieldset class="layui-elem-field">
    <legend>图片</legend>
    <div class="layui-field-box" id="imgsdiv">

    </div>
</fieldset>
<fieldset class="layui-elem-field">
    <legend>音频</legend>
    <div class="layui-field-box" id="audiodiv">
    </div>
</fieldset>

<fieldset class="layui-elem-field">
    <legend>视频</legend>
    <div class="layui-field-box" id="videodiv">

    </div>
</fieldset>

</body>

<script src="../layuiadmin/layui/layui.js"></script>
<script src="../layuiadmin/jquery.min.js"></script>
<script>
    layui.use(['form', 'layedit', 'laydate'], function () {
        var $ = layui.$
            , admin = layui.admin
            , element = layui.element
            , layer = layui.layer
            , laydate = layui.laydate
            , form = layui.form;

        var img = "${issue.iImg}";
        var audio = "${issue.iAudio}";
        var video = "${issue.iVideo}";
        var imgs = img.split(",");
        var audios = audio.split(",");
        var videos = video.split(",");

        for (var i = 0; i < imgs.length - 1; i++) {
            $('#imgsdiv').append(
                '<img width="320" height="240" class="layui-icon" src="' + imgs[i] + '"/>'
            );
        }

        for (var i = 0; i < audios.length - 1; i++) {
            $('#audiodiv').append(
                '<audio controls><source src="' + audios[i] + '" ></audio>'
            );
        }

        for (var i = 0; i < videos.length - 1; i++) {
            $('#videodiv').append(
                '<video width="320" height="240" controls><source src="' + videos[i] + '" type="video/mp4"></video>'
            );
        }

    })


</script>


</html>
