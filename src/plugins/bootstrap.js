import {BootstrapVue, IconsPlugin} from "bootstrap-vue";
import Vue from 'vue'
// install Bootstrap
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)

// 用于清空自带的默认样式：padding和字体
import "bootstrap-vue/dist/bootstrap-vue.css"
import "bootstrap/dist/css/bootstrap.css"