<template>
  <div class="upload-container">
    <el-upload
        ref="uploadFileRef"
        v-model:file-list="uploadFileList"
        :action="uploader.url"
        :headers="uploader.headers"
        :data="uploader.data"
        drag
        multiple
        :auto-upload="true"
        :show-file-list="false"
        :on-remove="handleFileRemove"
        :before-upload="handleBeforeUpload"
        :before-remove="handleBeforeRemove"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :on-change="handleFileChange"
        :limit="10"
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
      <el-table-column align="center" prop="type" label="文件类型" width="120px">
        <template #default="scope">
          {{ scope.row.type }}
        </template>
      </el-table-column>
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
          <el-progress :percentage="scope.row.percentage" :color="uploader.progressColors"/>
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
import { ElMessageBox, UploadFile, UploadFiles, UploadInstance, UploadRawFile, UploadUserFile } from "element-plus";
import { UploadFileVo } from '/@/api/upload/types';
import { globalHeaders } from '/@/utils/request';


// 服务地址
const baseUrl = import.meta.env.VITE_APP_BASE_API;
/** 上传器需要的参数 */
const uploader = reactive( {
  // 文件接口地址
  url: baseUrl + '/file/upload',
  headers: {
    ...globalHeaders()
  },
  // 上传文件附带的参数
  data: {
    dirIds: ''
  },
  // 进度条颜色
  progressColors: [
    { color: '#f56c6c', percentage: 20 },
    { color: '#e6a23c', percentage: 40 },
    { color: '#5cb87a', percentage: 60 },
    { color: '#1989fa', percentage: 80 },
    { color: '#6f7ad3', percentage: 100 },
  ],
} );
// 上传器实例
const uploadFileRef = ref<UploadInstance>();
// 待上传的文件
const uploadFileList = ref<UploadFileVo[]>( [] );

watch( uploadFileList, ( val ) => {
  console.log( val );
} )

const handleFileRemove = ( file: UploadFile ) => {
  console.log( '执行了文件删除: ', file )
}

/** 上传前回调事件 */
const handleBeforeUpload = ( rawFile: UploadRawFile ) => {
  console.log( '文件上传前回调: ', rawFile )
  return true;
}

/** 删除前回调事件 */
const handleBeforeRemove = ( uploadFile: UploadFile, uploadFiles: UploadFiles ) => {
  console.log( '文件删除前回调: ', uploadFile, uploadFiles )
}

// 上传成功回调
const handleUploadSuccess = ( res: any, file: UploadFile ) => {
  console.log( '上传成功回调: ', file )
}

const handleUploadError = ( error: Error, uploadFile: UploadFile, uploadFiles: UploadFiles ) => {
  console.log( '上传失败回调: ', error, uploadFile, uploadFiles )
}

// 文件改变事件
const handleFileChange = ( file: UploadFile, files: UploadFiles ) => {
  console.log( '触发文件变更回调: ', file, files )
  if ( file ) {
    handleUploadFile( file );
  } else if ( files ) {
    for (let i = 0; i < files.length; i++) {
      handleUploadFile( files[i] )
    }
  }
}

const handleUploadFile = ( file ) => {
  // 通过uid查找集合是否已经存在
  let index = uploadFileList.value.findIndex( obj => obj.uid === file.uid );
  if ( index !== -1 ) {
    uploadFileList.value = uploadFileList.value.map( obj => {
      if ( obj.uid === file.uid ) {
        return { type: file.raw?.type, ...file };
      }
      return obj;
    } )
  } else {
    let _file = { type: file.raw?.type, ...file }
    uploadFileList.value.push( _file );
  }
};

const handleFileDelete = ( file ) => {
  // 移除待上传的文件
  uploadFileRef.value!.handleRemove( file );
}
</script>

<style scoped lang="scss">

</style>