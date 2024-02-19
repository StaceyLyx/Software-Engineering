import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"

import Home from '../views/Home.vue'
import Login from '../views/Login'
import Register from '../views/Register'
import Search from "@/views/Search";

import Details from "@/components/Books/Details"
import AddBook from "@/components/Books/AddBook";
import DeleteBook from "@/components/Books/DeleteBook";
import ModifyBook from "@/components/Books/ModifyBook";
import SearchBook from "@/components/Books/SearchBook";
import SearchAuthor from "@/components/Authors/SearchAuthor";
import ModifyAuthor from "@/components/Authors/ModifyAuthor";
import AddAuthor from "@/components/Authors/AddAuthor";
import Book from "@/components/Book";
import Author from "@/components/Author";
import Manage from "@/views/Manage";
import User from "@/views/User";
import AuthorDetails from "@/components/Authors/AuthorDetails";
// 页面：1. 需要User 2. 需要ADMIN 3. 禁止user
import Error400 from "@/components/Error400"
import AddAdmin from "@/components/Admins/AddAdmin";
import Admin from "@/components/Admin";
import Borrow from "@/components/Borrow";
import Return from "@/components/Return";
import BorrowCheckReserve from "@/components/Borrow/BorrowCheckReserve";
import borrowCheck from "@/components/Borrow/BorrowCheck";
import ReturnCheck from "@/components/Return/ReturnCheck";

Vue.use(VueRouter)

const routes = [
    {
        number: 1,
        path: '/',
        name: '主页',
        component: Home,
        meta: {
            requireLogin: false, banLogin: false,
            // 登录需要
            // 身份需要
            requireUser: false,
            requireStudent: false, requireTeacher: false,
            requireAdmin: false,
        },
    }, {
        number: 2,
        path: '/search',
        name: '查询',
        meta: {},
        component: Search,

    }, {
        number: 3,
        path: '/login',
        name: '登录',
        component: Login,
        meta: {
            banLogin: true
        },
        style: 'float: right;'
    }, {
        number: 4,
        path: '/register',
        name: '注册',
        component: Register,
        meta: {
            banLogin: true
        },
        style: 'float: right;'
    }, {
        number: 5,
        path: '/user',
        name: '个人',
        component: User,
        meta: {
            requireLogin: true
        },

    }, {
        number: 6,
        path: '/manage',
        name: '管理',
        component: Manage,
        meta: {
            requireLogin: true,
            requireAdmin: true
        },
        children: [
            {
                name: '管理员管理',
                path: '/manage/admins',
                component: Admin,
                children: [{
                    id: 1, path: '/manage/addAdmin',
                    meta: {
                        requireLogin: true,
                        requireSuperAdmin: true
                    },
                    name: '添加管理员',
                    component: AddAdmin
                }]
            }, {
                name: '书籍管理',
                path: '/manage/books',
                component: Book,
                children: [{
                    id: 1,
                    path: '/manage/addBook',
                    name: '添加书籍',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: AddBook
                }, {
                    id: 2,
                    path: '/manage/deleteBook',
                    name: '删除书籍',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: DeleteBook
                }, {
                    id: 3,
                    path: '/manage/modifyBook',
                    name: '修改书籍',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: ModifyBook
                }, {
                    id: 4,
                    path: '/manage',
                    name: '查找书籍',
                    // meta: {requireUser: true, requireAdmin: true},
                    component: SearchBook
                }]
            }, {
                name: '作者管理',
                path: '/authors',
                component: Author,
                children: [{
                    path: '/manage/addAuthor',
                    name: '添加作者',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: AddAuthor
                }, {
                    path: '/manage/modifyAuthor',
                    name: '修改作者',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: ModifyAuthor
                }, {
                    path: '/manage/searchAuthor',
                    name: '查找作者',
                    meta: {
                        requireLogin: true,
                        requireAdmin: true
                    },
                    component: SearchAuthor
                }]
            }
            , {
                name: '图书借阅',
                path: '/manage/',
                component: Borrow,
                children: [{
                    name: '普通借书',
                    path:'/manage/borrowBook',
                    component: borrowCheck
                },{
                    name: '预约借书',
                    path:'/manage/borrowReservedBook',
                    component: BorrowCheckReserve
                }]
            }, {
                name: '图书归还',
                path: '/manage/returnBook',
                component: Return,
                children: [{
                    name: '普通还书',
                    path:'/manage/returnBook',
                    component: ReturnCheck
                }]
            }
//             、、name: '书籍管理',
//     path: '/manage/books',
//     component: Book,
//     children: [{
//     id: 1,
//     path: '/manage/addBook',
//     name: '添加书籍',
//     meta: {
//         requireLogin: true,
//         requireAdmin: true
//     },
//     component: AddBook
// }, {
        ]
    }, {
        path: '/books/:bookid',
        name: '详情',
        meta: {
            requireAdmin: false, requireUser: false
        },
        component: Details
    }, {
        path: '/authors/*',
        name: '作家详情',
        meta: {requireAdmin: false, requireUser: false},
        component: AuthorDetails
    }, {
        path: '/error400',
        name: '400错误',
        component: Error400
    }
];

const router = new VueRouter({
    routes,
    mode: "history"
})

export default router


// 前端登录拦截+管理员权限拦截
var is_intercept_when_router_changed = false;
router.beforeEach(function (toPath, fromPath, goNext) {
        // 需要登录的界面，拦截
        var isLogin = store.getters.isLogin
        var isAdmin = store.getters.isAdmin
        var isSuperAdmin = store.getters.isSuperAdmin
        var isUser = store.getters.isUser

        // 页面权限检测和身份匹配
        // 需要user
        if (toPath.matched.some(record => record.meta.requireLogin || record.meta.requireUser ||
            record.meta.requireAdmin || record.meta.requireSuperAdmin)) {
            if (isLogin) {
                // 需要超级管理员
                if (toPath.matched.some(record => record.meta.requireSuperAdmin)) {
                    if (isSuperAdmin) goNext();
                    else {
                        store.commit('setMsg', "需要超级管理员权限; 您不是超级管理员")
                        goNext({path: '/', query: {redirect: toPath.path}});
                    }
                }
                // 需要管理员
                if (toPath.matched.some(record => record.meta.requireAdmin)) {
                    if (isAdmin) goNext();
                    else {
                        store.commit('setMsg', "需要管理员权限; 您不是管理员")
                        goNext({path: '/', query: {redirect: toPath.path}});
                    }
                }
                // 用户身份required
                if (toPath.matched.some(record => record.meta.requireUser)) {
                    if (isUser) goNext();
                    else {
                        store.commit('setMsg', "需要学生/教师权限; 您不是学生/教师")
                        goNext({path: '/', query: {redirect: toPath.path}});
                    }
                }
                // 仅需要login
                goNext()
            } else { // 未登陆
                store.commit('setMsg', "需要登录才可以访问，您未登录")
                goNext({path: '/login', query: {redirect: toPath.fullPath}})
            }
        }

        // 禁止已登录
        else if (toPath.matched.some(record => record.meta.banLogin)) {
            if (isLogin) {
                store.commit('setMsg', "您已登陆，不可访问该页面");
                console.log('您已登陆不可访问');
                goNext({
                    path: '/',
                    query: {redirect: toPath.fullPath} //
                })
            } else goNext();

        } else // 没有限制
            goNext();

    }
)
