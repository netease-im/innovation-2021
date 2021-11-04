<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>用户管理</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="../layuiadmin/style/admin.css" media="all">
</head>
<body>

<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">添加用户</div>
        <div class="layui-card-body" style="padding: 15px;">
            <form class="layui-form" action="" lay-filter="component-form-group">
                <div class="layui-form-item">
                    <label class="layui-form-label">用户名</label>
                    <div class="layui-input-block">
                        <input type="text" name="username" lay-verify="title" autocomplete="off" placeholder="请输入用户名"
                               class="layui-input">
                    </div>
                </div>

                    <div class="layui-inline">
                        <label class="layui-form-label">权限组ID</label>
                        <div class="layui-input-inline">
                            <select name="ag_id" lay-verify="required" lay-search="">
                                <option value="">请选择</option>
                                <c:forEach items="${flatformAdminGroups}" var="group">
                                   <option value=${group.ag_id}>${group.ag_name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                <div class="layui-form-item">
                    <label class="layui-form-label">登录密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="password" id="password" lay-verify="pass" autocomplete="off" placeholder="请输入密码"
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">确认密码</label>
                    <div class="layui-input-block">
                        <input type="password" name="" id="password1" lay-verify="pass1" autocomplete="off" placeholder="请再次输入密码"
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">验证手机</label>
                        <div class="layui-input-inline">
                            <input type="tel" name="phone" lay-verify="required|phone" placeholder="请输入手机号"
                                   autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </div>
                <input type="hidden" name="status" id="status" value="1">
                <div class="layui-form-item layui-layout-admin">
                    <div class="layui-input-block">
                        <div class="layui-footer" style="left: 0;">
                            <button class="layui-btn" lay-submit="" lay-filter="component-form-demo">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
        </div>
    </div>
</div>


<script src="../layuiadmin/layui/layui.js"></script>
<script src="../layuiadmin/jquery.min.js"></script>
<script>
    layui.config({
        base: '../layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'form', 'laydate'], function () {
        var $ = layui.$
            , admin = layui.admin
            , element = layui.element
            , layer = layui.layer
            , laydate = layui.laydate
            , form = layui.form;

        form.render(null, 'component-form-group');

        laydate.render({
            elem: '#LAY-component-form-group-date'
        });

        /* 自定义验证规则 */
        form.verify({
            title: function (value) {
                if (value.length < 1) {
                    return '请输入必填内容';
                }
            }
            , pass: [/(.+){6,12}$/, '密码必须6到12位']
            , content: function (value) {
                layedit.sync(editIndex);
            }
            ,pass1: function(value){
                if(value!=$("#password").val()){
                    return '两次密码输入不同,请重新输入';
                }
            }
            ,usercheck:function (value){
                var msg;
                var data = {};
                data['account'] = value;
                 $.ajax({
                    type:"post",
                    url: "check",
                    async: false,
                    data:data,
                    dataType:'json',
                    success: function(data) {
                        if(data){
                            msg = "该账号已存在";
                        }
                    },
                    error: function() {
                    }
                  });
                if(value=='')
                    msg="账号不能为空"
                return msg;
          }

        });
        form.on('switch(switchTest)', function(data) {
            layer.msg((this.checked ? '已启用' : '已禁用'), {
                offset: '6px'
            });
            var sta = this.checked ? '1' :'0';
            document.getElementById("status").value=sta;
        });
        /* 监听提交 */
        form.on('submit(component-form-demo)', function (data) {
            $.post("insert", data.field, function (data) {
                // 获取 session
                if (data.status == "error") {
                    layer.alert("您没有新增权限，请联系管理员！");
                }
                if (data.status == "succ") {
                    layer.alert("新增成功", {icon: 6}, function () {
                        location.reload();
                        parent.layui.admin.events.closeThisTabs();
                    });
                }
            });
            return false;
        });
        form.on('select(type)',function (data) {
            if(data.value==1){
                $("#city").hide();
                $("#side").hide();
            }else if(data.value == 2){
                $("#city").show();
                $("#side").hide();
            }else{
                $("#city").show();
                $("#side").show();
            }
        })
    });

</script>
</body>
</html>
