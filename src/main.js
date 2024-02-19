import Vue from 'vue'
import App from './App.vue'

import store from './store'
import router from './router'

import './plugins/cookie.js'

import './plugins/element.js'
import './plugins/axios.js'
import './plugins/bootstrap.js'


// let k = {b:['a', 'b']}
// console.log("test:...",('a' in k.b))

let data = {
    id: 4,
    username: 'Test',
    // character: 'student',
    character:　'super-admin',
    place: '1',
    token: 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MTg5MjIzOTMsInVzZXJJRCI6IjEiLCJ1c2VybmFtZSI6InVzZXItdGVhY2hlcjEifQ.iR10znkt35JXwyFyQuMAdg2uI9lw4cmYmD7Ud3CnvYw'
}
store.commit("login", data)

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')


