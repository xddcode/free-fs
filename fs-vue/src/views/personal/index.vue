<template>
  <div class="personal layout-pd">
    <el-row>
      <!-- 个人信息 -->
      <el-col :xs="24" :sm="16">
        <el-card shadow="hover" header="个人信息">
          <div class="personal-user">
            <div class="personal-user-left">
              <el-upload class="h100 personal-user-left-upload" action="https://jsonplaceholder.typicode.com/posts/"
                         multiple :limit="1">
                <img :src="userStores.avatar"/>
              </el-upload>
            </div>
            <div class="personal-user-right">
              <el-row>
                <el-col :span="24" class="personal-title mb18">
                  {{ currentTime }}，{{ userStores.nickname }}，生活变的再糟糕，也不妨碍我变得更好！
                </el-col>
                <!--								<el-col :span="24">-->
                <!--									<el-row>-->
                <!--										<el-col :xs="24" :sm="8" class="personal-item mb6">-->
                <!--											<div class="personal-item-label">昵称：</div>-->
                <!--											<div class="personal-item-value">小柒</div>-->
                <!--										</el-col>-->
                <!--										<el-col :xs="24" :sm="16" class="personal-item mb6">-->
                <!--											<div class="personal-item-label">身份：</div>-->
                <!--											<div class="personal-item-value">超级管理</div>-->
                <!--										</el-col>-->
                <!--									</el-row>-->
                <!--								</el-col>-->
                <!--								<el-col :span="24">-->
                <!--									<el-row>-->
                <!--										<el-col :xs="24" :sm="8" class="personal-item mb6">-->
                <!--											<div class="personal-item-label">登录IP：</div>-->
                <!--											<div class="personal-item-value">192.168.1.1</div>-->
                <!--										</el-col>-->
                <!--										<el-col :xs="24" :sm="16" class="personal-item mb6">-->
                <!--											<div class="personal-item-label">登录时间：</div>-->
                <!--											<div class="personal-item-value">2021-02-05 18:47:26</div>-->
                <!--										</el-col>-->
                <!--									</el-row>-->
                <!--								</el-col>-->
              </el-row>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 消息通知 -->
      <el-col :xs="24" :sm="8" class="pl15 personal-info">
        <el-card shadow="hover">
          <template #header>
            <span>消息通知</span>
            <span class="personal-info-more">更多</span>
          </template>
          <div class="personal-info-box">
            <ul class="personal-info-ul">
              <li v-for="(v, k) in state.newsInfoList" :key="k" class="personal-info-li">
                <a :href="v.link" target="_block" class="personal-info-li-title">{{ v.title }}</a>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <!-- 存储配置 -->
      <el-col :span="24">
        <el-card v-loading="loading" shadow="hover" class="mt15 personal-edit" header="存储配置">
          <div v-for="(item, index) in storagePlatforms" :key="index" class="personal-edit-safe-box">
            <div class="personal-edit-safe-item">
              <div class="personal-edit-safe-item-left">
                <div class="personal-edit-safe-item-left-label">{{ item.name }}</div>
                <div class="personal-edit-safe-item-left-value">
                  配置状态：<el-tag v-if="item.status">已配置</el-tag><el-tag v-else type="warning">未配置</el-tag>
                </div>
              </div>
