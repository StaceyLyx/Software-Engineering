<template>
  <el-container class="">
    <el-main>
      <div style="width: fit-content;margin: auto; margin-top: 50px">
        <el-form :model="adminForm" :rules="adminFormRules"
                 ref="adminForm" label-width="100px"
                 class="demo-ruleForm" id="register-defined-pane">

          <el-form-item label="学工号" prop="username">
            <el-col :span="24">
              <el-input v-model="adminForm.username" placeholder="输入管理员账户名" autofocus></el-input>
            </el-col>
          </el-form-item>

          <el-form-item label="默认密码" prop="password">
            <el-col :span="24">
              <el-input v-model="adminForm.password" :placeholder="defaultPass" disabled></el-input>
            </el-col>
            <br/>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-col :span="24">
              <el-input v-model="adminForm.email" placeholder="请输入账户同名邮箱">
                <template slot="append">{{ appendix }}</template>
              </el-input>
            </el-col>
          </el-form-item>

          <el-form-item label="选择角色" prop="character">
            <el-col :span="24">
              <el-select v-model="adminForm.character" placeholder="请选择角色">
                <el-option label="管理员" value="admin"></el-option>
              </el-select>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('adminForm')">提交</el-button>
            <el-button @click="resetForm('adminForm')">重置</el-button>
            <span class="alertMsg">{{ alertMsg }}</span>
          </el-form-item>
        </el-form>
      </div>

      <el-dialog title="管理员添加成功！" :visible.sync="centerDialogVisible" width="30%" center>
        <hr/>
        <span>学工号：{{ adminForm.username }}</span>
        <br/>
        <span>密码：{{ adminForm.password }}</span>
        <br/>
        <span>注册邮箱：{{ adminForm.email }}</span>
        <hr/>

        <span slot="footer" class="dialog-footer">
              <el-button type="primary" @click="centerDialogVisible=!centerDialogVisible">关闭</el-button></span>
      </el-dialog>
    </el-main>

  </el-container>

</template>

<script>
import axios from "axios";

export default {
  name: "AddAdmin",
  data() {
    const checkUser = (rule, value, callback) => {

      const user_success = /^[-]|^[A-z][A-z0-9_\-]+/;// [A-z].所有大小写的字母；^\- | ^[A-z]匹配开头为-或者字母
      const user_len = /[\-A-z0-9_]{6,23}/;
      if (value.match(user_success) && value.match(user_len)) {
        // 异步通讯检测用户名存在，参数列表存疑
        if (value.indexOf('^') > 0 || value.indexOf('/') > 0 || value.indexOf('\\') > 0) {
          return callback(new Error('只能包含6-32个字⺟，数字或两种特殊字符（-_）且只能以字⺟或-开头'));
        } else
          return axios({
            method: 'post',
            url: '/register/username',
            data: {username: this.adminForm.username},
            // username: this.adminForm.username
          }).then(resp => {
            if (resp.status == 200)
              return callback();
            else
              return callback(new Error('后端数据库返回值无法识别'));
          }).catch(error => {
            if (error.response.status == 400)
              return callback(new Error('用户名已存在！'));
            else if (error.response.status == 403 || error.response.status == 500) {
              return callback(new Error('用户名非法！'));
            } else {
              console.log(error);
              return callback(new Error('后端数据库返回值无法识别'));
            }
          });
      }
      return callback(new Error('只能包含6-32个字⺟，数字或两种特殊字符（-_）且只能以字⺟或-开头'));
    }
    const checkEmail = (rule, value, callback) => {
      if (value == this.adminForm.username)
        return callback();
      else return callback(new Error("请输入账户同名邮箱！"))
    };
    return {
      alertMsg: '',
      centerDialogVisible: false,
      defaultPass: 'pass1234',
      appendix: '@fudan.edu.cn',
      adminForm: {
        username: '', password: this.defaultPass, email: '', character: 'admin'
      },
      adminFormRules: {
        username: [
          {required: true, message: '管理员账号不能为空', trigger: 'blur'},
          {validator: checkUser, trigger: 'blur'},
          {min: 6, max: 32, message: '⻓度为6-32个字符', trigger: 'change'},
        ],
        // password: [{required: true, message: '管理员密码不能为空', trigger: 'blur'}],
        email: [{required: true, message: '邮箱不能为空', trigger: 'blur'},
          {validator: checkEmail, trigger: 'blur'}],
        character: [{required: true, message: '管理员密码不能为空', trigger: 'blur'}],

      }
    }

  }, methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/register/submit', {
            username: this.adminForm.username,
            password: this.defaultPass,
            email: this.adminForm.email + this.appendix,
            authority: this.adminForm.character
          }).then(resp => {
            if (resp.status == 201) // 注册成功
              this.centerDialogVisible = true;
          }).catch(error => {
            if (error.response.status == 400) {
              this.alertMsg = "注册失败"
              console.log(error.response.data.message);
            } else {
              console.log(error)
              this.alertMsg = '请求错误';
            }
          })
        } else {
          console.log('填写有误');
          this.alertMsg = '填写有误'
        }
      });
    }, resetForm(formName) { // 清空表单
      this.$refs[formName].resetFields();
      this.adminForm.password = this.defaultPass
      this.alertMsg = ''

    },
  }
}
</script>

<style scoped>

</style>