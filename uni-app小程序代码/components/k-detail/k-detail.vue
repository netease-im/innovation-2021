<template>
	<view class="detail" :class="'theme-' + pinData.iType">
		<scroll-view class="scrollView" :scroll-y="true">
			<view class="detail-card">
				<swiper class="swiper" :indicator-dots="false" :autoplay="true" :interval="3000" :duration="1000">
					<swiper-item v-for="url in pinData.iImg" :key="url"><image class="swiper-img" :src="baseUrl + url" mode="aspectFill"></image></swiper-item>
				</swiper>
				<!-- 标题 -->
				<view class="detail-nav">
					<!-- <view class="type-icon">
						<image v-if="pinData.iType == 1" src="../../static/icon/type-icon/learn_icon.png" mode=""></image>
						<image v-if="pinData.iType == 2" src="../../static/icon/type-icon/travel_icon.png" mode=""></image>
						<image v-if="pinData.iType == 3" src="../../static/icon/type-icon/food_icon.png" mode=""></image>
						<image v-if="pinData.iType == 4" src="../../static/icon/type-icon/fun_icon.png" mode=""></image>
					</view> -->
					<!-- <view class="detail-title k-hideText">
						<text>{{ pinData.iTitle }}</text>
					</view> -->
					<!-- <view class="collect"><text class="iconfont icon-Bookmark1 collect-icon"></text></view> -->
				</view>
				<!-- 发布用户 -->
				<view class="user-info">
					<view class="user-left">
						<u-avatar v-if="pinData.frontUser.fuImg" :src="baseUrl + pinData.frontUser.fuImg" mode="circle" size="70" class="user-avatar"></u-avatar>
						<view class="user-mid">
							<text class="user-name">{{ pinData.frontUser.fuNickname }}</text>
							<text class="send-time">{{ $timeFormat(pinData.iCreateTime) | timeFrom }}发布</text>
						</view>
					</view>
					<view class="focus-btn" :class="pinData.follow?'':'focus-active'" @click="handleFocus">
						<text v-show="pinData.follow">取消关注</text>
						<text v-show="!pinData.follow" class="focus-text">关注</text>
					</view>
				</view>
				<!-- 缘拼时间 -->
				<view class="detail-cell">
					<view class="cell-left">
						<view class="cell-icon">
							<text class="iconfont icon-jilu left-icon"></text>
						</view>
						<view class="cell-content k-hideText" v-if="pinData.iStartTime">
							<view>{{pinData.iStartTime.date}}</view>
							<view class="main-content">{{timeFormat(pinData.iStartTime.time)}} - {{timeFormat(pinData.iEndTime.time)}}</view>
						</view>
					</view>
					<text class="iconfont icon-xiayiye arrow-icon"></text>
				</view>
				<!-- 地址/始发地 -->
				<view class="detail-cell" @click="routePlan">
					<view class="cell-left">
						<view class="cell-icon">
							<text class="iconfont icon-Location1 left-icon"></text>
						</view>
						<view class="cell-content k-hideText">
							<view>{{pinData.iPositionStart.name.address}}</view>
							<view class="main-content">{{pinData.iPositionStart.name.name}}</view>
						</view>
					</view>
					<text class="iconfont icon-xiayiye arrow-icon"></text>
				</view>
				<!-- 目的地 -->
				<view class="detail-cell" v-if="pinData.iPositionEnd" @click="routePlan">
					<view class="cell-left">
						<view class="cell-icon">
							<text class="iconfont icon-Location1 left-icon"></text>
						</view>
						<view class="cell-content k-hideText">
							<view>{{pinData.iPositionEnd.name.address}}</view>
							<view class="main-content">{{pinData.iPositionEnd.name.name}}</view>
						</view>
					</view>
					<text class="iconfont icon-xiayiye arrow-icon"></text>
				</view>
				<!-- 主要内容 -->
				<view class="detail-cell">
					<view class="cell-left">
						<view class="cell-icon">
							<text class="iconfont icon-xuexi left-icon" v-if="pinData.iType == 1"></text>
							<text class="iconfont icon-auto left-icon" v-if="pinData.iType == 2"></text>
							<text class="iconfont icon-xiaoshi left-icon" v-if="pinData.iType == 3"></text>
							<text class="iconfont icon-Game left-icon" v-if="pinData.iType == 4"></text>
						</view>
						<view class="cell-content k-hideText">
							<view v-if="pinData.iType == 1">学习内容</view>
							<view v-if="pinData.iType == 2">出行方式</view>
							<view v-if="pinData.iType == 3">美食类型</view>
							<view v-if="pinData.iType == 4">娱乐方式</view>
							<view class="main-content">{{pinData.iResume}}</view>
						</view>
					</view>
					<text class="iconfont icon-xiayiye arrow-icon"></text>
				</view>
				<!-- 缘拼状态 -->
				<p class="small-title">参与者</p>
				<view class="detail-user" v-if="pinData.issueAttend">
					<image :src="baseUrl + pinData.frontUser.fuImg" mode="" class="user-avatar"></image>
					<image :src="baseUrl+member.frontUser.fuImg" :style="'transform: translateX('+((index+1)*(-20))+'rpx);'" class="user-avatar" v-for="(member,index) in pinData.issueAttend.slice(0,5)" :key="index"></image>
					<view class="user-more" v-if="pinData.issueAttend.length > 5">
						<text>+{{pinData.issueAttend.length - 5}}</text>
					</view>
					<text class="view-all">
						<text class="view-all-text">{{pinData.issueAttend.length + 1}}参与者</text>
						<text class="iconfont icon-xiayiye arrow-icon"></text>
					</text>
				</view>
				<!-- 缘拼描述 -->
				<p class="small-title">缘拼描述</p>
				<view class="detail-content">
					{{pinData.iDetails}}
				</view>
				<!-- 地图 -->
				<p class="small-title">缘拼地点</p>
				<view class="map-container" @click.native="routePlan">
					<map
						class="map"
						:enable-zoom="false"
						@markertap="routePlan"
						:enable-scroll="false"
						:markers="markerList"
						:latitude="pinData.iPositionStart.location.latitude"
						:longitude="pinData.iPositionStart.location.longitude"
					></map>
				</view>
			</view>
			<u-toast ref="uToast" />
		</scroll-view>
	</view>
