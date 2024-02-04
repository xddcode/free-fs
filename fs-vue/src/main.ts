import { createApp } from 'vue';
import App from '/@/App.vue';
import store from '/@/stores';
import router from '/@/router';
import { directive } from '/@/directive';
import { i18n } from '/@/i18n';
import other from '/@/utils/other';

import ElementPlus from 'element-plus';
import '/@/theme/index.scss';
import VueGridLayout from 'vue-grid-layout';

const app = createApp(App);

directive(app);
other.elSvg(app);
app.use(ElementPlus);
app.use(router);
app.use(i18n);
app.use(store);
app.use(VueGridLayout);

app.mount('#app');