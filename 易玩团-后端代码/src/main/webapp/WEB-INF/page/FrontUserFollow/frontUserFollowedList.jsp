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
  <title>关注粉丝列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="layuiadmin/layui/css/layui.css" media="all">
  <link rel="stylesheet" href="layuiadmin/style/admin.css" media="all">
	<style>
		.layui-table-cell {
			height: 120px;
		!important;
			line-height: 120px;
		!important;
		}
	</style>
</head>
<body>
<div class="layui-card layadmin-header" style="display:block;">
    <div class="layui-breadcrumb" lay-filter="breadcrumb">
		<a >主页</a>
		<a><cite>关注粉丝列表</cite></a>
		<a><cite>关注粉丝列表</cite></a>
    </div>
</div>
  
<div class="layui-fluid">

		<div class="layui-card">
			<div class="layui-card-body">
				<table class="layui-hide" id="test-table-toolbar" lay-filter="test-table-toolbar">
			
				</table>
			 
				<script type="text/html" id="test-table-toolbar-barDemo">



					<%--<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>--%>

				</script>
				<div id="pagination" class="layui-table-page" style="padding:14px 0px 0px;text-align:right;"></div>
			</div>
		</div>
</div>
<script src="layuiadmin/layui/layui.js"></script>
<script>
layui.config({
    base: 'layuiadmin/' //静态资源所在路径
}).extend({
    index: 'lib/index' //主入口模块
}).use(['index','form','table'], function(){
    var admin = layui.admin
    ,table = layui.table;
	var flag=0;
	table.render({
		 elem: '#test-table-toolbar'
		,url: 'frontUserFollow/listjson?fufFuId=${fufFuId}' //修改这个地址，列表查询路径，数据类型见json/table/demo.js
		,toolbar: '#test-table-toolbar-toolbarDemo'
		,title: '用户数据表'
		,cols: [[
			 {filed:'iId',title:'编号', type:'numbers', width:80, fixed: 'left' }
			,{field:'img', title:'粉丝头像', width:120,
					templet: function (data) {
						return '<div><img alt="" src=' + 'https://zmetalhearty.com' + data.frontUser.fuImg + '></div>'
					}
				}
			,{field:'img', title:'粉丝昵称', width:120,
				templet: function (data) {
					return data.frontUser.fuNickname;
				}
			}
		]]
        ,even: true 
		,done: function(res, curr, count){
			if(flag==0){
				loadPagination(count);
				flag=1;
			}
		}
    });
	//点击头部按钮操作
    table.on('toolbar(test-table-toolbar)', function(obj){
		if(obj.event == 'add'){
			parent.layui.index.openTabsPage("frontUserFollow/addpage", "添加方案");
		}
    });

	table.on('edit(test-table-toolbar)', function(obj){ 
		//注：edit是固定事件名，test是table原始容器的属性 lay-filter="对应的值"
		var data={};
		data['field']=obj.field;//当前编辑的字段名
		data['value']=obj.value;//得到修改后的值
		data['id']=obj.data.id;//所在行的所有相关数据
		admin.req({
			type: "POST"
			,url: 'frontUserFollow/echodata' //实际使用请改成服务端真实接口
			,data: data
			,done: function(res){
				layer.tips('信息修改成功', this, {
					tips: [3, '#2F9688'],
					time: 1000
				});
			}
		});
	});
	//这段不需要修改
	function loadPagination(count){
		var laypage = layui.laypage;
		laypage.render({
			 elem: 'pagination'
			,count: count //数据总数，从服务端得到
			,layout: ['count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
			 // 限定条数   总数、计数  上一页     页     下一页    到第几页、跳
			,curr: 1 
			,groups:5//显示 连续页码
			,first: '首页'
			,last: '尾页' 
			,jump: function(obj, first){
				//obj包含了当前分页的所有参数，比如：
				console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
				console.log(obj.limit); //得到每页显示的条数
				if(!first){
					table.reload('test-table-toolbar', {
						where: { //设定异步数据接口的额外参数，任意设
							page:obj.curr
						}
					});
				}
			}
		});
	}

    //监听搜索
	var form = layui.form;
    form.on('submit(LAY-js-search)', function(data){
		var fileds = data.field;
		fileds['page']=1;
		//执行重载
		flag=0;
		table.reload('test-table-toolbar', {
			where: fileds
		});
    });
    //监听行工具事件
    table.on('tool(test-table-toolbar)', function(obj){

		var frontUserFollow=obj.data;


    }); 
});

</script>
</body>
</html>
