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
        :auto-upload="autoUpload"
        :show-file-list="false"
        :on-remove="handleFileRemove"
        :before-upload="handleBeforeUpload"
        :before-remove="handleBeforeRemove"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :on-change="handleFileChange"
        :on-exceed="handleExceed"
        :limit="uploader.limit"
    >
<!--    <el-upload-->
<!--        ref="uploadFileRef"-->
<!--        v-model:file-list="uploadFileList"-->
<!--        drag-->
<!--        multiple-->
<!--        :auto-upload="autoUpload"-->
<!--        :show-file-list="false"-->
<!--        :on-remove="handleFileRemove"-->
<!--        :http-request="handleHttpRequest"-->
<!--        :limit="uploader.limit"-->
<!--    >-->
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
    <el-table v-if="uploadFileList.length > 0" :data="uploadFileList" border :style="{ width: '100%', marginTop: '10px' }" max-height="250">
      <el-table-column align="center" prop="name" label="文件名" width="160px">
        <template #default="scope">
          <el-text truncated>{{ scope.row.name }}</el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="type" label="文件类型" width="120px">
        <template #default="scope">
          {{ scope.row.type?? scope.row.raw.type }}
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
          <el-button v-if="(autoUpload && scope.row.percentage !== 100) || (!autoUpload && scope.row.percentage > 0)"
                     type="danger" :icon="RemoveFilled"
                     @click="handleAbort(scope.row)">终止
          </el-button>
          <el-button v-if="(autoUpload && scope.row.percentage === 100) || (!autoUpload && scope.row.percentage === 0)"
                     type="warning" :icon="Delete" size="default"
                     @click="handleFileDelete(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts" name="fileUploader">
import { Delete, UploadFilled, RemoveFilled } from "@element-plus/icons-vue";
import {
  ElMessage,
  UploadFile,
  UploadFiles,
  UploadInstance,
  UploadRawFile,
  UploadRequestOptions,
} from "element-plus";
import { UploadFileVo } from '/@/api/upload/types';
import { globalHeaders } from '/@/utils/request';
import { md5File } from '/@/utils/chunkFile';

const props = defineProps({
  /**
   * 当前上传的文件夹ID
   * 目前就只需要文件夹id, 看后面考虑是否优化组件直接传data
   * #TODO Yann 文件夹id命名问题
   */
  dirIds: {
    type: [ String, Number ],
    default: -1,
  },
  /**
   * 是否自动上传
   */
  autoUpload: {
    type: Boolean,
    default: false,
  }
})

// 服务地址
const baseUrl = import.meta.env.VITE_APP_BASE_API;
/** 上传器需要的参数 */
const uploader = reactive({
  // 文件接口地址
  url: baseUrl + '/file/upload',
  headers: {
    ...globalHeaders()
  },
  // 上传文件附带的参数
  data: {
    dirIds: ''
  },
  limit: 10,
  // 进度条颜色
  progressColors: [
    { color: '#f56c6c', percentage: 20 },
    { color: '#e6a23c', percentage: 40 },
    { color: '#5cb87a', percentage: 60 },
    { color: '#1989fa', percentage: 80 },
    { color: '#6f7ad3', percentage: 100 },
  ],
});
// 上传器实例
const uploadFileRef = ref<UploadInstance>();
// 待上传的文件
const uploadFileList = ref<UploadFileVo[]>([]);

watch(() => props.dirIds, (val) => {
  console.log(val)
}, { deep: true })

watch(uploadFileList, (val) => {
  console.log(val);
})

/**
 * 自定义上传方法
 * @param options
 */
const handleHttpRequest = async (options: UploadRequestOptions) => {
  console.log(options)
  // const file = options.file;

}

/**
 * 获取一个上传任务，没有则初始化一个
 */
// const getTaskInfo = async (file) => {
//   let task;
//   const identifier = await md5File(file)
//   const { code, data, msg } = await taskInfo(identifier)
//   if (code === 200000) {
//     task = data
//     if (!task) {
//       const initTaskData = {
//         identifier,
//         fileName: file.name,
//         totalSize: file.size,
//         chunkSize: 5 * 1024 * 1024
//       }
//       const { code, data, msg } = await initTask(initTaskData)
//       if (code === 200000) {
//         task = data
//       } else {
//         ElNotification.error({
//           title: '文件上传错误',
//           message: msg
//         })
//       }
//     }
//   } else {
//     ElNotification.error({
//       title: '文件上传错误',
//       message: msg
//     })
//   }
//   return task
// }

const handleFileRemove = (file: UploadFile) => {
  console.log('执行了文件删除: ', file)
}

/** 上传前回调事件 */
const handleBeforeUpload = (rawFile: UploadRawFile) => {
  console.log('文件上传前回调: ', rawFile)
  // 可以在这里校验
  return true;
}

/** 删除前回调事件 */
const handleBeforeRemove = (uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  console.log('文件删除前回调: ', uploadFile, uploadFiles)
}

// 上传成功回调
const handleUploadSuccess = (res: any, file: UploadFile) => {
  console.log('上传成功回调: ', file)
}

const handleUploadError = (error: Error, uploadFile: UploadFile, uploadFiles: UploadFiles) => {
  console.log('上传失败回调: ', error, uploadFile, uploadFiles)
}

// 文件改变事件
const handleFileChange = async (file: UploadFileVo, files: UploadFileVo[]) => {
  console.log('触发文件变更回调: ', file, files)
  // 文件超过x大小自动执行分片
  const id = await md5File(file.raw);
  console.log('md计算值: ', id)
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
        return { type: file.raw?.type, ...file };
      }
      return obj;
    })
  } else {
    let _file = { type: file.raw?.type, ...file }
    uploadFileList.value.push(_file);
  }
};

const handleFileDelete = (file) => {
  // 移除待上传的文件
  uploadFileRef.value!.handleRemove(file);
}

const handleAbort = (file) => {
  uploadFileRef.value!.abort(file);
}

// 文件个数超出
const handleExceed = () => {
  ElMessage({
    type: 'error',
    message: `上传文件数量不能超过 ${uploader.limit} 个!`,
  })
}

/**
 * 手动上传
 */
const doUpload = () => {
  uploadFileRef.value!.submit();
}

onMounted(() => {
  // 组件每次重新加载时候都会执行
  console.log('组件加载: ', props.dirIds)
})

defineExpose({ doUpload });
</script>

<style scoped lang="scss">

</style>