</template>

<script>
export default {
	props: {
		issueId: null
	},
	data() {
		return {
			theme: '',
			baseUrl: 'https://zmetalhearty.com',
			pinData: {},
			isPin: false,
			markerList: []
		};
	},
	computed: {
		timeFormat() {
			return function(e) {
				let list = e.split(':');
				return list[0]+'点'+list[1]+'分'
			}
		}
	},
	created() {
		this.getDetail(this.issueId);
	},
	methods: {
		goChat() {
			if(this.pinData.frontUser.fuId != uni.getStorageSync('userid')) {
				uni.navigateTo({
					url: '../chat/chat-single?id='+this.pinData.frontUser.fuId
				})
			}else {
				this.showToast('不可以和自己聊天哦','warning')
			}
		},
		goPin() {
			if(this.pinData.frontUser.fuId != uni.getStorageSync('userid')) {
				if(this.isPin) {
					this.showToast('您已缘拼成功','warning')
				}else {
					this.request('issueAttend/add','POST',{
						fuId: uni.getStorageSync('userid'),
						iId: this.pinData.id
					})
					.then(res => {
						console.log(res);
						if(res.data.status == 'succ') {
							this.showToast('缘拼成功！','success')
						}
					})
				}
			}else {
				this.showToast('不可以和自己拼团哦','warning')
			}
			
		},
		showToast(title, type) {
			this.$refs.uToast.show({ title,type });
		},
		routePlan() {
			let plugin = requirePlugin('routePlan');
			let key = '5SKBZ-TBSW3-QTW33-YJLYA-LKI2Z-IPFMF'; //使用在腾讯位置服务申请的key
			let referer = '缘拼'; //调用插件的app的名称
			let navigation = 1;
			if (this.pinData.iType == 2) {
				let startPoint = JSON.stringify({
					//起点
					name: this.pinData.iPositionStart.name.name,
					latitude: this.pinData.iPositionStart.location.latitude,
					longitude: this.pinData.iPositionStart.location.longitude
				});
				let endPoint = JSON.stringify({
					//终点
					name: this.pinData.iPositionEnd.name.name,
					latitude: this.pinData.iPositionEnd.location.latitude,
					longitude: this.pinData.iPositionEnd.location.longitude
				});
				uni.navigateTo({
					url: 'plugin://routePlan/index?key=' + key + '&referer=' + referer + '&endPoint=' + endPoint + '&startPoint=' + startPoint + '&navigation=' + navigation
				});
			} else {
				let endPoint = JSON.stringify({
					//起点
					name: this.pinData.iPositionStart.name.name,
					latitude: this.pinData.iPositionStart.location.latitude,
					longitude: this.pinData.iPositionStart.location.longitude
				});
				uni.navigateTo({
					url: 'plugin://routePlan/index?key=' + key + '&referer=' + referer + '&endPoint=' + endPoint + '&navigation=' + navigation
				});
			}
		},
		handleFocus() {
			if(this.pinData.frontUser.fuId != uni.getStorageSync('userid')) {
				var url = this.pinData.follow ? 'delete' : 'add';
				this.request('frontUserFollow/'+url,'POST', {
					fuId: uni.getStorageSync('userid'),
					fufFuId: this.pinData.frontUser.fuId
				})
				.then(res => {
					console.log(res);
					this.pinData.follow = !this.pinData.follow;
				})
			}else {
				this.showToast('不可以关注自己哦','warning')
			}
		},
		issueFormat(issue) {
			if(issue.iImg) {
				issue.iImg = issue.iImg.split(',');
				issue.iImg.splice(1,1);
			}
			if(issue.iStartTime) {
				issue.iStartTime = {
					date: issue.iStartTime.trim().split(/\s+/)[0],
					time: issue.iStartTime.trim().split(/\s+/)[1]
				}
			}
			if(issue.iEndTime) {
				issue.iEndTime = {
					date: issue.iEndTime.trim().split(/\s+/)[0],
					time: issue.iEndTime.trim().split(/\s+/)[1]
				}
			}
			if(issue.iAddress) {
				issue.iAddress = JSON.parse(issue.iAddress);
			}
			if (issue.iPositionStart) {
				issue.iPositionStart = JSON.parse(issue.iPositionStart);
				this.markerList.push({
					id: 1,
					iconPath: '/static/icon/discovery-icon/self-location.png',
					width: 45,
					height: 45,
					latitude: issue.iPositionStart.location.latitude,
					longitude: issue.iPositionStart.location.longitude
				});
			}
			if (issue.iPositionEnd) {
				issue.iPositionEnd = JSON.parse(issue.iPositionEnd);
				this.markerList.push({
					id: 1,
					iconPath: '/static/icon/discovery-icon/self-location.png',
					width: 45,
					height: 45,
					latitude: issue.iPositionEnd.location.latitude,
					longitude: issue.iPositionEnd.location.longitude
				});
			}
			if(issue.issueAttend.length > 0) {
				issue.issueAttend.forEach(item => {
					if(item.fuId == uni.getStorageSync('userid')) {
						this.isPin = true;
					}
				})
			}
			return issue;
		},
		getDetail(id) {
			this.request('issue/getDetails','POST',{
				iId: id,
				fromFuId: uni.getStorageSync('userid')
			})
			.then(res => {
				this.pinData = this.issueFormat(res.data.data);
				console.log(this.pinData)
			})
		},
		goBack() {
			uni.navigateBack({
				delta: 1
			})
		}
	}
};
</script>

