<template>
	<view class="body">
		<ul class="circles">
			<li v-for="i in 10" :key="i"></li>
		</ul>
		<view class="title"><image src="../../static/image/logo.png" mode="" class="logo"></image></view>
		<swiper :current="pageIndex" class="swiper">
			<swiper-item @touchmove.stop>
				<view class="login">
					<view class="login_box">
						<view class="wxBtn" @click="weixinLogin">
							<image src="../../static/image/wechat.png" style="z-index: 1;width: 120upx;height: 120upx;background-color: #EEEEEE;border-radius: 60upx;"></image>
						</view>
						<view class="loginBtn">登 录</view>
					</view>
					<view class="learnMore" @click="jump">了 解 更 多</view>
				</view>
			</swiper-item>
			<swiper-item @touchmove.stop>
				<view class="intro">
					<view class="intro_top"><text>缘 拼 简 介</text></view>
					<text>
						易玩团是一款专注于解决大学生社交难题的小程序，您是否有以下烦恼：想找个志同道合的同学互相督促学习？想出去搓一顿好的却没有人陪？想去KTV放声歌唱朋友却没空？即刻易玩团一下，扩大你的朋友圈！
					</text>
				</view>
				<view class="backBtn" @click="back">去 登 录</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
export default {
	data() {
		return {
			pageIndex: 0,
			status: '',
			phoneNum: '',
			pwdNum: '',
			registPhone: '',
			registPwd: '',
			confirmPwd: ''
		};
	},
	onLoad(e) {
		if(e.info) {
			uni.showToast({
				title:e.info,
				icon: 'none'
			})
		}
	},
	methods: {
		jump() {
			this.pageIndex = 1;
		},
		back() {
			this.pageIndex = 0;
		},
		weixinLogin(e) {
			uni.getUserProfile({
				desc: '快速创建账号',
				success: (res) => {
					console.log(res.userInfo);
					uni.showLoading({
						title: '加载中'
					});
					var userInfo = res.userInfo;
					console.log(userInfo)
					uni.setStorageSync('userInfo', userInfo);
					uni.login({
						success: res => {
							this.request('frontUser/login', 'POST', {
								jsCode: res.code
							})
							.then(res => {
								uni.setStorageSync('openid', res.data.openid);
								var uploadUserInfo = {
									fuImg: userInfo.avatarUrl,
									fuNickname: userInfo.nickName,
									fuOpenid: res.data.openid,
									fuSex: userInfo.gender
								};
								return this.request('frontUser/userUpload', 'POST', uploadUserInfo);
							})
							.then(res => {
								console.log(res)
								uni.setStorageSync('userid', res.data.userid);
								this.initService();
							});
						}
					});
				}
			})
		},
		initService() {
			this.request('yunXin/login', 'POST', {
				fuImg: uni.getStorageSync('userInfo').avatarUrl,
				fuId: uni.getStorageSync('userid'),
				fuOpenid: uni.getStorageSync('openid'),
				fuSex: uni.getStorageSync('userInfo').gender
			}).then(res => {
				console.log(res);
				uni.setStorageSync('yunXinInfo', res.data.info);
				uni.setStorageSync('historyList',[]);
				this.initYunXin();
				uni.hideLoading();
				uni.switchTab({
					url: '../index/index',
					success: res => {
						uni.setStorageSync('isLogin', true);
					}
				});
			});
		}
	}
};
</script>

<style>
body {
	margin: 0;
	padding: 0;
	height: 100vh;
	width: 100%;
	background-color: #ffffff;
	/* background-image: linear-gradient(125deg,#2c3e50,#27ae60,#2980b9,#ffaaff,#8e44ad);
		background-size: 400%;
		animation: bganimation 15s infinite; */
}
/* @keyframes bganimation {
	  0%{
	    background-position: 0% 50%;
	  }
	  50%{
	    background-position: 100% 50%;
	  }
	  100%{
	    background-position: 0% 50%;
	  }
	} */
