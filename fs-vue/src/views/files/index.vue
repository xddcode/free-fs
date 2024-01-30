<template>
  <div ref="layoutRef" class="layout-padding">
    <el-card  shadow="hover" class="layout-padding-auto">
      <template #header>
        <!-- 头部: 面包屑, 左边一个小图标 -->
        <div class="file-card-header">
          <div class="left-header">
            <SvgIcon name="ele-Files" :size="30" color="#3498db" title="文件"/>
          </div>
          <div class="right-header">
            <el-breadcrumb :separator-icon="ArrowRight">
              <el-breadcrumb-item :to="{ path: '/' }">根目录</el-breadcrumb-item>
              <el-breadcrumb-item>目录一</el-breadcrumb-item>
              <el-breadcrumb-item>目录二</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
      </template>

      <!-- 主体以文件列表为主 -->
      <el-scrollbar :max-height="cardBodyHeight - 130">
        <div class="file-grid-container">
          <div v-for="(file, index) in fileList" class="file-grid-item" :body-style="{ padding: '0px' }" :key="index">
            <div class="file-item">
              <div class="file-icon">
                <img v-if="file.isImg" :src="file.url" alt="文件" />
                <img v-else :src="getAssetsFile(file.type)" alt="文件" />
              </div>
              <div class="file-name">{{ file.name }}</div>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="fileList">
import {ArrowRight} from '@element-plus/icons-vue'
import {getAssetsFile} from "/@/utils/fti";
import {FileVO} from "/@/api/files/types";

const layoutRef = ref();
const cardBodyHeight = ref(0);

watch(layoutRef, () => {
  if (layoutRef.value) {
    cardBodyHeight.value = layoutRef.value.clientHeight;
  }
})

// const fileList = ref([]);
// 文件集合
const fileList = reactive([
  {
    id: 1,
    name: '默认文件夹',
    url: '',
    type: 'dir',
    isImg: false,
  },
  {
    id: 2,
    name: 'food.jpeg',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'jpeg',
    isImg: true,
  },
  {
    id: 5,
    name: 'food.pdf',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'pdf',
    isImg: false,
  },
  {
    id: 6,
    name: 'food.txt',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'txt',
    isImg: false,
  },
  {
    id: 7,
    name: 'food.doc',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'doc',
    isImg: false,
  },
  {
    id: 8,
    name: 'food.jpeg',
    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100',
    type: 'jpeg',
    isImg: true,
  },
]);
</script>

<style scoped lang="scss">

.file-card-header {
  display: flex;
  line-height: 30px;
  justify-content: left;
  align-items: center;

  .left-header {
    margin: 0 10px;
  }

  .right-header {
    //margin: auto 0;
  }
}

</style>