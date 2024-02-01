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
              <!-- #TODO Yann 这个组件有无限递归导致内存溢出问题 -->
<!--              <fs-options-->
<!--                  ref="fileOperationsRef"-->
<!--                  :id="file.id.toString()"-->
<!--                  @visibleChange="handleVisible"-->
<!--              >-->
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
<!--              </fs-options>-->
            </div>
          </div>
        </el-scrollbar>

        <target-box :on-drop="handleFileDrop"></target-box>
      </div>
    </el-card>

    <el-dialog
        v-model="uploadDialog.visible"
        :title="uploadDialog.title"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        :before-close="handleUploadDiaglogClose"
        width="50%"
    >
      <div class="upload-container">
        <el-upload
            ref="uploadFileRef"
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
            drag
            multiple
            :auto-upload="false"
            :show-file-list="false"
            :before-upload="handleBeforeUpload"
            :on-success="handleUploadSuccess"
            :on-change="handleFileChange"
            :limit="10"
            :headers="headers"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将要上传的文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip color-danger">
              支持大文件上传，分片上传，断点续传...
            </div>
          </template>
        </el-upload>
        <!-- 文件列表 -->
        <el-table :data="uploadFileList" border :style="{ width: '100%', marginTop: '10px' }" max-height="250">
          <el-table-column align="center" prop="name" label="文件名" width="160px" >
            <template #default="scope">
              <el-text truncated>{{ scope.row.name }}</el-text>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="type" label="文件类型" width="120px" />
          <el-table-column align="center" prop="size" label="大小" width="100px" >
            <template #default="scope">
              <el-text>{{ Math.ceil(scope.row.size / 1024) }} kb</el-text>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="status" label="状态" width="100px">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 'ready'">准备</el-tag>
              <el-tag v-else-if="scope.row.status === 'uploading'">上传中</el-tag>
              <el-tag v-if="scope.row.status === 'success'">成功</el-tag>
              <el-tag v-if="scope.row.status === 'fail'">失败</el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="percentage" label="上传进度" >
            <template #default="scope">
              <el-progress :percentage="scope.row.percentage" :color="uploadDialog.progressColors" />
            </template>
          </el-table-column>
          <el-table-column align="center" prop="option" label="操作" width="120px" >
            <template #default="scope">
              <el-button type="warning" :icon="Delete" size="default" @click="handleFileDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelUpload" >取消</el-button>
          <el-button type="primary" @click="submitUpload" :loading="uploadDialog.uploadLoading">
            确认上传
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="albumList">
import { ArrowRight, UploadFilled, Delete } from '@element-plus/icons-vue'
import {getFileSvg} from "/@/utils/fti";
import FsOptions from "/@/components/fs/fsOptions.vue";
import TargetBox from "/@/components/fs/targetBox.vue";
import type { UploadFile, UploadFiles, UploadInstance} from "element-plus";
import { UploadFileVo } from "/@/api/upload/types";
import { globalHeaders } from '/@/utils/request';
import { ElMessage, ElMessageBox} from "element-plus";

const cardBodyRef = ref(0);
const uploadDialog = reactive({
  visible: false,
  title: '上传文件',
  progressColors: [
    { color: '#f56c6c', percentage: 20 },
    { color: '#e6a23c', percentage: 40 },
    { color: '#5cb87a', percentage: 60 },
    { color: '#1989fa', percentage: 80 },
    { color: '#6f7ad3', percentage: 100 },
  ],
  uploadLoading: false,
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
const uploadFileList = ref<UploadFileVo>([]);
const headers = ref(globalHeaders());
const handleUploadDiaglogClose = () => {
  if (uploadDialog.uploadLoading) {
    // 后面改成最小化的效果最好
    ElMessageBox.alert('文件正在上传中，请勿关闭..', '提示', {
      confirmButtonText: 'OK',
    })
    return;
  }
  uploadDialog.visible = false;
}


/* 上传前事件 */
const handleBeforeUpload = (file: any) => {
  console.log(file)
}

// 上传成功回调
const handleUploadSuccess = (res: any, file: UploadFile) => {
  console.log('success', file)
}

// 文件改变事件
const handleFileChange = (file: UploadFile, files: UploadFiles) => {
  console.log('change', file)
  if (file) {
    handleUploadFile(file);
  } else if (files) {
    for (let i = 0; i < files.length; i++) {
      handleUploadFile(files[i])
    }
  }
}

const handleUploadFile = (file) => {
  // 通过uid查找集合是否已经存在
  let index = uploadFileList.value.findIndex(obj => obj.uid === file.uid);
  if (index !== -1) {
    uploadFileList.value = uploadFileList.value.map(obj => {
      if (obj.uid === file.uid) {
        return { type: file.raw?.type , ...file};
      }
      return obj;
    })
  } else {
    let _file = { type: file.raw?.type , ...file}
    uploadFileList.value.push(_file);
  }
}

const handleFileDelete = (file) => {
  // 移除待上传的文件
  uploadFileRef.value!.handleRemove(file);
  // 移除集合元素
  uploadFileList.value.splice(uploadFileList.value.indexOf(file), 1)
}

/* 手动点击上传 */
const submitUpload = () => {
  if (uploadFileList.value.length > 0) {
    // 校验列表中是否存在上传失败的文件
    // let failList = uploadFileList.value.filter(item => item.status === 'fail');
    // for (let i = 0; i < failList.length; i++) {
    //   failList[i].status = 'ready';
    //   handleUploadFile(failList[i]);
    // }

    uploadDialog.uploadLoading = true;
    uploadFileRef.value!.submit();
  } else {
    ElMessage.error({
      message: '未选择要上传的文件..',
      type: 'warning',
    });
  }
}

const cancelUpload = () => {
  if (uploadDialog.uploadLoading) {
    uploadFileRef.value!.abort();
    uploadDialog.uploadLoading = false;
  } else {
    ElMessage.error({
      message: '暂无文件正在上传..',
      type: 'warning',
    });
  }
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