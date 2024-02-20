<template>
  <el-breadcrumb style="line-height: 30px;" :separator-icon="ArrowRight">
    <el-breadcrumb-item
        v-for="(item, index) in folderList"
        @click="handleClickItem(item.id)"
        :key="index">
        <span class="breadcrumb-title"
              :style="{ fontWeight: ((index === folderList.length - 1) ? 'bold' : '') }">{{ item.name }}</span>
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>
<script setup lang="ts" name="FolderBreadcrumb">
import { ArrowRight } from '@element-plus/icons-vue';
import { useFilesApi } from '/@/api/files';
import { DirVo } from "/@/api/files/types";
import { PropType } from "vue-demi";

/**
 * 动态传递
 */
const props = defineProps({
  /**
   * 目录ID
   */
  folderId: {
    type: [ String, Number ],
    default: -1,
  },
  /**
   * 点击文件夹事件
   */
  onClick: {
    type: Function as PropType<(folderId: string | number) => void>
  }
})

const initFolderData: DirVo = {
  id: -1,
  name: '根目录',
  pid: -1
}
/**
 * 目录层级列表
 */
const folderList = ref<DirVo[]>([initFolderData]);

/**
 * 选择 文件夹 事件
 * @param folderId
 */
const handleClickItem = (folderId: string | number) => {
  props.onClick?.(folderId);
}

const getFolderList = async (id) => {
  const res = await useFilesApi().getLevelFolders(id);
  const list = res.data;
  if (list.length === 0) {
    folderList.value = [initFolderData];
  } else {
    folderList.value = [initFolderData, ...res.data];
  }
}
/**
 * 监听当前传递的目录ID
 */
watch(() => props.folderId, (val) => {
  // 获取层级接口 | 直接通过组件传递实现
  getFolderList(val);
}, { deep: true, immediate: true })
</script>

<style lang="scss" scoped>
.breadcrumb-title {
  font-size: 16px;
  cursor: pointer;
}
</style>