<style lang="scss">
.detail {
	.scrollView {
		height: calc(100vh - 398upx);
	}
	.swiper {
		width: 100%;
		height: 300upx;
		border-radius: 30upx;
		transform: translateY(0);
		overflow: hidden;
		&-img {
			width: 100%;
			height: 100%;
			background-color: #eee;
		}
	}
	.back-btn {
		width: 100upx;
		height: 100upx;
		position: fixed;
		box-shadow: 0 16upx 24upx -8upx #aaa;
		top: 80upx;
		left: 50upx;
		border-radius: 30upx;
		background-color: #fff;
		color: $minus-text;
		text-align: center;
		line-height: 100upx;
		z-index: 9;
	}
	&-card {
		width: 100%;
		box-sizing: border-box;
		padding: 50upx;
		background-color: #fff;
		border-radius: 30upx 30upx 0 0;
	}
	&-nav {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 30upx;
		.type-icon {
			width: 100upx;
			height: 100upx;
			border-radius: 30upx;
			display: flex;
			justify-content: center;
			align-items: center;
			background-color: #eee;
			& > image {
				width: 60upx;
				height: 60upx;
			}
		}
		.detail-title {
			width: calc(100% - 120upx);
			font-size: 40upx;
			font-weight: bold;
			color: $main-text;
			text-align: left;
		}
		.collect {
			width: 100upx;
			height: 100upx;
			display: flex;
			border-radius: 30upx;
			justify-content: center;
			align-items: center;
			.collect-icon {
				color: $light;
				font-size: 50upx;
			}
		}
	}
	.user-info {
		width: 100%;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-bottom: 50upx;
		font-size: 26upx;
		font-weight: normal;
		.user-avatar {
			margin-right: 10upx;
		}
		.user-left {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}
		.user-mid {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: flex-start;
			.send-time {
				font-size: 24upx;
				color: $light;
			}
		}
		.focus-btn {
			width: 120upx;
			height: 60upx;
			border-radius: 20upx;
			text-align: center;
			line-height: 60upx;
			color: $dark;
		}
	}
	&-user {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		padding: 0 0 20upx 0;
		position: relative;
		.user-avatar,.empty-avatar {
			width: 80upx;
			height: 80upx;
			box-sizing: border-box;
			border-radius: 50%;
			border: 4upx solid #fff;
		}
		.user-more {
			width: 80upx;
			height: 80upx;
			border-radius: 50%;
			text-align: center;
			line-height: 72upx;
			font-size: 24upx;
			background-color: #aaa;
			color: #fff;
			border: 4upx solid #fff;
			transform: translateX(-100upx);
		}
		.view-all {
			position: absolute;
			right: 40upx;
			.user-count {
				font-weight: bold;
				padding: 0 4upx;
			}
			.arrow-icon {
				margin-left: 20upx;
			}
		}
	}
	&-cell {
		width: 100%;
		padding-bottom: 20upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-right: 40upx;
		.cell-icon {
			width: 100upx;
			height: 100upx;
			background-color: $light-grey;
			color: $minus-text;
			text-align: center;
			line-height: 100upx;
			border-radius: 30upx;
			margin-right: 20upx;
			.left-icon {
				font-size: 50upx;
			}
		}
		.cell-left {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}
		.cell-content {
			width: 450upx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: flex-start;
			font-size: 26upx;
			color: $light;
			line-height: 46upx;
			overflow: hidden;
			text-overflow:ellipsis;
			white-space: nowrap;
			view {
				width: 100%;
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
			}
			.main-content {
				color: $dark;
			}
		}
	}
	.small-title {
		padding: 30upx 0;
		font-size: 34upx;
		font-weight: bold;
		color: $main-text;
	}
	.detail-content {
		line-height: 50upx;
		color: $dark;
		letter-spacing: 2upx;
		padding-bottom: 20upx;
	}
	.map,.map-container {
		width: 100%;
		height: 300upx;
		border-radius: 30upx;
		overflow: hidden;
		transform: translateY(0);
	}
	.arrow-icon {
		color: $light;
	}
}
.bottom-bar {
	height: 160upx;
	width: 100%;
	position: fixed;
	bottom: 0;
	left: 0;
	backdrop-filter: blur(10px);
	box-sizing: border-box;
	padding: 30upx;
	display: flex;
	justify-content: flex-end;
	align-items: center;
	background-color: rgba($color: #fff, $alpha: .7);
	.pin-btn {
		width: 200upx;
		height: 100upx;
		border-radius: 30upx;
		background-color: $dark;
		color: $white;
		line-height: 100upx;
		text-align: center;
		.icon-pintuan {
			margin-right: 6upx;
		}
	}
}
.theme-1 {
	.collect-icon-active {
		color: $learn;
	}
	.focus-active {
		color: $learn !important;
	}
	.cell-icon {
		background-color: $learn-light;
		box-shadow: 0 14upx 20upx -6upx $learn-light;
		.left-icon {
			color: $learn;
		}
	}
	.user-more {
		background-color: $learn;
		color: #fff;
	}
	.view-all-text {
		color: $learn;
	}
	.pin-btn {
		background-color: $learn;
		box-shadow: 0 14upx 24upx -8upx $learn;
	}
	.pin-active {
		background-color: $learn-light;
		color: $learn;
		box-shadow: 0 14upx 24upx -8upx $learn-light;
	}
}

.theme-2 {
	.collect-icon-active {
		color: $travel;
	}
	.focus-active {
		color: $travel !important;
	}
	.cell-icon {
		background-color: $travel-light;
		box-shadow: 0 14upx 20upx -6upx $travel-light;
		.left-icon {
			color: $travel;
		}
	}
	.user-more {
		background-color: $travel;
		color: #fff;
	}
	.view-all-text {
		color: $travel;
	}
	.pin-btn {
		background-color: $travel;
		box-shadow: 0 14upx 24upx -8upx $travel;
	}
	.pin-active {
		background-color: $travel-light;
		color: $travel;
		box-shadow: 0 14upx 24upx -8upx $travel-light;
	}
}

.theme-3 {
	.collect-icon-active {
		color: $food;
	}
	.focus-active {
		color: $food !important;
	}
	.cell-icon {
		background-color: $food-light;
		box-shadow: 0 14upx 20upx -6upx $food-light;
		.left-icon {
			color: $food;
		}
	}
	.user-more {
		background-color: $food;
		color: #fff;
	}
	.view-all-text {
		color: $food;
	}
	.pin-btn {
		background-color: $food;
		box-shadow: 0 14upx 24upx -8upx $food;
	}
	.pin-active {
		background-color: $food-light;
		color: $food;
		box-shadow: 0 14upx 24upx -8upx $food-light;
	}
}

.theme-4 {
	.collect-icon-active {
		color: $fun;
	}
	.focus-active {
		color: $fun !important;
	}
	.cell-icon {
		background-color: $fun-light;
		box-shadow: 0 14upx 20upx -6upx $fun-light;
		.left-icon {
			color: $fun;
		}
	}
	.user-more {
		background-color: $fun;
		color: #fff;
	}
	.view-all-text {
		color: $fun;
	}
	.pin-btn {
		background-color: $fun;
		box-shadow: 0 14upx 24upx -8upx $fun;
	}
	.pin-active {
		background-color: $fun-light;
		color: $fun;
		box-shadow: 0 14upx 24upx -8upx $fun-light;
	}
}
</style>
