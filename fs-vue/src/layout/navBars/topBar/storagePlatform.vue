<script lang="ts" setup name="storagePlatformTopBar">
import { storeToRefs } from 'pinia'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { useFsConfig } from '/@/stores/modules/fs.store';
import { useStorageApi } from '/@/api/storage';
import to from 'await-to-js';
const storeFsConfig = useFsConfig();
const { fileStorage, fileStorageList } = storeToRefs(storeFsConfig);
const router = useRouter();

const onComponentStorageChange = async (storage) => {
  const [ err, res ] = await to(useStorageApi().checkStorageConfig(storage));
  if (!res?.data) {
    ElMessageBox.confirm('您好，您正在切换的存储平台暂未配置，请确认是否前往配置？', '提醒',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
      router.push('/personal');
    }).catch(() => {})
  } else {
    // 切换后台配置
    const [ err ] = await to(useStorageApi().toggleSettingStatus(storage));
    if (err) {
      ElMessage.error("存储平台切换失败: " + err.message);
    } else {
      fileStorage.value = storage;
      window.location.reload();
    }
  }
}

// 获取支持平台存储类型
const getStorageList = async () => {
  const [ err ] = await to(storeFsConfig.loadFileStorageList());
  if (err) {
    ElMessage.error('支持储存平台列表加载失败: ' + err.message)
  }
}

onMounted(() => {
  // 加载支持的存储平台
  getStorageList();
})
</script>

<template>
  <el-dropdown :show-timeout="70" :hide-timeout="50" trigger="click" @command="onComponentStorageChange">
    <div class="layout-navbars-breadcrumb-user-icon">
      <i class="iconfont icon-neiqianshujuchucun" :title="$t('message.user.title10')"></i>
      <span v-for="(item, index) in fileStorageList" :key="index" class="color-primary" style="margin-left: 3px;">
          <span v-if="item.key === fileStorage">[ {{ item.value }} ]</span>
        </span>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item v-for="item in fileStorageList"
                          :command="item.key"
                          :disabled="fileStorage === item.key"
                          :key="item.key"
        >{{ item.value }}</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<style scoped lang="scss">
.layout-navbars-breadcrumb-user {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  &-link {
    height: 100%;
    display: flex;
    align-items: center;
    white-space: nowrap;
    &-photo {
      width: 25px;
      height: 25px;
      border-radius: 100%;
    }
  }
  &-icon {
    padding: 0 10px;
    cursor: pointer;
    color: var(--next-bg-topBarColor);
    height: 50px;
    line-height: 50px;
    display: flex;
    align-items: center;
    &:hover {
      background: var(--next-color-user-hover);
      i {
        display: inline-block;
        animation: logoAnimation 0.3s ease-in-out;
      }
    }
  }
  :deep(.el-dropdown) {
    color: var(--next-bg-topBarColor);
  }
  :deep(.el-badge) {
    height: 40px;
    line-height: 40px;
    display: flex;
    align-items: center;
  }
  :deep(.el-badge__content.is-fixed) {
    top: 12px;
  }
}
</style>