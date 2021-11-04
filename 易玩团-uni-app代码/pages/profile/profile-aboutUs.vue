<template>
	<view class="intro">
		<image class="intro-img" src="https://tva1.sinaimg.cn/large/006KgH1wly1gvzz5dn05tj30go0e1n0v.jpg" mode=""></image>
		<text class="intro-title">{{title}}</text>
		<text class="intro-describe">{{describe}}</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: '关于我们',
				describe: '我们是来自内蒙古师范大学的易玩团开发团队，团队有两名前端开发工程师，一名后端开发工程师以及一名安卓开发工程师。我们一直在致力于打造这样一款解决大学生社交难题的小程序，理念是想将大学生的普遍社交场景从线上转至线下，提倡一个面对面的社交方式。并且通过精确化社交需求来方便用户浏览及选择合适的易玩团活动。'
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.intro {
		height: 100vh;
		padding: 50upx;
		box-sizing: border-box;
		@include flex-column-center;
		.intro-img {
			width: 650upx;
			height: 600upx;
		}
		.intro-title {
			font-size: 50upx;
			letter-spacing: 2upx;
			line-height: 100upx;
			color: $primary;
		}
		.intro-describe {
			font-size: 34upx;
			line-height: 50upx;
			color: $minus-text;
			letter-spacing: 2upx;
		}
	}
</style>
