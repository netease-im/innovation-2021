<template>
	<view>
		<swiper class="swiper" :current="swiperIndex" @change="swiperChange">
			<swiper-item v-for="(intro,index) in introList" :key="index">
				<view class="swiper-item">
					<image class="intro-img" :src="intro.img" mode=""></image>
					<text class="intro-title">{{intro.title}}</text>
					<text class="intro-describe">{{intro.describe}}</text>
				</view>
			</swiper-item>
		</swiper>
		<view class="forward-btn k-active" @click="nextPage">
			<image class="next-icon" src="../../static/icon/intro-icon/arrow.png" mode=""></image>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				swiperIndex: 0,
				introList: [
					{
						img: 'https://tva1.sinaimg.cn/large/006KgH1wly1gvzy5bftz5j30k00hsq6r.jpg',
						title: '浏览易玩团',
						describe: '不只是分类浏览，可以通过地图查看周围的易玩团哦~'
					},
					{
						img: 'https://tva1.sinaimg.cn/large/006KgH1wly1gvzy5kzp4kj30k00hs0ws.jpg',
						title: '加入易玩团',
						describe: '找到合适的易玩团，就大胆的加入！加入后也会加入相应群聊，一起规划好出行计划~'
					},
					{
						img: 'https://tva1.sinaimg.cn/large/006KgH1wly1gvzy5qpidaj30k00hswj3.jpg',
						title: '发布易玩团',
						describe: '如果想自己发起易玩团活动也是可以的哦~只需在学习、出行、美食和娱乐四种分类中选择即可发起~'
					}
				]
			}
		},
		methods: {
			nextPage() {
				if(this.swiperIndex != 2) {
					this.swiperIndex++;
				}
			},
			swiperChange(e) {
				this.swiperIndex = e.detail.current;
			}
		}
	}
</script>

<style lang="scss">
	.swiper {
		height: 90vh;
		&-item {
			height: 90vh;
			padding: 50upx;
			box-sizing: border-box;
			@include flex-column-center;
			.intro-img {
				width: 650upx;
				height: 650upx;
			}
			.intro-title {
				align-self: flex-start;
				padding-left: 20upx;
				font-size: 50upx;
				letter-spacing: 2upx;
				line-height: 100upx;
				color: $primary;
			}
			.intro-describe {
				align-self: flex-start;
				padding-left: 20upx;
				font-size: 34upx;
				line-height: 50upx;
				color: $minus-text;
				letter-spacing: 2upx;
			}
		}
	}
	.forward-btn {
		width: 200upx;
		height: 120upx;
		border-radius: 56upx;
		position: fixed;
		bottom: 5vh;
		right: 50upx;
		background-color: $primary;
		color: #fff;
		@include flex-row-center;
		box-shadow: 0 10upx 20upx -10upx $primary;
		.next-icon {
			width: 40upx;
			height: 40upx;
		}
	}
</style>
