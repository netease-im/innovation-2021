<template>
	<view>
		<view class="nav">
			<u-avatar v-if="user.fuImg" :src="baseUrl + user.fuImg" :size="150"></u-avatar>
			<view class="status-container">
				<view class="status-box">
					<text class="main-text">12</text>
					<text class="minus-text">缘拼</text>
				</view>
				<view class="status-box">
					<text class="main-text">{{user.followedCount}}</text>
					<text class="minus-text">关注</text>
				</view>
				<view class="status-box">
					<text class="main-text">{{user.isFollowedCount}}</text>
					<text class="minus-text">粉丝</text>
				</view>
			</view>
		</view>
		<view class="user-name">{{user.fuNickname}}</view>
		<view class="opration">
			<view class="focus-btn k-active" :class="user.isFocus ? 'active' : ''" @click="focus">
				<text v-show="!user.isFocus">关 注</text>
				<text v-show="user.isFocus">已 关 注</text>
			</view>
			<view class="message-btn k-active" @click="goChat">
				<text class="iconfont icon-fasong"></text>
			</view>
		</view>
		<text class="k-small-title">
			<text v-if="user.fuSex == 1">他</text>
			<text v-if="user.fuSex == 2">她</text>
			的缘拼
		</text>
		<kList v-if="user.fuId" type="userIssue" :userId="user.fuId"></kList>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
	import kList from '../../components/k-list/k-issue-list.vue';
	export default {
		components: {
			kList
		},
		data() {
			return {
				baseUrl: getApp().globalData.baseUrl,
				user: {}
			}
		},
		methods: {
			focus() {
				if(this.user.fuId == uni.getStorageSync('userid')) {
					this.showToast('不可以关注自己哦','warning');
				}else {
					var url = this.user.isFocus ? 'delete' : 'add';
					this.request('frontUserFollow/'+url,'POST', {
						fuId: uni.getStorageSync('userid'),
						fufFuId: this.user.fuId
					})
					.then(res => {
						console.log(res);
						this.user.isFocus = !this.user.isFocus;
					})
				}
			},
			goChat() {
				if(uni.getStorageSync('userid') != this.user.fuId) {
					uni.navigateTo({
						url: '../chat/chat-single?id=' + this.user.fuId
					})
				}else {
					this.showToast('不可以和自己聊天哦','warning');
				}
			},
			showToast(title, type) {
				this.$refs.uToast.show({ title, type });
			},
			getUserInfo(id) {
				this.request('frontUser/getUserInfo','POST',{
					fuId: id,
					nFuId: uni.getStorageSync('userid')
				})
				.then(res => {
					console.log(res);
					this.user = res.data;
				})
			},
		},
		onLoad(e) {
			this.getUserInfo(e.id);
		}
	}
</script>

<style lang="scss">
	.nav {
		width: 100%;
		padding: 30upx 50upx;
		box-sizing: border-box;
		@include flex-row-center;
		.status-container {
			width: calc(100% - 150upx);
			@include flex-row-center;
			.status-box {
				width: 50%;
				line-height: 50upx;
				@include flex-column-center;
				.main-text {
					font-size: 40upx;
					font-weight: bold;
					color: $primary;
				}
				.minus-text {
					font-size: 24upx;
					color: $minus-text;
				}
			}
		}
	}
	.user-name {
		padding: 0 50upx;
	}
	.opration {
		width: 100%;
		padding: 30upx 50upx 0 50upx;
		@include flex-row-spaceBetween;
		.focus-btn {
			width: calc(100% - 104upx);
			height: 72upx;
			border: 4upx solid $primary;
			background-color: $primary;
			border-radius: 20upx;
			color: #fff;
			text-align: center;
			line-height: 72upx;
			transition: all .4s ease;
		}
		.active {
			background-color: #f5f5f5;
			color: $primary;
		}
		.message-btn {
			width: 72upx;
			height: 72upx;
			border-radius: 20upx;
			border: 4upx solid $primary;
			background-color: #f5f5f5;
			color: $primary;
			font-weight: bold;
			text-align: center;
			line-height: 72upx;
		}
	}
</style>
