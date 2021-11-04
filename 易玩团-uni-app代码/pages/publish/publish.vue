<template>
	<view class="post" :class="theme">
		<text class="k-title">{{ title }}</text>
		<view class="post-info">
			<!-- 标题 -->
			<view class="post-input" :class="activeIndex == 1 ? 'active' : ''">
				<text class="cell-title">标题</text>
				<input class="cell-input" @focus="goActive(1)" placeholder-class="clickNote" @blur="cancelActive()" placeholder="起一个好听的标题吧" type="text" v-model="form.title" />
			</view>
			<!-- 人数 -->
			<view class="post-cell" :class="activeIndex == 2 ? 'active' : ''" @click="showCount">
				<text class="cell-title">人数</text>
				<view class="cell-content">
					<text v-if="form.peopleCount != ''">{{ form.peopleCount }} 人</text>
					<text v-else class="clickNote">点击选择</text>
				</view>
			</view>
			<!-- 地址/始发地 -->
			<view class="post-cell" :class="activeIndex == 3 ? 'active' : ''" @click="getLocation('start')">
				<text class="cell-title">
					<text v-if="currentType == 2">始发地</text>
					<text v-else>地址</text>
				</text>
				<view class="cell-content k-hideText">
					<text v-if="form.startLocationName != ''">{{ form.startLocationName.address + form.startLocationName.name }}</text>
					<text v-else class="clickNote">点击选择</text>
				</view>
				<text class="iconfont icon-Location1"></text>
			</view>
			<!-- 目的地 -->
			<view class="post-cell" v-if="currentType == 2" :class="activeIndex == 9 ? 'active' : ''" @click="getLocation('final')">
				<text class="cell-title">目的地</text>
				<view class="cell-content k-hideText">
					<text v-if="form.finalLocationName != ''">{{ form.finalLocationName.address + form.finalLocationName.name }}</text>
					<text v-else class="clickNote">点击选择</text>
				</view>
				<text class="iconfont icon-Location1"></text>
			</view>
			<!-- 开始时间 -->
			<view class="post-short">
				<view
					class="post-cell left"
					:class="activeIndex == 4 ? 'active' : ''"
					@click="
						showStartDateSelector = true;
						goActive(4);
					"
				>
					<text class="cell-title">开始日期</text>
					<view class="cell-content">
						<text v-if="form.startDate != ''">{{ form.startDate }}</text>
						<text v-else class="clickNote">点击选择</text>
					</view>
					<text class="iconfont icon-Calendar"></text>
				</view>
				<view
					class="post-cell right"
					:class="activeIndex == 5 ? 'active' : ''"
					@click="
						showStartTimeSelector = true;
						goActive(5);
					"
				>
					<text class="cell-title">开始时间</text>
					<view class="cell-content">
						<text v-if="form.startTime != ''">{{ form.startTime }}</text>
						<text v-else class="clickNote">点击选择</text>
					</view>
					<text class="iconfont icon-jilu"></text>
				</view>
			</view>
			<!-- 结束时间 -->
			<view class="post-short">
				<view
					class="post-cell left"
					:class="activeIndex == 6 ? 'active' : ''"
					@click="
						showEndDateSelector = true;
						goActive(6);
					"
				>
					<text class="cell-title">结束日期</text>
					<view class="cell-content">
						<text v-if="form.endDate != ''">{{ form.endDate }}</text>
						<text v-else class="clickNote">点击选择</text>
					</view>
					<text class="iconfont icon-Calendar"></text>
				</view>
				<view
					class="post-cell right"
					:class="activeIndex == 7 ? 'active' : ''"
					@click="
						showEndTimeSelector = true;
						goActive(7);
					"
				>
					<text class="cell-title">结束时间</text>
					<view class="cell-content">
						<text v-if="form.endTime != ''">{{ form.endTime }}</text>
						<text v-else class="clickNote">点击选择</text>
					</view>
					<text class="iconfont icon-jilu"></text>
				</view>
			</view>
			<!-- 定制内容 -->
			<view class="post-input" :class="activeIndex == 10 ? 'active' : ''">
				<text class="cell-title">
					<text v-if="currentType == 1">学习内容</text>
					<text v-if="currentType == 2">出行方式</text>
					<text v-if="currentType == 3">美食类型</text>
					<text v-if="currentType == 4">娱乐方式</text>
				</text>
				<input class="cell-input" @focus="goActive(10)" placeholder-class="clickNote" @blur="cancelActive()" placeholder="简要说明一下" type="text" v-model="form.main" />
			</view>
			<!-- 描述 -->
			<view class="post-input textarea" :class="activeIndex == 8 ? 'active' : ''">
				<text class="cell-title">描述</text>
				<u-input
					class="cell-input"
					type="textarea"
					:auto-height="true"
					@focus="goActive(8)"
					@blur="cancelActive()"
					placeholder="简单描述一下你的易玩团吧"
					v-model="form.detail"
					:custom-style="textAreaStyle"
				/>
				<u-upload ref="uUpload" :action="uploadUrl" width="176" height="176" :file-list="fileList"></u-upload>
			</view>
			<view class="submit-btn k-hover k-active" @click="submit">
				<text v-if="!isPublish">发布易玩团</text>
				<u-loading v-else mode="circle" color="#fff"></u-loading>
			</view>
		</view>

		<u-toast ref="uToast" />

		<u-picker v-model="showCountSelector" mode="selector" @cancel="cancelActive()" @confirm="getCount" :default-selector="[0]" :range="countRange"></u-picker>

		<u-picker v-model="showStartDateSelector" mode="time" @cancel="cancelActive()" @confirm="time => getTime(time, 'startDate')" :params="dateParams"></u-picker>
		<u-picker v-model="showStartTimeSelector" mode="time" @cancel="cancelActive()" @confirm="time => getTime(time, 'startTime')" :params="timeParams"></u-picker>
		<u-picker v-model="showEndDateSelector" mode="time" @cancel="cancelActive()" @confirm="time => getTime(time, 'endDate')" :params="dateParams"></u-picker>
		<u-picker v-model="showEndTimeSelector" mode="time" @cancel="cancelActive()" @confirm="time => getTime(time, 'endTime')" :params="timeParams"></u-picker>
	</view>
