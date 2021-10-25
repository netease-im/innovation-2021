<template>
	<view class="content">
		<!-- 首页头部 -->
		<view class="nav">
			<!-- 搜索框 -->
			<view class="searchBar">
				<image src="../../static/icon/index-icon/search.png" mode=""></image>
				<input type="text" v-model="searchContent" placeholder-class="searchPH" placeholder="搜索" @confirm="search" confirm-type="search" />
			</view>
			<view class="add k-active" @click="showAdd"><text class="iconfont icon-jiahao add-icon"></text></view>
		</view>
		<!-- 分类列表 -->
		<scroll-view scroll-x class="tab-swiper">
			<view class="swiper-container">
				<view v-for="(item, index) in tabList" @click="changeSwiper(index)" :class="swiperCurrent == index ? 'active' : ''" :key="index" class="tab">{{ item }}</view>
				<view class="line" :style="'left: ' + offsetX"><view class="line-main"></view></view>
			</view>
		</scroll-view>
		<!-- tab列表 -->
		<swiper class="swiper" :current="swiperCurrent" @change="getSwiperIndex">
			<swiper-item class="swiper-item" v-for="(item, index) in tabs" :key="index">
				<k-main-list v-if="index == 0" type="all"></k-main-list>
				<k-issue-list v-else :type="item.type"></k-issue-list>
			</swiper-item>
		</swiper>
		<!-- 发布弹出层 -->
		<u-popup v-model="showType" mode="bottom" border-radius="30">
			<text class="k-title">发布</text>
			<k-sort-list></k-sort-list>
		</u-popup>
		<u-toast ref="uToast" />
	</view>
</template>

<script>
import kMainList from '../../components/k-list/k-main-list.vue';
import kIssueList from '../../components/k-list/k-issue-list.vue';
import kSortList from '../../components/k-list/k-sort-list.vue';
export default {
	components: {
		kMainList,
		kIssueList,
		kSortList
	},
	data() {
		return {
			showType: false,
			sortIndex: -1,
			offsetX: 0,
			tabList: ['首页', '全部', '拼学习', '拼出行', '拼美食', '拼娱乐'],
			tabs: [
				{
					name: '首页',
					type: 'all'
				},
				{
					name: '全部',
					type: 'all'
				},
				{
					name: '拼学习',
					type: 'learn'
				},
				{
					name: '拼出行',
					type: 'travel'
				},
				{
					name: '拼美食',
					type: 'food'
				},
				{
					name: '拼娱乐',
					type: 'fun'
				}
			],
			current: 0,
			swiperCurrent: 0,
			searchContent: ''
		};
	},
	watch: {
		swiperCurrent(newVal) {
			this.offsetX = String(newVal * 16.6) + '%';
		}
	},
	onLoad(e) {
		if(uni.getStorageSync('isLogin')) {
			this.showToast('登录成功!','success');
			uni.setStorageSync('isLogin',false)
		}
	},
	methods: {
		search() {
			if(this.searchContent != '') {
				uni.navigateTo({
					url: '../search/search?searchContent='+this.searchContent
				})
			}
		},
		showToast(title,type) {
			this.$refs.uToast.show({
				title,
				type
			});
		},
		showAdd() {
			this.showType = true;
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

<style lang="scss" scoped>
page {
	background-color: #f6f6f6;
}
.fade-enter-active,
.fade-leave-active {
	transition: opacity 0.5s;
}
.fade-enter,
.fade-leave-active {
	opacity: 0;
}
.content {
	width: 100%;
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: flex-start;
	// 头部
	.nav {
		width: calc(100% - 40upx);
		margin: 30upx 20upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.searchBar {
			width: calc(100% - 120upx);
			height: 100upx;
			border-radius: 30upx;
			background-color: #ffffff;
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
		font-size: 34upx;
		height: 80upx;
		.swiper-container {
			width: calc(160upx * 6);
			height: 100%;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-wrap: nowrap;
			position: relative;
		}
		.line {
			width: 160upx;
			height: 16upx;
			position: absolute;
			bottom: 18upx;
			left: 0;
			z-index: -1;
			display: flex;
			justify-content: center;
			align-items: center;
			transition: all 0.3s ease;
			&-main {
				width: 40%;
				height: 100%;
				background-color: $primary;
				border-radius: 4upx;
			}
		}
	}
	.tab {
		width: 160upx;
		height: 100%;
		text-align: center;
		line-height: 80upx;
		color: #888;
		transition: all 0.3s ease;
	}
	.active {
		color: #444;
		transform: scale(1.3) translateY(-3px);
	}
	.swiper,
	.scrollView {
		width: 100%;
		height: calc(100vh - 240upx);
	}
}
</style>
