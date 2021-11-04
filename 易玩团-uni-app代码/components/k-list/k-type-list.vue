<template>
	<view class="body">
		<scroll-view class="sort-container" :scroll-x="true">
			<view v-for="(sort, index) in sortList" @click="handleSortClick(index)" :class="currentIndex == index ? 'clicked' : ''" :style="'background-color: ' + sort.color + ';box-shadow: 0 3px 7px -2px ' + sort.shadowColor " :key="index" class="sort-box">
				<image class="sort-img" :src="sort.url"></image>
				<text class="sort-text">{{sort.content}}</text>
			</view>
		</scroll-view>
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
					url: '/static/icon/sort-icon/sort-1.png',
					color: '#fdcccf',
					content: '爱情',
					shadowColor: '#fdd6dc'
				},
				{
					url: '/static/icon/sort-icon/sort-2.png',
					color: '#fdcccf',
					content: '生日',
					shadowColor: '#fdd6dc'
				},
				{
					url: '/static/icon/sort-icon/sort-3.png',
					color: '#cacffa',
					content: '道歉',
					shadowColor: '#d7dcfa'
				},
				{
					url: '/static/icon/sort-icon/sort-4.png',
					color: '#cacffa',
					content: '长辈',
					shadowColor: '#d7dcfa'
				},
				{
					url: '/static/icon/sort-icon/sort-5.png',
					color: '#ffd9a7',
					content: '开业',
					shadowColor: '#ffe6c7'
				},
				{
					url: '/static/icon/sort-icon/sort-6.png',
					color: '#ffd9a7',
					content: '祝贺',
					shadowColor: '#ffe6c7'
				}
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
	methods: {
		handleSortClick(index) {
			this.currentIndex = this.currentIndex == index ?  -1 : index;
		}
	}
};
</script>

<style lang="scss">
.body {
	width: 100vw;
	.sort-container {
		width: 100%;
		height: 180upx;
		overflow: visible;
		white-space: nowrap;
		.sort-box {
			width: 120upx;
			height: 120upx;
			margin-top: 20upx;
			border-radius: 40upx;
			display: inline-flex;
			justify-content: center;
			align-items: center;
			margin-right: 20upx;
			position: relative;
			transition: all .2s ease-in-out;
			&:nth-child(1) {
				margin-left: 5%;
			}
			&:nth-last-child(1) {
				margin-right: 5%;
			}
			.sort-img {
				width: 60upx;
				height: 60upx;
			}
			.sort-text {
				position: absolute;
				bottom: -46upx;
				left: 50%;
				transform: translateX(-50%);
				font-size: 28upx;
				color: #888;
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
