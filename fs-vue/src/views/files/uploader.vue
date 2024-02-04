<template>
  <div class="upload-container">
    <el-upload
        ref="uploadFileRef"
        :action="uploadApi"
        drag
        multiple
        :auto-upload="true"
        :show-file-list="false"
        :before-upload="handleBeforeUpload"
        :on-success="handleUploadSuccess"
        :on-change="handleFileChange"
        :limit="10"
        :headers="headers"
    >
      <el-icon class="el-icon--upload">
        <UploadFilled/>
      </el-icon>
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
      <el-table-column align="center" prop="name" label="文件名" width="160px">
        <template #default="scope">
          <el-text truncated>{{ scope.row.name }}</el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="type" label="文件类型" width="120px"/>
      <el-table-column align="center" prop="size" label="大小" width="100px">
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
      <el-table-column align="center" prop="percentage" label="上传进度">
        <template #default="scope">
          <el-progress :percentage="scope.row.percentage" :color="uploadDialog.progressColors"/>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="option" label="操作" width="120px">
        <template #default="scope">
          <el-button type="warning" :icon="Delete" size="default" @click="handleFileDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts" name="fileUploader">
import { Delete, UploadFilled } from "@element-plus/icons-vue";
import { ElMessageBox, UploadFile, UploadFiles, UploadInstance } from "element-plus";
import { UploadFileVo } from '/@/api/upload/types';
import { globalHeaders } from '/@/utils/request';

const uploadDialog = reactive({
  visible: false,
  title: '上传文件',
  progressColors: [
    {color: '#f56c6c', percentage: 20},
    {color: '#e6a23c', percentage: 40},
    {color: '#5cb87a', percentage: 60},
    {color: '#1989fa', percentage: 80},
    {color: '#6f7ad3', percentage: 100},
  ],
  uploadLoading: false,
});

/** 文件上传框需要的函数, 后续拆分成组件 */
const uploadFileRef = ref<UploadInstance>();
const uploadFileList = ref<UploadFileVo>([]);
const headers = ref(globalHeaders());
const baseUrl = import.meta.env.VITE_APP_BASE_API;
const uploadApi = ref(baseUrl + '/file/upload?dirIds=1');


/* 上传前事件 */
const handleBeforeUpload = ( file: any ) => {
  console.log(file)
}

// 上传成功回调
const handleUploadSuccess = ( res: any, file: UploadFile ) => {
  console.log('success', file)
}

// 文件改变事件
const handleFileChange = ( file: UploadFile, files: UploadFiles ) => {
  console.log('change', file)
  if ( file ) {
    handleUploadFile(file);
  } else if ( files ) {
    for (let i = 0; i < files.length; i ++) {
      handleUploadFile(files[i])
    }
  }
}

const handleUploadFile = ( file ) => {
  // 通过uid查找集合是否已经存在
  let index = uploadFileList.value.findIndex(obj => obj.uid === file.uid);
  if ( index !== - 1 ) {
    uploadFileList.value = uploadFileList.value.map(obj => {
      if ( obj.uid === file.uid ) {
        return {type: file.raw?.type, ...file};
      }
      return obj;
    })
  } else {
    let _file = {type: file.raw?.type, ...file}
    uploadFileList.value.push(_file);
  }
};

const handleFileDelete = ( file ) => {
  // 移除待上传的文件
  uploadFileRef.value!.handleRemove(file);
  // 移除集合元素
  uploadFileList.value.splice(uploadFileList.value.indexOf(file), 1)
}
</script>

<style scoped lang="scss">

</style>