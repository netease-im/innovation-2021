import Vue from 'vue'


export default function(method, methodType, data, json) {
	const baseUrl = 'https://www.zmetalhearty.com/yuanpin/WeChat/'
	if(json) {
		var headers = {
			'content-type': 'application/json'
		};
	}else {
		var headers = {
			'content-type': 'application/x-www-form-urlencoded'
		};
	}
	
	let userInfo = uni.getStorageSync('userInfo');
	if(!userInfo && method != 'frontUser/login') {
		uni.navigateTo({
			url: '/pages/login/login'
		})
		return new Promise((resolve,reject) => {
			resolve('not login yet')
		});
	}
	
	return new Promise((resolve,reject) => {
		uni.request({
			url: baseUrl + method,
			data: data,
			header: headers,
			method: methodType,
			success: (response) => {
				resolve(response)
			},
			fail: (error) => {
				if (error && error.response) {
					showError(error.response);
				}
				reject(error)
			}
		})
	})
}

export const showError = error => {
	let errorMsg = ''
	switch (error.status) {
		case 400:
			errorMsg = '请求参数错误'
			break
		case 401:
			errorMsg = '未授权，请登录'
			break
		case 403:
			errorMsg = '跨域拒绝访问'
			break
		case 404:
			errorMsg = `请求地址出错: ${error.config.url}`
			break
		case 408:
			errorMsg = '请求超时'
			break
		case 500:
			errorMsg = '服务器内部错误'
			break
		case 501:
			errorMsg = '服务未实现'
			break
		case 502:
			errorMsg = '网关错误'
			break
		case 503:
			errorMsg = '服务不可用'
			break
		case 504:
			errorMsg = '网关超时'
			break
		case 505:
			errorMsg = 'HTTP版本不受支持'
			break
		default:
			errorMsg = error.msg
			break
	}

	uni.showToast({
		title: errorMsg,
		icon: 'none',
		duration: 1000,
		complete: function() {
			setTimeout(function() {
				uni.hideToast();
			}, 1000);
		}
	});
}