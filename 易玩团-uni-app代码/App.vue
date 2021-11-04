<script>
import { initYunXin } from 'utils/initYunXin.js';

export default {
	globalData: {
		PHUrl: 'https://via.placeholder.com/',
		baseUrl: 'https://zmetalhearty.com',
		messageEmptyImg: 'https://zmetalhearty.com/file/1635052274558Z4835Z1740Z7240',
		contentEmptyImg: 'https://zmetalhearty.com/file/1635052322614Z3626Z551Z6559',
		friendEmptyImg: 'https://zmetalhearty.com/file/1635052348167Z1015Z431Z1454',
		notifacationEmptyImg: 'https://zmetalhearty.com/file/1635052375439Z6966Z1819Z5962',
		collectionEmptyImg: 'https://zmetalhearty.com/file/1635052405835Z7370Z5028Z1797',
		list: [],
		currentMsg: null
	},
	onLaunch: function() {
		console.log('App Launch');
		// uni.setEnableDebug({
		// 	enableDebug: true
		// })
		if (!uni.getStorageSync('yunXinInfo')) {
			uni.reLaunch({
				url: 'pages/login/login'
			});
		} else {
			initYunXin();
		}
	},
	onShow: function() {
		console.log('App Show');
	},
	onHide: function() {
		console.log('App Hide');
	},
	methods: {
		watch: function(method, istr) {
			var obj = this.globalData;
			console.log(obj);
			Object.defineProperty(obj, istr, {
				configurable: true,
				enumerable: true,
				set: function(value) {
					this._consumerGoodsStatus = value;
					method(value);
				},
				get: function(value) {
					return this._consumerGoodsStatus;
				}
			});
		}
	}
};
</script>

<style lang="scss">
@import 'static/css/iconfont.css';
@import 'uview-ui/index.scss';
@import 'static/css/global.css';
</style>
