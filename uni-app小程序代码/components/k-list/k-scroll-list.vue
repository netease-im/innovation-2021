<template>
	<!-- 缘拼列表 -->
	<view class="issueList">
		<text class="k-small-title">
			{{title}}
		</text>
		<scroll-view scroll-x class="scroll-x">
			<view class="issue k-active" @click="goDetail(issue.iId)" :class="'theme-' + issue.iType" v-for="issue in issueList" :key="issue.iId">
				<!-- 缘拼图片 -->
				<view class="issue-imgList">
					<image :src="baseUrl + issue.iImg[0]" lazy-load class="issue-img" mode="aspectFill"></image>
				</view>
				<!-- 缘拼类型 -->
				<view class="issue-icon">
					<image v-if="issue.iType == 1" src="../../static/icon/type-icon/learn_icon.png" mode=""></image>
					<image v-if="issue.iType == 2" src="../../static/icon/type-icon/travel_icon.png" mode=""></image>
					<image v-if="issue.iType == 3" src="../../static/icon/type-icon/food_icon.png" mode=""></image>
					<image v-if="issue.iType == 4" src="../../static/icon/type-icon/fun_icon.png" mode=""></image>
				</view>
				<view class="issue-nav">
					<!-- 缘拼标题 -->
					<view class="issue-title k-hideText">
						{{ issue.iTitle }}
					</view>
			
					<!-- 缘拼时间 -->
					<text class="issue-time">
						<text class="iconfont icon-jilu" style="margin-right: 10upx;"></text>
						<text>{{ timeFormat(issue.iStartTime.time) }}</text>
					</text>
				</view>
			
				<!-- 缘拼日期/地址 -->
				<view class="issue-info">
					<view class="issue-location k-hideText">
						<text class="iconfont icon-Calendar location-icon"></text>
						<view class="location-text">{{ issue.iStartTime.date }}</view>
					</view>
					<view class="issue-location k-hideText">
						<text class="iconfont icon-Location1 location-icon"></text>
						<view class="k-hideText location-text-1">{{ issue.iPositionEnd.name.name || issue.iPositionStart.name.name }}</view>
					</view>
				</view>
				
				<!-- 缘拼情况 -->
				<view class="issue-user" v-if="issue.issueAttend">
					<image
						:src="baseUrl+issue.frontUser.fuImg"
						class="user-avatar"
						lazy-load
					></image>
					<image
						:src="baseUrl+member.frontUser.fuImg"
						:style="'transform: translateX(' + (index + 1) * -20 + 'rpx);'"
						class="user-avatar"
						v-for="(member, index) in issue.issueAttend.slice(0, 5)"
						:key="index"
						lazy-load
					></image>
					<view class="user-more" v-if="issue.partMember.length > 5">
						<text>+{{ issue.partMember.length - 5 }}</text>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
export default {
	props: {
		type: null,
		title: null
	},
	data() {
		return {
			isLoad: true,
			baseUrl: 'https://zmetalhearty.com',
			currentPage: 1,
			issueList: [],
			refreshTrigger: false
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
		this.getParams();
	},
	mounted() {},
	methods: {
		refresh() {
			this.refreshTrigger = true;
			this.currentPage = 1;
			this.getIssueList();
		},
		next() {
			this.currentPage++;
			this.getIssueList('next');
		},
		getIssueList(data) {
			var url, params;
			if(this.type != null) {
				var params = {
					fromFuId: uni.getStorageSync('userid'),
					page: 1,
					limit: 10
				}
				switch(this.type) {
					case 'all' :
					url = 'issue/listjson';
					break;
					case 'near' :
					url = 'issue/getIssueByKilometers';
					params = data;
					break;
				}
			}
			if(url != null && params != null) {
				this.request(url,'POST',params)
				.then(res => {
					console.log(res);
					this.isLoad = false;
					this.refreshTrigger = false;
					if(this.type == 'near') {
						this.issueList = this.issueFormat(res.data.IssueList);
						console.log(this.issueList);
					}else {
						this.issueList = this.issueFormat(res.data.data);
						console.log(this.issueList);
					}
				});
			}
		},
		issueFormat(data) {
			data.forEach(issue => {
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
				if(issue.iPositionStart) {
					issue.iPositionStart = JSON.parse(issue.iPositionStart);
				}
				if(issue.iPositionEnd) {
					issue.iPositionEnd = JSON.parse(issue.iPositionEnd);
				}
			})
			return data;
		},
		goDetail(id) {
			uni.navigateTo({
				url: '../../pages/detail/detail?id='+id
			});
		},
		getParams() {
			uni.getLocation({
				type: 'gcj02',
				success: res => {
					console.log(res);
					var params = {
						latitude: res.latitude,
						longitude: res.longitude,
						fromFuId: uni.getStorageSync('userid')
					}
					this.getIssueList(params) //利用自身定位获取周围帖子
				}
			});
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
	padding-bottom: 20upx;
	margin-top: 40upx;
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
