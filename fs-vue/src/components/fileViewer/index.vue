<template>
  <div class="file-viewer-container" style="height: 100vh">
    <vue-office-excel
      :src="getFileSrc"
      style="height: 100%"
      @rendered="renderedHandler"
      @error="errorHandler"
    >
    </vue-office-excel>
  </div>
</template>

<script lang="ts" setup name="fileViewer">
// import VueOfficeDocx from '@vue-office/docx';
import VueOfficeExcel from "@vue-office/excel";
// import VueOfficePdf from "@vue-office/pdf";
import { computed } from "vue";

import '@vue-office/excel/lib/index.css';

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
}

const errorHandler = () => {
  console.log("渲染失败")
}
</script>

<style scoped lang="scss">



</style>