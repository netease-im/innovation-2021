import NIM from './NIM_SDK.js';
import Vue from 'vue';
const appKey = "aabe2bca19bf374ff36bf25834cb5f95";

export var data = {};
var yx = null;
export function initYunXin() {
	if (uni.getStorageSync('yunXinInfo')) {
		yx = NIM.getInstance({
			debug: false,
			appKey,
			account: uni.getStorageSync('yunXinInfo').accid, //后端给的在网易云获取的accid
			token: uni.getStorageSync('yunXinInfo').token, //后端给的在网易云获取的token
			db: false, //若不要开启数据库请设置false。SDK默认为true。
			isRoamingable: true,
			isHistoryable: true,
			isSyncable: true,
			onconnect: function() {
				console.log('连接成功');
			},
			onwillreconnect: function(obj) {
				// 此时说明 SDK 已经断开连接, 请开发者在界面上提示用户连接已断开, 而且正在重新建立连接
				console.log('即将重连');
				console.log(obj.retryCount);
				console.log(obj.duration);
			},
			ondisconnect: onDisconnect,
			onerror: function(error) {
				console.log(error);
			},
			// 多端登录
			onloginportschange: onLoginPortsChange,
			// 用户关系
			onblacklist: onBlacklist,
			onsyncmarkinblacklist: onMarkInBlacklist,
			onmutelist: onMutelist,
			onsyncmarkinmutelist: onMarkInMutelist,
			// 好友关系
			onfriends: onFriends,
			onsyncfriendaction: onSyncFriendAction,
			// 用户名片
			onmyinfo: onMyInfo,
			onupdatemyinfo: onUpdateMyInfo,
			onusers: onUsers,
			onupdateuser: onUpdateUser,
			// 群组
			onteams: onTeams,
			onsynccreateteam: onCreateTeam,
			onUpdateTeam: onUpdateTeam,
			onteammembers: onTeamMembers,
			// onsyncteammembersdone: onSyncTeamMembersDone,
			onupdateteammember: onUpdateTeamMember,
			// 群消息业务已读通知
			onTeamMsgReceipt: onTeamMsgReceipt,
			// 会话
			onsessions: onSessions,
			onupdatesession: onUpdateSession,
			// 消息
			onroamingmsgs: onRoamingMsgs,
			onofflinemsgs: onOfflineMsgs,
			onmsg: onMsg,
			// 系统通知
			onofflinesysmsgs: onOfflineSysMsgs,
			onsysmsg: onSysMsg,
			onupdatesysmsg: onUpdateSysMsg,
			onsysmsgunread: onSysMsgUnread,
			onupdatesysmsgunread: onUpdateSysMsgUnread,
			onofflinecustomsysmsgs: onOfflineCustomSysMsgs,
			oncustomsysmsg: onCustomSysMsg,
			// 收到广播消息
			onbroadcastmsg: onBroadcastMsg,
			onbroadcastmsgs: onBroadcastMsgs,
			// 同步完成
			onsyncdone: onSyncDone
		});
		Vue.prototype.yx = yx;
	} else {
		console.log('no yunXin info!!')
	}
}

function onDisconnect(error) {
	// 此时说明 SDK 处于断开状态, 开发者此时应该根据错误码提示相应的错误信息, 并且跳转到登录页面
	console.log('丢失连接');
	console.log(error);
	if (error) {
		switch (error.code) {
			// 账号或者密码错误, 请跳转到登录页面并提示错误
			case 302:
				goLogin('token失效，请重新登录')
				break;
				// 重复登录, 已经在其它端登录了, 请跳转到登录页面并提示错误
			case 417:
				goLogin('重复登录')
				break;
				// 被踢, 请提示错误后跳转到登录页面
			case 'kicked':
				goLogin('本端被踢，请重新登录')
				break;
			default:
				break;
		}
	}
}

function goLogin(info) {
	uni.navigateTo({
		url: '/pages/login/login?info=' + info
	})
}

function onLoginPortsChange(loginPorts) {
	console.log('当前登录帐号在其它端的状态发生改变了', loginPorts);
}

function onBlacklist(blacklist) {
	console.log('收到黑名单', blacklist);
	data.blacklist = yx.mergeRelations(data.blacklist, blacklist);
	data.blacklist = yx.cutRelations(data.blacklist, blacklist.invalid);
	refreshBlacklistUI();
}