.wxBtn {
	width: 120upx;
	height: 120upx;
	border-radius: 50%;
	padding: 0;
	animation-name: fade;
	animation-duration: 1s;
	animation-timing-function: linear;
}
.swiper {
	height: 65vh;
	width: 100%;
	text-align: center;
}
.logo {
	width: 300upx;
	height: 300upx;
	margin: 200upx auto 90upx auto;
	line-height: 300upx;
	border-radius: 30upx;
	animation-name: fade;
	animation-duration: 1.5s;
	animation-timing-function: linear;
}
@keyframes fade {
	0% {
		opacity: 0;
	}

	100% {
		opacity: 1;
	}
}
.login_box {
	height: calc(65vh - 200upx);
	padding-top: 50%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	text-align: center;
}
.loginBtn {
	width: 240upx;
	height: 120upx;
	border-radius: 15upx;
	text-align: center;
	line-height: 120upx;
	color: #555;
	font-size: 30upx;
	margin: 0 auto;
	z-index: 1;
	animation-name: fade;
	animation-duration: 1.5s;
	animation-timing-function: linear;
}
.title {
	width: 100%;
	height: 35vh;
	display: flex;
	justify-content: center;
	align-items: center;
}
.swiper {
	width: 100%;
	height: 65vh;
}
.login {
	width: 100%;
	height: 100%;
}
.learnMore {
	height: 200upx;
	line-height: 120upx;
	text-align: center;
	font-size: 28upx;
	color: #555;
	font-weight: 100;
	animation-name: fade;
	animation-duration: 2s;
	animation-timing-function: linear;
}
.backBtn {
	height: 200upx;
	line-height: 120upx;
	text-align: center;
	font-size: 28upx;
	color: #555;
	font-weight: 100;
}
.intro {
	width: 80vw;
	height: calc(65vh - 200upx);
	padding: 30upx;
	border-radius: 30upx;
	color: #555;
	margin: 0 auto;
	line-height: 50upx;
	letter-spacing: 6upx;
}
.intro_top {
	font-size: 36upx;
	height: 80upx;
	width: 100%;
	line-height: 80upx;
	border-bottom: 2upx #eee solid;
	text-align: center;
	margin-bottom: 20upx;
}
.circles {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	padding: 0;
}

.circles li {
	position: absolute;
	display: block;
	list-style: none;
	width: 20upx;
	height: 20upx;
	background: rgba(48, 198, 176, 0.4);
	animation: animate 25s linear infinite;
	bottom: -150upx;
}

.circles li:nth-child(1) {
	left: 25%;
	width: 80upx;
	height: 80upx;
	animation-delay: 0s;
}

.circles li:nth-child(2) {
	left: 10%;
	width: 20upx;
	height: 20upx;
	animation-delay: 2s;
	animation-duration: 12s;
}

.circles li:nth-child(3) {
	left: 70%;
	width: 20upx;
	height: 20upx;
	animation-delay: 4s;
}

.circles li:nth-child(4) {
	left: 40%;
	width: 60upx;
	height: 60upx;
	animation-delay: 0s;
	animation-duration: 18s;
}

.circles li:nth-child(5) {
	left: 65%;
	width: 20upx;
	height: 20upx;
	animation-delay: 0s;
}

.circles li:nth-child(6) {
	left: 75%;
	width: 110upx;
	height: 110upx;
	animation-delay: 3s;
}

.circles li:nth-child(7) {
	left: 35%;
	width: 150upx;
	height: 150upx;
	animation-delay: 7s;
}

.circles li:nth-child(8) {
	left: 50%;
	width: 25upx;
	height: 25upx;
	animation-delay: 15s;
	animation-duration: 45s;
}

.circles li:nth-child(9) {
	left: 20%;
	width: 15upx;
	height: 15upx;
	animation-delay: 2s;
	animation-duration: 35s;
}

.circles li:nth-child(10) {
	left: 85%;
	width: 150upx;
	height: 150upx;
	animation-delay: 0s;
	animation-duration: 11s;
}

@keyframes animate {
	0% {
		transform: translateY(0) rotate(0deg);
		opacity: 1;
		border-radius: 0;
	}

	100% {
		transform: translateY(-1000upx) rotate(720deg);
		opacity: 0;
		border-radius: 50%;
	}
}
</style>
