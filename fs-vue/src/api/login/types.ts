export interface LoginVo {
    /** 用户名 */
    username: string;
    /** 密码 */
    password: string;
    /** 验证码 */
    code: string;
    /** 验证码uuid */
    uuid: string;
}

export interface UserInfo {
    /** 用户名 */
    username: string;
    /** 昵称 */
    nickname: string;
    /** 头像 */
    avatar: string;
    /** 用户id */
    userId: string;
    /** 角色集合 */
    roleList: [];
    /** 权限集合 */
    authList: [];
}