function onMarkInBlacklist(obj) {
	console.log(obj);
	console.log(obj.account + '被你在其它端' + (obj.isAdd ? '加入' : '移除') + '黑名单');
	if (obj.isAdd) {
		addToBlacklist(obj);
	} else {
		removeFromBlacklist(obj);
	}
}

function addToBlacklist(obj) {
	data.blacklist = yx.mergeRelations(data.blacklist, obj.record);
	refreshBlacklistUI();
}

function removeFromBlacklist(obj) {
	data.blacklist = yx.cutRelations(data.blacklist, obj.record);
	refreshBlacklistUI();
}

function refreshBlacklistUI() {
	// 刷新界面
}

function onMutelist(mutelist) {
	console.log('收到静音列表', mutelist);
	data.mutelist = yx.mergeRelations(data.mutelist, mutelist);
	data.mutelist = yx.cutRelations(data.mutelist, mutelist.invalid);
	refreshMutelistUI();
}

function onMarkInMutelist(obj) {
	console.log(obj);
	console.log(obj.account + '被你' + (obj.isAdd ? '加入' : '移除') + '静音列表');
	if (obj.isAdd) {
		addToMutelist(obj);
	} else {
		removeFromMutelist(obj);
	}
}

function addToMutelist(obj) {
	data.mutelist = yx.mergeRelations(data.mutelist, obj.record);
	refreshMutelistUI();
}

function removeFromMutelist(obj) {
	data.mutelist = yx.cutRelations(data.mutelist, obj.record);
	refreshMutelistUI();
}

function refreshMutelistUI() {
	// 刷新界面
}

function onFriends(friends) {
	console.log('收到好友列表', friends);
	data.friends = yx.mergeFriends(data.friends, friends);
	data.friends = yx.cutFriends(data.friends, friends.invalid);
	refreshFriendsUI();
}

function onSyncFriendAction(obj) {
	console.log(obj);
	switch (obj.type) {
		case 'addFriend':
			console.log('你在其它端直接加了一个好友' + obj.account + ', 附言' + obj.ps);
			onAddFriend(obj.friend);
			break;
		case 'applyFriend':
			console.log('你在其它端申请加了一个好友' + obj.account + ', 附言' + obj.ps);
			break;
		case 'passFriendApply':
			console.log('你在其它端通过了一个好友申请' + obj.account + ', 附言' + obj.ps);
			onAddFriend(obj.friend);
			break;
		case 'rejectFriendApply':
			console.log('你在其它端拒绝了一个好友申请' + obj.account + ', 附言' + obj.ps);
			break;
		case 'deleteFriend':
			console.log('你在其它端删了一个好友' + obj.account);
			onDeleteFriend(obj.account);
			break;
		case 'updateFriend':
			console.log('你在其它端更新了一个好友', obj.friend);
			onUpdateFriend(obj.friend);
			break;
	}
}

function onAddFriend(friend) {
	data.friends = yx.mergeFriends(data.friends, friend);
	refreshFriendsUI();
}

function onDeleteFriend(account) {
	data.friends = yx.cutFriendsByAccounts(data.friends, account);
	refreshFriendsUI();
}

function onUpdateFriend(friend) {
	data.friends = yx.mergeFriends(data.friends, friend);
	refreshFriendsUI();
}

function refreshFriendsUI() {
	// 刷新界面
}

function onMyInfo(user) {
	console.log('收到我的名片', user);
	data.myInfo = user;
	updateMyInfoUI();
}

function onUpdateMyInfo(user) {
	console.log('我的名片更新了', user);
	data.myInfo = yx.util.merge(data.myInfo, user);
	updateMyInfoUI();
}

function updateMyInfoUI() {
	// 刷新界面
}

function onUsers(users) {
	console.log('收到用户名片列表', users);
	data.users = yx.mergeUsers(data.users, users);
}

function onUpdateUser(user) {
	console.log('用户名片更新了', user);
	data.users = yx.mergeUsers(data.users, user);
}

function onTeams(teams) {
	console.log('群列表', teams);
	data.teams = yx.mergeTeams(data.teams, teams);
	onInvalidTeams(teams.invalid);
}

