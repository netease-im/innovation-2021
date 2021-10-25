<template>
	<!-- 缘拼列表 -->
	<view class="issueList">
		<scroll-view scroll-y class="scrollView">
			<!-- 地图卡片 -->
			<k-map-card></k-map-card>
			
			<!-- 缘拼状态 -->
			<view class="issue-status" @click="goStatus">
				<view class="status-icon">
					{{currentAttendCount}}<text style="font-size: 20rpx;">+</text>
				</view>
				<view class="status-info">
					<text v-if="currentAttendCount != 0">当前有<text class="info-main">{{currentAttendCount}}</text>个缘拼正在进行中</text>
					<text v-if="currentAttendCount == 0">当前没有缘拼正在进行中</text>
				</view>
				<text class="iconfont icon-xiayiye go-icon"></text>
			</view>
			
			<!-- 热门缘拼 -->
			<k-scroll-list type="all" title="热门缘拼"></k-scroll-list>
			
			<!-- 只差一位 -->
			<k-scroll-list type="near" title="离你最近"></k-scroll-list>
		</scroll-view>
	</view>
</template>

<script>
import kScrollList from './k-scroll-list.vue'
import kMapCard from '../k-card/k-map-card.vue';
export default {
	components: {
		kScrollList,
		kMapCard
	},
	props: {
		type: null
	},
	data() {
		return {
			isLoad: true,
			baseUrl: 'https://zmetalhearty.com',
			currentAttendCount: 0
		};
	},
	mounted() {},
	methods: {
		getCurrentYuanPin() {
			this.request('issueAttend/listjson','POST',{
				fromFuId: uni.getStorageSync('userid'),
				page: 1,
				limit: 20,
				fuId: uni.getStorageSync('userid')
			})
			.then(res => {
				if(res.data.data.length != 0) {
					this.currentAttendCount = res.data.data.length;
				}
			})
		},
		goStatus() {
			uni.navigateTo({
				url: '../../pages/profile/profile-myPin?type='+1
			})
		}
	}
};
</script>

<style>
page {
	background-color: #f5f5f5;
}
</style>

<style lang="scss" scoped>
.issueList {
	// padding-bottom: 20upx;
	.scrollView {
		height: calc(100vh - 240upx);
	}
	.k-small-title {
		padding: 0 20upx;
	}
	.scroll-x {
		width: 100%;
		height: 600upx;
		white-space: nowrap;
	}
	.empty {
		width: 100%;
		height: calc(100vh - 300upx);
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
	.issue-status {
		width: calc(100% - 40upx);
		margin: 20upx;
		padding: 0 20upx;
		height: 140upx;
		border-radius: 30upx;
		background-color: #fff;
		display: flex;
		justify-content: flex-start;
		align-items: center;
		position: relative;
		.status-icon {
			width: 80upx;
			height: 80upx;
			border-radius: 20upx;
			background-color: rgba($color: $primary, $alpha: .4);
			color: $primary;
			font-weight: bold;
			font-size: 44upx;
			text-align: center;
			line-height: 80upx;
			margin-right: 20upx;
		}
		.status-info {
			color: $minus-text;
			.info-main {
				color: $primary;
			}
		}
		.go-icon {
			position: absolute;
			right: 20upx;
			top: 50%;
			transform: translateY(-50%);
			color: #aaa;
		}
	}
	.issue {
		display: inline-block;
		width: 500upx;
		padding: 20upx 20upx 10upx 20upx;
		box-sizing: border-box;
		background-color: #fff;
		border-radius: 30upx;
		margin: 20upx 20upx 0 20upx;
		position: relative;
		&-icon {
			width: 80upx;
			height: 80upx;
			border-radius: 16upx;
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			left: 40upx;
			top: 40upx;
			background-color: $grey;
			image {
				width: 60upx;
				height: 60upx;
			}
		}
		&-nav {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-top: 20upx;
		}
		&-title {
			width: 350upx;
			font-size: 34upx;
			font-weight: bold;
			color: $main-text;
			overflow: hidden;
			text-overflow: ellipsis;
			white-space: nowrap;
			image {
				width: 50upx;
				height: 50upx;
				margin-right: 16upx;
			}
		}
		&-time {
			color: $dark-grey;
			font-size: 26upx;
			line-height: 50upx;
		}
		&-tag-list {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}
		&-tag {
			border: 2upx solid #007aff;
			color: #007aff;
			font-size: 24upx;
			border-radius: 10upx;
			padding: 4upx 10upx;
			margin: 20upx 16upx 20upx 0;
		}
		&-content {
			margin-bottom: 20upx;
			line-height: 50upx;
			color: $minus-text;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 2;
			overflow: hidden;
		}
		&-imgList {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			.issue-img {
				width: 100%;
				height: 300upx;
				border-radius: 20upx;
			}
		}
		&-user {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			padding-bottom: 20upx;
			.user-avatar,
			.empty-avatar {
				width: 60upx;
				height: 60upx;
				box-sizing: border-box;
				border-radius: 50%;
				border: 4upx solid #fff;
				background-color: #eee;
			}
			.user-more {
				width: 60upx;
				height: 60upx;
				border-radius: 50%;
				text-align: center;
				line-height: 52upx;
				font-size: 24upx;
				border: 4upx solid #fff;
				transform: translateX(-100upx);
			}
			.empty-avatar {
				text-align: center;
				line-height: 52upx;
				background-color: $light-grey;
				.plus-icon {
					font-size: 40upx;
					color: #ffffff;
				}
			}
		}
		&-info {
			display: flex;
			justify-content: flex-start;
			align-items: center;
		}
		&-location {
			min-width: 200upx;
			border-radius: 10upx;
			color: $light;
			line-height: 80upx;
			margin-right: 20upx;
			display: flex;
			justify-content: flex-start;
			align-items: center;
			.location-icon {
				margin-right: 10upx;
				font-size: 34upx;
				padding-top: 4upx;
			}
			.location-text {
				width: 160upx;
			}
			.location-text-1 {
				width: 300upx;
			}
		}
		.pin-active {
			background-color: $light;
			color: $dark;
		}
	}
	.theme-1 {
		.user-count {
			color: $learn;
		}
		.issue-icon {
			background-color: $learn-light;
		}
		.issue-time {
			color: $learn;
			padding: 10upx 16upx;
			border-radius: 10upx;
		}
		.user-more {
			background-color: $learn-light;
			color: $learn;
		}
	}
	.theme-2 {
		.user-count {
			color: $travel;
		}
		.issue-icon {
			background-color: $travel-light;
		}
		.issue-time {
			color: $travel;
			padding: 10upx 16upx;
			border-radius: 10upx;
		}
		.user-more {
			background-color: $travel-light;
			color: $travel;
		}
	}
	.theme-3 {
		.user-count {
			color: $food;
		}
		.issue-icon {
			background-color: $food-light;
		}
		.issue-time {
			color: $food;
			padding: 10upx 16upx;
			border-radius: 10upx;
		}
		.user-more {
			background-color: $food-light;
			color: $food;
		}
	}
	.theme-4 {
		.user-count {
			color: $fun;
		}
		.issue-icon {
			background-color: $fun-light;
		}
		.issue-time {
			color: $fun;
			padding: 10upx 16upx;
			border-radius: 10upx;
		}
		.user-more {
			background-color: $fun-light;
			color: $fun;
		}
	}
}
</style>
