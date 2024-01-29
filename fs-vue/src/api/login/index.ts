import request from '/@/utils/request';
import { LoginVo } from "/@/api/login/types";

/**
 * （不建议写成 request.post(xxx)，因为这样 post 时，无法 params 与 data 同时传参）
 *
 * 登录api接口集合
 * @method signIn 用户登录
 * @method signOut 用户退出登录
 */
export function useLoginApi() {
	return {
		/** 登录 */
		signIn: (data: LoginVo) => {
			return request({
				url: '/auth/login',
				method: 'post',
				data,
			});
		},
		/** 登出 */
		signOut: () => {
			return request({
				url: '/auth/logout',
				method: 'get',
			});
		},
		/** 登录验证码 */
		getCodeImg: () => {
			return request({
				url: '/captcha',
				method: 'get',
			});
		},
		/** 获取用户信息 */
		getUserInfo: () => {
			return request({
				url: '/user/getInfo',
				method: 'get'
			})
		}
	};
}
