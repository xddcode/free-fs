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
                <el-breadcrumb-item>根目录</el-breadcrumb-item>
                <el-breadcrumb-item>目录一</el-breadcrumb-item>
                <el-breadcrumb-item>目录二</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </div>
          <div class="right-header">
            <el-button type="primary" size="default" @click="uploadDialog.visible = true">
              上传文件
              <SvgIcon class="el-icon--right" name="iconfont icon-shangchuan"></SvgIcon>
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
                <el-tooltip :content="file.name" effect="light">
                  <el-text class="file-item-name" truncated>{{ file.name }}</el-text>
                </el-tooltip>
                <div class="file-item-time">{{ file.putTime || '--' }}</div>
              </div>
              <!--              </fs-options>-->
            </div>
          </div>
        </el-scrollbar>

        <!--        <target-box :on-drop="handleFileDrop"></target-box>-->
      </div>
    </el-card>

    <el-dialog
        v-model="uploadDialog.visible"
        :title="uploadDialog.title"
        :close-on-click-modal="false"
        :close-on-press-escape="false"
        width="800px"
    >
      <file-uploader/>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelUpload">取消</el-button>
          <el-button type="primary" @click="submitUpload" :loading="uploadDialog.uploadLoading">
            确认上传
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="filesManager">
import { ArrowRight } from '@element-plus/icons-vue'
import { getFileSvg } from "/@/utils/fti";
import TargetBox from "/@/components/fs/targetBox.vue";
import { useFilesApi } from "/@/api/files";
import { defineAsyncComponent } from "vue";
import to from "await-to-js";
import { FileVO } from "/@/api/files/types";

const FileUploader = defineAsyncComponent( () => import('/@/views/files/uploader.vue') );

const cardBodyRef = ref( 0 );
const uploadDialog = reactive( {
  visible: false,
  title: '上传文件',
  uploadLoading: false,
} );

// 文件集合
const fileList = ref<FileVO[]>( [] );

//下拉菜单右键打开新的，要关闭之前的
const fileOperationsRef = ref();
const handleVisible = ( id: string | number, visible: boolean ) => {
  if ( !visible ) return;
  fileOperationsRef.value.forEach( ( item: any ) => {
    if ( item.id === id ) return;
    item.handleClose();
  } );
}

/** 文件拖拽事件 */
const droppedFiles = ref( [] );
const handleFileDrop = ( item: any ) => {
  console.log( item )
  if ( item ) {
    droppedFiles.value = item.files
  }
}

/* 手动点击上传 */
const submitUpload = () => {
  // if (uploadFileList.value.length > 0) {
  //   // 校验列表中是否存在上传失败的文件
  //   // let failList = uploadFileList.value.filter(item => item.status === 'fail');
  //   // for (let i = 0; i < failList.length; i++) {
  //   //   failList[i].status = 'ready';
  //   //   handleUploadFile(failList[i]);
  //   // }
  //
  //   uploadDialog.uploadLoading = true;
  //   uploadFileRef.value!.submit();
  // } else {
  //   ElMessage.error({
  //     message: '未选择要上传的文件..',
  //     type: 'warning',
  //   });
  // }
  uploadDialog.visible = false;
}

const cancelUpload = () => {
  // if (uploadDialog.uploadLoading) {
  //   uploadFileRef.value!.abort();
  //   uploadDialog.uploadLoading = false;
  // } else {
  //   ElMessage.error({
  //     message: '暂无文件正在上传..',
  //     type: 'warning',
  //   });
  // }
  uploadDialog.visible = false;
}


// 获取文件列表
const loadFileList = async () => {
  const [err, res] = await to( useFilesApi().fileList( {} ) );
  if ( !err ) {
    fileList.value = res.data;
  } else {

  }
}

onMounted( () => {
  loadFileList();
} )
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