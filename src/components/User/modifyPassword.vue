<template>
  <el-container>
    <el-form :model="modifyPassForm" :rules="modifyPassRule"
             ref="modifyPassForm" style="width: 500px">

      <el-form-item label="旧密码" prop="oldPass">
        <el-col :span="24">
          <el-input placeholder="请输入旧密码" v-model="modifyPassForm.oldPass" autofocus show-password></el-input>
        </el-col>
      </el-form-item>


      <el-form-item label="新密码" prop="newPass">
        <el-col :span="24">
          <el-input placeholder="请输入新密码" v-model="modifyPassForm.newPass" show-password></el-input>
        </el-col>
      </el-form-item>
      <hr/>

      <el-form-item>
        <el-button :icon="bt.icon" :disabled="bt.disabled"
                   :type="bt.type" @click="submitPassModify('modifyPassForm')">
          {{ bt.content }}
        </el-button>
        <span class="alertMsg">{{ alertMsg }}</span>
      </el-form-item>

    </el-form>
  </el-container>
</template>

<script>
import axios from "axios";

export default {
  name: "modifyPassword",
  data() {

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

      let username = this.$cookies.get('currentUser').name;
      // 不能包含账号
      if (username !== '' && value.toString().includes(username.toString()))
        return callback(new Error("密码不能包含账号"));

      // '字⺟，数字或者特殊字符（-_）⾄少包含两种';
      if (value.match(REG_NEW) && !value.match(REG_NUM) && !value.match(REG_CHAR) && !value.match(REG_SPEC)) { // 合法密码：中强度密码
        if (value.match(REG_NUM2) && value.match(REG_CHAR2) && value.match(REG_SPEC2)) ;// 字母数字特殊符号都存在-强密码
        return callback();
      } else {// 非法字符密码
        if (!value.match(REG_NEW)) {
          return callback(new Error("非法字符：字⺟，数字或者特殊字符（-_）⾄少包含两种"))
        } else if (value.match(REG_NUM) || value.match(REG_CHAR) || value.match(REG_SPEC)) {// 无效低强度密码
        }
        return callback(new Error("弱密码：字⺟，数字或者特殊字符（-_）⾄少包含两种"));
      }
    };

    return {
      bt: {
        content: '提交',
        type: 'primary',
        icon: 'el-icon-thumb',
        disabled: false
      },
      alertMsg: '',
      modifyPassForm: {oldPass: '', newPass: ''},
      modifyPassRule: {
        oldPass: [{required: true, message: '不可为空', trigger: 'blur'}],
        newPass: [
          {required: true, message: '不可为空', trigger: 'blur'},
          {validator: checkPassword, trigger: 'blur'}
        ]
      }
    }
  }, methods: {
    submitPassModify(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid)
          axios({
            method: 'post',
            url: '/account/modifyPassword',
            data: {
              username: this.$cookies.get('currentUser').name,
              oldPassword: this.modifyPassForm.oldPass,
              newPassword: this.modifyPassForm.newPass
            }
          }).then(resp => {
            if (resp.status == 200) {
              this.alertMsg = "修改密码成功";
              this.bt.content = "成功";
              this.bt.type = 'success';
              this.bt.icon = 'el-icon-success';
              this.bt.disabled = true;

              this.$refs[formName].resetFields();

            }

          }).catch(error => {
            if (error.response.status == 400)
              this.alertMsg = "原密码错误";
            else if (error.response.status == 406)
              this.alertMsg = "新密码与原密码一致/新密码非法";
            else
              this.alertMsg = "Mistake in new password";

            // test
            console.log("Error:", error);
          })
      });
    },

  }
}
</script>

<style scoped>

</style>