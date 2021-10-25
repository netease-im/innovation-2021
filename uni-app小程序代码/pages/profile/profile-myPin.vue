<template>
	<view class="content">
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
				<k-issue-list :type="item.type"></k-issue-list>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
	import kIssueList from '../../components/k-list/k-issue-list.vue';
	export default {
		components: {
			kIssueList
		},
		data() {
			return {
				showType: false,
				sortIndex: -1,
				offsetX: 0,
				tabList: ['已发布', '已参与', '已收藏', '已浏览'],
				tabs: [
					{
						name: '已发布',
						type: 'publish'
					},
					{
						name: '已参与',
						type: 'attend'
					},
					{
						name: '已收藏',
						type: 'collection'
					},
					{
						name: '已浏览',
						type: 'history'
					}
				],
				current: 0,
				swiperCurrent: 0
			};
		},
		watch: {
			swiperCurrent(newVal) {
				this.offsetX = String(newVal * 25) + '%';
			}
		},
		onLoad(e) {
			this.swiperCurrent = e.type;
		},
		methods: {
			showToast(title,type) {
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
				this.swiperCurrent = index
			},
			// scroll-view到底部加载更多
			onreachBottom() {},
			getSwiperIndex(e) {
				this.swiperCurrent = e.detail.current;
			}
		}
	}
</script>

<style lang="scss">
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
			width: 100%;
			height: 100%;
			display: flex;
			justify-content: center;
			align-items: center;
			flex-wrap: nowrap;
			position: relative;
		}
		.line {
			width: 25%;
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
		width: 25%;
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
		height: calc(100vh - 80upx);
	}
}
</style>
