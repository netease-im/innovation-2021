<template>
	<!-- 缘拼列表 -->
	<view class="issueList" :class="theme">
		<scroll-view scroll-y :refresher-triggered="refreshTrigger" class="scrollView" @refresherrefresh="refresh" refresher-enabled @scrolltolower="next">
			<view class="empty" v-if="issueList.length == 0">
				<image :src="emptyImg" class="empty-img" mode=""></image>
				<text class="empty-text">暂无内容</text>
			</view>
			<view class="issue k-active" @click="goDetail(issue.iId)" :class="'theme-' + issue.iType" v-for="issue in issueList" :key="issue.iId">
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
				<!-- 缘拼标签 -->
				<!-- <view class="issue-tag-list">
					<view class="issue-tag" v-for="tag in issue.tag" :key="tag">
						#{{tag}}
					</view>
				</view> -->
			
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
			
				<!-- 缘拼内容 -->
				<view class="issue-content">{{ issue.iDetails }}</view>
				
				<!-- 缘拼图片 -->
				<view class="issue-imgList">
					<image :src="baseUrl + issue.iImg[0]" lazy-load class="issue-img" mode="aspectFill"></image>
				</view>
				
				<!-- 缘拼情况 -->
				<view class="issue-user">
					<image :src="baseUrl+issue.frontUser.fuImg" mode="cover" class="user-avatar" lazy-load></image>
					<view v-if="issue.issueAttend">
						<image
							:src="baseUrl+member.frontUser.fuImg"
							:style="'transform: translateX(' + (index + 1) * -20 + 'rpx);'"
							class="user-avatar"
							v-for="(member, index) in issue.issueAttend.slice(0, 5)"
							:key="index"
							lazy-load
							mode="cover"
						></image>
					</view>
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
		userId: null,
		searchContent: null
	},
	data() {
		return {
			theme: '',
			emptyImg: getApp().globalData.contentEmptyImg,
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
		this.getHeight();
		this.getIssueList();
		if(!uni.getStorageSync('historyList')) {
			uni.setStorageSync('historyList',[])
		}
	},
	mounted() {},
	methods: {
		getHeight() {
			var pinTypeList = ['all','learn','travel','food'];
			var typeList = ['publish','attend','collection','history'];
			if(typeList.indexOf(this.type) != -1) {
				this.theme = 'myPin';
			}else if(this.type == 'userIssue') {
				this.theme = 'userIssueList'
			}
		},
		refresh() {
			this.refreshTrigger = true;
			this.currentPage = 1;
			this.getIssueList();
		},
		next() {
			this.getIssueList('next');
		},
		getIssueList(e) {
			var url, params;
			var json = false;
			var method = 'POST';
			var isRequest = true;
			if(this.type != null) {
				var params = {
					fromFuId: uni.getStorageSync('userid'),
					page: this.currentPage,
					limit: 10
				}
				switch(this.type) {
					case 'all' :
					url = 'issue/listjson';
					break;
					case 'learn' :
					url = 'issue/listjson';
					params.iType = 1;
					break;
					case 'travel' :
					url = 'issue/listjson';
					params.iType = 2;
					break;
					case 'food' :
					url = 'issue/listjson';
					params.iType = 3;
					break;
					case 'fun' :
					url = 'issue/listjson';
					params.iType = 4;
					break;
					case 'userIssue' :
					url = 'issue/listjson';
					params.fuId = this.userId;
					break;
					case 'publish' :
					url = 'issue/listjson';
					params.fuId = uni.getStorageSync('userid');
					break;
					case 'attend' :
					url = 'issueAttend/listjson';
					params.fuId = uni.getStorageSync('userid');
					break;
					case 'collection' :
					url = 'frontUserIssueCollections/listjson';
					params.fuId = Number(this.userId);
					break;
					case 'history' :
					if(uni.getStorageSync('historyList').length != 0) {
						url = `issue/getByIIdList?fromFuId=${uni.getStorageSync('userid')}&iIds=${JSON.stringify(...uni.getStorageSync('historyList'))}`;
					}else {
						isRequest = false;
					}
					break;
					case 'search' :
					url = 'issue/listjson';
					if(this.searchContent != '') {
						params.iTitle = this.searchContent;
					}
					break;
				}
			}
			if(url != null && params != null && isRequest) {
				this.request(url, method, params, json)
				.then(res => {
					console.log(this.type);
					console.log(res);
					this.refreshTrigger = false;
					if(res.data.data.length != 0) {
						this.currentPage++;
						if(e != 'next') {
							if(this.type == 'attend' || this.type == 'collection') {
								this.issueList = this.issueFormat(res.data.data.map(item => item.issue));
							}else {
								this.issueList = this.issueFormat(res.data.data);
							}
						}else {
							if(this.type == 'attend' || this.type == 'collection') {
								this.issueList.push(...this.issueFormat(res.data.data.map(item => item.issue)));
							}else {
								this.issueList.push(...this.issueFormat(res.data.data));
							}
						}
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
	.scrollView {
		height: calc(100vh - 240upx);
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
	.issue {
		width: calc(100% - 40upx);
		padding: 34upx 30upx 10upx 120upx;
		box-sizing: border-box;
		background-color: #fff;
		border-radius: 20upx;
		margin: 20upx 20upx 0 20upx;
		position: relative;
		&-icon {
			width: 80upx;
			height: 80upx;
			border-radius: 30upx;
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			left: 20upx;
			top: 30upx;
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
				width: 30%;
				height: 120upx;
				border-radius: 20upx;
			}
		}
		&-user {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			padding: 20upx 0;
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
.userIssueList {
	.scrollView {
		height: calc(100vh - 470upx);
	}
	.empty {
		height: calc(100vh - 480upx);
	}
}
.myPin {
	.scrollView {
		height: calc(100vh - 120upx);
	}
	.empty {
		height: calc(100vh - 120upx);
	}
}
</style>
