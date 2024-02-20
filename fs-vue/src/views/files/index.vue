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
              <folder-breadcrumb :on-click="handleClickBreadcrumb" :folder-id="queryParams.dirId"></folder-breadcrumb>
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

      <div v-loading="loading" ref="cardBodyRef" class="file-card-body" @contextmenu.prevent="showContextMenu"
           @click="hideContextMenu">
        <!-- 主体以文件列表为主 -->
        <el-scrollbar>
          <div class="file-grid-container">
            <div v-for="(file, index) in fileList"
                 class="file-grid-item" :key="index"
                 @contextmenu.prevent.stop="showFileItemContextMenu($event, file)"
                 @click="handleClickFileItem(file)"
            >
              <!-- 文件主体 -->
              <div class="file-item-box">
                <div class="file-icon">
                  <!-- 拖拽监听的是文件, 不想让监听这里 -->
                  <div class="file-icon__img" :style="{ backgroundImage: 'url(' + getFileSvg(file.type) + ')' }"></div>
                </div>
                <el-tooltip :content="file.name" effect="light">
                  <el-text class="file-item-name" truncated>{{ file.name }}</el-text>
                </el-tooltip>
                <div class="file-item-time">{{ file.putTime || '--' }}</div>
              </div>
            </div>
          </div>
        </el-scrollbar>
        <!--        <target-box :on-drop="handleFileDrop"></target-box>-->
      </div>
    </el-card>

    <!-- 右键菜单: 放在template的第一级元素下 -->
    <context-menu ref="contextMenuRef"></context-menu>

    <!-- 新增|编辑文件夹/文件 -->
    <el-dialog v-model="formDialog.visible"
               :title="formDialog.title"
               width="300px"
               append-to-body
               :style="{ borderRadius: '15px' }">

      <div class="file-form-icon">
        <div class="file-form-icon__img" :style="{ backgroundImage: 'url(' + getFileSvg(formDialog.type) + ')' }"></div>
      </div>
      <el-form :model="form" :rules="rules" ref="fileFormRef">
        <el-form-item>
          <el-input v-model="form.name" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="formDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveFile" v-loading="formDialog.buttonLoading">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 上传框 -->
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

    <!-- 预览框 -->
    <el-dialog v-model="viewerDialog.visible" title="文件预览" class="viewer-dialog" fullscreen destroy-on-close>
      <file-viewer :src="viewerValue"></file-viewer>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="filesManager">
import { ArrowRight } from '@element-plus/icons-vue'
import { getFileSvg } from "/@/utils/fti";
import TargetBox from "/@/components/fs/targetBox.vue";
import { useFilesApi } from "/@/api/files";
import { DirVo, FileForm, FileQuery, FileVO } from "/@/api/files/types";

/**  引入文件组件  */
const FileViewer = defineAsyncComponent(() => import('/@/components/fileViewer/index.vue'))
const FolderBreadcrumb = defineAsyncComponent(() => import('/@/components/folderBreadcrumb/index.vue'))
const FileUploader = defineAsyncComponent(() => import('/@/views/files/uploader.vue'));
const ContextMenu = defineAsyncComponent(() => import('/@/components/contextMenu/index.vue'))
const contextMenuRef = ref();
/** 页面变量 */
const loading = ref(true);

/** 上传弹窗 */
const uploadDialog = reactive({
  visible: false,
  title: '上传文件',
  uploadLoading: false,
});

/** 表单弹窗 */
const formDialog = reactive({
  visible: false,
  title: '',
  type: '',
  buttonLoading: false,
});
const initFormData: FileForm = {
  id: '',
  pid: '',
  name: '新建文件夹',
}
// form表单
const fileFormRef = ref<ElFormInstance>();
const data = reactive<PageData<FileForm, FileQuery>>({
  form: { ...initFormData },
  queryParams: {
    dirId: -1,
    pageNum: 1,
    pageSize: 20
  },
  rules: {}
})
const { queryParams, form, rules } = toRefs(data);

/** 文件集合 */
const fileList = ref<FileVO[]>([]);

// ****************  文件夹面包屑  ********************
const handleClickBreadcrumb = (pid) => {
  queryParams.value.dirId = pid;
  loadFileList();
}
// **************************************************

// ****************  文件夹预览  ********************
const viewerDialog = reactive({
  visible: false,
})
const viewerValue = ref('')
// **************************************************

/** 文件拖拽事件 */
const droppedFiles = ref([]);
const handleFileDrop = (item: any) => {
  console.log(item)
  if (item) {
    droppedFiles.value = item.files
  }
}

/* 手动点击上传 */
const submitUpload = () => {
  uploadDialog.visible = false;
}
const cancelUpload = () => {
  uploadDialog.visible = false;
}

/** 鼠标右键文件事件 */
const showFileItemContextMenu = (event, file: FileVO) => {
  const items = [
    {
      id: 1,
      label: "查看",
      event: () => {
        console.log(file)
      },
      icon: 'ele-View'
    },
    {
      id: 2,
      label: "复制",
      event: () => {
        console.log('2--------')
      },
      icon: 'ele-CopyDocument',
    },
  ];
  const items2 = [
    {
      id: 3,
      label: "重命名",
      event: () => {
        console.log('3--------')
      },
      icon: 'ele-EditPen'
    },
    {
      id: 4,
      label: "删除",
      event: () => {
        console.log('4--------')
      },
      icon: 'ele-Delete'
    }
  ];
  if (file.isDir) {
    contextMenuRef.value.show(event, [...items2]);
  } else {
    contextMenuRef.value.show(event, [...items, ...items2]);
  }
}

/** 鼠标右键事件 */
const showContextMenu = (event) => {
  const items = [
    {
      id: 1,
      label: "新建文件夹",
      event: () => {
        formDialog.title = '新建文件夹';
        formDialog.type = 'dir';
        formDialog.visible = true;
      },
      icon: 'ele-FolderAdd'
    },
    {
      id: 2,
      label: "上传文件",
      event: () => {
        console.log('2--------')
      },
      icon: 'ele-DocumentAdd',
    },
    {
      id: 3,
      label: "刷新页面",
      event: () => {
        loadFileList();
      },
      icon: 'ele-Refresh'
    }
  ];
  contextMenuRef.value.show(event, items);
}

const hideContextMenu = () => {
  contextMenuRef.value.hide();
}

// 获取文件列表
const loadFileList = async () => {
  loading.value = true;
  const res = await useFilesApi().fileList(queryParams.value);
  fileList.value = res.data;
  loading.value = false;
}

// 点击文件
const handleClickFileItem = (file: FileVO) => {
  console.log('File: ', file)
  if (file.isDir) {
    // 跳转文件夹
    queryParams.value.dirId = file.id;
    loadFileList();
  } else {
    viewerValue.value = file.url;
    viewerDialog.visible = true;
  }
}

// 保存文件|目录
const handleSaveFile = async () => {
  fileFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      formDialog.buttonLoading = true;
      if (form.value.id) {

      } else {
        // 判断是否为目录
        if (formDialog.type === 'dir') {
          await useFilesApi().addFolder(form.value).finally(() => formDialog.buttonLoading = false)
        }
      }
      formDialog.visible = false;
      await loadFileList();
    }
  })
}

onMounted(() => {
  loadFileList();
})
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

.viewer-dialog {
  max-height: 100vh;
  .el-dialog__body {
    max-height: 100vh;
  }
}
</style>