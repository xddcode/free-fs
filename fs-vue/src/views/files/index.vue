<template>
  <div class="layout-padding">
    <el-card shadow="hover" class="layout-padding-auto">
      <template #header>
        <!-- 头部: 面包屑, 左边一个小图标 -->
        <div class="file-card-header">
          <div class="left-header">
            <SvgIcon name="ele-Files" :size="30" color="#3498db" title="文件"/>
          </div>
          <div class="right-header">
            <el-breadcrumb :separator-icon="ArrowRight">
              <el-breadcrumb-item :to="{ path: '/' }">根目录</el-breadcrumb-item>
              <el-breadcrumb-item>目录一</el-breadcrumb-item>
              <el-breadcrumb-item>目录二</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </div>
      </template>

      <!-- 主体以文件列表为主 -->
      <div class="columns is-multiline">
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>
        <div class="column is-1"  shadow="always">
          <div class="file-box"></div>
        </div>



      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="fileList">
import {ArrowRight} from '@element-plus/icons-vue'
import {getAssetsFile} from "/@/utils/fti";
import {FileVO} from "/@/api/files/types";

const fileList = ref([]);
</script>

<style scoped lang="scss">

.file-card-header {
  display: flex;
  line-height: 30px;
  justify-content: left;
  align-items: center;

  .left-header {
    margin: 0 10px;
  }

  .right-header {
    //margin: auto 0;
  }
}

</style>