<template>
	<view class="map">
		<view class="map-container" @click="goDiscovery">
			<map
				id="mapCard"
				:longitude="selfLocation.longitude"
				:markers="issueList"
				:latitude="selfLocation.latitude"
				:enable-zoom="false"
				:enable-scroll="false"
				class="map-main"
			>
				<view class="map-info">
					<view class="info-content">
						<view class="info-main">附近易玩团</view>
						<view class="info-minus">
							<text class="iconfont icon-Location1"></text>
							<view>{{locationName}}</view>
						</view>
						<view class="info-status">
							<text>
								{{ issueList.length }}
								<text style="font-size: 30upx;">+</text>
							</text>
						</view>
					</view>
				</view>
			</map>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			locationName: '',
			selfLocation: {},
			issueList: []
		};
	},
	methods: {
		goDiscovery() {
			uni.switchTab({
				url: '../../pages/discovery/discovery'
			})
		},
		handleMarkerClick(e) {
			// 地图图标点击事件，点击进入详情
			console.log(e);
			var id = e.detail.markerId;
			uni.navigateTo({
				url: '../detail/detail?id=' + id
			});
		},
		moveToSelfLocation() {
			// 移动到自己的位置
			var mapCtx = uni.createMapContext('mapCard');
			mapCtx.moveToLocation({
				longitude: this.selfLocation.longitude,
				latitude: this.selfLocation.latitude,
				success: res => {
					console.log(res);
				}
			});
		},
		getSelfLocation() {
			// 获取自身经纬度
			uni.getLocation({
				type: 'gcj02',
				success: res => {
					console.log(res);
					this.selfLocation = {
						latitude: res.latitude,
						longitude: res.longitude
					};
					uni.request({
						url: 'https://apis.map.qq.com/ws/geocoder/v1?location=' + res.latitude + ',' + res.longitude + '&key=' + '5SKBZ-TBSW3-QTW33-YJLYA-LKI2Z-IPFMF',
						header: {
							'content-type': 'application/json' // 默认值
						},
						success:(res) => {
							this.locationName = res.data.result.formatted_addresses.rough;
						}
					});
					this.getIssueList(); //利用自身定位获取周围帖子
				}
			});
		},
		handleRegionChange(e) {
			// 地图移动触发的事件
			e.type == 'end' ? this.getIssueList() : '';
		},
		getIssueList() {
			this.request('issue/getIssueByKilometers', 'POST', {
				latitude: this.selfLocation.latitude,
				longitude: this.selfLocation.longitude,
				fromFuId: uni.getStorageSync('userid')
			}).then(res => {
				console.log(res);
				if(res == 'not login yet') {
					return
				}else if(res.data.IssueList.length != 0) {
					this.issueList = this.$mapIssueFormat(res.data.IssueList);
				}
			});
		}
	},
	created() {
		// 页面加载时触发
		this.getSelfLocation(); // 获取自身定位
	}
};
</script>

<style lang="scss" scoped>
.map {
	&-container {
		width: calc(100% - 40upx);
		margin: 40upx 20upx;
		height: 400upx;
		border-radius: 30upx;
		transform: translateY(0);
		overflow: hidden;
		// position: relative;
	}
	&-main {
		width: 100%;
		height: 400upx;
	}
	&-info {
		width: calc(100% - 20upx);
		height: 150upx;
		box-sizing: border-box;
		padding: 0 30upx;
		position: absolute;
		bottom: 10upx;
		left: 10upx;
		border-radius: 26upx;
		background-color: rgba($color: #fff, $alpha: 0.6);
		backdrop-filter: blur(10px);
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-start;
		line-height: 60upx;
		.info-main {
			font-size: 40upx;
			font-weight: bold;
		}
		.info-minus {
			display: flex;
			justify-content: flex-start;
			align-items: center;
			color: $minus-text;
		}
		.info-status {
			width: 80upx;
			height: 80upx;
			position: absolute;
			border-radius: 20upx;
			top: 50%;
			transform: translateY(-50%);
			right: 30upx;
			text-align: center;
			line-height: 80upx;
			font-weight: bold;
			font-size: 40upx;
			color: $primary;
			background-color: rgba($color: $primary, $alpha: 0.4);
		}
	}
}
</style>
