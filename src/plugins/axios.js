import axios from 'axios'
import Vue from "vue";

import store from "@/store";
import App from "@/App";
import router from "@/router";
import qs from "qs";

Vue.prototype.$axios = axios;

// 拦截并stringify所有post参数
// 拦截并为所有请求的请求头加上token
axios.interceptors.request.use((request) => {
        console.log("..拦截发送请求...     ", request)
        var isLogin = store.getters.isLogin;

        // 添加域名
        if (request.url.indexOf('http') < 0) {
            // request.url = 'https://www.fastmock.site/mock/b02a7b063aff24d06a76c3c496a21e3f/account'
            //     + request.url;
            request.url = 'http://localhost:8181' + request.url;
            // request.url = 'http://120.78.151.169:8181' + request.url;
        }


        if (isLogin) { // 检查token，阻止发送请求
            var token = Vue.$cookies.get('token');
            // Authorization
            // console.log("...检查本地token...", token);
            if (!token) {
                console.log("[main.js]身份过期，请重新登录")        // token失效
                store.commit('logout'); // reset cookie状态
                App.methods.syncCookie(cookies)

                if (router.path == '/') // 技巧：先定向到login，但是会被拦截器认为是已登录用户，强制跳转主页/,实现刷新
                    router.replace("/login");
                else
                    router.replace("/");
            }

        }
        if (request.method == 'post' || request.method == 'get') {
            request.data = qs.stringify(request.data)
            request.headers.token = token; // 为所有请求的header加上token
        }
        // console.log("请求通过，发送请求，method=", request.method)
        return request;

    },
    (error) => Promise.reject(error)
)


axios.interceptors.response.use
((response) => {
    console.log("...拦截后端响应...", response)
    var token = Vue.$cookies.get('token');
    var isLogin = store.getters.isLogin;

    var backend_token = response.data.token

    if (!isLogin) {// 不需要检查token 对于登录而言，此方法在设置cookie之前，不会存在冲突
        // 跳过login和register的检查
    } else if (backend_token == token) {
        Vue.$cookies.set('token', backend_token, '10min');
        console.log("请求成功，延长cookie时间为10min")
    }
    // console.log("return response")
    return response;
}, (error) => {
    // console.log("[main.js]处理响应error", error)
    if (!error.response) {    // 原因：请求发生错误
        console.log("响应错误：信息", error.message)
    }// 状态
    var isLogin = store.getters.isLogin;

    var status = error.response.status;
    var errorText = codeMessage[status] || error.response.statusText

    // if (isLogin && status == 401) {
    //     Vue.prototype.$message.error("Token失效")
    //     return Promise.reject(error)
    // }
    // else if (status != 401 && status != 400 && status != 403)
    //     Vue.prototype.$message.warning("响应状态：" + status);
    return Promise.reject(error)
})

// 状态码错误信息
const codeMessage = {
    200: '服务器成功返回请求的数据。',
    201: '新建或修改数据成功。',
    202: '一个请求已经进入后台排队（异步任务）。',
    204: '删除数据成功。',

    400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
    401: '用户没有权限（令牌、用户名、密码错误）。',
    403: '用户得到授权，但是访问是被禁止的。',
    404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
    406: '请求的格式不可得。',
    410: '请求的资源被永久删除，且不会再得到的。',
    422: '当创建一个对象时，发生一个验证错误。',
    500: '服务器发生错误，请检查服务器。',
    502: '网关错误。',
    503: '服务不可用，服务器暂时过载或维护。',
    504: '网关超时。',
};
