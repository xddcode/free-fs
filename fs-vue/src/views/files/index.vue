<template>
  <div class="layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <template #header>
        <!-- 头部: 面包屑, 左边一个小图标 -->
        <div class="file-card-header">
          <div class="left-header">
            <div class="left-header__icon">
              <SvgIcon name="ele-Files" :size="30" color="#3498db" title="文件"/>
            </div>
            <div class="left-header__title">
              <el-breadcrumb style="line-height: 30px;" :separator-icon="ArrowRight">
                <el-breadcrumb-item :to="{ path: '/' }">根目录</el-breadcrumb-item>
                <el-breadcrumb-item>目录一</el-breadcrumb-item>
                <el-breadcrumb-item>目录二</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </div>
          <div class="right-header">
            <el-button type="primary" size="default" @click="uploadDialog.visible = true">
              上传文件<SvgIcon class="el-icon--right" name="iconfont icon-shangchuan"></SvgIcon>
            </el-button>
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

    <el-dialog
        v-model="uploadDialog.visible"
        :title="uploadDialog.title"
        width="50%"
    >
      <div class="upload-container">
        <el-upload
            ref="uploadFileRef"
            drag
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
            multiple
            :auto-upload="false"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将要上传的文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              jpg/png files with a size less than 500kb
            </div>
          </template>
        </el-upload>
        <!-- 文件列表 -->
        <el-table :data="uploadFileList" border style="width: 100%">
          <el-table-column prop="name" label="文件名" width="180" />
          <el-table-column prop="type" label="文件类型" width="180" />
          <el-table-column prop="address" label="Address" />
        </el-table>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="submitUpload">
            确认上传
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="albumList">
import { ArrowRight, UploadFilled } from '@element-plus/icons-vue'
import {getFileSvg} from "/@/utils/fti";
import FsOptions from "/@/components/fs/fsOptions.vue";
import TargetBox from "/@/components/fs/targetBox.vue";
import type { UploadInstance } from "element-plus";

const cardBodyRef = ref(0);
const uploadDialog = ref({
  visible: false,
  title: '上传文件',
});

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

/** 文件上传框需要的函数, 后续拆分成组件 */
const uploadFileRef = ref<UploadInstance>();
const uploadFileList = ref([]);
/* 手动点击上传 */
const submitUpload = () => {
  uploadFileRef.value!.submit()
}
</script>

<style scoped lang="scss">

.file-card-header {
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;

  .left-header {
    display: flex;
    justify-content: left;
    align-items: center;

    &__icon {
      margin: 0 10px;
    }

    &__title {
    }
  }

  .right-header {
  }
}

.file-card-body {
  height: calc(100vh - 50px - 100px - 45px);
  position: relative;
}

/** 上传对话框样式 */
.upload-container {
  width: 100%;
}
</style>