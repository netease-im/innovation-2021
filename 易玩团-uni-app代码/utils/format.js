export function issueFormat(issue) {
	if (issue.iImg) {
		issue.iImg = issue.iImg.split(',');
		issue.iImg.splice(1, 1);
	}
	if (issue.iStartTime) {
		issue.iStartTime = {
			date: issue.iStartTime.trim().split(/\s+/)[0],
			time: issue.iStartTime.trim().split(/\s+/)[1]
		}
	}
	if (issue.iEndTime) {
		issue.iEndTime = {
			date: issue.iEndTime.trim().split(/\s+/)[0],
			time: issue.iEndTime.trim().split(/\s+/)[1]
		}
	}
	if (issue.iAddress) {
		issue.iAddress = JSON.parse(issue.iAddress);
	}
	if (issue.iPositionStart) {
		issue.iPositionStart = JSON.parse(issue.iPositionStart);
	}
	if (issue.iPositionEnd) {
		issue.iPositionEnd = JSON.parse(issue.iPositionEnd);
	}
	return issue;
}

export function mapIssueFormat(list) {
	list.forEach(issue => {
		if (issue.iconPath) {
			issue.iconPath = 'https://zmetalhearty.com' + issue.iconPath; 
		}
	})
	return list;
}

export function timeFormat(formTime) {
	var newTime = new Date(formTime)
	return Date.parse(newTime);
}
