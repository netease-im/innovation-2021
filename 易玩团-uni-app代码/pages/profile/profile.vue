<template>
	<scroll-view class="content" scroll-y="true">
		<view class="backAni">
			<div class="bg"></div>
			<div class="bg bg2"></div>
			<div class="bg bg3"></div>
		</view>
		<view class="card">
			<view class="avatar_container">
				<u-avatar class="avatar" :src="userInfo.avatarUrl" :show-sex="false" :sex-icon="userInfo.gender" mode="circle"></u-avatar>
				<text>{{userInfo.nickName}}</text>
			</view>
			<view class="user_info" @click="goContact(0)">
				<view class="collection">
					<text class="number">{{userStatus.followedCount}}</text>
					<text class="type">关注</text>
				</view>
				<view class="view" @click="goContact(1)">
					<text class="number">{{userStatus.isFollowedCount}}</text>
					<text class="type">粉丝</text>
				</view>
			</view>
			<view class="order">
				<view class="order_title" @click="goPin(0)">
					<text>我的易玩团</text>
					<text class="iconfont icon-xiayiye"></text>
				</view>
				<view class="order_detail">
					<!-- 易玩团分类 -->
					<view class="order_type k-active" @click="goPin(index)" v-for="(order, index) in orderList" :key="index">
						<text class="iconfont type-icon" :class="order.class"></text>
						<text>{{ order.type }}</text>
					</view>
				</view>
				<view class="order_card" v-if="currentAttend.issue.iId">
					<image src="../../static/icon/type-icon/learn_icon.png"v-if="currentAttend.issue.iType == 1" mode=""></image>
					<image src="../../static/icon/type-icon/travel_icon.png" v-if="currentAttend.issue.iType == 2" mode=""></image>
					<image src="../../static/icon/type-icon/food_icon.png" v-if="currentAttend.issue.iType == 3" mode=""></image>
					<image src="../../static/icon/type-icon/fun_icon.png" v-if="currentAttend.issue.iType == 4" mode=""></image>
					<view class="order_info">
						<text class="order_address">{{currentAttend.issue.iTitle}}</text>
						<text class="order_status">已参与</text>
					</view>
				</view>
				<view class="order_card" v-else>
					<image src="../../static/icon/type-icon/fun_icon.png" mode=""></image>
					<view class="order_info">
						<text class="order_address">暂无易玩团</text>
					</view>
				</view>
			</view>
			<view class="opration">
				<!-- 操作列表 -->
				<view class="opration_cell" v-for="(opration,index) in oprationList" :key="index" @click="goOpration(index)">
					<view class="opration_left">
						<p class="iconfont type-icon" :class="opration.class"></p>
						<text>{{opration.text}}</text>
					</view>
					<text class="iconfont icon-xiayiye"></text>
				</view>
				<button class="opration_cell disable-btn" open-type="contact">
					<view class="opration_left">
						<p class="iconfont type-icon icon-kefu"></p>
						<text>联系客服</text>
					</view>
					<text class="iconfont icon-xiayiye"></text>
				</button>
			</view>
			<view class="quit" @click="logOut">
				<text>退出登录</text>
			</view>
		</view>
	</scroll-view>
</template>

<script>
export default {
	data() {
		return {
			userInfo: uni.getStorageSync('userInfo'),
			userStatus: {},
			currentAttend: {},
			contact: {
				img: '/static/icon/refunde.png',
				type: '客服'
			},
			orderList: [
				{
					class: 'icon-fasong',
					type: '已发布'
				},
				{
					class: 'icon-pintuan',
					type: '已参与'
				},
				{
					class: 'icon-shoucang',
					type: '已收藏'
				},
				{
					class: 'icon-jilu',
					type: '已浏览'
				}
			],
			oprationList:[
				{
					class:'icon-wenjian',
					text:'使用教程'
				},
				{
					class:'icon-hezuo',
					text:'关于我们'
				}
			]
		};
	},
	onShow() {
		this.getUserInfo();
		this.getCurrentYuanPin();
	},
	methods: {
		goOpration(index) {
			var url;
			if(index == 0) {
				url = './profile-intro'
			}else {
				url = './profile-aboutUs'
			}
			uni.navigateTo({ url })
		},
		getCurrentYuanPin() {
			this.request('issueAttend/listjson','POST',{
				fromFuId: uni.getStorageSync('userid'),
				page: 1,
				limit: 1,
				fuId: uni.getStorageSync('userid')
			})
			.then(res => {
				console.log('get yuanpin');
				console.log(res)
				if(res.data.data.length != 0) {
					this.currentAttend = res.data.data[0];
				}
			})
		},
		getUserInfo() {
			this.request('frontUser/getUserInfo','POST',{
				fuId: uni.getStorageSync('userid'),
				nFuId: uni.getStorageSync('userid')
			})
			.then(res => {
				console.log(res);
				this.userStatus = res.data;
			})
		},
		goContact(type) {
			uni.navigateTo({
				url: 'profile-contact?type='+type
			})
		},
		navigate(route) {
			uni.navigateTo({
				url: route
			})
		},
		goPin(index) {
			uni.navigateTo({
				url: 'profile-myPin?type='+index
			})
		},
		logOut() {
			uni.clearStorageSync('userInfo');
			uni.clearStorageSync('yunXinInfo');
			uni.clearStorageSync('openid');
			uni.clearStorageSync('userid');
			uni.navigateTo({
				url: '../login/login'
			})
		}
	}
};
</script>

