<template>
  <div v-loading="loading" class="file-viewer-container" style="height: 70vh">
    <vue-office-excel
      :src="getFileSrc"
      style="height: 100%;"
      @rendered="renderedHandler"
      @error="errorHandler"
    >
    </vue-office-excel>
  </div>
</template>

<script lang="ts" setup name="fileViewer">
// import VueOfficeDocx from '@vue-office/docx';
// import VueOfficeExcel from '@vue-office/excel';
// import VueOfficePdf from "@vue-office/pdf";
const VueOfficeExcel = defineAsyncComponent(() => import('@vue-office/excel'))

import '@vue-office/excel/lib/index.css';

const loading = ref(true);
/** 组件传递 */
const props = defineProps({
  src: {
    type: String,
    default: () => '',
  },
})

const getFileSrc = computed(() => {
  console.log(props)
  return props?.src;
});

const renderedHandler = () => {
  console.log("渲染完成")
  setTimeout(() => {
    loading.value = false;
  }, 1500)
}

const errorHandler = () => {
  console.log("渲染失败")
}

onMounted(() => {
  loading.value = true;
})
</script>

<style lang="scss" scoped>
:deep(.vue-office-excel-main) {
  box-sizing: content-box;

  * {
    box-sizing: content-box;
  }
}
</style>