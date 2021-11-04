<template>
	<view>
		<text class="k-title">
			评论通知
		</text>
		<view class="empty" v-if="notifacationList.length == 0">
			<image :src="emptyImg" class="empty-img" mode="cover"></image>
			<text class="empty-text">
				<text>暂无评论</text>
			</text>
		</view>
		<view class="notifacation k-active" @click="goDetail(notice.issue.iId)" v-for="(notice, index) in notifacationList" :key="index">
			<u-avatar :src="baseUrl+notice.frontUser.fuImg" mode="square"></u-avatar>
			<view class="notifacation-info">
				<view class="info-main">
					<text style="font-weight: bold;margin-right: 4upx;">{{notice.frontUser.fuNickname}}</text>
					评论了你的
					<text class="info-special">易玩团</text>
				</view>
				<view style="color: #888;">
					{{notice.nMsg}}
				</view>
			</view>
			<text class="iconfont icon-xiayiye"></text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				baseUrl: getApp().globalData.baseUrl,
				notifacationList: [],
				emptyImg: getApp().globalData.contentEmptyImg
			}
		},
		onLoad() {
			this.getVerifyList();
		},
		onReachBottom() {
			this.getVerifyList('next');
		},
		methods: {
			getVerifyList(type) {
				this.request('news/listjson','POST',{
					page: this.page,
					limit: 10,
					nType: 2,
					toUserId: uni.getStorageSync('userid')
				})
				.then(res => {
					console.log(res);
					if(res.data.data.length != 0) {
						if(type == 'next') {
							this.page++;
							this.notifacationList.push(...this.format(res.data.data));
						}else {
							this.page++;
							this.notifacationList = this.format(res.data.data);
						}
					}
				})
			},
			format(list) {
				list.forEach(item => {
					item.issue = this.$issueFormat(item.issue);
				})
				return list;
			},
			goDetail(id) {
				uni.navigateTo({
					url: '../../pages/detail/detail?id='+id
				});
			}
		}
	}
</script>

<style lang="scss">
page {
	background-color: #F5f5f5;
}
.notifacation {
	width: calc(100% - 100upx);
	margin: 30upx auto;
	padding: 20upx;
	background-color: #fff;
	border-radius: 20upx;
	@include flex-row-spaceBetween;
	&-info {
		width: calc(100% - 150upx);
		padding: 0 20upx;
		height: 100%;
		.info-main {
			height: 100%;
			@include line-2-ellipsis;
			.info-special {
				color: $primary;
			}
		}
	}
	&-opration {
		@include flex-row-center;
		.opration-icon {
			width: 52upx;
			height: 52upx;
			border-radius: 50%;
			@include flex-row-center;
		}
		.allow {
			border: 4upx solid green;
			color: green;
			margin-right: 16upx;
		}
		.deny {
			border: 4upx solid red;
			color: red;
		}
	}
}
.empty {
	width: 100%;
	height: calc(100vh - 400upx);
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	&-img {
		width: 50%;
	}
	&-text {
		font-size: 40upx;
		letter-spacing: 2upx;
		color: $light;
	}
}
</style>
