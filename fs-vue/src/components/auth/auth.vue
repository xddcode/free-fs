<template>
	<slot v-if="getUserAuthBtnList" />
</template>

<script setup lang="ts" name="auth">
import { computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useUserInfo } from '/@/stores/modules/userInfo';

// #TODO Yann 待修改

// 定义父组件传过来的值
const props = defineProps({
	value: {
		type: String,
		default: () => '',
	},
});

// 定义变量内容
const stores = useUserInfo();
const { authList } = storeToRefs(stores);

// 获取 pinia 中的用户权限
const getUserAuthBtnList = computed(() => {
	return authList.some((v: string) => v === props.value);
});
</script>
