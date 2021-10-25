import request from '../config/request.js'

export async function getUserInfo(id, type) {
	var baseUrl = 'https://zmetalhearty.com';
	var data;
	var p = await request('frontUser/getDetails', 'POST', {
		fuId: id
	})
	p.then(res => {
		console.log(res);
		if (type == 'name') {
			data = res.data.data.fuNickname;
		} else if (type == 'img') {
			data = baseUrl + res.data.data.fuImg;
		}
		return data;
	})
	
}
