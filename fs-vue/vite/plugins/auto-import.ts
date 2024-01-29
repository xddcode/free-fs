import AutoImport from 'unplugin-auto-import/vite';

export default (path: any) => {
  return AutoImport({
    // 自动导入 Vue 相关函数
    imports: ['vue', 'vue-router', '@vueuse/core', 'pinia'],
    eslintrc: {
      enabled: false,
      filepath: './.eslintrc-auto-import.json',
      globalsPropValue: true
    },
    resolvers: [
      // 自动导
    ],
    vueTemplate: true, // 是否在 vue 模板中自动导入
    dts: path.resolve(path.resolve(__dirname, '../../src'), 'types', 'auto-imports.d.ts')
  });
};