<!--              <div class="personal-edit-safe-item-left">-->
<!--                <div class="personal-edit-safe-item-left-value">-->
<!--                  配置状态：<el-tag v-if="item.status">已配置</el-tag><el-tag v-else type="warning">未配置</el-tag>-->
<!--                </div>-->
<!--              </div>-->
<!--              <div class="personal-edit-safe-item-left">-->
<!--                <div class="personal-edit-safe-item-left-value">-->
<!--                  启用状态：-->
<!--                  <el-switch-->
<!--                    v-model="item.enabled"-->
<!--                    :disabled="!item.status"-->
<!--                    active-text="启用" inactive-text="停用"-->
<!--                    :active-action-icon="Select"-->
<!--                    :inactive-action-icon="CloseBold"-->
<!--                    @change="handleChangeEnabled($event, item)"-->
<!--                    v-loading="switchLoading"-->
<!--                  />-->
<!--                </div>-->
<!--              </div>-->
              <div class="personal-edit-safe-item-right">
                <el-button type="primary" @click="handleSetupSetting(item)">立即配置</el-button>
              </div>
            </div>
          </div>
          <div class="personal-edit-safe-box">
            <div class="personal-edit-safe-item flex-center">
              后续将支持更多存储平台，敬请期待。
              <div class="waves-circle color-primary">
                <SvgIcon name="ele-More" :size="20"></SvgIcon>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="formDialog.visible"
               :title="formDialog.title"
               width="600px"
               append-to-body
               :style="{ borderRadius: '15px' }">
      <el-form :model="form" :rules="rules" ref="fileFormRef" label-position="top" label-width="auto">
        <el-form-item
            v-for="item in currentPlatformConfig"
            :key="item.identifier"
            :label="item.label"
            :prop="item.identifier"
            :rules="{
              required: item.validation.required,
              message: `${item.label}不能为空`,
              trigger: 'blur',
            }"
        >
          <el-input v-model="form[item.identifier]" type="text" :placeholder="`请填写${item.label}`" />
          <el-alert v-if="item.description" :title="item.description" type="info" show-icon class="mt-2" :closable="false" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="formDialog.visible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" v-loading="formDialog.buttonLoading">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="personal">
import { Select, CloseBold } from '@element-plus/icons-vue';
import { formatAxis } from '/@/utils/formatTime';
import store from '/@/stores'
import { useUserInfo } from '/@/stores/modules/userInfo';
import { useStorageApi } from '/@/api/storage';
import { PlatformStructure, StoragePlatformVO, StorageSettingForm, StorageSettingVO } from '/@/api/storage/types'
import { newsInfoList, recommendList } from './mock';
import { ElMessage } from "element-plus";
import to from "await-to-js";

const userStores = useUserInfo(store);

// 定义变量内容
const state = reactive<PersonalState>({
  newsInfoList,
  recommendList,
  personalForm: {
    name: '',
    email: '',
    autograph: '',
    occupation: '',
    phone: '',
    sex: '',
  },
});

// 当前时间提示语
const currentTime = computed(() => {
  return formatAxis(new Date());
});
const loading = ref(true);
const switchLoading = ref(false);

const formDialog = reactive({
  visible: false,
  title: '',
  buttonLoading: false,
})
const fileFormRef = ref<ElFormInstance>();
// 表单是动态生成的
const form = ref({});
const rules = ref({});

const storagePlatforms = ref<StoragePlatformVO[]>([]);
// 加载存储平台配置结构
const loadStoragePlatforms = async () => {
  loading.value = true;
  const res = await useStorageApi().getStoragePlatformsConfig();
  storagePlatforms.value = res.data;
  loading.value = false;
};

const storageSetting = ref<StorageSettingVO>({});
const currentPlatformConfig = ref<Array<PlatformStructure>>([]);
const currentPlatform = ref<string>('');
const handleSetupSetting = async (vo?: StoragePlatformVO) => {
  currentPlatformConfig.value = vo?.configScheme;
  currentPlatform.value = vo?.identifier;
  if (vo?.configScheme.length === 0) {
    ElMessage.error(`${vo.name}存储平台配置错误, 请联系管理员.`);
    return;
  }
  if (vo?.status) {
    // 已配置, 获取配置明细
    const res = await useStorageApi().getStorageSetting(vo.identifier);
    // 在这里绑定数据
    storageSetting.value = res.data;
    form.value = res.data.configData;
    formDialog.title = '修改配置';
  } else {
    formDialog.title = '新增配置';
  }
  formDialog.visible = true;
}

const handleSave = async () => {
  fileFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      const data: StorageSettingForm = { id: undefined, platformIdentifier: "", configData: JSON.stringify(form.value) };
      formDialog.buttonLoading = true;
      if (storageSetting) {
        data.id = storageSetting.value.id;
      }
      data.platformIdentifier = currentPlatform.value;
      await useStorageApi().saveStorageSetting(data).finally(() => formDialog.buttonLoading = false);
      ElMessage.success('修改成功');
      await loadStoragePlatforms();
      formDialog.visible = false;
    }
  })
}