</template>

<script>
export default {
	data() {
		return {
			title: '拼学习',
			theme: 'learn',
			activeIndex: -1,
			isPublish: false,
			form: {
				title: '',
				peopleCount: '',
				startLocationName: '',
				finalLocationName: '',
				startLocation: {},
				finalLocation: {},
				startDate: '',
				startTime: '',
				endDate: '',
				endTime: '',
				main: '',
				detail: ''
			},
			uploadUrl: 'https://zmetalhearty.com/yuanpin/WeChat/file/uploadImg',
			currentType: 1,
			locationInfo: {},
			showCountSelector: false,
			showStartDateSelector: false,
			showStartTimeSelector: false,
			showEndDateSelector: false,
			showEndTimeSelector: false,
			countRange: [2, 3, 4, 5, 6, 7, 8],
			dateParams: {
				year: true,
				month: true,
				day: true,
				hour: false,
				minute: false,
				second: false
			},
			timeParams: {
				year: false,
				month: false,
				day: false,
				hour: true,
				minute: true,
				second: false
			},
			textAreaStyle: {
				lineHeight: '50rpx',
				minHeight: '250rpx'
			},
			action: 'http://www.example.com/upload',
			fileList: []
		};
	},
	onLoad(data) {
		console.log(data);
		if (data.type) {
			this.currentType = data.type;
			this.changeTheme(Number(data.type));
		}
		uni.getLocation({
			type: 'gcj02',
			geocode: true,
			success: res => {
				console.log(res);
				
				this.locationInfo = {
					location: {
						latitude: res.latitude,
						longitude: res.longitude
					}
				};
			}
		});
	},
	methods: {
		createTeam() {
			this.yx.createTeam({
			  type: 'normal',
			  name: this.form.title,
			  avatar: 'avatar',
			  accounts: [String(uni.getStorageSync('userid')),'100000'],
			  level: 50,
			  ps: this.form.detail,
				joinMode: 'noVerify',
				beInviteMode: 'noVerify',
				inviteMode: 'all',
			  done: (err,obj) => {
					console.log(obj);
					this.submitData(obj.team.teamId)
				}
			});
		},
		showToast(title, type) {
			this.$refs.uToast.show({
				title,
				type
			});
		},
		timeFormat(formTime) {
			var newTime = new Date(formTime);
			return Date.parse(newTime);
		},
		submit() {
			var img = '';
			var files = this.$refs.uUpload.lists.filter(val => {
				return val.progress == 100;
			});
			if (files.length != 0) {
				console.log(files);
				img = files[0].response.data + ',';
			}
			if (this.form.title == '') {
				this.showToast('请填写标题', 'warning');
				return;
			}
			if (this.form.peopleCount == '') {
				this.showToast('请选择人数', 'warning');
				return;
			}
			if (this.form.startLocationName == '') {
				if (this.currentType != 2) {
					this.showToast('请选择地址', 'warning');
				} else {
					this.showToast('请选择始发地', 'warning');
				}
				return;
			}
			if (this.form.finalLocationName == '' && this.currentType == 2) {
				this.showToast('请选择目的地', 'warning');
				return;
			}
			if (this.form.startDate == '') {
				this.showToast('请选择开始日期', 'warning');
				return;
			}
			if (this.form.startTime == '') {
				this.showToast('请选择开始时间', 'warning');
				return;
			}
			if (this.form.endDate == '') {
				this.showToast('请选择结束日期', 'warning');
				return;
			}
			if (this.form.endTime == '') {
				this.showToast('请选择结束时间', 'warning');
				return;
			}
			if (this.form.main == '') {
				switch (this.currentType) {
					case 1:
						this.showToast('请填写学习内容', 'warning');
						break;
					case 2:
						this.showToast('请填写出行方式', 'warning');
						break;
					case 3:
						this.showToast('请填写美食类型', 'warning');
						break;
					case 4:
						this.showToast('请填写娱乐方式', 'warning');
						break;
				}
				return;
			}
			if (this.form.detail == '') {
				this.showToast('请填写易玩团描述', 'warning');
				return;
			}
			if (img == '') {
				this.showToast('请上传至少一张图片', 'warning');
				return;
			}
			if(!this.isPublish) {
				this.isPublish = true;
				this.createTeam()
			}
		},
		submitData(teamId) {
			var img = '';
			// var startTime = this.timeFormat(this.form.startDate + ' ' + this.form.startTime + ':00');
			// var endTime =  this.timeFormat(this.form.endDate + ' ' + this.form.endTime + ':00');
			var startTime = this.form.startDate + ' ' + this.form.startTime + ':00';
			var endTime = this.form.endDate + ' ' + this.form.endTime + ':00';
			console.log(startTime);
			var files = this.$refs.uUpload.lists.filter(val => {
				return val.progress == 100;
			});
			if (files.length != 0) {
				img = files[0].response.data + ',';
			}
			if (this.currentType == 2) {
				var submitData = {
					fuId: uni.getStorageSync('userid'),
					iTitle: this.form.title,
					iResume: this.form.main,
					iStatus: 1,
					iDetails: this.form.detail,
					iImg: img,
					iPeopleNumber: Number(this.form.peopleCount),
					iStartTime: startTime,
					iEndTime: endTime,
					iType: this.currentType,
					iSex: 3,
					iAddress: JSON.stringify(this.locationInfo),
					iAudio: teamId,
					iPositionStart: JSON.stringify({
						name: this.form.startLocationName,
						location: this.form.startLocation
					}),
					iPositionEnd: JSON.stringify({
						name: this.form.finalLocationName,
						location: this.form.finalLocation
					})
				};
			} else {
				var submitData = {
					fuId: uni.getStorageSync('userid'),
					iTitle: this.form.title,
					iResume: this.form.main,
					iStatus: 1,
					iDetails: this.form.detail,
					iImg: img,
					iPeopleNumber: Number(this.form.peopleCount),
					iStartTime: startTime,
					iEndTime: endTime,
					iType: this.currentType,
					iSex: 3,
					iAddress: JSON.stringify(this.locationInfo),
					iAudio: teamId,
					iPositionStart: JSON.stringify({
						name: this.form.startLocationName,
						location: this.form.startLocation
					}),
				};
			}
			console.log(submitData);
			this.request('issue/add', 'POST', submitData).then(res => {
				console.log(res);
				if (res.data.status == 'succ') {
					this.showToast('发布成功！', 'success');
					setTimeout(() => {
						uni.navigateBack({
								delta: 1
							});
					},2000);
				}
			});
		},
		goActive(id) {
			this.activeIndex = id;
		},
		cancelActive() {
			this.activeIndex = -1;
		},
		changeTheme(type) {
			switch (type) {
				case 1:
					this.title = '拼学习';
					this.theme = 'learn';
					break;
				case 2:
					this.title = '拼出行';
					this.theme = 'travel';
					break;
				case 3:
					this.title = '拼美食';
					this.theme = 'food';
					break;
				case 4:
					this.title = '拼娱乐';
					this.theme = 'fun';
					break;
			}
		},
		showCount() {
			this.showCountSelector = true;
			this.goActive(2);
		},
		getCount(index) {
			this.form.peopleCount = this.countRange[index];
		},
		showTime() {
			this.showTimeSelector = true;
		},
		getTime(time, type) {
			console.log(time);
			this.cancelActive();
			switch (type) {
				case 'startDate':
					this.form.startDate = time.year + '-' + time.month + '-' + time.day;
					break;
				case 'startTime':
					this.form.startTime = time.hour + ':' + time.minute;
					break;
				case 'endDate':
					this.form.endDate = time.year + '-' + time.month + '-' + time.day;
					break;
				case 'endTime':
					this.form.endTime = time.hour + ':' + time.minute;
					break;
			}
		},
		getLocation(type) {
			if (type == 'start') {
				this.goActive(3);
			} else {
				this.goActive(9);
			}
			uni.chooseLocation({
				geocode: true,
				latitude: this.locationInfo.latitude,
				longitude: this.locationInfo.longitude,
				success: res => {
					console.log(res);
					this.cancelActive();
					if (type == 'start') {
						this.form.startLocationName = {
							address: res.address,
							name: res.name
						};
						this.form.startLocation = {
							latitude: res.latitude,
							longitude: res.longitude
						};
					} else {
						this.form.finalLocationName = {
							address: res.address,
							name: res.name
						};
						this.form.finalLocation = {
							latitude: res.latitude,
							longitude: res.longitude
						};
					}
				}
			});
		}
	}
};
</script>

