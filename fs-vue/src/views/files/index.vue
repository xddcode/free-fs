<template>
  <div class="layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <template #header>
        <!-- 头部: 面包屑, 左边一个小图标 -->
        <div class="file-card-header">
          <div class="left-header">
            <SvgIcon name="ele-Files" :size="30" color="#3498db" title="文件"/>
          </div>
          <div class="right-header">
            <el-breadcrumb style="line-height: 30px;" :separator-icon="ArrowRight">
              <el-breadcrumb-item :to="{ path: '/' }">根目录</el-breadcrumb-item>
              <el-breadcrumb-item>目录一</el-breadcrumb-item>
              <el-breadcrumb-item>目录二</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
      </template>

      <div ref="cardBodyRef" class="file-card-body"
           @dragover="handleDragOver"
           @dragleave="handleDragLeave"
           @drop="handleDrop"
      >
        <!-- 主体以文件列表为主 -->
        <el-scrollbar :max-height="cardBodyRef.clientHeight">
          <div class="file-grid-container">
            <div v-for="(file, index) in fileList" class="file-grid-item" :key="index">
              <!-- 文件操作组件 -->
              <fs-options
                  ref="fileOperationsRef"
                  :id="file.id.toString()"
                  @visibleChange="handleVisible"
              >
                <!-- 文件主体 -->
                <div class="file-item-box">
                  <div class="file-icon">
                    <img :src="getFileSvg(file.type)" :alt="file.name"/>
                  </div>
                  <div class="file-item-name">
                    {{ file.name }}
                  </div>
                  <div class="file-item-time">{{ file.createTime || '--' }}</div>
                </div>
              </fs-options>
            </div>
          </div>
        </el-scrollbar>

        <div v-if="isDragging" class="overlay">
          <div class="overlay-text">拖放文件到此处</div>
          <div class="overlay-icon" :style="{background: themeConfig.primary}">
            <SvgIcon name="iconfont icon-shangchuan" color="#FFFFFF" :size="50"></SvgIcon>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="fileList">
import { debounce, throttle } from 'lodash';
import {ArrowRight} from '@element-plus/icons-vue'
import { storeToRefs } from 'pinia';
import { useThemeConfig } from '/@/stores/modules/themeConfig';
import {getFileSvg} from "/@/utils/fti";
import FsOptions from "/@/components/fs/fsOptions.vue";


const storesThemeConfig = useThemeConfig();
const { themeConfig } = storeToRefs(storesThemeConfig);
const cardBodyRef = ref(0);

// const fileList = ref([]);
// 文件集合
const fileList = reactive([
  {
    id: 1,
    name: '默认文件夹',
    url: '',
    type: 'dir',
    isImg: false,
    createTime: '2021/08/01 12:00',
  },
  {
    id: 2,
    name: 'food.jpeg',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'jpeg',
    isImg: true,
    createTime: '2021/08/01 12:00',
  },
  {
    id: 5,
    name: 'food.pdf',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'pdf',
    isImg: false,
    createTime: '2021/08/01 12:00',
  },
  {
    id: 6,
    name: 'food.txt',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'txt',
    isImg: false,
    createTime: '2021/08/01 12:00',
  },
  {
    id: 7,
    name: 'food.doc',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'doc',
    isImg: false,
    createTime: '2021/08/01 12:00',
  },
  {
    id: 8,
    name: 'food.jpeg',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'jpeg',
    isImg: true,
    createTime: '2021/08/01 12:00',
  }
]);

//下拉菜单右键打开新的，要关闭之前的
const fileOperationsRef = ref();
const handleVisible = (id, visible) => {
  if (!visible) return;
  fileOperationsRef.value.forEach((item) => {
    if (item.id === id) return;
    item.handleClose();
  });
}

const isDragging = ref(false);
const handleDragOver = (event: DragEvent) => {
  console.log(event)
  event.preventDefault(); // 阻止默认行为，允许放置
  isDragging.value = true;
};
// 节流函数, 减少事件触发频率
const throttleHandleDragOver = throttle(handleDragOver, 500)

// #TODO Yann 在组件中调用节流控制的@dragover后, 影响@drop事件触发
const handleDrop = (event) => {
  console.log(event)
  event.preventDefault(); // 阻止浏览器默认行为，不会自动打开文件管理页面
  event.stopPropagation();
  isDragging.value = false;
  // 移除事件时同步取消节流操作
  throttleHandleDragOver && throttleHandleDragOver.cancel();
  const DataTransferItemList = event.dataTransfer!.items; // 获取文件的数据
  console.log(DataTransferItemList); // 打印看看
};

const handleDragLeave = (event) => {
  if (!cardBodyRef.value.contains(event.relatedTarget)) {
    isDragging.value = false;
    // 移除事件时同步取消节流操作
    throttleHandleDragOver && throttleHandleDragOver.cancel();
  }
};

// onMounted(() => {
//   // 在组件挂载时，为 dragover 事件处理函数应用节流函数
//   #TODO Yann  没用...
//   cardBodyRef.value.addEventListener('dragover',
//       throttleHandleDragOver
//   );
// })
</script>

<style scoped lang="scss">

.file-card-header {
  display: flex;
  line-height: 30px;
  justify-content: left;
  align-items: center;

  .left-header {
    margin: 0 10px;
    padding-top: 10px;
  }

  .right-header {
    //margin: auto 0;
  }
}

.file-card-body {
  height: calc(100vh - 50px - 100px - 45px);
  position: relative;

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
}

</style>