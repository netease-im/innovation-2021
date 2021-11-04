<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>权限树扩展分享</title>
    <link rel="stylesheet" type="text/css" href="../layuiadmin/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="../layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" type="text/css" href="../layuiadmin/layui_exts/tree_themes/auth-skin-default.css">
    <link rel="stylesheet" type="text/css" href="../layuiadmin/layui_exts/tree_themes/auth-skin-universal.css">
    <link rel="stylesheet" type="text/css" href="../layuiadmin/layui_exts/tree_themes/template.css">
    <script type="text/javascript" src="../layuiadmin/layui/layui.js"></script>
    <script type="text/javascript" src="../layuiadmin/index.js"></script>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">平台权限</div>
        <div class="layui-card-body" style="padding: 15px;">
            <form class="layui-form" action="" lay-filter="component-form-group">
                <input type="hidden" name="agId" id="agId" value="0">

                <div class="layui-form-item">
                    <label class="layui-form-label">角色名称</label>
                    <div class="layui-input-block">
                        <input class="layui-input" type="text" name="role" placeholder="请输入角色名，格式必须为ROLE_,例如ROLE_ADMIN"/>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">角色别名</label>
                    <div class="layui-input-block">
                        <input class="layui-input" type="text" name="ag_name" placeholder="请输入角色别名"/>
                    </div>
                </div>

                <div class="layui-form-item">
                    <label class="layui-form-label">角色描述</label>
                    <div class="layui-input-block">
                        <input type="text" name="ag_info" lay-verify="agInfo" autocomplete="off" placeholder="请输入角色描述"
                               class="layui-input">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">选择权限</label>
                    <div class="layui-input-block">
                        <div id="LAY-auth-tree-index"></div>
                    </div>
                </div>
                <div class="layui-form-item layui-layout-admin">
                    <div class="layui-input-block">
                        <div class="layui-footer" style="left: 0;">
                            <button class="layui-btn" lay-submit="" lay-filter="LAY-auth-tree-submit">立即提交</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<!-- 状态模板 -->
<style type="text/css">
    .layui-layer-page .layui-layer-content {
        padding: 20px;
        line-height: 22px;
    }
</style>
<!--节点状态渲染模板-->
<!--列表转树渲染模板-->
<!-- 树转单选树的DOM -->
<!--隐藏左侧操作的DOM-->
</html>

