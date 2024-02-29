<template>
  <div v-loading="loading" class="file-viewer-container" style="height: 100vh">
    <!-- excel -->
    <div v-if="type === 'xlsx'" class="viewer-area">
      <vue-office-excel
          :src="getFileSrc"
          style="height: 100%;"
          @rendered="renderedHandler"
          @error="errorHandler"
      >
      </vue-office-excel>
    </div>

    <!-- pdf -->
    <div v-else-if="type === 'pdf'" class="viewer-area">
      <vue-office-pdf
          :src="getFileSrc"
          style="height: 100%;"
          @rendered="renderedHandler"
          @error="errorHandler"
      >
      </vue-office-pdf>
    </div>

    <!-- word -->
    <div v-else-if="['doc', 'docx'].indexOf(type) > 0" class="viewer-area">
      <vue-office-docx
          :src="getFileSrc"
          style="height: 100%;"
          @rendered="renderedHandler"
          @error="errorHandler"
      >
      </vue-office-docx>
    </div>

    <!-- 图片 -->
    <div v-else-if="['png', 'jpg', 'jpeg', 'gif'].indexOf(type) > 0" class="viewer-area">
      <div class="image-viewer">
        <el-image class="image-viewer__image" :src="getFileSrc" :preview-src-list="[getFileSrc]" loading="eager" >
        </el-image>
      </div>
    </div>

    <!-- 未知文件 -->
    <div v-else class="viewer-area">
      <div class="file-default-viewer">
        <div class="file-default-viewer-icon" :style="{ backgroundImage: 'url(' + getFileSvg(type) + ')' }">
          <div class="title">
            未知文件类型
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup name="fileViewer">
import { getFileSvg } from "/@/utils/fti";
// import VueOfficeDocx from '@vue-office/docx';
// import VueOfficeExcel from '@vue-office/excel';
// import VueOfficePdf from "@vue-office/pdf";
const VueOfficeExcel = defineAsyncComponent(() => import('@vue-office/excel'));
const VueOfficePdf = defineAsyncComponent(() => import('@vue-office/pdf'));
const VueOfficeDocx = defineAsyncComponent(() => import('@vue-office/docx'));

import '@vue-office/excel/lib/index.css';

const loading = ref(false);
/** 组件传递 */
const props = defineProps({
  src: {
    type: String,
    default: () => '',
  },
  type: {
    type: String,
    default: () => 'none',
  }
})

const getFileSrc = computed(() => {
  console.log(props)
  return props?.src;
});

const renderedHandler = () => {
  console.log("渲染完成")
  // setTimeout(() => {
  //   loading.value = false;
  // }, 1500)
}

const errorHandler = () => {
  console.log("渲染失败")
}

onMounted(() => {
  // loading.value = true;
})
</script>

<style lang="scss" scoped>
:deep(.vue-office-excel-main) {
  box-sizing: content-box;

  * {
    box-sizing: content-box;
  }
}

// 隐藏element图片预览的关闭按钮
//:deep(.image-viewer ) {
//  .el-image-viewer__close {
//    display: none;
//  }
//}

.viewer-area {
  height: 100%;
  width: 100%;
}

.image-viewer {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;

  &__image {
    height: 500px;
  }
}

.file-default-viewer {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  background-color: #eaeef1;

  &-icon {
    height: 120px;
    width: 240px;
    margin: auto;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center center;
    position: relative;

    .title {
      position: absolute;
      bottom: -35px;
      text-align: center;
      width: 100%;
      font-size: larger;
      line-height: 1.5;

      /* 禁用文本复制 */
      -webkit-user-select: none; /* Safari */
      -moz-user-select: none; /* Firefox */
      -ms-user-select: none; /* IE10+/Edge */
      user-select: none;
    }
  }
}
</style>