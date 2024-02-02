import { resolve } from 'path';
import { ConfigEnv, defineConfig, loadEnv } from 'vite';
import { buildConfig } from './src/utils/build';
import createPlugins from './vite';

const pathResolve = (dir: string) => {
	return resolve(__dirname, '.', dir);
};

const alias: Record<string, string> = {
	'/@': pathResolve('./src/'),
	'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js',
};

const viteConfig = defineConfig(({mode, command}: ConfigEnv) => {
	const env = loadEnv(mode, process.cwd());
	return {
		plugins: createPlugins(env, command === 'build'),
		root: process.cwd(),
		resolve: { alias },
		base: command === 'serve' ? './' : env.VITE_PUBLIC_PATH,
		optimizeDeps: {
			include: [
				'vue',
				'vue-router',
				'pinia',
				'axios',
			],
			exclude: ['vue-demi']
		},
		server: {
			host: '0.0.0.0',
			port: env.VITE_PORT as unknown as number,
			open: JSON.parse(env.VITE_OPEN),
			hmr: true,
			proxy: {
				[env.VITE_APP_BASE_API]: {
					target: env.VITE_API_URL,
					changeOrigin: true,
					rewrite: (path) => path.replace(new RegExp('^' + env.VITE_APP_BASE_API), '')
				}
			},
		},
		build: {
			outDir: 'dist',
			chunkSizeWarningLimit: 1500,
			rollupOptions: {
				output: {
					chunkFileNames: 'assets/js/[name]-[hash].js',
					entryFileNames: 'assets/js/[name]-[hash].js',
					assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
					manualChunks(id) {
						if (id.includes('node_modules')) {
							return id.toString().match(/\/node_modules\/(?!.pnpm)(?<moduleName>[^\/]*)\//)?.groups!.moduleName ?? 'vender';
						}
					},
				},
				...(JSON.parse(env.VITE_OPEN_CDN) ? { external: buildConfig.external } : {}),
			},
		},
		css: { preprocessorOptions: { css: { charset: false } } },
		define: {
			__VUE_I18N_LEGACY_API__: JSON.stringify(false),
			__VUE_I18N_FULL_INSTALL__: JSON.stringify(false),
			__INTLIFY_PROD_DEVTOOLS__: JSON.stringify(false),
			__NEXT_VERSION__: JSON.stringify(process.env.npm_package_version),
			__NEXT_NAME__: JSON.stringify(process.env.npm_package_name),
		},
	};
});

export default viteConfig;
