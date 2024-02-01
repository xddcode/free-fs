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

      <div ref="cardBodyRef" class="file-card-body">
        <!-- 主体以文件列表为主 -->
        <el-scrollbar>
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
<!--                    <img :src="getFileSvg(file.type)" :alt="file.name"/>-->
                    <!-- 拖拽监听的是文件, 不想让监听这里 -->
                    <div class="file-icon__img" :style="{ backgroundImage: 'url(' + getFileSvg(file.type) + ')' }"></div>
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

        <target-box :on-drop="handleFileDrop"></target-box>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="albumList">
import {ArrowRight} from '@element-plus/icons-vue'
import {getFileSvg} from "/@/utils/fti";
import FsOptions from "/@/components/fs/fsOptions.vue";
import TargetBox from "/@/components/fs/targetBox.vue";

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
const handleVisible = (id: string | number, visible: boolean) => {
  if (!visible) return;
  fileOperationsRef.value.forEach((item: any) => {
    if (item.id === id) return;
    item.handleClose();
  });
}

const droppedFiles = ref([]);
const handleFileDrop = (item: any) => {
  console.log(item)
  if (item) {
    droppedFiles.value = item.files
  }
}
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
}

</style>