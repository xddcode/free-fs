<template>
  <el-dropdown
      ref="dropdownRef"
      trigger="contextmenu"
      :id="id"
      @visible-change="handleVisible(id, $event)"
      :style="{ width: '100%' }"
  >
    <slot></slot>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item :icon="View">
          查看
        </el-dropdown-item>
        <el-dropdown-item :icon="CopyDocument">
          复制
        </el-dropdown-item>
        <el-dropdown-item :icon="Rank">
          移动
        </el-dropdown-item>
        <el-dropdown-item :icon="EditPen">
          重命名
        </el-dropdown-item>
        <el-dropdown-item divided :icon="Delete">
          删除
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script lang="ts" setup>
import { View, CopyDocument, Rank, EditPen, Delete } from '@element-plus/icons-vue'

const props = defineProps({
  id: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['visibleChangeFunc']);

//下拉菜单右键打开新的，要关闭之前的
const dropdownRef = ref();
function handleVisible(id, visible) {
  if (!visible) return;
  emit('visibleChange', id, visible);
}

function handleClose() {
  dropdownRef.value.handleClose();
}

const id = ref(props.id);
defineExpose({
  id,
  handleClose,
});
</script>