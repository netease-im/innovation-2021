<template>
	<view class="map">
		<map
			id="map"
			:longitude="selfLocation.longitude"
			:markers="issueList"
			@markertap="handleMarkerClick"
			@regionchange="handleRegionChange"
			:latitude="selfLocation.latitude"
			style="width: 750rpx; height: 100vh"
		>
			<cover-view class="self-location" @click.native.stop="moveToSelfLocation">
				<cover-image class="location-icon" src="../../static/icon/discovery-icon/self-location.png"></cover-image>
			</cover-view>
		</map>
	</view>
</template>

<script>
export default {
	data() {
		return {
			selfLocation: {},
			issueList: []
		}
	},
	methods: {
		handleMarkerClick(e) { // 地图图标点击事件，点击进入详情
			console.log(e);
			var id = e.detail.markerId;
			uni.navigateTo({
				url: '../detail/detail?id='+id
			})
		},
		moveToSelfLocation() { // 移动到自己的位置
			var mapCtx = uni.createMapContext('map');
			mapCtx.moveToLocation({
				longitude: this.selfLocation.longitude,
				latitude: this.selfLocation.latitude,
				success:(res) => {
					console.log(res);
				}
			});
		},
		getSelfLocation() { // 获取自身经纬度
			uni.getLocation({
				type: 'gcj02',
				success: res => {
					console.log(res);
					this.selfLocation = {
						latitude: res.latitude,
						longitude: res.longitude
					};
					this.getIssueList() //利用自身定位获取周围帖子
				}
			});
		},
		handleRegionChange(e) { // 地图移动触发的事件
			e.type == 'end' ? this.getIssueList() : '';
		},
		getIssueList() { // 获取定位周围的帖子
			var mapCtx = uni.createMapContext('map');
			mapCtx.getCenterLocation({
				success:(res) => {
					console.log(res);
					this.request('issue/getIssueByKilometers','POST', {
						latitude: res.latitude,
						longitude: res.longitude,
						fromFuId: uni.getStorageSync('userid')
					})
					.then(res => {
						console.log(res);
						this.issueList = this.$mapIssueFormat(res.data.IssueList);
					})
				}
			});
		}
	},
	onLoad() { // 页面加载时触发
		this.getSelfLocation(); // 获取自身定位
	}
};
</script>

<style lang="scss" scoped>
.map {
	.self-location {
		width: 100upx;
		height: 100upx;
		position: fixed;
		right: 30upx;
		top: 30upx;
		text-align: center;
		line-height: 100upx;
		background-color: #fff;
		display: flex;
		justify-content: center;
		align-items: center;
		// background-color: rgba($color: #fff, $alpha: 0.8);
		// backdrop-filter: blur(10px);
		border-radius: 20upx;
		box-shadow: 0 10upx 20upx -6upx $light;
		.location-icon {
			width: 60upx;
			height: 60upx;
		}
	}
}
</style>
