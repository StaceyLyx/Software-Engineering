import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

var is_logout_post = false;
const Any = {
    state: {
        verbose: 1,
        teacherRole: {
            teacher: 'teacher'
        }, studentRole: {
            student: 'student',
        }, userRole: {
            student: 'student',
            teacher: 'teacher'
        }, adminRole: {
            admin: 'admin',
            'super-admin': 'super-admin',
        }, superAdminRole: {
            'super-admin': 'super-admin'
        },
        currentCharacter: String,
        library: {
            1: "文科图书馆", 2: "理科图书馆", 3: "医科图书馆",
            4: "张江图书馆", 5: "古籍部", 6: "李兆基图书馆"
        }
    },
    mutations: {
        login(state, data) {
            let cookies = Vue.$cookies
            if (data.hasOwnProperty('id') && data.hasOwnProperty('token')) {

                cookies.set('currentUser',
                    {id: data.id, name: data.username}, '10min');
                cookies.set('character', data.character, '10min');
                cookies.set('token', data.token, '10min');

                if (data.imgBedToken)
                    cookies.set('imgBedToken', data.imgBedToken, '10min');
                else
                    cookies.set('imgBedToken',
                        "10ae5f57f97bfcb9e82cf2b3690038cf", '10min');

                state.currentCharacter = data.character

                if (state.currentCharacter in state.adminRole)
                    cookies.set('place', data.place, '10min');

                console.log("state:", state)

                console.log("[登录成功] 您的ID为", data.id, ", 学号为", data.username,
                    "; Cookie为", cookies, "; Place为", data.place);
            } else
                console.log('[cookie]:前端连接出错，请检查login，无法设置cookie');

        },
        logout(state) {
            let cookies = Vue.$cookies
            console.log("Vue cookie:", cookies);

            if (this.getters.isLogin) {
                cookies.set('currentUser', null, '0s');
                cookies.set('character', null, '0s');
                cookies.set('token', 1, '0s');
                cookies.set('place', null, '10min');
                state.currentCharacter = null

                console.log("[登出完成]")

            } else
                console.log('[登出失败] 已经登出，无法登出');

        }, setMsg(state, msg) {
            Vue.prototype.$message({
                message: msg,
                type: 'info'
            })
        }
    },
    getters: {
        isUser: (state, getters) => {
            return (state.currentCharacter in state.userRole)

        }, isAdmin: (state, getters) => {
            return (state.currentCharacter in state.adminRole)

        }, isSuperAdmin: (state, getters) => {
            return (state.currentCharacter in state.superAdminRole)

        }, isLogin: (state, getters) => {
            return getters.isUser || getters.isAdmin;

        }, isVisitor: (state, getters) => {
            return !getters.isLogin

        }, verbose: (state, getters) => {
            return state.verbose
        }, library: (state, getters) => (libraryId) => {
            console.log("store, library, id:", libraryId)
            return state.library[libraryId]
        }, allLibrary:(state, getters)=>{
            return state.library
        }
    }
}


export default new Vuex.Store({
    // actions: {},
    modules: {
        Any: Any
    }
})