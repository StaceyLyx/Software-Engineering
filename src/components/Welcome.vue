<template>
  <el-container class="welcomeWindow">
    <div class="content">
      <div style="display: block; width: fit-content; margin: auto;font-style: italic;
       text-decoration-line: underline;">
        <h1 style=" font-weight: lighter">Welcome! </h1>
      </div>
      <hr/>

      <h2 style="display: block; width: fit-content; margin: auto;font-style: italic; margin-bottom: 10px">
        Here is a guide for
        <span>{{ character ? character : 'Visitor' }} </span>
      </h2>
      <el-row style="padding: 0">
        <div class="inner-content">
          <div class="my-info-input-group" v-if="isVisitor">
            <el-col :span="12" class="my-info-input">
              <el-input readonly value="You can search book and browser their details page.">
                <template class="my-info-input-ele" slot="prepend">Functions for u:</template>
              </el-input>
            </el-col>
            <el-col :span="18" class="my-info-input">
              <el-input readonly
                        value="Please login as a teacher or student for reserve books, or register a new account.">
                <template class="my-info-input-ele" slot="prepend">More:</template>
              </el-input>
            </el-col>
          </div>


          <div class="my-info-input-group" v-if="isAdmin">
            <el-col :span="12" class="my-info-input">
              <el-input readonly :value="'Your id in user-authority table is '+this.$cookies.get('currentUser').id">
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
      <div style="width: fit-content; margin: auto">
        <hr/>
        <el-button size="small" @click="Hello">Hello</el-button>
      </div>

    </div>

    <div v-if="!isVisitor">
      <el-dialog id="my-dialog" title="修改全局参数" :visible.sync="showModifyPass" width="50%" center>
        <subPage style="width: fit-content; margin: auto"/>
      </el-dialog>
    </div>

  </el-container>
</template>

<script>
import myTestMode from "@/components/TestMode";

export default {
  name: 'Welcome',
  components: {subPage: myTestMode},
  props: {
    msg: "1"
  }, data() {
    return {
      showModifyPass: false,

      character: String,
      isVisitor: Boolean,
      isAdmin: Boolean,
      isUser: Boolean
    }
  }, updated() {
    this.refreshAll()
  }, mounted() {
    this.refreshAll()
  }, methods: {
    logout() {
      this.$store.commit('logout'); // 在storage中reset登出
      this.$router.replace("/login");
    }, refreshAll() {

      if (this.$store.getters.verbose) console.log("Welcome: isAdmin: ", this.isAdmin)

      this.character = this.$cookies.get('character');
      this.isAdmin = this.$store.getters.isAdmin;
      this.isUser = this.$store.getters.isUser;
      this.isVisitor = this.$store.getters.isVisitor;

    }, Hello() {
      this.$message.info('Hello');
      this.showModifyPass = !this.showModifyPass;
    }
  }
}
</script>

<style src="../assets/my_css/app.css"></style>
<style src="../assets/my_css/welcome.css"></style>
<style>


</style>
