<template>
	<view class="chat">
		<!-- 聊天页头 -->
		<view class="chat-nav">
			<text class="iconfont icon-shangyiye back-btn" @click="navigateBack"></text>
			<view class="chat-main-title">
				<image v-if="user.fuImg" :src="baseUrl + user.fuImg" class="chat-avatar" mode="aspectFill"></image>
				<text v-if="user.fuNickname">{{ user.fuNickname }}</text>
			</view>
			<text class="iconfont icon-shangyiye" style="color: #fff;"></text>
		</view>
		<!-- 消息列表 -->
		<scroll-view :scroll-with-animation="isAni" :scroll-into-view="scrollId" :scroll-top="scrollData" :scroll-y="true" class="chat-list" v-if="chatList.length > 0">
			<view :class="line.from != user.fuId ? 'chat-cell-me' : 'chat-cell-other'" :id="'scroll-'+line.time" v-for="(line, index) in chatList" :key="line.id">
				<!-- 自己的消息 -->
				<view class="cell-me" v-if="line.from != user.fuId">
					<view class="cell-pop-me">
						<!-- 用户名 -->
						<view class="cell-user">{{ me.nickName }}</view>
						<!-- 消息内容 -->
						<view class="cell-content">{{ line.text }}</view>
					</view>
					<!-- 用户头像 -->
					<view class="cell-img-container">
						<image class="cell-img" :src="me.avatarUrl" mode="aspectFill"></image>
					</view>
				</view>
				<!-- 别人的消息 -->
				<view class="cell-other" v-if="line.from == user.fuId">
					<!-- 用户头像 -->
					<view class="cell-img-container"><image class="cell-img" :src="baseUrl + user.fuImg" mode="aspectFill"></image></view>
					<view class="cell-pop-other">
						<!-- 用户名 -->
						<view class="cell-user">{{ user.fuNickname }}</view>
						<!-- 消息内容 -->
						<view class="cell-content">{{ line.text }}</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<!-- 底部操作栏 -->
		<view class="chat-bottom" :style="'bottom:' + offsetY + 'px'">
			<u-input v-model="sendText" cursor-spacing="8" type="textarea" :fixed="true" :height="50" class="textArea" :auto-height="true" />
			<view class="send-btn k-active" @click="send">发送</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			baseUrl: 'https://zmetalhearty.com',
			offsetY: 0,
			userId: 1,
			scrollData: 9999,
			sendText: '',
			user: {},
			isAni: false,
			myId: uni.getStorageSync('userid'),
			me: uni.getStorageSync('userInfo'),
			chatList: [],
			scrollId: ''
		};
	},
	methods: {
		getHistory() {
			this.yx.getHistoryMsgs({
				scene: 'p2p',
				asc: true,
				to: this.user.fuId,
				done: (err, res) => {
					console.log(res);
					this.chatList = res.msgs;
				}
			});
		},
		navigateBack() {
			uni.navigateBack({
				delta: 1
			});
		},
		send() {
			var msg = this.yx.sendText({
				scene: 'p2p',
				to: this.user.fuId,
				text: this.sendText,
				done: (error, msg) => {
					console.log(error);
					console.log(msg);
					this.sendText = '';
					this.chatList.push(msg);
					this.scrollId = 'scroll-'+msg.time
					console.log(this.scrollId)
					this.isAni = true
				}
			});
		},
		getUserInfo(id) {
			this.request('frontUser/getUserInfo', 'POST', {
				fuId: id,
				nFuId: uni.getStorageSync('userid')
			}).then(res => {
				console.log(res);
				this.user = res.data;
				this.getHistory();
			});
		},
		pushMsg(msg) {
			if(msg.from == this.user.fuId) {
				this.chatList.push(msg);
				this.scrollId = 'scroll-'+msg.time;
				console.log(this.scrollId)
				this.isAni = true
			}
		}
	},
	onLoad(e) {
		console.log(e)
		if (e.id) {
			this.getUserInfo(e.id);
		}
		getApp().watch(this.pushMsg, 'currentMsg');
	},
	onShow() {
		uni.onKeyboardHeightChange(res => {
			console.log(res);
			this.offsetY = res.height;
		});
	},
	onUnload() {
		if(this.chatList.length > 0) {
			console.log(this.chatList[0].sessionId)
			this.yx.resetSessionUnread(this.chatList[0].sessionId)
		}
		uni.offKeyboardHeightChange(() => {
			this.offsetY = 0;
		});
	}
};
</script>

<style>
page {
	background-color: #f5f5f5;
}
</style>

<style lang="scss">
.chat {
	&-nav {
		width: 100%;
		padding: 80upx 30upx 30upx 30upx;
		font-size: 50upx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 6upx 20upx -6upx $grey;
		background-color: #fff;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 99;
		.back-btn {
			font-size: 50upx;
			color: $light;
		}
	}
	&-main-title {
		height: 130upx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		font-size: 28upx;
		color: $main-text;
	}
	&-avatar {
		width: 80upx;
		height: 80upx;
		border-radius: 50%;
	}
	// 聊天列表
	&-list {
		position: absolute;
		width: calc(100% - 40upx);
		padding: 240upx 20upx 160upx 20upx;
		top: 0;
		height: calc(100vh - 400upx);
	}
	&-cell-me {
		justify-content: flex-end;
	}
	&-cell-other {
		justify-content: flex-start;
	}
	&-cell-me,
	&-cell-other {
		display: flex;
		align-items: center;
		.cell-me,
		.cell-other {
			width: calc(100% - 100upx);
			display: flex;

			align-items: flex-start;
			padding-top: 14upx;
		}
		.cell-me {
			justify-content: flex-end;
		}
		.cell-other {
			justify-content: flex-start;
		}
		.cell-pop-other {
			text-align: left;
			margin-left: 20upx;
			.cell-content {
				border-radius: 0 20upx 20upx 20upx;
				background-color: #fff;
				color: $main-text;
			}
		}
		.cell-pop-me {
			text-align: right;
			margin-right: 20upx;
			.cell-content {
				border-radius: 20upx 0 20upx 20upx;
				background-color: $primary;
				color: #fff;
			}
		}
		.cell-pop-me,
		.cell-pop-other {
			max-width: calc(100% - 100upx);
			.cell-user {
				font-size: 26upx;
				color: $minus-text;
				line-height: 50upx;
			}
			.cell-content {
				padding: 20upx;
			}
		}
		.cell-img-container {
			width: 80upx;
			height: 80upx;
		}
		.cell-img {
			width: 80upx;
			height: 80upx;
			border-radius: 30upx;
		}
	}
	.chat-bottom {
		position: fixed;
		bottom: 0;
		left: 0;
		z-index: 99;
		width: 100%;
		padding: 20upx 30upx 50upx 30upx;
		background-color: #fff;
		display: flex;
		justify-content: space-between;
		align-items: center;
		.textArea {
			max-height: 200upx;
			padding: 10upx;
			width: calc(100% - 200upx);
			background-color: $light-grey;
			border-radius: 20upx;
			line-height: 70upx !important;
			overflow-y: scroll;
			::v-deep textarea {
				padding: 0;
			}
		}
		.send-btn {
			width: 140upx;
			height: 80upx;
			background-color: $primary;
			box-shadow: 0 8upx 20upx -8upx $primary;
			color: #fff;
			text-align: center;
			line-height: 80upx;
			border-radius: 30upx;
		}
	}
}
</style>
