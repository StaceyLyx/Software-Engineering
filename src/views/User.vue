<template>
  <el-container class="userWindow">
    <div class="content">

      <div style="display: block; width: fit-content; margin: auto; margin-bottom: 10px;font-style: italic;
       text-decoration-line: underline;"><h1>Hello!</h1></div>


      <el-collapse v-model="activeNames" @change="handleChange">

        <div
            style="display: block; width: fit-content; margin: auto; margin-bottom: 10px; margin-top: 10px; font-style: italic;"
            class="myTag">
          <h2> <span>
        {{ (isUser ? 'User' + ": " + name : (isAdmin ? 'Admin' + ": " + name : 'Illegal User')) }}
        </span></h2>
        </div>

        <el-row style="padding: 0px">
          <div class="inner-content">
            <div class="my-info-input-group" v-if="isAdmin">
              <el-col :span="12" class="my-info-input">
                <el-input readonly :value="'Your id in user-authority table is '+id">
                  <template class="my-info-input-ele" slot="prepend">Basic message:</template>
                </el-input>
              </el-col>
              <el-col :span="12" class="my-info-input">
                <el-input readonly :value="this.$store.getters.library(this.$cookies.get('place'))">
                  <template class="my-info-input-ele" slot="prepend">Work place:</template>
                </el-input>
              </el-col>
              <el-col :span="12" class="my-info-input">
                <el-input readonly :value="'You can visit to do book and author management.'">
                  <template class="my-info-input-ele" slot="prepend">Functions for u:</template>
                </el-input>
              </el-col>
              <el-col :span="18" class="my-info-input">
                <el-input readonly :value="'If you want to do the reserve or borrow some books, ' +
               'please logout and then login as \'user\''">
                  <template class="my-info-input-ele" slot="prepend">More:</template>
                </el-input>
              </el-col>

            </div>
            <div class="my-info-input-group" v-if="isUser">
              <el-col :span="12" class="my-info-input">
                <el-input readonly :value="'Your id in user-authority table is '+id">
                  <template class="my-info-input-ele" slot="prepend">Basic message:</template>
                </el-input>
              </el-col>
              <el-col :span="12" class="my-info-input">
                <el-input readonly :value="'You can visit to \'search\' for books details.'">
                  <template class="my-info-input-ele" slot="prepend">Functions for u:</template>
                </el-input>
              </el-col>
              <el-col :span="18" class="my-info-input">
                <el-input readonly :value="'If you want to do the book or author management, ' +
               'please logout and then login as \'admin\''">
                  <template class="my-info-input-ele" slot="prepend">More:</template>
                </el-input>
              </el-col>
            </div>
          </div>

        </el-row>

        <div>
          <el-collapse-item name="account">
              <template class="my-collapse-title" slot="title" >
                <h4>账户信息</h4>
              </template>
            <div class="my-info-input-group">

              <el-col :span="12" class="my-info-input">
                <el-input v-model="userInfo.username" readonly>
                  <template class="my-info-input-ele" slot="prepend">{{ isUser ? '学号:' : '账号:' }}</template>
                </el-input>
              </el-col>
              <el-col :span="12" class="my-info-input">
                <el-input v-model="userInfo.authority" readonly>
                  <template class="my-info-input-ele" slot="prepend">权限:</template>
                </el-input>
              </el-col>
              <el-col :span="20" class="my-info-input">
                <el-input v-model="userInfo.email" placeholder="请输入学号邮箱" readonly>
                  <template class="my-info-input-ele" slot="prepend">邮箱:</template>
                </el-input>
              </el-col>

              <el-button class="my-info-input-button"
                         @click="showModifyPass = !showModifyPass">修改账户信息
              </el-button>
            </div>
          </el-collapse-item>
        </div>
        <br/>

        <div v-if="isUser">
          <el-collapse-item name="reservation">
            <template slot="title" class="my-collapse-title"><h4>查询预约记录</h4></template>
            <reservation-view :is-search="isSearch"/>
          </el-collapse-item>
        </div>
      </el-collapse>


    </div>

    <el-dialog id="my-dialog" title="修改密码" :visible.sync="showModifyPass" width="50%" center>
      <modifyPassword style="width: fit-content; margin: auto"/>
    </el-dialog>


  </el-container>
</template>
<script>

import modifyPassword from "@/components/User/modifyPassword";
import store from "@/store"
import reservationView from "@/components/User/reservationView";

export default {
  name: "User",
  components: {modifyPassword, reservationView},

  data() {
    return {
      isSearch: false,
      userInfo: {userId: '', username: '', email: '', authority: '', place: ''},
      appendix: '@fudan.edu.cn',

      activeNames: ["account"],
      showModifyPass: false,

      currentUser: Object,
      isAdmin: Boolean,
      isUser: Boolean,
      id: Number,
      place: String,
      name: String
    }
  }, updated() {
    this.freshAll()
  }, created() {
    this.freshAll()
  }, methods: {
    logout() {
      this.$store.commit('logout'); // 在storage中reset登出
      this.$router.replace("/login");
    }, freshAll() {

      this.currentUser = this.$cookies.get('currentUser');
      this.isAdmin = store.getters.isAdmin;
      this.isUser = store.getters.isUser;
      this.id = this.currentUser.id;
      this.name = this.currentUser.name;
      this.place = store.getters.library(this.$cookies.get('place'))

      if (store.getters.verbose) {

        console.log("User: $cookie.place:", this.$cookies.get('place'))

        console.log("User: isUser:", this.isUser)
        console.log("User: isAdmin:", this.isAdmin)
        console.log("User: id = ", this.id)
        console.log("User: place: ", this.place)
      }
    }, handleChange(val) {
      console.log("Handle change: ", val);
      for (var i in val) {
        console.log("the element of val is:", val[i])
        if (val[i] == 'reservation') {
          console.log(" val i == reservation! set true!")
          this.isSearch = true
        }
      }


    }


  }, mounted() {
    this.userInfo.userId = this.$cookies.get('currentUser').id;
    this.userInfo.username = this.$cookies.get('currentUser').name;
    this.userInfo.email = this.userInfo.username + this.appendix;
    this.userInfo.authority = this.$cookies.get('character');

    if (this.isAdmin && this.$cookies.get('place')) {
      this.userInfo.place = this.$cookies.get('place');
    }
  }
}
</script>

<style scoped src="../assets/my_css/app.css">

</style>