/**
 * #TODO Yann 未完成施工
 * @param event
 * @param item
 */
const handleChangeEnabled = async (event, item: StoragePlatformVO) => {
  switchLoading.value = true;
  const [err, res] = await to(useStorageApi().toggleSettingStatus(item.identifier));
  if (err) {
    console.log(err)
    event = !event;
  }
  switchLoading.value = false;
  ElMessage.success('状态切换成功')
}

watch(()=> formDialog.visible, (val) => {
  if (!val) {
    storageSetting.value = {};
    form.value = {};
  }
})

onMounted(() => {
  loadStoragePlatforms();
})
</script>

<style scoped lang="scss">
@import '../../theme/mixins/index.scss';

.personal {
  .personal-user {
    height: 130px;
    display: flex;
    align-items: center;

    .personal-user-left {
      width: 100px;
      height: 130px;
      border-radius: 3px;

      :deep(.el-upload) {
        height: 100%;
      }

      .personal-user-left-upload {
        img {
          width: 100%;
          height: 100%;
          border-radius: 3px;
        }

        &:hover {
          img {
            animation: logoAnimation 0.3s ease-in-out;
          }
        }
      }
    }

    .personal-user-right {
      flex: 1;
      padding: 0 15px;

      .personal-title {
        font-size: 18px;
        @include text-ellipsis(1);
      }

      .personal-item {
        display: flex;
        align-items: center;
        font-size: 13px;

        .personal-item-label {
          color: var(--el-text-color-secondary);
          @include text-ellipsis(1);
        }

        .personal-item-value {
          @include text-ellipsis(1);
        }
      }
    }
  }

  .personal-info {
    .personal-info-more {
      float: right;
      color: var(--el-text-color-secondary);
      font-size: 13px;

      &:hover {
        color: var(--el-color-primary);
        cursor: pointer;
      }
    }

    .personal-info-box {
      height: 130px;
      overflow: hidden;

      .personal-info-ul {
        list-style: none;

        .personal-info-li {
          font-size: 13px;
          padding-bottom: 10px;

          .personal-info-li-title {
            display: inline-block;
            @include text-ellipsis(1);
            color: var(--el-text-color-secondary);
            text-decoration: none;
          }

          & a:hover {
            color: var(--el-color-primary);
            cursor: pointer;
          }
        }
      }
    }
  }

  .personal-recommend-row {
    .personal-recommend-col {
      .personal-recommend {
        position: relative;
        height: 100px;
        border-radius: 3px;
        overflow: hidden;
        cursor: pointer;

        &:hover {
          i {
            right: 0px !important;
            bottom: 0px !important;
            transition: all ease 0.3s;
          }
        }

        i {
          position: absolute;
          right: -10px;
          bottom: -10px;
          font-size: 70px;
          transform: rotate(-30deg);
          transition: all ease 0.3s;
        }

        .personal-recommend-auto {
          padding: 15px;
          position: absolute;
          left: 0;
          top: 5%;
          color: var(--next-color-white);

          .personal-recommend-msg {
            font-size: 12px;
            margin-top: 10px;
          }
        }
      }
    }
  }

  .personal-edit {
    .personal-edit-title {
      position: relative;
      padding-left: 10px;
      color: var(--el-text-color-regular);

      &::after {
        content: '';
        width: 2px;
        height: 10px;
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        background: var(--el-color-primary);
      }
    }

    .personal-edit-safe-box {
      border-bottom: 1px solid var(--el-border-color-light, #ebeef5);
      padding: 15px 0;

      .personal-edit-safe-item {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .personal-edit-safe-item-left {
          flex: 1;
          overflow: hidden;

          .personal-edit-safe-item-left-label {
            color: var(--el-text-color-regular);
            margin-bottom: 5px;
          }

          .personal-edit-safe-item-left-value {
            color: var(--el-text-color-secondary);
            @include text-ellipsis(1);
            margin-right: 15px;
          }
        }
      }

      &:last-of-type {
        padding-bottom: 0;
        border-bottom: none;
      }
    }
  }
}
</style>
