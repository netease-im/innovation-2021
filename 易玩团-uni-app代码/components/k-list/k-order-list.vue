<template>
	<view class="list">
		<view class="order" v-for="(order,index) in orderList" :key="index">
			<view class="order-nav">
				<view class="shop-info">
					<u-avatar class="shop-img" size="50" :src="order.shop.shop_img"></u-avatar>
					<text>{{order.shop.shop_name}}</text>
				</view>
				<text class="order-status">
					<text v-if="order.order_status == 1" class="yellow">未付款</text>
					<text v-if="order.order_status == 2" class="blue">待发货</text>
					<text v-if="order.order_status == 3" class="orange">待收货</text>
					<text v-if="order.order_status == 4" class="green">交易成功</text>
				</text>
			</view>
			
			<view class="good" v-for="(good,goodIndex) in order.goods" :key="goodIndex">
				<u-image border-radius="20" :width="100" :height="100" src="good.good_img"></u-image>
				<view class="good-info">
					<view class="good-bottom">
						<text>{{good.good_name}}</text>
						<text class="good-price"><text class="good-icon">￥</text>{{good.good_price}}</text>
					</view>
					<text>x{{good.good_quantity}}</text>
				</view>
			</view>
			
			<view class="total">
				总计：￥ <text>299</text>
			</view>
			
			<view class="opration">
				<u-button class="op-btn minus" :ripple="true" ripple-bg-color="#f3c56e">查看物流</u-button>
				<u-button class="op-btn main" :ripple="true" ripple-bg-color="#64b1e3">评价</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderList: [
					{
						create_time: '2020-12-31 5:22:00',
						order_status: 1,
						total_price: 244,
						goods: [
							{
								good_img: '',
								good_name: '满天星',
								good_price: '21',
								good_quantity: '1'
							},
							{
								good_img: '',
								good_name: 'goosafaf',
								good_price: '21',
								good_quantity: '1'
							}
						],
						shop: {
							shop_img: '',
							shop_name: 'Dasiy的花店',
						}
					},
					{
						create_time: '2020-12-31 5:22:00',
						order_status: 1,
						total_price: 244,
						goods: [
							{
								good_img: '',
								good_name: '满天星',
								good_price: '21',
								good_quantity: '1'
							},
							{
								good_img: '',
								good_name: 'goosafaf',
								good_price: '21',
								good_quantity: '1'
							}
						],
						shop: {
							shop_img: '',
							shop_name: 'Dasiy的花店',
						}
					},
					{
						create_time: '2020-12-31 5:22:00',
						order_status: 1,
						total_price: 244,
						goods: [
							{
								good_img: '',
								good_name: '满天星',
								good_price: '21',
								good_quantity: '1'
							},
							{
								good_img: '',
								good_name: 'goosafaf',
								good_price: '21',
								good_quantity: '1'
							}
						],
						shop: {
							shop_img: '',
							shop_name: 'Dasiy的花店',
						}
					}
				]
			};
		},
		created() {
			this.$api.request('flowerOrder/listjson','POST',{
				uId: uni.getStorageSync('userInfo').userid,
				page: 1,
				limit: 10
			})
			.then(res => {
				console.log(res);
				// this.orderList = res.data.data;
			})
		}
	}
</script>

<style>
	page {
		background-color: #f6f6f6;
	}
</style>

<style lang="scss" scoped>
	.list {
		.order {
			width: calc(100% - 40upx);
			box-sizing: border-box;
			padding: 20upx 30upx;
			background-color: #FFFFFF;
			margin: 20upx;
			border-radius: 30upx;
			&-nav {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding-bottom: 20upx;
				.shop-info {
					display: flex;
					justify-content: center;
					align-items: center;
					height: 60upx;
					line-height: 60upx;
					.shop-img {
						width: 50upx;
						height: 50upx;
						margin-right: 14upx;
					}
				}
				.order-status {
					.yellow {
						color: $yellow;
					}
					.blue {
						color: #3498db;
					}
					.orange {
						color: #e67e22;
					}
					.green {
						color: #2ecc71;
					}
				}
			}
			.good {
				width: 100%;
				padding: 10upx 0;
				display: flex;
				justify-content: space-between;
				align-items: center;
				&-bottom {
					display: flex;
					flex-direction: column;
					.good-price {
						color: #e67e22;
					}
					.good-icon {
						font-size: 20upx;
					}
				}
				&-info {
					width: calc(100% - 120upx);
					display: flex;
					justify-content: space-between;
					align-items: center;
				}
			}
			.total {
				padding: 20upx;
				text-align: right;
				color: #555;
				& > text {
					font-size: 34upx;
					letter-spacing: 2upx;
					font-weight: bold;
				}
			}
			.opration {
				display: flex;
				justify-content: flex-end;
				align-items: center;
				.op-btn {
					width: 160upx;
				}
				.minus {
					::v-deep button {
						background-color: $yellow;
						color: #fff;
						border-radius: 20upx;
					}
					margin-right: 20upx;
				}
				.main {
					::v-deep button {
						background-color: #3498db;
						color: #fff;
						border-radius: 20upx;
					}
					
				}
			}
		}
	}
</style>
