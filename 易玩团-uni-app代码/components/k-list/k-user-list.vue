<template>
	<!-- <u-index-list class="index-list" :scrollTop="ScrollTop" @select="select">
		<view v-for="(item, index) in indexList" :key="index" :id="'anchor-' + item">
			<u-index-anchor :index="item" />
			
		</view>
	</u-index-list> -->
	<view class="user">
		<view class="empty" v-if="userList.length == 0">
			<image :src="emptyImg" class="empty-img" mode=""></image>
			<text class="empty-text">
				<text>暂无</text>
				<text v-if="type=='focus'">关注</text>
				<text v-if="type=='fan'">粉丝</text>
			</text>
		</view>
		<view class="user-cell" v-for="(user, index) in userList" :key="index">
			<view class="user-left" @click="goUserInfo(user.followedFrontUser.fuId)">
				<u-avatar :src="baseUrl + ( type == 'focus' ? user.followedFrontUser.fuImg : user.frontUser.fuImg )" size="80" mode="circle"></u-avatar>
				<text class="user-name">{{ type == 'focus' ? user.followedFrontUser.fuNickname : user.frontUser.fuNickname }}</text>
			</view>
			<view class="focus-btn" :class="user.isFollow ? '' : 'focus-active'" @click="handleFocus(user,index)">
				<text v-show="user.isFollow">取消关注</text>
				<text v-show="!user.isFollow">关注</text>
			</view>
		</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
export default {
	props: {
		type: null
	},
	data() {
		return {
			emptyImg: getApp().globalData.friendEmptyImg,
			baseUrl: 'https://zmetalhearty.com',
			scrollTop: 0,
			userList: [],
			indexList: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
			focusCount: 3,
			fanCount: 3,
			showType: false,
			sortIndex: -1,
			offsetX: 0,
			current: 0,
			swiperCurrent: 0,
			query: null
		};
	},
	onPageScroll(e) {
		console.log(e);
		this.scrollTop = e.scrollTop;
	},
	created() {
		if (this.type != null) {
			var params;
			if (this.type == 'focus') {
				params = {
					fuId: uni.getStorageSync('userid')
				};
			} else if (this.type == 'fan') {
				params = {
					fufFuId: uni.getStorageSync('userid')
				};
			}
			this.request('frontUserFollow/getAll', 'POST', params).then(res => {
				console.log(res);
				this.userList = res.data.data;
				this.$emit(this.type + 'Count', {
					length: this.userList.length
				});
			});
		}
	},
	methods: {
		goUserInfo(id) {
			uni.navigateTo({
				url: '../profile/profile-other?id=' + id
			});
		},
		showToast(title, type) {
			this.$refs.uToast.show({
				title,
				type
			});
		},
		handleFocus(user,index) {
			var id = this.type == 'focus' ? user.followedFrontUser.fuId : user.frontUser.fuId;
			var url = user.isFollow ? 'delete' : 'add';
			this.request('frontUserFollow/'+url,'POST', {
				fuId: uni.getStorageSync('userid'),
				fufFuId: id
			})
			.then(res => {
				console.log(res);
				this.userList[index].isFollow = !this.userList[index].isFollow;
			})
		},
		select(e) {
			// const query = uni.createSelectorQuery();
			// console.log(query.select('#anchor-' + e).boundingClientRect());
			// query.selectViewport().scrollOffset();
			// query.exec(function(res) {
			// 	// debugger;
			// 	res[0].top; // #the-id节点的上边界坐标
			// 	res[1].scrollTop; // 显示区域的竖直滚动位置
			// 	_this.height = resu.windowHeight - res[0].top + 'px';
			// 	console.log('打印高度', res[0].height);
			// 	console.log('打印demo的元素的信息', res);
			// });
		}
	}
};
</script>

<style lang="scss" scoped>
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
.user {
	width: 100%;
	padding-top: 280upx;
}
.index-list {
	::v-deep .u-index-bar__sidebar {
		z-index: 99999;
	}
}
.user-cell {
	padding: 20upx 40upx 20upx 30upx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	.user-left {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		.user-name {
			margin-left: 14upx;
		}
	}
	.focus-btn {
		width: 110upx;
		height: 60upx;
		text-align: center;
		line-height: 56upx;
		border-radius: 20upx;
		border: 2upx solid $primary;
		color: $primary;
		transition: all 0.3s ease;
		text {
			font-size: 24upx;
		}
	}
	.focus-active {
		background-color: $primary;
		color: #fff;
	}
}
</style>
