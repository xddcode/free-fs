const FS_TOKEN_KEY = 'FS_TOKEN';
const tokenStorage = useStorage<null | string>(FS_TOKEN_KEY, null);

/** 获取token */
export const getToken = () => tokenStorage.value;

/** session设置token */
export const setToken = (token: string) => (tokenStorage.value = token);

/** 清除token */
export const rmToken = () => (tokenStorage.value = null);