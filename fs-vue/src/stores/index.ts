import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

// 创建
const store = createPinia();
// pinia-plugin-persistedstate持久化插件
store.use(piniaPluginPersistedstate);

export default store;
