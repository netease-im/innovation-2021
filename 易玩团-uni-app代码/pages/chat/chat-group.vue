<template>
	<view class="chat">
		<!-- 聊天页头 -->
		<view class="chat-nav">
			<view class="back-btn">
				<text class="iconfont icon-shangyiye back-btn-icon" @click="navigateBack"></text>
			</view>
			<view class="chat-main-title">
				<view class="chat-avatar">
					<image class="chat-small-avatar" :src="avatar.img" v-for="(avatar, index) in memberAvatar.slice(0,3)" mode="cover" :key="index"></image>
					<view class="chat-small-avatar" v-if="user.memberCount > 2">+{{ user.memberCount - 3 }}</view>
				</view>
				<view class="flex-column-center" v-if="user.name">
					<text class="chat-title">{{ user.name }}</text>
					<text class="chat-member-count">{{ user.memberCount }}位参与者</text>
				</view>
			</view>
			<!-- 标签页 -->
			<view class="tab-switcher">
				<view class="tab" :class="swiperIndex == index ? 'tab-active' : ''" v-for="(tab,index) in tabList" :key="index" @click="swiperIndex = index">
					{{ tab }}
				</view>
				<view class="bottom-line" :style="'left:'+offsetX"></view>
			</view>
		</view>
		<swiper class="chat-swiper" :current="swiperIndex" @change="swiperChange">
			<swiper-item>
				<!-- 消息列表 -->
				<scroll-view :scroll-with-animation="isAni" :scroll-into-view="scrollId" :scroll-top="scrollData" :scroll-y="true" class="chat-list" v-if="chatList.length > 0">
					<view :class="line.from == myId ? 'chat-cell-me' : 'chat-cell-other'" :id="'scroll-'+line.time" v-for="(line, index) in chatList" :key="line.id">
						<!-- 自己的消息 -->
						<view class="cell-me" v-if="line.from == myId">
							<view class="cell-pop-me">
								<!-- 用户名 -->
								<view class="cell-user">{{ me.nickName }}</view>
								<!-- 消息内容 -->
								<view class="cell-content">{{ line.text == '' ? '欢迎加入我的易玩团！' : line.text }}</view>
							</view>
							<!-- 用户头像 -->
							<view class="cell-img-container">
								<image class="cell-img" :src="me.avatarUrl" mode="aspectFill"></image>
							</view>
						</view>
						<!-- 别人的消息 -->
						<view class="cell-other" v-if="line.from != myId">
							<!-- 用户头像 -->
							<view class="cell-img-container"><image class="cell-img" :src="memberInfo[line.from].img" mode="aspectFill"></image></view>
							<view class="cell-pop-other">
								<!-- 用户名 -->
								<view class="cell-user">{{ memberInfo[line.from].name }}</view>
								<!-- 消息内容 -->
								<view class="cell-content">{{ line.text == '' ? '欢迎加入我的易玩团！' : line.text }}</view>
							</view>
						</view>
					</view>
				</scroll-view>
				<!-- 底部操作栏 -->
				<view class="chat-bottom" :style="'bottom:' + offsetY + 'px'">
					<u-input v-model="sendText" cursor-spacing="8" type="textarea" :fixed="true" :height="50" class="textArea" :auto-height="true" />
					<view class="send-btn k-active" @click="send">发送</view>
				</view>
			</swiper-item>
			<swiper-item>
				<view class="swiper-item">
					<k-detail v-if="issueId" :issueId="issueId"></k-detail>
				</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
