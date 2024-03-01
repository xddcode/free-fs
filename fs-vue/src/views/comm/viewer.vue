<template>
  <div class="layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <file-viewer :src="query.src" :type="query.type"></file-viewer>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="filesViewer">
import { useRoute } from 'vue-router';  //1.先在需要跳转的页面引入useRouter
const { query, params } = useRoute();

const FileViewer = defineAsyncComponent(() => import('/@/components/fileViewer/index.vue'))
</script>

<style scoped>

</style>