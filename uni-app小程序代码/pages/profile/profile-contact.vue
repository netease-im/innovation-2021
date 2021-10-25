<template>
	<view class="content">
		<view class="content-nav">
			<!-- 首页头部 -->
			<view class="nav">
				<!-- 搜索框 -->
				<view class="searchBar">
					<image src="../../static/icon/index-icon/search.png" mode=""></image>
					<input type="text" value="" placeholder-class="searchPH" placeholder="搜索" />
				</view>
			</view>
			<!-- 分类列表 -->
			<view class="tab-swiper">
				<view class="swiper-container">
					<view @click="changeSwiper(0)" :class="swiperCurrent == 0 ? 'active' : ''" class="tab">{{ focusNumber }}位关注</view>
					<view @click="changeSwiper(1)" :class="swiperCurrent == 1 ? 'active' : ''" class="tab">{{ fanNumber }}位粉丝</view>
					<view class="line" :style="'left: ' + offsetX"><view class="line-main"></view></view>
				</view>
			</view>
		</view>
		<!-- tab列表 -->
		<swiper class="swiper" :current="swiperCurrent" @change="getSwiperIndex">
			<swiper-item class="swiper-item">
				<scroll-view scroll-y class="scrollView" @scroll="focusScroll"><k-user-list @focusCount="focusCount" type="focus"></k-user-list></scroll-view>
			</swiper-item>
			<swiper-item class="swiper-item">
				<scroll-view scroll-y class="scrollView" @scroll="fanScroll"><k-user-list @fanCount="fanCount" type="fan"></k-user-list></scroll-view>
			</swiper-item>
		</swiper>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
import kUserList from '../../components/k-list/k-user-list.vue';
export default {
	components: { kUserList },
	data() {
		return {
			scrollTop: 0,
			user: {
				name: 'kang',
				isFocus: false
			},
			focusNumber: 0,
			fanNumber: 0,
			indexList: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'],
			showType: false,
			sortIndex: -1,
			offsetX: 0,
			current: 0,
			swiperCurrent: 0,
			focusScrollTop: null,
			fanScrollTop: null
		};
	},
	watch: {
		swiperCurrent(newVal) {
			this.offsetX = String(newVal * 50) + '%';
		}
	},
	onLoad(e) {
		this.swiperCurrent = Number(e.type);
		uni.setNavigationBarTitle({
			title: uni.getStorageSync('userInfo').nickName
		});
	},
	methods: {
		focusCount(e) {
			this.focusNumber = e.length;
		},
		fanCount(e) {
			this.fanNumber = e.length;
		},
		focusScroll(e) {
			this.focusScrollTop = e.detail.scrollTop;
		},
		fanScroll(e) {
			this.fanScrollTop = e.detail.scrollTop;
		},
		showToast(title, type) {
			this.$refs.uToast.show({
				title,
				type
			});
		},
		goIntro() {
			uni.navigateTo({
				url: '../intro/intro'
			});
		},
		// tabs通知swiper切换
		changeSwiper(index) {
			this.swiperCurrent = index;
		},
		// scroll-view到底部加载更多
		onreachBottom() {},
		getSwiperIndex(e) {
			this.swiperCurrent = e.detail.current;
		}
	}
};
</script>

<style>
	page {
		background-color: #fff;
	}
</style>

<style lang="scss" scoped>


.content {
	width: 100%;
	height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: flex-start;
	// 头部
	.content-nav {
		width: 100%;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 99;
		background-color: rgba($color: #fff, $alpha: 0.8);
		backdrop-filter: blur(15px);
	}
	.nav {
		width: calc(100% - 60upx);
		margin: 30upx 30upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.searchBar {
			width: 100%;
			height: 100upx;
			border-radius: 30upx;
			background-color: #f5f5f5;
			display: flex;
			align-items: center;
			padding: 0 26upx;
			& > image {
				width: 40upx;
				height: 40upx;
			}
			& > input {
				flex: 1;
				height: 100%;
				line-height: 100upx;
				padding: 0 20upx;
				color: #555555;
			}
			.searchPH {
				color: $grey;
			}
		}
		.add {
			width: 100upx;
			height: 100upx;
			text-align: center;
			line-height: 100upx;
			border-radius: 30upx;
			background-color: #fff;
			color: $primary;
			&-icon {
				font-size: 40upx;
			}
		}
	}
	// tab选择器
	.tab-swiper {
		width: 100%;
		font-size: 30upx;
		height: 100upx;
		padding-bottom: 30upx;
		.swiper-container {
			width: 100%;
			height: 100%;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-wrap: nowrap;
			position: relative;
		}
		.line {
			width: 50%;
			height: 16upx;
			position: absolute;
			bottom: -20upx;
			left: 0;
			z-index: -1;
			display: flex;
			justify-content: center;
			align-items: center;
			transition: all 0.3s ease;
			&-main {
				width: 10upx;
				height: 10upx;
				background-color: $primary;
				border-radius: 50%;
			}
		}
	}
	.tab {
		width: 50%;
		height: 100%;
		text-align: center;
		line-height: 100upx;
		color: #888;
		transition: all 0.3s ease;
	}
	.active {
		color: $primary;
		transform: scale(1.1) translateY(-3px);
	}
	.swiper,
	.scrollView {
		width: 100%;
		height: 100vh;
		overflow: visible;
	}
	.scrollView {
		width: 100%;
		height: 100vh;
	}
}
</style>
