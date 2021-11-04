<template>
	<view class="chat">
		<view class="operationContainer">
			<view class="operation k-active" v-for="(op, index) in operationList" :key="index" @click="go(op.route)">
				<view class="iconContainer" :class="op.class"><text class="iconfont" :class="op.icon"></text></view>
				<text>{{ op.type }}</text>
			</view>
		</view>
		<!-- 消息列表 -->
		<text class="k-small-title">消息列表</text>
		<view class="empty" v-if="list.length == 0">
			<image class="empty-img" :src="emptyUrl" mode=""></image>
			<text class="empty-text">暂无内容</text>
		</view>
		<u-swipe-action class="chat-cell" :show="chat.show" :index="index" v-for="(chat, index) in list" :key="chat.id" @click="click" @open="open" :options="options">
			<view class="chat-container k-active" @click="goChat(chat)">
				<image class="chat-img" v-if="chat.scene == 'p2p'" :src="chat.updateTime.img" mode=""></image>
				<view class="chat-img chat-avatar-container" v-if="chat.scene == 'team'">
					<image class="chat-avatar" :src="avatar.img" v-for="(avatar, index) in chat.updateTime.memberAvatar" mode="cover" :key="index"></image>
					<view class="chat-avatar" v-if="chat.updateTime.memberCount > -1">+{{ chat.updateTime.memberCount }}</view>
				</view>
				<view class="chat-content">
					<p class="chat-name">{{ chat.updateTime.name || 'unknown' }}</p>
					<p class="chat-message k-hideText">{{ chat.lastMsg.text }}</p>
				</view>
				<view class="chat-info">
					<p class="chat-time">{{ chat.lastMsg.time | timeFrom }}</p>
					<view class="chat-unread" :style="'opacity:' + (chat.unread != 0 ? '1' : '0')">{{ chat.unread }}</view>
				</view>
			</view>
		</u-swipe-action>
	</view>
</template>

<script>
export default {
	data() {
		return {
			emptyUrl: getApp().globalData.messageEmptyImg,
			showLoading: false,
			lastSessionId: 0,
			operationList: [
				{
					url: '/static/icon/like_white.png',
					class: 'like',
					icon: 'icon-dianzan1',
					type: '点赞',
					route: '../notifacation/like'
				},
				{
					url: '/static/icon/message_white.png',
					class: 'message',
					icon: 'icon-taolunqu',
					type: '回复',
					route: '../notifacation/comment'
				},
				{
					url: '/static/icon/friends_white.png',
					class: 'friends',
					icon: 'icon-Profile1',
					type: '粉丝',
					route: '../notifacation/focus'
				},
				{
					url: '/static/icon/friends_white.png',
					class: 'audit',
					icon: 'icon-icon_xinxixiugai',
					type: '审核',
					route: '../notifacation/verify'
				}
			],
			list: getApp().globalData.list,
			disabled: false,
			btnWidth: 180,
			show: false,
			options: [
				// {
				// 	text: '置顶',
				// 	style: {
				// 		backgroundColor: '#007aff'
				// 	}
				// },
				{
					text: '删除',
					style: {
						backgroundColor: '#dd524d'
					}
				}
			]
		};
	},
	watch: {},
	computed: {},
	onLoad() {
		this.list = this.listFormat(getApp().globalData.list);
		getApp().watch(this.getList, 'list');
	},
	onShow() {},
	methods: {
		open(index) {
			console.log(index)
			this.list[index].show = true;
			console.log(this.list[index].show)
			this.list.map((val, idx) => {
				if (index != idx) this.list[idx].show = false;
			});
		},
		go(url) {
			console.log(url);
			uni.navigateTo({
				url
			});
		},
		goChat(chat) {
			console.log(chat);
			if (chat.scene == 'p2p') {
				uni.navigateTo({
					url: './chat-single?id=' + chat.to
				});
			} else if (chat.scene == 'team') {
				uni.navigateTo({
					url: './chat-group?id=' + chat.to
				});
			}
		},
		getList(list) {
			this.list = this.listFormat(list);
			console.log(this.list);
		},
		listFormat(list) {
			var baseUrl = 'https://zmetalhearty.com';
			list.forEach(item => {
				item.show = false;
				if (item.scene == 'p2p') {
					this.request('frontUser/getDetails', 'POST', {
						fuId: item.to
					}).then(res => {
						item.updateTime = {
							img: baseUrl + res.data.data.fuImg,
							name: res.data.data.fuNickname
						};
					});
				} else if (item.scene == 'team') {
					this.yx.getTeamMembers({
						teamId: item.to,
						done: (error, memeber) => {
							console.log(memeber);
							this.yx.getTeam({
								teamId: item.to,
								done: (error, team) => {
									item.updateTime = {
										memberCount: memeber.members.length - 4,
										name: team.name,
										memberAvatar: []
									};
									memeber.members.map((current, memberIndex) => {
										if (current.account != '100000') {
											this.request('frontUser/getDetails', 'POST', {
												fuId: Number(current.account)
											}).then(res => {
												if (memberIndex < 4) {
													item.updateTime.memberAvatar.push({
														img: baseUrl + res.data.data.fuImg
													});
												}
											});
										}
									});
								}
							});
						}
					});
				}
			});
			this.list.push();
			return list;
		},
		click(index, index1) {
			console.log(index1);
			if (index1 == 0) {
				this.yx.deleteSession({
					scene: this.list[index].scene,
					to: this.list[index].to,
					done: (error, obj) => {
						console.log(obj);
						console.log('删除服务器上的会话' + (!error ? '成功' : '失败'));
						this.list.splice(index, 1);
						// this.list.forEach(item => {
						// 	item.show = false;
						// })
					}
				});
			}
		},
		// 如果打开一个的时候，不需要关闭其他，则无需实现本方法
		
	}
};
</script>

