<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>帖子管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../layuiadmin/style/admin.css" media="all">
</head>
<body>
<div class="layui-container">
    <form class="layui-form" action="" enctype="multipart/form-data">
        <div class="layui-form-item">
            <label class="layui-form-label">标题</label>
            <div class="layui-input-block">
                <input id="title"  type="text" name="issueid" maxlength="300" required  lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">上传文件</label>
            <div class="layui-input-inline uploadHeadImage">
                <div class="layui-upload-drag" id="uploadFile">
                    <i class="layui-icon"></i>
                    <p>点击上传文件，或将文件拖拽到此处！</p>
                </div>
            </div>
            <div class="layui-input-inline">
                <div id="uploadResult" class="layui-upload-list">

                </div>
            </div>
        </div>


        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn"  id="uploadBtn">提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>
</div>



<script src="../layuiadmin/layui/layui.js"></script>
<script src="../layuiadmin/jquery.min.js"></script>
<script>
    layui.config({
        base: '../layuiadmin/' //静态资源  所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'form', 'laydate','upload'], function () {
        var $ = layui.$
            , admin = layui.admin
            , element = layui.element
            , layer = layui.layer
            , laydate = layui.laydate
            , form = layui.form
            , upload = layui.upload;

        form.render(null, 'component-form-group');

        laydate.render({
            elem: '#LAY-component-form-group-date'
        });


        var uploadInst = upload.render({
            elem: '#uploadFile', //绑定元素
            url: '/issue/upload', //上传接口
            method: 'POST',
            auto: false,
            accept: 'file',
            bindAction: '#uploadBtn',
            size: 10240,//限制文件大小10M
            multiple: true,
            data:{
                issueid:1261
            },

            before: function(obj) {
                alert(JSON.stringify(obj))
                layer.load();

                this.data.title = $('#title').val();
            },

            done: function(data, index, upload) {//上传完毕回调
                alert(data)
                layer.closeAll('loading');

                if (data.no === 1) {
                    layer.msg("上传成功！", {
                        icon: 6
                    });
                } else {
                    layer.msg("上传失败，请稍后重试！", {
                        icon: 5
                    });
                }

                var uuid = data.uuid;

                $("#uploadResult").append('<input readonly type="text" value="'+uuid+'" class="layui-input" />');
            }
            , error: function() {//请求异常回调
                layer.closeAll('loading');
                layer.msg('网络异常，请稍后重试！');
            }
        });





    });

</script>
</body>
</html>
