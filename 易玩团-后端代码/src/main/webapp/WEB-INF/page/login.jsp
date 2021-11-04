<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录 - 缘拼管理系统</title>
    <!-- 网页 -->
    <link rel="stylesheet" type="text/css" href="layuiadmin/login.css">
    <style>
        .logo {
            position: absolute;
            top: 20px;
            left: 20px;
        }

        .large-header {
            position: relative;
            width: 100%;
            background: #663333;
            overflow: hidden;
            background-size: cover;
            background-position: center center;
            z-index: 1;
        }

        .large-header {
            /*background-image: url("layuiadmin/img/login-bg.jpg");*/
        }

        .logo_box {
            width: 400px;
            height: 500px;
            padding: 35px;
            color: #EEE;
            position: absolute;
            left: 50%;
            top: 50%;
            margin-left: -200px;
            margin-top: -270px;
        }

        .logo_box h3 {
            height: 20px;
            font: 20px "microsoft yahei", Helvetica, Tahoma, Arial, "Microsoft jhengHei", sans-serif;
            color: #FFFFFF;
            height: 20px;
            line-height: 20px;
            padding: 0 0 35px 0;
        }

        .input_outer {
            height: 46px;
            padding: 0 5px;
            margin-bottom: 30px;
            border-radius: 50px;
            position: relative;
            border: rgba(255, 255, 255, 0.2) 2px solid !important;
        }

        .u_user {
            width: 25px;
            height: 25px;
            background: url("layuiadmin/img/login_ico.png");
            background-position: -125px 0;
            position: absolute;
            margin: 10px 13px;
        }
        .check_num {
            width: 100px;
            height: 46px;
            margin-left: 210px;
        }
        .us_uer {
            width: 25px;
            height: 25px;
            background-image: url("layuiadmin/img/login_ico.png");
            background-position: -125px -34px;
            position: absolute;
            margin: 10px 13px;
        }
        .us_check {
            width: 25px;
            height: 25px;
            background-image: url("layuiadmin/img/login_ico.png");
            background-position: -125px -212px;
            position: absolute;
            margin: 10px 13px;
        }
        .text {
            width: 220px;
            height: 46px;
            outline: none;
            display: inline-block;
            font: 14px "microsoft yahei", Helvetica, Tahoma, Arial, "Microsoft jhengHei";
            margin-left: 50px;
            border: none;
            background: none;
            line-height: 46px;
        }
        .text1 {
            width: 150px;
            height: 46px;
            outline: none;
            display: inline-block;
            font: 14px "microsoft yahei", Helvetica, Tahoma, Arial, "Microsoft jhengHei";
            margin-left: 50px;
            border: none;
            background: none;
            line-height: 46px;
        }

        #imgverify {
            float: right;
            margin-top: -39px;
            border-radius: 20px;
            margin-right: -4px;
        }

        input[type='submit'] {
            width: 100%;
            background-color: #0096e6;
            border-radius: 50px;
            padding: 15px;
            border: none;
            font-size: 20px;
            margin-bottom: 20px;
            margin-top: 20px;
        }

        /* 去input自动填充背景 */
        input:-webkit-autofill,
        input:-webkit-autofill:hover,
        input:-webkit-autofill:focus,
        input:-webkit-autofill:active {
            -webkit-transition-delay: 99999s;
            -webkit-transition: color 99999s ease-out, background-color 99999s ease-out;
        }

        .color-white {
            color: #FFFFFF;
        }

        .align-center {
            text-align: center;
        }

        .social-icons ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }
    </style>
</head>

<body>
<div id="large-header" class="large-header" style="height: 760px;">
<%--    <canvas id="demo-canvas" width="1536" height="760"></canvas>--%>
    <div class="logo_box">
        <h3 class="align-center">缘拼管理系统</h3>
        <form action="/login" method="post" id="loginform">
            <div class="input_outer">
                <span class="u_user"></span>
                <input type="text" name="username" class="text" style="color: #FFFFFF !important;background:none"
                       placeholder="请输入用户名登录">
            </div>
            <div class="input_outer">
                <span class="us_uer"></span>
                <input type="password" name="password" class="text"
                       style="color: #FFFFFF !important; position:absolute; z-index:100;" placeholder="请输入登录密码">
            </div>

           <%-- <input type="checkbox" name="rember" id="rember" value="true">
            <label for="rember"><span></span>记住我</label>--%>
            <input type="submit" name="submit" id="login_btn" value="登 录" class="color-white">

            <input name="token" value="747bc044" type="hidden">
        </form>



<%--        <div class="social-icons align-center">--%>
<%--            <p> -使用第三方账号登录- </p>--%>
<%--            <ul>--%>
<%--                <a href=""><img src="layuiadmin/img/wxlogin.png" width="35px"></a> </ul>--%>
<%--        </div>--%>
    </div>
</div>
<script src="layuiadmin/TweenLite.min.js"></script>
<script src="layuiadmin/EasePack.min.js"></script>
<script src="layuiadmin/layui/layui.js" type="text/javascript"></script>
<script src="layuiadmin/jquery.min.js" type="text/javascript"></script>
<script>
    layui.config({
        base: 'layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'user'], function(){
        var $ = layui.$
            ,setter = layui.setter
            ,admin = layui.admin
            ,form = layui.form
            ,router = layui.router()
            ,search = router.search;

        form.render();
        //提交
    });


