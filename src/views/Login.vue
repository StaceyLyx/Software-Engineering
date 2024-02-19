<template>
  <el-container class="loginWindow">

    <el-main>
      <el-form :model="ruleForm" :rules="rules"
               ref="ruleForm" label-width="100px" id="defined-pane">

        <el-form-item label="学工号" prop="username">
          <el-col :span="24">
            <el-input v-model="ruleForm.username" autofocus></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="24">
            <el-input v-model="ruleForm.password" show-password></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="选择身份" prop="character">
          <el-col :span="24">
            <el-select v-model="ruleForm.character" placeholder="请选择身份">
              <el-option label="管理员" value="admin"></el-option>
              <el-option label="超级管理员" value="super-admin"></el-option>
              <el-option label="学生" value="student"></el-option>
              <el-option label="老师" value="teacher"></el-option>
            </el-select>
          </el-col>
        </el-form-item>


        <el-form-item label="选择地点" prop="place" :style="shownPlace?'':'display: none'">
          <el-col :span="24">
            <el-select v-model="ruleForm.place" placeholder="请选择地点">
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
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <span class="alertMsg">{{ alertMsg }}</span>
        </el-form-item>

      </el-form>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    const checkChar = (rule, value, callback) => {
      this.shownPlace = (value == 'admin' || value == 'super-admin');
      return callback()
    }

    const checkPlace = (rule, value, callback) => {
      if (!this.shownPlace)
        return callback();

      if (value == '') return callback(new Error("管理员需要选择上班地点"));
      else return callback();
    }
    return {
      alertMsg: '',
      ruleForm: {username: '', password: '', character: 'admin', place: ''},
      rules: {
        username: [{required: true, message: '学工号不可为空', trigger: 'blur'}],
        password: [{required: true, message: '密码不可为空', trigger: 'blur'}],
        character: [
          {required: true, message: '请选择身份', trigger: 'blur'},
          {validator: checkChar, trigger: 'change'}],
        place: [{validator: checkPlace, trigger: 'change'}]
      },
      shownPlace: true
    };
  }, methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) this.login();
      });
    },
    login() {


      var chara = this.ruleForm.character;
      if (chara == 'super-admin')
        chara = "superAdmin"
      this.$axios({
        method: 'post',
        url: '/login?authority=' + chara,
        data: {
          username: this.ruleForm.username,
          password: this.ruleForm.password,
        }
      }).then(resp => {

        if (resp.status == 200) {
          var data = {
            id: resp.data.userAuthorityId,
            username: this.ruleForm.username,
            character: this.ruleForm.character,
            // token: resp.headers.token
            token: resp.data.token,
            place: this.ruleForm.place

          };

          this.$message.success("登陆成功!跳转到主页...")

          this.$store.commit('login', data);
          this.$router.push('/') // 跳转到主页

          return
        }

        this.alertMsg = '连接出错了';

      }).catch(error => {
        console.log(error.response)
        if (error.response.status == 401 || error.response.status == 400 || error.response.status == 403) {
          this.alertMsg = '用户名或密码或身份错误';
        } else {
          console.log("后端发生错误：", error)
          this.alertMsg = '后端连接错误';
        }
      })

    }
  }
}


</script>
<style src="../assets/my_css/app.css">
</style>
