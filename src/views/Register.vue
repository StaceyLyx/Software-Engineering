<template>
  <el-container class="registerWindow">
    <el-main>
      <el-form :model="ruleForm" :rules="rules"
               ref="ruleForm" label-width="100px"
               class="demo-ruleForm" id="register-defined-pane">

        <el-form-item label="学工号" prop="username">
          <el-col :span="24">
            <el-input v-model="ruleForm.username" placeholder="输入11位学工号" autofocus></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-col :span="24">
            <el-input v-model="ruleForm.password" placeholder="输入6-32位字⺟、数字或者特殊字符(-_),并⾄少包含两种类型" show-password></el-input>
          </el-col>
          <br/>
          <el-col :span="24">
            <el-progress :percentage="percentage" :color="progressColors"
                         :text-inside="true" :stroke-width="20"
                         :format="passwordStrengthBar"></el-progress>
          </el-col>
        </el-form-item>

        <el-form-item label="确认密码" prop="password_2">
          <el-col :span="24">
            <el-input v-model="ruleForm.password_2" placeholder="请再次输入相同密码确认" show-password></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-col :span="24">
            <el-input v-model="ruleForm.email" placeholder="请输入学号邮箱">
              <template slot="append">{{appendix}}</template>
            </el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="选择角色" prop="character">
          <el-col :span="24">
            <el-select v-model="ruleForm.character" placeholder="请选择角色">
              <el-option label="学生" value="student"></el-option>
              <el-option label="老师" value="teacher"></el-option>
            </el-select>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <span class="alertMsg">{{ alertMsg }}</span>
        </el-form-item>

      </el-form>

      <el-dialog title="注册成功！" :visible.sync="centerDialogVisible" width="30%" center>
        <hr/>
        <span>学工号：{{ ruleForm.username }}</span>
        <br/>
        <span>密码：{{ ruleForm.password }}</span>
        <br/>
        <span>注册邮箱：{{ ruleForm.email }}</span>
        <hr/>

        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="goToLogin">去登陆</el-button>
  </span>
      </el-dialog>
    </el-main>
  </el-container>
</template>


<script>
import axios from "axios";

