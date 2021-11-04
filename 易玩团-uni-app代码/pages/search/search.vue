<template>
	<view>
		<text class="k-title">搜索结果</text>
		<kIssueList v-if="searchContent != ''" type="search" :searchContent="searchContent"></kIssueList>
	</view>
</template>

<script>
	import kIssueList from '../../components/k-list/k-issue-list';
	export default {
		components: {
			kIssueList
		},
		data() {
			return {
				searchContent: ''
			}
		},
		onLoad(data) {
			console.log(data)
			if(data.searchContent) {
				this.searchContent = data.searchContent;
			}
		},
		methods: {
			
		}
	}
</script>

<style>

</style>
