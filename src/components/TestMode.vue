<template>
  <el-container>
    <el-main>

      <!--:rules="testRules"-->
      <el-form :model="testForm"
               ref="testForm" label-width="100px" id="defined-pane">

        <el-form-item label="学工号" prop="username">
          <el-col :span="24">
            <el-input v-model="testForm.username" autofocus></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="userId" prop="userId">
          <el-col :span="24">
            <el-input v-model="testForm.userId"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="imgBedToken" prop="imgBedToken">
          <el-col :span="24">
            <el-input v-model="testForm.imgBedToken"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="身份" prop="character">
          <el-col :span="24">
            <el-select v-model="testForm.character" placeholder="请选择身份">
              <el-option label="管理员" value="admin"></el-option>
              <el-option label="超级管理员" value="super-admin"></el-option>
              <el-option label="学生" value="student"></el-option>
              <el-option label="老师" value="teacher"></el-option>
            </el-select>
          </el-col>
        </el-form-item>


        <el-form-item label="选择地点" prop="place">
          <el-col :span="24">
            <el-select v-model="testForm.place" placeholder="请选择地点">
              <el-option label="文科图书馆" value="1"></el-option>
              <el-option label="理科图书馆" value="2"></el-option>
              <el-option label="医科图书馆" value="3"></el-option>
              <el-option label="张江图书馆" value="4"></el-option>
              <el-option label="古籍部" value="5"></el-option>
              <el-option label="李兆基图书馆" value="6"></el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="setArgus">设置全局</el-button>
        </el-form-item>

      </el-form>

    </el-main>

  </el-container>
</template>

<script>
import App from "@/App";

export default {
  name: "myTestMode",
  data() {
    return {
      testForm: {
        username: this.$cookies.get('currentUser').name,
        userId: this.$cookies.get('currentUser').id,
        character: this.$cookies.get('character'),
        place: this.$cookies.get('place'),
        imgBedToken: this.$cookies.get('imgBedToken')
      },


    }
  }, methods: {
    setArgus() {
      var form = this.testForm
      console.log(form.username)

      var data = {
        id: form.userId,
        username: form.username,
        character: form.character,
        // token: resp.headers.token
        token: this.$cookies.get('token'),
        imgBedToken: form.imgBedToken,
        place: form.place
      }
      this.$store.commit('login', data);
    }
  }
}
</script>

<style scoped>

</style>