<style lang="scss">
	.icon-xiayiye {
		color: $dark-grey;
	}
.content {
	width: 100%;
	height: 100vh;
	background-color: #f8f8f8;
}
.backAni {
	width: 100%;
	height: 70vh;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 0;
}
.bg {
	animation: slide 3s ease-in-out infinite alternate;
	background-image: linear-gradient(-60deg, $primary 50%, $grey 50%);
	bottom: 0;
	left: -50%;
	opacity: 0.5;
	position: absolute;
	right: -50%;
	top: 0;
	z-index: -1;
}
.bg2 {
	animation-direction: alternate-reverse;
	animation-duration: 4s;
}
.bg3 {
	animation-duration: 5s;
}
@keyframes slide {
	0% {
		transform: translateX(-25%);
	}
	100% {
		transform: translateX(25%);
	}
}
.card {
	width: 100%;
	border-radius: 20upx 20upx 0 0;
	position: absolute;
	left: 0;
	top: 40vh;
	background-color: #f8f8f8;
	padding: 100upx 50upx 50upx 50upx;
	.avatar_container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		line-height: 60upx;
		color: #fff;
		font-size: 32upx;
		letter-spacing: 4upx;
		position: absolute;
		top: -20vh;
		left: 50%;
		transform: translate(-50%,-50%);
		::v-deep .u-avatar {
			width: 160upx !important;
			height: 160upx !important;
			border: 4upx solid #eee;
		}
	}
	.user_info {
		width: 80%;
		height: 120upx;
		border-radius: 30upx;
		box-shadow: 0 20upx 20upx -14upx #ddd;
		padding-right: 20upx;
		background-color: #fff;
		position: absolute;
		left: 10%;
		top: -60upx;
		display: grid;
		padding: 10upx 0;
		grid-template-columns: 1fr 1fr;
		.collection,
		.view {
			height: 100upx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			line-height: 50upx;
			.type {
				letter-spacing: 2upx;
				color: #888;
				font-size: 26upx;
			}
			.number {
				letter-spacing: 2upx;
				color: #555;
				font-weight: bold;
				font-size: 32upx;
			}
		}
		.collection {
			border-right: 2upx solid #eee;
		}
	}
	.order,
	.deliver {
		width: 100%;
		border-radius: 30upx;
		background-color: #ffffff;
		box-shadow: 0 20upx 20upx -14upx #ddd;
		padding: 30upx 20upx;
		&_title {
			height: 60upx;
			font-weight: bold;
			letter-spacing: 2upx;
			color: #555;
			display: flex;
			justify-content: space-between;
			align-items: flex-start;
			image {
				width: 40upx;
				height: 40upx;
			}
		}
		&_detail {
			display: grid;
			grid-template-columns: repeat(4, 1fr);
		}
		&_type {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			margin-top: 20upx;
			image {
				width: 60upx;
				height: 60upx;
			}
			image:nth-child(1) {
				width: 64upx;
				height: 64upx;
			}
			text {
				line-height: 40upx;
				font-size: 24upx;
				color: $dark;
			}
		}
		.type-icon {
			font-size: 50upx;
			color: $primary;
			line-height: 70upx;
		}
		&_card {
			width: 100%;
			height: 120upx;
			padding: 0 20upx;
			margin-top: 20upx;
			background-color: #f8f8f8;
			border-radius: 30upx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			image {
				width: 80upx;
				height: 80upx;
				border-radius: 20upx;
			}
		}
		&_info {
			width: calc(100% - 100upx);
			height: 100%;
			display: flex;
			flex-direction: column;
			justify-content: center;
		}
		&_status {
			color: $primary;
			font-size: 24upx;
		}
		&_address {
			color: #888;
			display: -webkit-box;
			-webkit-line-clamp: 1;
			overflow: hidden;
			text-overflow: ellipsis;
			-webkit-box-orient: vertical;
			word-break: break-all;
		}
	}
	.opration {
		width: 100%;
		border-radius: 30upx;
		padding: 0 20upx;
		margin-top: 30upx;
		background-color: #fff;
		box-shadow: 0 20upx 20upx -14upx #ddd;
		font-size: 28upx;
		&_cell {
			width: 100%;
			height: 90upx;
			display: flex;
			justify-content: space-between;
			align-items: center;
		}
		.disable-btn {
			width: 100%;
			background-color: #fff;
			padding: 0;
			font-size: 28upx;
			&::after {
				border: none;
			}
		}
		.type-icon {
			font-size: 36upx;
			color: $primary;
			margin-right: 20upx;
			width: 50upx;
			text-align: center;
		}
		&_left {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			image {
				width: 40upx;
				height: 40upx;
				margin-right: 20upx;
			}
			text {
				color: #555;
				letter-spacing: 2upx;
			}
		}
		.arrow {
			width: 40upx;
			height: 40upx;
		}
	}
	.quit {
		width: 100%;
		height: 90upx;
		border-radius: 30upx;
		background-color: #fff;
		box-shadow: 0 20upx 20upx -14upx #ddd;
		color: #e74c3c;
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 30upx;
		letter-spacing: 2upx;
		image {
			width: 36upx;
			height: 36upx;
			margin-right: 16upx;
		}
	}
}
</style>
