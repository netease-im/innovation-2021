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
        <div class="layui-card-header">平台功能模块</div>
        <div class="layui-card-body" style="padding: 15px;">
            <form class="layui-form" action="" lay-filter="component-form-group">
                <input type="hidden" name="au_id" value="${flatformAdminMenu.au_id}">
                <input type="hidden" value="${flatformAdminMenu.au_p_id}" id="au_p_id">
                <div class="layui-form-item">
                    <label class="layui-form-label">URL地址</label>
                    <div class="layui-input-block">
                        <input type="text" name="au_url" lay-verify="required" value="${flatformAdminMenu.au_url}" autocomplete="off" placeholder="请输入URL"
                               class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">名称</label>
                    <div class="layui-input-block">
                        <input type="text" name="au_title" lay-verify="required" autocomplete="off" placeholder="请输入名称" value="${flatformAdminMenu.au_title}" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">描述</label>
                    <div class="layui-input-block">
                        <input type="text" name="au_info" lay-verify="required" autocomplete="off" placeholder="请输入描述" value="${flatformAdminMenu.au_info}" class="layui-input">
                    </div>
                </div>

                <div class="layui-form-item">
                    <div class="layui-inline">
                        <label class="layui-form-label">功能级别</label>
                        <div class="layui-input-inline">
                            <select lay-filter="test" name="au_level" lay-verify="required" lay-search="">
                                <option value="1" <c:if test="${flatformAdminMenu.au_level == 1}">selected</c:if>>一级菜单</option>
                                <option value="2" <c:if test="${flatformAdminMenu.au_level == 2}">selected</c:if>>二级菜单</option>
                                <option value="3" <c:if test="${flatformAdminMenu.au_level == 3}">selected</c:if>>三级菜单</option>
                            </select>
                        </div>
                    </div>
                </div>

                    <input type="hidden" name="au_type" value="1">



                <div class="layui-form-item">
                    <label class="layui-form-label">上级模块</label>
                    <div class="layui-input-block">
                        <select name="au_p_id" id="au_pid" lay-verify="required" lay-search="">
                        </select>
                    </div>
                </div>



                <div class="layui-form-item">
                    <label class="layui-form-label">菜单内点击</label>
                    <div class="layui-input-block">
                        <input type="checkbox"  name="auclickable"  <c:if test="${flatformAdminMenu.au_clickable == 1}"> checked</c:if> id="auClickable"  lay-skin="switch" lay-text="有效|无效">
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">功能前图标</label>
                    <div class="layui-input-block">
                        <input type="text" name="au_class" lay-verify="required" value="${flatformAdminMenu.au_class}" autocomplete="off" placeholder="请输入class"
                               class="layui-input">
                    </div>
                </div>
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
            elem: '#LAY-component-form-group-date1'
            , type: 'datetime'
        });
        laydate.render({
            elem: '#LAY-component-form-group-date2'
            , type: 'datetime'
        });
        $(function(){
            $("#au_pid").empty();
            var res = {};
            res['au_p_id'] = $("#au_p_id").val();

            var url = "findByPid";//get
            if($("#au_p_id").val()>0){
                $.ajax({
                    type:"post",
                    url: url,
                    async: false,
                    data:res,
                    dataType:'json',
                    success: function(data) {
                        if(data.status ==="succ"){

                            $('#au_pid').append(new Option(data.flatformAdminMenu.au_title,data.flatformAdminMenu.au_id));//往下拉菜单里添加元素

                        }else{
                            $('#au_pid').append(new Option("数据异常","0"));
                        }
                    },
                    error: function() {

                    }
                });
            }else{
                $('#au_pid').append(new Option("无上级菜单","0"));
            }


        })
        form.render();//菜单渲染 把内容加载进去
        /* 自定义验证规则 */
        form.verify({
        });

        /* 监听提交 */
        form.on('submit(component-form-demo)', function (data) {
            $.post("menu/update", data.field, function (data) {
                // 获取 session
                if (data.status === "error") {
                    layer.alert("您没有更新权限，请联系管理员！");
                }
                if (data.status === "succ") {
                    layer.alert("更新成功", {icon: 6}, function () {
                        parent.layui.admin.events.closeThisTabs();
                    });
                }
            });
            return false;
        });
        form.on('select(test)', function(data){
            if (data.value>1){
                $("#au_pid").empty();
                var res = {};
                res['value'] = data.value -1;
                var url = "getMenuList";//get
                $.ajax({
                    type:"post",
                    url: url,
                    async: false,
                    data:res,
                    dataType:'json',
                    success: function(data) {
                        if(data.status ==="succ"){
                            $.each(data.flatformAdminMenus,function(index,item){

                                $('#au_pid').append(new Option(item.au_title,item.au_id));//往下拉菜单里添加元素
                            })

                        }else{
                            $('#au_pid').append(new Option("数据异常","0"));
                        }

                    },
                    error: function() {
                        alert("发生错误");
                    }
                });
                form.render();//菜单渲染 把内容加载进去
            }else{
                $("#au_pid").empty();
                $('#au_pid').append(new Option("无上级菜单","0"));
                form.render();//菜单渲染 把内容加载进去
            }

        });
    });
</script>
</body>
</html>

