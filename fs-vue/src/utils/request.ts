import axios, { AxiosInstance } from 'axios';
import { ElMessage, ElMessageBox } from 'element-plus';
import errorCode from "/@/utils/errorCode";
import { Session } from "/@/utils/storage";
import { useUserInfo } from "/@/stores/modules/userInfo";
import { HttpStatus } from "/@/types/enums/RespEnum";
import { getToken } from "/@/utils/authToken";

// 是否显示重新登录
export const isRelogin = { show: false };
// 配置新建一个 axios 实例
axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
const service: AxiosInstance = axios.create({
	baseURL: import.meta.env.VITE_API_URL,
	timeout: 50000,
	// paramsSerializer: {
	// 	serialize(params) {
	// 		return qs.stringify(params, { allowDots: true });
	// 	},
	// },
});

// 添加请求拦截器
service.interceptors.request.use(
	(config) => {
		const isToken = (config.headers || {}).isToken === false;
		// 是否需要防止数据重复提交
		const isRepeatSubmit = (config.headers || {}).repeatSubmit === false;
		// 在发送请求之前做些什么 token
		if (getToken() && !isToken) {
			config.headers!['Authorization'] = 'Bearer ' + getToken();
		}
		// get请求映射params参数
		if (config.method === 'get' && config.params) {
			let url = config.url + '?' + tansParams(config.params);
			url = url.slice(0, -1);
			config.params = {};
			config.url = url;
		}
		if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
			const requestObj = {
				url: config.url,
				data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
				time: new Date().getTime()
			};
			const sessionObj = Session.get('sessionObj');
			if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
				Session.set('sessionObj', requestObj);
			} else {
				const s_url = sessionObj.url; // 请求地址
				const s_data = sessionObj.data; // 请求数据
				const s_time = sessionObj.time; // 请求时间
				const interval = 500; // 间隔时间(ms)，小于此时间视为重复提交
				if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
					const message = '数据正在处理，请勿重复提交';
					console.warn(`[${s_url}]: ` + message);
					return Promise.reject(new Error(message));
				} else {
					Session.set('sessionObj', requestObj);
				}
			}
		}
		// FormData数据去请求头Content-Type
		if (config.data instanceof FormData) {
			delete config.headers['Content-Type'];
		}
		return config;
	},
	(error) => {
		// 对请求错误做些什么
		console.log(error);
		return Promise.reject(error);
	}
);

// 添加响应拦截器
service.interceptors.response.use(
	(res) => {
		// 未设置状态码则默认成功状态
		const code = res.data.code || HttpStatus.SUCCESS;
		// 获取错误信息
		const msg = errorCode[code] || res.data.msg || errorCode['default'];
		// 二进制数据则直接返回
		if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
			return res.data;
		}
		if (code === 401) {
			// prettier-ignore
			if (!isRelogin.show) {
				isRelogin.show = true;
				ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
					confirmButtonText: '重新登录',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					isRelogin.show = false;
					useUserInfo().logout().then(() => {
						location.href = import.meta.env.VITE_APP_CONTEXT_PATH + 'index';
					});
				}).catch(() => {
					isRelogin.show = false;
				});
			}
			return Promise.reject('无效的会话，或者会话已过期，请重新登录。');
		} else if (code === HttpStatus.SERVER_ERROR) {
			ElMessage({ message: msg, type: 'error' });
			return Promise.reject(new Error(msg));
		} else if (code === HttpStatus.WARN) {
			ElMessage({ message: msg, type: 'warning' });
			return Promise.reject(new Error(msg));
		} else if (code !== HttpStatus.SUCCESS) {
			ElNotification.error({ title: msg });
			return Promise.reject('error');
		} else {
			return Promise.resolve(res.data);
		}
	},
	(error) => {
		// 对响应错误做点什么
		if (error.message.indexOf('timeout') != -1) {
			ElMessage.error('网络超时');
		} else if (error.message == 'Network Error') {
			ElMessage.error('网络连接错误');
		} else {
			if (error.response.data) ElMessage.error(error.response.statusText);
			else ElMessage.error('接口路径找不到');
		}
		return Promise.reject(error);
	}
);

/**
 * 参数处理
 * @param {*} params  参数
 */
export const tansParams = (params: any) => {
	let result = '';
	for (const propName of Object.keys(params)) {
		const value = params[propName];
		const part = encodeURIComponent(propName) + '=';
		if (value !== null && value !== '' && typeof value !== 'undefined') {
			if (typeof value === 'object') {
				for (const key of Object.keys(value)) {
					if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
						const params = propName + '[' + key + ']';
						const subPart = encodeURIComponent(params) + '=';
						result += subPart + encodeURIComponent(value[key]) + '&';
					}
				}
			} else {
				result += part + encodeURIComponent(value) + '&';
			}
		}
	}
	return result;
};

// 导出 axios 实例
export default service;
