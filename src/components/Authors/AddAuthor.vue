<template>
  <el-container>
    <el-footer style="height: 0px"></el-footer>
    <el-main>
      <hr/>
      <el-form :model="ruleForm" :rules="rules"
               ref="ruleForm" label-width="100px" class="demo-ruleForm">

        <el-form-item label="姓名" prop="name">
          <el-col :span="24">
            <el-input v-model="ruleForm.name" show-title></el-input>
          </el-col>
          <br/>
        </el-form-item>


        <el-form-item label="出生日期" prop="birth">
          <el-date-picker
              v-model="ruleForm.birth"
              type="date"
              placeholder="选择日期">
          </el-date-picker>

        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-col :span="24">
            <el-input v-model="ruleForm.gender"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="国家" prop="country">
          <el-col :span="24">
            <el-input v-model="ruleForm.country"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="介绍" prop="introduction">
          <el-col :span="24">
            <el-input v-model="ruleForm.introduction"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>

      </el-form>
      <hr/>

      <el-dialog title="添加作者成功！" :visible.sync="centerDialogVisible" width="50%" center>
        <span>ID：{{ ruleForm.authorID }}</span>
        <span>姓名：{{ ruleForm.name }}</span>
        <span>性别：{{ ruleForm.gender }}</span>
        <span>出生日期：{{ ruleForm.birth }}</span>
        <span>国家：{{ ruleForm.country }}</span>
        <span>介绍：{{ ruleForm.introduction }}</span>

<!--        <span slot="footer" class="dialog-footer">-->
<!--          <el-button type="primary" @click="goToDetails">查看图书详情</el-button>-->
<!--        </span>-->
      </el-dialog>
    </el-main>
  </el-container>

</template>

<script>
export default {
  name: "AddAuthor",
  props: {
    msg: String
  }, data() {
    var checkBirth = (rule, value, callback) => {

      const birth = /^/;
    }

    return {
      centerDialogVisible: false,
      ruleForm: {
        name: '',
        birth: '',
        gender: '',
        country: '',
        introduction: ''
      }, rules: {
        name: [{required: true, message: '姓名不能为空', trigger: 'blur'}],
        birth: [{required: true, message: '不能为空', trigger: 'blur'},
          {validator: checkBirth, message: '格式不正确', trigger: 'change'}],
        gender: [{required: true, message: '不能为空', trigger: 'blur'}],
        country: [{required: true, message: '不能为空', trigger: 'blur'}],
        introduction: [{max: 800, message: '不超过800个字', trigger: 'blur'}]
      }
    }
  }, methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.centerDialogVisible = true;
        } else {
          console.log('填写有误');
        }
      });
    }, resetForm(formName) { // 清空表单
      this.$refs[formName].resetFields();
    }

  }
}
</script>

<style scoped>

</style>