</script>
<script>
    (function () {
        var width, height, largeHeader, canvas, ctx, points, target, animateHeader = true;
        // Main
        initHeader();
        initAnimation();
        addListeners();

        function initHeader() {
            width = window.innerWidth;
            height = window.innerHeight;
            target = { x: width / 2, y: height / 2 };

            largeHeader = document.getElementById('large-header');
            largeHeader.style.height = height + 'px';

            canvas = document.getElementById('demo-canvas');
            canvas.width = width;
            canvas.height = height;
            ctx = canvas.getContext('2d');

            // create points
            points = [];
            for (var x = 0; x < width; x = x + width / 20) {
                for (var y = 0; y < height; y = y + height / 20) {
                    var px = x + Math.random() * width / 20;
                    var py = y + Math.random() * height / 20;
                    var p = { x: px, originX: px, y: py, originY: py };
                    points.push(p);
                }
            }

            // for each point find the 5 closest points
            for (var i = 0; i < points.length; i++) {
                var closest = [];
                var p1 = points[i];
                for (var j = 0; j < points.length; j++) {
                    var p2 = points[j]
                    if (!(p1 == p2)) {
                        var placed = false;
                        for (var k = 0; k < 5; k++) {
                            if (!placed) {
                                if (closest[k] == undefined) {
                                    closest[k] = p2;
                                    placed = true;
                                }
                            }
                        }

                        for (var k = 0; k < 5; k++) {
                            if (!placed) {
                                if (getDistance(p1, p2) < getDistance(p1, closest[k])) {
                                    closest[k] = p2;
                                    placed = true;
                                }
                            }
                        }
                    }
                }
                p1.closest = closest;
            }

            // assign a circle to each point
            for (var i in points) {
                var c = new Circle(points[i], 2 + Math.random() * 2, 'rgba(255,255,255,0.3)');
                points[i].circle = c;
            }
        }

        // Event handling
        function addListeners() {
            if (!('ontouchstart' in window)) {
                window.addEventListener('mousemove', mouseMove);
            }
            window.addEventListener('scroll', scrollCheck);
            window.addEventListener('resize', resize);
        }

        function mouseMove(e) {
            var posx = posy = 0;
            if (e.pageX || e.pageY) {
                posx = e.pageX;
                posy = e.pageY;
            }
            else if (e.clientX || e.clientY) {
                posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
                posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
            }
            target.x = posx;
            target.y = posy;
        }

        function scrollCheck() {
            if (document.body.scrollTop > height) animateHeader = false;
            else animateHeader = true;
        }

        function resize() {
            width = window.innerWidth;
            height = window.innerHeight;
            largeHeader.style.height = height + 'px';
            canvas.width = width;
            canvas.height = height;
        }

        // animation
        function initAnimation() {
            animate();
            for (var i in points) {
                shiftPoint(points[i]);
            }
        }

        function animate() {
            if (animateHeader) {
                ctx.clearRect(0, 0, width, height);
                for (var i in points) {
                    // detect points in range
                    if (Math.abs(getDistance(target, points[i])) < 4000) {
                        points[i].active = 0.3;
                        points[i].circle.active = 0.6;
                    } else if (Math.abs(getDistance(target, points[i])) < 20000) {
                        points[i].active = 0.1;
                        points[i].circle.active = 0.3;
                    } else if (Math.abs(getDistance(target, points[i])) < 40000) {
                        points[i].active = 0.02;
                        points[i].circle.active = 0.1;
                    } else {
                        points[i].active = 0;
                        points[i].circle.active = 0;
                    }

                    drawLines(points[i]);
                    points[i].circle.draw();
                }
            }
            requestAnimationFrame(animate);
        }

        function shiftPoint(p) {
            TweenLite.to(p, 1 + 1 * Math.random(), {
                x: p.originX - 50 + Math.random() * 100,
                y: p.originY - 50 + Math.random() * 100, ease: Circ.easeInOut,
                onComplete: function () {
                    shiftPoint(p);
                }
            });
        }

        // Canvas manipulation
        function drawLines(p) {
            if (!p.active) return;
            for (var i in p.closest) {
                ctx.beginPath();
                ctx.moveTo(p.x, p.y);
                ctx.lineTo(p.closest[i].x, p.closest[i].y);
                ctx.strokeStyle = 'rgba(156,217,249,' + p.active + ')';
                ctx.stroke();
            }
        }

        function Circle(pos, rad, color) {
            var _this = this;

            // constructor
            (function () {
                _this.pos = pos || null;
                _this.radius = rad || null;
                _this.color = color || null;
            })();

            this.draw = function () {
                if (!_this.active) return;
                ctx.beginPath();
                ctx.arc(_this.pos.x, _this.pos.y, _this.radius, 0, 2 * Math.PI, false);
                ctx.fillStyle = 'rgba(156,217,249,' + _this.active + ')';
                ctx.fill();
            };
        }

        // Util
        function getDistance(p1, p2) {
            return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
        }

    })();
</script>
<script type="text/javascript">
    $(document).on("click", "#login_btn", function () {
        $.ajax({
            url: "login_p",
            type: 'POST',
            data: $("#loginform").serialize(),
            success: function (data) {
                if (data.status === 'ok') {
                    //登录成功
                    window.location.href="index"
                } else if (data.status === 'error') {
                    layer.confirm("账号密码错误!", {
                        icon: 2,
                        skin: 'layui-layer-lan',
                        btn: ['确定'] //按钮
                    }, function(){
                        window.location.reload();
                    });
                } else {
                    alert(1);
                    window.location.href="index"
                    /*
                    layer.confirm("验证码错误!", {
                        icon: 2,
                        skin: 'layui-layer-lan',
                        btn: ['确定'] //按钮
                    }, function(){
                        window.location.reload();
                    });*/
                }
            },
            error: function () {
                layer.msg("出现错误，请尝试刷新页面!", {icon: 2});
            }
        });
        return false;
    });
</script>
</body>
</html>