<style lang="scss">
.operationContainer {
	width: calc(100% - 40upx);
	padding: 40upx 20upx;
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin: 0 auto;
	.operation {
		height: 170upx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		text {
			color: $minus-text;
			letter-spacing: 4upx;
			font-size: 28upx;
		}
	}
	.iconContainer {
		width: 120upx;
		height: 120upx;
		border-radius: 50upx;
		text-align: center;
		line-height: 120upx;
	}
	.like {
		background-color: $food-light;
		box-shadow: 0upx 4upx 14upx 0upx $food-light;
		text {
			color: $food;
			font-size: 60upx;
		}
	}
	.message {
		background-color: $learn-light;
		box-shadow: 0upx 4upx 14upx 0upx $learn-light;
		text {
			color: $learn;
			font-size: 60upx;
		}
	}
	.friends {
		background-color: $travel-light;
		box-shadow: 0upx 4upx 14upx 0upx $travel-light;
		text {
			color: $travel;
			font-size: 60upx;
		}
	}
	.audit {
		background-color: $fun-light;
		box-shadow: 0upx 4upx 14upx 0upx $fun-light;
		text {
			color: $fun;
			font-size: 60upx;
		}
	}
	.iconContainer image {
		width: 55upx;
		height: 55upx;
	}
}
// 消息列表
.chat-cell {
	margin-top: 30upx;
}
.chat-container {
	display: flex;
	justify-content: space-between;
	padding: 20upx 50upx;
	line-height: 60upx;
	.chat-img {
		width: 120upx;
		height: 120upx;
		background-color: $grey;
		border-radius: 30upx;
		margin: 0;
	}
	.chat-avatar-container {
		@include flex-row-center;
		flex-wrap: wrap;
	}
	.chat-avatar {
		width: 50upx;
		height: 50upx;
		border-radius: 10upx;
		margin: 5upx;
		background-color: rgba($color: $primary, $alpha: 0.3);
		color: $primary;
		font-size: 20upx;
		@include flex-row-center;
	}
	.chat-content {
		width: calc(100% - 260upx);
		text-align: left;
		.chat-name {
			font-size: 32upx;
			color: $main-text;
		}
		.chat-message {
			width: 400upx;
			font-size: 24upx;
			color: $minus-text;
		}
	}
	.chat-info {
		width: 100upx;
		text-align: right;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: flex-end;
		.chat-time {
			font-size: 24upx;
			color: $minus-text;
		}
		.chat-unread {
			width: 50upx;
			height: 50upx;
			border-radius: 20upx;
			background-color: rgba($color: $unread, $alpha: 0.3);
			color: $unread;
			text-align: center;
			line-height: 50upx;
			margin-top: 0;
		}
	}
}
</style>
