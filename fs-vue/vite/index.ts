import vue from '@vitejs/plugin-vue';
import vueSetupExtend from 'vite-plugin-vue-setup-extend-plus';
import viteCompression from 'vite-plugin-compression';
import createHtmlVariable from './plugins/html-variable';
import createAutoImport from './plugins/auto-import';
import path from 'path';

export default (viteEnv: any, isBuild = false): [] => {
    const vitePlugins: any = [];
    vitePlugins.push(vue());
    vitePlugins.push(vueSetupExtend());
    vitePlugins.push(viteCompression());
    vitePlugins.push(createAutoImport(path));
    vitePlugins.push(createHtmlVariable(viteEnv));
    return vitePlugins;
};