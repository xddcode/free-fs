<script setup lang="ts">
import { NativeTypes } from 'react-dnd-html5-backend';
import { useDrop, DropTargetMonitor } from 'vue3-dnd';
import { computed, unref } from 'vue';
import { toRefs } from '@vueuse/core';
import {useThemeConfig} from "/@/stores/modules/themeConfig";
import {storeToRefs} from "pinia";
const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);

/** 组件传递 */
const props = defineProps<{
  onDrop: (item: { files: any[] }) => void
}>()

const zIndex = ref(-1);
const [collect, drop] = useDrop(() => ({
  accept: [ NativeTypes.FILE ],
  drop(item: { files: any[] }) {
    props.onDrop?.(item)
    zIndex.value = -1;
  },
  canDrop(item, monitor: DropTargetMonitor) {
    // console.log('canDrop', item, monitor.getItemType())
    // if (monitor.getItem().items) {
      zIndex.value = 10;
      return true;
    // }
    // return false;
  },
  hover(item: any) {
  },
  collect: (monitor: DropTargetMonitor) => {
    const item = monitor.getItem() as any
    if (item) {
      console.log('collect', item.files, item.items)
    }
    return {
      isOver: monitor.isOver(),
      canDrop: monitor.canDrop(),
    }
  },
}))
const { canDrop, isOver } = toRefs(collect)
const isActive = computed(() => unref(canDrop) && unref(isOver))
watch(isActive, (val) => {
  if (val) {
    zIndex.value = 10;
  } else {
    zIndex.value = -1;
  }
})
</script>

<template>
  <div :ref="drop" class="overlay-context" :style="{zIndex: zIndex}">
    <div v-if="isActive" class="overlay">
      <div class="overlay-text">拖放文件到此处</div>
      <div class="overlay-icon" :style="{ background: themeConfig.primary }">
        <SvgIcon name="iconfont icon-shangchuan" color="#FFFFFF" :size="50"></SvgIcon>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.overlay-context {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;

  &-text {
    color: white;
    font-size: 18px;
  }

  &-icon {
    margin-top: 10px;
    width: 66px;
    height: 66px;
    border-radius: 50%;
    //background: #26a59a;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>