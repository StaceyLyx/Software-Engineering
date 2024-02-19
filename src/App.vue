<template>
  <el-container id="app">
    <el-menu id="menuNav" mode="horizontal" background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
             :default-active="activeIndex">

      <router-link to="/">
        <el-menu-item index="1" style="float: left">
          主页
        </el-menu-item>
      </router-link>
      <router-link to="/search">
        <el-menu-item index="2" style="float: left">
          搜索
        </el-menu-item>
      </router-link>

      <router-link to="/manage"><!-- 只有管理员才能执行书籍管理 -->
        <el-menu-item index="3" v-bind:class="{'hidden-component': !allowAdmin }"
                      style="float: left">
          管理
        </el-menu-item>
      </router-link>


      <el-menu-item disabled style="float: left;">
        {{ allowAdmin ? '管理员' : allowLogin ? '用户' : '游客' }}
      </el-menu-item>

      <router-link to="/login">
        <el-menu-item index="4" v-bind:class="{'hidden-component': !banLogin }" style="float: right;">
          登录
        </el-menu-item>
      </router-link>

      <router-link to="/register">
        <el-menu-item index="5" v-bind:class="{'hidden-component': !banLogin }" style="float: right;">
          注册
        </el-menu-item>
      </router-link>

      <router-link to="/user">
        <el-menu-item index="6" v-bind:class="{'hidden-component': !allowLogin }"
                      style="float: right;">个人
        </el-menu-item>
      </router-link>

      <router-link to="/">
        <el-menu-item index="7" @click="logout"
                      v-bind:class="{'hidden-component': !allowLogin }"
                      style="float: right;">登出
        </el-menu-item>
      </router-link>

    </el-menu>

    <router-view class="appSubWindow"/>

    <el-footer style="height: 0px"/>
  </el-container>
</template>

<script>


export default {
  name: 'app',
  data() {
    return {
      appTrigger: true,

      allowAdmin: Boolean,
      allowLogin: Boolean,
      banLogin: Boolean,
      activeIndex: this.$route.path
    };
  }, mounted() {
    this.syncCookie(this.$cookies);
  }, updated() {
    this.syncCookie(this.$cookies);
  }, methods: {
    logout() {
      this.$store.commit('logout'); // 在storage中reset登出
      this.syncCookie(this.$cookies);

      if (this.activeIndex == '/') // 技巧：先定向到login，但是会被拦截器认为是已登录用户，强制跳转主页/,实现刷新
        this.$router.replace("/login");
    }, syncCookie(cookies) {
      if (this.$store.getters.verbose) {
        console.log("character: ", this.$cookies.get("character"))
        console.log("syn... user \n", this.allowLogin, " admin ", this.allowAdmin)
      }
      this.allowLogin = this.$store.getters.isLogin
      this.allowAdmin = this.$store.getters.isAdmin
      this.banLogin = this.$store.getters.isVisitor;
      this.activeIndex = this.$route.path;

    }
  }
}
</script>

<style src="./assets/my_css/app.css">

</style>