export default {
  name: "Register",
  data() {

    const checkUser = (rule, value, callback) => {
      const user_success = /^[0-9]+/; // 11位学号
      const user_len = /[0-9]{11}/;
      // 异步通讯检测学工号存在，参数列表存疑
      if (value.match(user_success) && value.match(user_len)) {
        this.ruleForm.email = value;

        return axios({
          method: 'post',
          url: '/register/username',
          data: {username: this.ruleForm.username},
          username:  this.ruleForm.username
        }).then(resp => {
          if (resp.status == 200)
            return callback();
          else
            return callback(new Error('后端数据库返回值无法识别'));
        }).catch(error => {
          if (error.response && error.response.status == 400)
            return callback(new Error('学工号已存在！'));
          else if (error.response && (error.response.status == 403 || error.response.status == 500)) {
            return callback(new Error('学工号非法！'));
          } else {
            console.log(error);
            return callback(new Error('后端数据库返回值无法识别'));
          }
        });

      }

      return callback(new Error('学工号为11位数字'));
    };

    const checkPassword = (rule, value, callback) => {
      const REG_NEW = /^[A-z0-9\-_]{6,32}$/; // 字⺟，数字或者特殊字符（-_）
      // 至少两种==不能只有一种
      const REG_NUM = /^[0-9]{6,32}$/; // 只有数字若干
      const REG_CHAR = /^[A-z]{6,32}$/; // 只有字母若干
      const REG_SPEC = /^[_\-]{6,32}$/; // 只有-和_若干
      const REG_NUM2 = /[0-9]/; // 存在数字
      const REG_CHAR2 = /[A-z]/; // 存在字母
      const REG_SPEC2 = /[_\-]/; // 存在-和_

      if (!value.match(REG_NEW)) {
        return callback(new Error("无效密码：6-32位字⺟，数字或者特殊字符（-_）⾄少包含两种"));
      }

      // 不能包含账号
      if (this.ruleForm.username !== '' &&
          value.toString().includes(this.ruleForm.username.toString()))
        return callback(new Error("密码不能包含账号"));

      // '字⺟，数字或者特殊字符（-_）⾄少包含两种';
      if (value.match(REG_NEW) && !value.match(REG_NUM) && !value.match(REG_CHAR)
          && !value.match(REG_SPEC)) {
        // 合法密码：中强度密码
        this.percentage = 67;
        // 字母数字特殊符号都存在-强密码
        if (value.match(REG_NUM2) && value.match(REG_CHAR2) && value.match(REG_SPEC2))
          this.percentage = 100;
        return callback();
      } else {
        // 非法字符密码
        if (!value.match(REG_NEW)) {
          this.percentage = 0;
          return callback(new Error("非法字符：字⺟，数字或者特殊字符（-_）⾄少包含两种"))
        } else if (value.match(REG_NUM) || value.match(REG_CHAR) || value.match(REG_SPEC)) {
          // 无效低强度密码
          this.percentage = 33;
        }
        return callback(new Error("弱密码：字⺟，数字或者特殊字符（-_）⾄少包含两种"));
      }
    };

    const checkPassword2 = (rule, value, callback) => {
      if (value === this.ruleForm.password) return callback();
      else return callback(new Error("两次输入的密码不一致"));
    };

    const checkEmail = (rule, value, callback) => {
      if(value == this.ruleForm.username)
      return callback();
      else return callback(new Error("请输入学号邮箱！"))
    };

    return {
      appendix:'@fudan.edu.cn',
      alertMsg: '',

      isExist: null,
      centerDialogVisible: false,
      percentage: 0,
      progressColors: [
        {color: '#f56c6c', percentage: 33},
        {color: '#e6a23c', percentage: 67},
        {color: '#5cb87a', percentage: 100}
      ], ruleForm: {
        username: '',
        password: '',
        password_2: '',
        email: '',
        character: ''
      }, rules: {
        username: [
          //只能包含字⺟，数字或两种特殊字符（-_）且只能以字⺟或-开头 : /^[-]|^[A-z][A-z0-9_\-]+/;
          {required: true, message: '学工号不能为空', trigger: 'blur'},
          {validator: checkUser, trigger: 'blur'},
          {min:11, max: 11, message: '⻓度为11个数字', trigger: 'change'},
        ], password: [
          {required: true, message: '密码不能为空', trigger: 'change'},
          {min: 6, max: 32, message: '⻓度为6-32个字符', trigger: 'change'},
          {validator: checkPassword, trigger: 'change'}
        ], password_2: [
          {required: true, message: '不能为空', trigger: 'blur'},
          {validator: checkPassword2, trigger: 'change'}
        ], email: [
          {required: true, message: '邮箱不能为空', trigger: 'blur'},
          {validator: checkEmail, trigger: 'blur'}
        ], character: [
          {required: true, message: '身份不能为空', trigger: 'blur'},
        ]
      } // Ending the definition of rules.
    }; // Ending the return statement.
  } // Ending the data definition.
  ,
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/register/submit', {
            username: this.ruleForm.username,
            password: this.ruleForm.password,
            email: this.ruleForm.email+this.appendix,
            authority: this.ruleForm.character
          }).then(resp => {
            if (resp.status == 201) // 注册成功
              this.centerDialogVisible = true;
          }).catch(error => {
            if (error.response.status == 400) {
              this.alertMsg = "注册失败, 用户名或邮箱重复"
              console.log(error.response.data.message);
            } else {
              console.log(error)
              this.alertMsg = '请求错误';
            }
          })
        } else {
          console.log('填写有误');
          return false;
        }
      });
    },
    resetForm(formName) { // 清空表单
      this.$refs[formName].resetFields();
    },
    passwordStrengthBar(percentage) { // 密码强度信息提示
      if (percentage === 0)
        return '无效密码';
      else if (percentage === 33)
        return '低强度密码';
      else if (percentage === 67)
        return '中强度密码';
      else return '高强度密码';
    },
    goToLogin() { // 注册成功
      this.centerDialogVisible = false;
      this.$router.replace({path: '/login'}); // 跳转到登陆页面
    }
  }
};

</script>


<style scoped>

</style>