<style>
page {
	background-color: #fff;
}
</style>
<style lang="scss">
.post {
	padding-bottom: 60upx;
	&-info {
		padding: 0 50upx;
	}
	&-cell,
	&-input {
		transition: all 0.3s ease;
		padding: 0 30upx;
		height: 100upx;
		color: $minus-text;
		border-radius: 20upx;
		margin-top: 90upx;
		border: 4upx solid $grey;
		position: relative;
		color: $dark;
		.cell-title {
			position: absolute;
			top: -60upx;
			font-size: 28upx;
			left: 0;
		}
		.cell-input,
		.cell-content {
			flex: 1;
			line-height: 92upx;
			height: 100%;
			color: $main-text !important;
			.clickNote {
				color: $minus-text;
			}
		}
	}
	.textarea {
		padding: 20upx 30upx;
		height: auto !important;
		letter-spacing: 2upx
		::v-deep .u-add-tips {
			display: none;
		}
		::v-deep .u-icon__icon {
			color: #fff;
		}
	}
	&-short {
		display: flex;
		justify-content: space-between;
		align-items: center;
		.left {
			width: 60%;
		}
		.right {
			width: 35%;
		}
	}
	&-cell {
		display: flex;
		justify-content: center;
		align-items: center;
		.cell-text {
			padding-right: 20upx;
		}
	}
	.submit-btn {
		width: 100%;
		height: 100upx;
		border-radius: 20upx;
		color: #fff;
		background-color: $light;
		text-align: center;
		line-height: 100upx;
		margin-top: 50upx;
	}
}

.learn {
	.k-title::after {
		background-color: $learn-light;
	}
	.active {
		border: 4upx solid $learn;
		color: $learn;
	}
	.submit-btn {
		background-color: $learn;
		box-shadow: 0 14upx 24upx -6upx $learn;
	}
}

.travel {
	.k-title::after {
		background-color: $travel-light;
	}
	.active {
		border: 4upx solid $travel;
		color: $travel;
	}
	.submit-btn {
		background-color: $travel;
		box-shadow: 0 14upx 24upx -6upx $travel;
	}
}

.food {
	.k-title::after {
		background-color: $food-light;
	}
	.active {
		border: 4upx solid $food;
		color: $food;
	}
	.submit-btn {
		background-color: $food;
		box-shadow: 0 14upx 24upx -6upx $food;
	}
}

.fun {
	.k-title::after {
		background-color: $fun-light;
	}
	.active {
		border: 4upx solid $fun;
		color: $fun;
	}
	.submit-btn {
		background-color: $fun;
		box-shadow: 0 14upx 24upx -6upx $fun;
	}
}
</style>