function onInvalidTeams(teams) {
	data.teams = yx.cutTeams(data.teams, teams);
	data.invalidTeams = yx.mergeTeams(data.invalidTeams, teams);
	refreshTeamsUI();
}

function onCreateTeam(team) {
	console.log('你创建了一个群', team);
	data.teams = yx.mergeTeams(data.teams, team);
	refreshTeamsUI();
	onTeamMembers({
		teamId: team.teamId,
		members: owner
	});
}

function refreshTeamsUI() {
	// 刷新界面
}

function onTeamMembers(teamId, members) {
	console.log('群id', teamId, '群成员', members);
	var teamId = obj.teamId;
	var members = obj.members;
	data.teamMembers = data.teamMembers || {};
	data.teamMembers[teamId] = yx.mergeTeamMembers(data.teamMembers[teamId], members);
	data.teamMembers[teamId] = yx.cutTeamMembers(data.teamMembers[teamId], members.invalid);
	refreshTeamMembersUI();
}
// function onSyncTeamMembersDone() {
//     console.log('同步群列表完成');
// }
function onUpdateTeam(team) {
	console.log('群状态更新', team)
}

function onUpdateTeamMember(teamMember) {
	console.log('群成员信息更新了', teamMember);
	onTeamMembers({
		teamId: teamMember.teamId,
		members: teamMember
	});
}

function refreshTeamMembersUI() {
	// 刷新界面
}

function onTeamMsgReceipt(teamMsgReceipts) {
	console.log('群消息已读通知', teamMsgReceipts)
}

function onSessions(sessions) {
	console.log('收到会话列表', sessions);
	getApp().globalData.list = yx.mergeSessions(data.sessions, sessions);
	console.log(getApp().globalData.list)
	data.sessions = yx.mergeSessions(data.sessions, sessions);
	updateSessionsUI();
}

function onUpdateSession(session) {
	console.log('会话更新了', session);
	getApp().globalData.list = yx.mergeSessions(data.sessions, session);
	data.sessions = yx.mergeSessions(data.sessions, session);
	console.log(data.sessions)
	updateSessionsUI();
}

function updateSessionsUI() {
	// 刷新界面
}

function pushMsg(msgs) {
	if (!Array.isArray(msgs)) {
		msgs = [msgs];
	}
	var sessionId = msgs[0].sessionId;
	data.msgs = data.msgs || {};
	data.msgs[sessionId] = yx.mergeMsgs(data.msgs[sessionId], msgs);
	console.log(data)
}

function onRoamingMsgs(obj) {
	console.log('漫游消息', obj);
	pushMsg(obj.msgs);
}

function onOfflineMsgs(obj) {
	console.log('离线消息', obj);
	pushMsg(obj.msgs);
}

function onMsg(msg) {
	console.log('收到消息', msg.scene, msg.type, msg);
	getApp().globalData.currentMsg = msg;
	pushMsg(msg);
}

function onBroadcastMsg(msg) {
	console.log('收到广播消息', msg);
}

function onBroadcastMsgs(msgs) {
	console.log('收到广播消息列表', msgs);
}

function onOfflineSysMsgs(sysMsgs) {
	console.log('收到离线系统通知', sysMsgs);
	pushSysMsgs(sysMsgs);
}

function onSysMsg(sysMsg) {
	console.log('收到系统通知', sysMsg)
	pushSysMsgs(sysMsg);
}

function onUpdateSysMsg(sysMsg) {
	pushSysMsgs(sysMsg);
}

function pushSysMsgs(sysMsgs) {
	data.sysMsgs = yx.mergeSysMsgs(data.sysMsgs, sysMsgs);
	refreshSysMsgsUI();
}

function onSysMsgUnread(obj) {
	console.log('收到系统通知未读数', obj);
	data.sysMsgUnread = obj;
	refreshSysMsgsUI();
}

function onUpdateSysMsgUnread(obj) {
	console.log('系统通知未读数更新了', obj);
	data.sysMsgUnread = obj;
	refreshSysMsgsUI();
}

function refreshSysMsgsUI() {
	// 刷新界面
}

function onOfflineCustomSysMsgs(sysMsgs) {
	console.log('收到离线自定义系统通知', sysMsgs);
}

function onCustomSysMsg(sysMsg) {
	console.log('收到自定义系统通知', sysMsg);
}

function onSyncDone() {
	console.log('同步完成');
}