import kDetail from '../../components/k-detail/k-detail.vue';
export default {
	components: {
		kDetail
	},
	data() {
		return {
			teamId: '',
			issueId: '',
			offsetX: '0',
			swiperIndex: 0,
			baseUrl: 'https://zmetalhearty.com',
			offsetY: 0,
			userId: 1,
			scrollData: 9999,
			sendText: '',
			user: {
				memberCount: '',
				name: ''
			},
			memberInfo: {},
			memberAvatar: [],
			isAni: false,
			myId: uni.getStorageSync('userid'),
			me: uni.getStorageSync('userInfo'),
			chatList: [],
			scrollId: '',
			tabList: ['消息','详情']
		};
	},
	watch: {
		swiperIndex(newVal) {
			if(newVal == 0) {
				this.offsetX = '0';
			}else if(newVal == 1) {
				this.offsetX = '50%'
			}
		}
	},
	computed: {
		getAvatar(id) {
			return function(id) {
				console.log('id='+id);
				this.memberAvatar.forEach((item,index) => {
					if(item.id == id) {
						console.log('get avatar!!!!!')
						console.log(item.img)
						return item.img
					}
				})
			}
		},
		getName(id) {
			return function(id) {
				this.memberAvatar.forEach((item,index) => {
					if(item.id == id) {
						return item.name
					}
				})
			}
		}
	},
	methods: {
		swiperChange(e) {
			this.swiperIndex = e.detail.current;
		},
		getHistory() {
			this.yx.getHistoryMsgs({
				scene: 'team',
				asc: true,
				to: this.teamId,
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
				scene: 'team',
				to: this.teamId,
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
		getTeamInfo(id) {
			this.yx.getTeamMembers({
				teamId: id,
				done: (error, memeber) => {
					console.log(memeber);
					memeber.members.map((current, memberIndex) => {
						this.yx.getTeam({
							teamId: id,
							done: (error, team) => {
								this.user = {
									memberCount: memeber.members.length - 1,
									name: team.name
								}
							}
						})
						if(current.account != '100000') {
							this.request('frontUser/getDetails', 'POST', {
								fuId: Number(current.account)
							}).then(res => {
								this.memberAvatar.push({
									img: this.baseUrl + res.data.data.fuImg,
									name: res.data.data.fuNickname,
									id: res.data.data.fuId
								});
								this.memberInfo[current.account] = {
									img: this.baseUrl + res.data.data.fuImg,
									name: res.data.data.fuNickname,
									id: res.data.data.fuId
								}
							});
						}
					})
				}
			});
			this.getYuanPinId();
			this.getHistory();
		},
		pushMsg(msg) {
			if(msg.target == this.teamId) {
				this.chatList.push(msg);
				this.scrollId = 'scroll-'+msg.time;
				console.log(this.scrollId)
				this.isAni = true
			}
		},
		getYuanPinId() {
			this.request('issue/listjson','POST',{
				fromFuId: uni.getStorageSync('userid'),
				page: 1,
				limit: 1,
				iAudio: this.teamId
			})
			.then(res => {
				console.log(res);
				this.issueId = res.data.data[0].iId;
			})
		}
	},
	onLoad(e) {
		console.log(e)
		if (e.id) {
			this.teamId = e.id;
			this.getTeamInfo(e.id);
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
		padding: 80upx 30upx 0 30upx;
		font-size: 50upx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		box-shadow: 0 6upx 20upx -6upx $grey;
		background-color: #fff;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 99;
		position: relative;
		.back-btn {
			position: absolute;
			left: 50upx;
			top: 50%;
			transform: translateY(-50%);
			background-color: #F5F5F5;
			border-radius: 50%;
			width: 70upx;
			height: 70upx;
			@include flex-row-center;
			&-icon {
				font-size: 36upx;
				color: $light;
			}
		}
		.tab-switcher {
			height: 100upx;
			width: 100%;
			position: relative;
			@include flex-row-center;
			.tab {
				width: 50%;
				height: 100%;
				line-height: 100upx;
				text-align: center;
				font-size: 30upx;
				color: $minus-text;
				@include transition;
			}
			.tab-active {
				color: $primary;
			}
			.bottom-line {
				height: 4upx;
				background-color: $primary;
				width: 50%;
				position: absolute;
				left: 0;
				bottom: 0;
				@include transition;
			}
		}
	}
	&-main-title {
		height: 180upx;
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		align-items: center;
		font-size: 28upx;
		color: $main-text;
		.chat-title {
			font-weight: bold;
			font-size: 32upx;
		}
		.chat-member-count {
			font-size: 24upx;
			color: $minus-text;
		}
	}
	&-avatar {
		width: 80upx;
		height: 80upx;
		border-radius: 20upx;
		@include flex-row-center;
		flex-wrap: wrap;
		background-color: #eee;
		.chat-small-avatar {
			width: 30upx;
			height: 30upx;
			margin: 5upx;
			border-radius: 6upx;
		}
	}
	&-swiper {
		width: 100%;
		height: calc(100vh - 370upx);
	}
	// 聊天列表
	&-list {
		position: absolute;
		width: calc(100% - 40upx);
		padding: 0 20upx 0 20upx;
		top: 0;
		height: calc(100vh - 520upx);
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
