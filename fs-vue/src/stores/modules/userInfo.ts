import { defineStore } from 'pinia';
import to from "await-to-js";
import { useLoginApi } from '/@/api/login';
import { LoginVo } from '/@/api/login/types';
import { setToken, rmToken } from "/@/utils/authToken";
import defAva from '/@/assets/imgs/def_avatar.jpg';
import store from "/@/stores";

/**
 * 用户信息
 * @methods setUserInfos 设置用户信息
 */
export const useUserInfo = defineStore('userInfo', () => {
        // 用户名
        const username = ref('');
        // 昵称
        const nickname = ref('');
        // 用户id
        const userId = ref<String | number>('');
        // 头像
        const avatar = ref('');
        // 角色集合
        const roleList = ref<Array<string>>([]);
        // 权限集合
        const authList = ref<Array<string>>([]);

        /** 用户登录 */
        const login = async (data: LoginVo): Promise<void> => {
            const [ err, res ] = await to(useLoginApi().signIn(data));
            if (res) {
                const data = res.data;
                setToken(data.token);
                return Promise.resolve();
            }
            return Promise.reject(err);
        };
        /** 用户登出 */
        const logout = async (): Promise<void> => {
            await useLoginApi().signOut();
            roleList.value = [];
            authList.value = [];
            rmToken();
        }
        /** 获取用户信息 */
        const getUserInfo = async (): Promise<void> => {
            const [ err, res ] = await to(useLoginApi().getUserInfo());
            if (res) {
                const user = res.data;
                const defAvatar = user.avatar == '' || user.avatar == null ? defAva : user.avatar;

                username.value = user.username;
                nickname.value = user.nickname;
                avatar.value = defAvatar;
                roleList.value = user.roleList;
                authList.value = user.authList;
                userId.value = user.id;
                return Promise.resolve();
            }
            return Promise.reject(err);
        }

        return {
            userId,
            nickname,
            avatar,
            roleList,
            authList,
            login,
            logout,
            getUserInfo,
        }
    },
    {
        persist: true,
    },);

export default useUserInfo;
// 非setup
export function useUserStoreHook() {
    return useUserInfo(store);
}