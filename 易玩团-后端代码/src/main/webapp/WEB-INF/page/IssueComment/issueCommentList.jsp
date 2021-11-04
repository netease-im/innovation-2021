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
  <title>费用设置</title>
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
		<a><cite>帖子管理</cite></a>
		<a><cite>帖子管理</cite></a>
    </div>
</div>
  
<div class="layui-fluid">

		<div class="layui-card">
			<div class="layui-card-body">
				<table class="layui-hide" id="test-table-toolbar" lay-filter="test-table-toolbar">
			
				</table>
			 
				<script type="text/html" id="test-table-toolbar-barDemo">


					<a class="layui-btn layui-btn-xs" lay-event="detail">详情</a>
					{{# if(d.iStatus !== 1){ }}
					<a class="layui-btn layui-btn-xs" lay-event="adopt">通过</a>
					{{# } }}
					{{# if(d.iStatus !== 3){ }}
					<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="refuse">不通过</a>
					{{# } }}


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
		,url: 'issue/listjson' //修改这个地址，列表查询路径，数据类型见json/table/demo.js
		,toolbar: '#test-table-toolbar-toolbarDemo'
		,title: '用户数据表'
		,cols: [[
			 {filed:'iId',title:'编号', type:'numbers', width:80, fixed: 'left' }
			,{field:'iTitle', title:'标题', width:300}
			,{field:'img', title:'用户头像', width:120,
					templet: function (data) {
						return '<div><img alt="" src=' + '' + data.user.img + '></div>'
					}
				}
			,{field:'img', title:'用户头像', width:120,
				templet: function (data) {
					return '<div><img alt="" src=' + '' + data.user.img + '></div>'
				}
			}
			,{field:'iCreateTime', title:'创建时间', width:300}
			, {
				field: 'iStatus', title: '状态', width: 200, templet: function (data) {
					if (data.iStatus === 1) {
						return "<span style='color:green'>正常</span>";
					} else if (data.iStatus === 2) {
						return "<span style='color:gray'>待审核</span>";
					} else if (data.iStatus === 3) {
						return "<span style='color:red'>未通过审核</span>";
					}
				}
			}
			,{fixed: 'right', title:'操作', toolbar: '#test-table-toolbar-barDemo'}
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
			parent.layui.index.openTabsPage("issue/addpage", "添加方案");
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
			,url: 'issue/echodata' //实际使用请改成服务端真实接口
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

		var issue=obj.data;
		if(obj.event === 'adopt'){
			var data = {};
			data['iId'] = issue.iId
			layer.confirm('确认通过吗', function(index){
				admin.req({
					type: "POST"
					,url: 'issue/adopt' //实际使用请改成服务端真实接口
					,data: data
					,done: function(res){
						if(res){
							layer.alert("通过成功，请刷新列表")
							layer.close(index);
							table.reload('test-table-toolbar', {});
						}
					}
				})
			});
		}else if(obj.event === 'refuse'){
			layer.confirm('确认不通过吗', function(index){
				var data = {};
				data['iId'] = issue.iId
				admin.req({
					type: "POST"
					,url: 'issue/refuse' //实际使用请改成服务端真实接口
					,data: data
					,done: function(res){
						if(res){
							layer.alert("不通过成功，请刷新列表")
							layer.close(index);
							table.reload('test-table-toolbar', {});
						}
					}
				})
			});
		}else if(obj.event === 'detail'){
			parent.layui.index.openTabsPage("issue/details?iId="+obj.data.iId, "查看");
		}


    }); 
});

</script>
</body>
</html>
