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
// export const useUserInfo = defineStore('userInfo', {
//     state: () : UserInfo => ({
//         /** 用户名 */
//         username: '',
//         /** 昵称 */
//         nickname: '',
//         /** 头像 */
//         avatar: '',
//         /** 用户id */
//         userId: '',
//         /** 角色集合 */
//         roleList: [],
//         /** 权限集合 */
//         authList: [],
//     }),
//     actions: {
//         /** 用户登录 */
//         async login(data: LoginVo): Promise<void> {
//             const [ err, res ] = await to(useLoginApi().signIn(data));
//             if (res) {
//                 const data = res.data;
//                 setToken(data.token);
//                 // 用户信息
//                 this.$state = data.user;
//                 return Promise.resolve();
//             }
//             return Promise.reject(err);
//         },
//         /** 用户登出 */
//         async logout(): Promise<void> {
//             await useLoginApi().signOut();
//             this.$state = {
//                 username: '',
//                 nickname: '',
//                 avatar: '',
//                 userId: '',
//                 roleList: [],
//                 authList: [],
//             };
//             rmToken();
//             return Promise.resolve();
//         },
//         async setUserInfo(): Promise<void> {
//             const [err, res] = await to(useLoginApi().getUserInfo());
//             if (res) {
//                 const user = res.data;
//
//
//                 this.$state = user;
//             }
//             // if (Session.get(LOGIN_USER_KEY)) {
//             //     this.user = Session.get(LOGIN_USER_KEY);
//             // } else {
//             //     api获取用户信息
//             // }
//         }
//     },
//     persist: true
// });

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