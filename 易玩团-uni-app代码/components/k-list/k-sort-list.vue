<template>
	<view class="body">
		<view class="sort-container">
			<view v-for="(sort, index) in sortList" @click="handleSortClick(index)" :style="'background-color: ' + sort.color + ';box-shadow: 0 3px 7px -2px ' + sort.shadowColor " :key="index" class="sort-box">
				<image class="sort-img" :src="sort.url"></image>
				<view class="sort-text">{{sort.content}}</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	name: 'k-sort-list',
	data() {
		return {
			currentIndex: -1,
			sortList: [
				{
					url: '/static/icon/type-icon/learn_icon.png',
					color: '#cce2fd',
					content: '拼学习',
					shadowColor: '#cce2fd'
				},
				{
					url: '/static/icon/type-icon/travel_icon.png',
					color: '#c3f5e3',
					content: '拼出行',
					shadowColor: '#c3f5e3'
				},
				{
					url: '/static/icon/type-icon/food_icon.png',
					color: '#fff1cf',
					content: '拼美食',
					shadowColor: '#fff1cf'
				},
				{
					url: '/static/icon/type-icon/fun_icon.png',
					color: '#d5d9f8',
					content: '拼娱乐',
					shadowColor: '#d5d9f8'
				},
			]
		};
	},
	watch: {
		currentIndex(newVal,oldVal) {
			this.$emit('transferSortIndex',{
				index : this.currentIndex
			})
		}
	},
	onLoad() {},
	methods: {
		handleSortClick(index) {
			uni.navigateTo({
				url: '../../pages/publish/publish?type='+(index+1)
			})
		}
	}
};
</script>

<style lang="scss" scoped>
.body {
	width: 100vw;
	.sort-container {
		width: 100%;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		align-items: flex-start;
		padding: 0 50upx 50upx 50upx;
		.sort-box {
			width: 300upx;
			height: 160upx;
			margin-top: 20upx;
			border-radius: 40upx;
			display: flex;
			justify-content: center;
			align-items: center;
			position: relative;
			transition: all .2s ease-in-out;
			// &:nth-child(1) {
			// 	margin-left: 5%;
			// }
			&:nth-last-child(1) {
				margin-right: 0;
			}
			.sort-img {
				width: 80upx;
				height: 80upx;
			}
			.sort-text {
				width: 140upx;
				text-align: center;
				font-size: 34upx;
				color: $light;
			}
			&:active {
				transform: scale(.8);
			}
		}
		.clicked {
			background-color: #fff !important;
			box-shadow: 0 4upx 14upx -4upx #aaa !important;
		}
	}
}
</style>
