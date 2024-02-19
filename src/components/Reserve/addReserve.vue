<template>
<!--  <el-container class="addReserveWindow">-->
<!--    <el-main>-->
<!--      <el-form :model="ruleForm" :rules="rules"-->
<!--               ref="ruleForm" label-width="100px"-->
<!--               class="demo-ruleForm" id="defined-pane">-->

<!--        <el-form-item label="学工号" prop="username">-->
<!--          <el-col :span="24">-->
<!--            <el-input v-model="ruleForm.username"></el-input>-->
<!--          </el-col>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="密码" prop="password">-->
<!--          <el-col :span="24">-->
<!--            <el-input v-model="ruleForm.password" show-password></el-input>-->
<!--          </el-col>-->
<!--          <br/>-->
<!--          <el-col :span="24">-->
<!--            <el-progress :percentage="percentage" :color="progressColors"-->
<!--                         :text-inside="true" :stroke-width="20"-->
<!--                         :format="passwordStrengthBar"></el-progress>-->
<!--          </el-col>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="确认密码" prop="password_2">-->
<!--          <el-col :span="24">-->
<!--            <el-input v-model="ruleForm.password_2" show-password></el-input>-->
<!--          </el-col>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="邮箱" prop="email">-->
<!--          <el-col :span="24">-->
<!--            <el-input v-model="ruleForm.email"></el-input>-->
<!--          </el-col>-->
<!--        </el-form-item>-->

<!--        <el-form-item label="选择角色" prop="character">-->
<!--          <el-col :span="24">-->
<!--            <el-select v-model="ruleForm.character" placeholder="请选择角色">-->
<!--              <el-option label="学生" value="student"></el-option>-->
<!--              <el-option label="老师" value="teacher"></el-option>-->
<!--            </el-select>-->
<!--          </el-col>-->
<!--        </el-form-item>-->

<!--        <el-form-item>-->
<!--          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>-->
<!--          <el-button @click="resetForm('ruleForm')">重置</el-button>-->
<!--          <span class="alertMsg">{{ alertMsg }}</span>-->
<!--        </el-form-item>-->

<!--      </el-form>-->

<!--      <el-dialog title="注册成功！" :visible.sync="centerDialogVisible" width="30%" center>-->
<!--        <hr/>-->
<!--        <span>学工号：{{ ruleForm.username }}</span>-->
<!--        <br/>-->
<!--        <span>密码：{{ ruleForm.password }}</span>-->
<!--        <br/>-->
<!--        <span>注册邮箱：{{ ruleForm.email }}</span>-->
<!--        <hr/>-->

<!--        <span slot="footer" class="dialog-footer">-->
<!--    <el-button type="primary" @click="goToLogin">去登陆</el-button>-->
<!--  </span>-->
<!--      </el-dialog>-->
<!--    </el-main>-->
<!--  </el-container>-->

</template>

<script>
export default {
  name: "addReserve"
}
</script>

<style scoped>

</style>