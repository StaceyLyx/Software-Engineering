<template>
  <el-container>
    <el-main>
      <!--    先是查询item: 输入isbn,查询copybook信息, table展示-->
      <div style="width: fit-content;margin: auto">
        <el-form :model="borrowSubmitForm" :rules="borrowSubmitRules"
                 ref="borrowSubmitForm" style="width:500px;" label-position="top">
          <el-form-item label="请输入学工号：" prop="username">

            <el-col :span="20">
              <el-input maxlength="11"
                        show-word-limit v-model="borrowSubmitForm.username"></el-input>
            </el-col>
          </el-form-item>

          <el-col :span="12" class="my-info-input">
            <el-input v-model="copyId" readonly>
              <template class="my-info-input-ele" slot="prepend">副本ID:</template>
            </el-input>
          </el-col>

          <el-col :span="12" class="my-info-input">
            <el-input v-model="title" readonly>
              <template class="my-info-input-ele" slot="prepend">书名:</template>
            </el-input>
          </el-col>


          <el-col :span="12" class="my-info-input">
            <el-input v-model="borrowSubmitForm.adminId" readonly>
              <template class="my-info-input-ele" slot="prepend">管理员ID:</template>
            </el-input>
          </el-col>
          <el-col :span="12" class="my-info-input">
            <el-input v-model="borrowSubmitForm.libraryId" readonly>
              <template class="my-info-input-ele" slot="prepend">
                {{ this.$store.getters.library(borrowSubmitForm.libraryId) }}ID:
              </template>
            </el-input>
          </el-col>


          <el-col :span="3">
            <el-button :type="myicn.type" @click="borrowBook('checkReservationForm')"
                       :disabled="myicn.type=='success'">Check
            </el-button>
          </el-col>

          <el-form-item>
            <el-col :span="24">
              <i :class="myicn.name" :style="'margin-left: 2px; '+ myicn.type"></i>
            </el-col>
          </el-form-item>
        </el-form>
      </div>

      <hr/>
    </el-main>
    <el-footer style="height: 0px"/>
  </el-container>


</template>

<script>
import axios from "axios";

export default {
  name: "borrowSubmit",
  props: {
    copyId: Number,
    isbn: String,
    title: String,
    libraryId: Number
    //username :待输入

  },
  data() {

    const checkLibrary = (rule, value, callback) => {
      if (this.$cookies.get('place') == this.libraryId)
        return callback();
      else {
        this.$message.info("副本所在图书馆: " + this.$store.getters.library(this.libraryId))
        this.$message.info("当前管理员所在图书馆: " + this.$store.getters.library(this.$cookies.get('place')))

        return callback(new Error("该书籍不在该管理员可操作的图书馆内！"))
      }

    }
    return {
      myicn: {
        type: 'default', // success
        name: '', // el-icon-loading / success/ error
        style: 'color: green'
      },
      copybookData: Object,
      borrowSubmitForm: {
        copyId: this.copyId,
        username: String,
        adminId: Number,
        libraryId: Number
      },
      borrowSubmitRules: {
        copyId: [
          //只能包含字⺟，数字或两种特殊字符（-_）且只能以字⺟或-开头 : /^[-]|^[A-z][A-z0-9_\-]+/;
          {required: true, message: '副本编号不能为空,请先进行副本查询', trigger: 'blur'}
        ], username: [
          {required: true, message: '学工号不能为空', trigger: 'change'},
          {min: 11, max: 11, message: '⻓度为11个数字', trigger: 'change'},
          // {validator: checkUsername, trigger: 'change'}
        ], adminId: [{required: true, message: '管理员信息不能为空', trigger: 'change'}],
        libraryId: [
          {required: true, message: '图书馆信息不能为空', trigger: 'change'},
          {validator: checkLibrary, trigger: 'blur'}

        ]
      }

    }
  }, methods: {
    borrowBook() {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeIconStatus('loading')

          var copyId = this.copyId
          var username = this.borrowSubmitForm.username;
          var adminId = this.$cookies.get('currentUser').id;
          var libraryId = this.$cookies.get('place');
          axios({
            method: 'post',
            url: '/admin/resources/books/borrowBook',
            data: {
              copyId: copyId,
              username: username,
              adminId: adminId,
              libraryId: libraryId
            }
          }).then(resp => {
            console.log("现场结束请求成功！状态码：", resp.status)
            if (resp.status == 200) {
              console.log("更新图书状态为已借出、添加借阅记录！")
              this.$message.success("借阅成功！")
              this.changeIconStatus('success')
            } else {
              this.$message.info("借阅状态码:", resp.status)
              this.changeIconStatus('error')
            }
          }).catch(error => {
            this.changeIconStatus('error')
            console.log("现场借书出错！", error)

            if (error.response) {

              if (error.response.status == 402) {
                console.log("借书失败！")
                this.$message.warning("副本状态不正确！已借出或者已被预约！")
              } else if (error.response.status == 401) {
                console.log("输入的学工号不存在！")
                this.$message.warning("输入的学工号不存在，请重新输入！")
              } else if (error.response) {
                this.$message.warning("借阅状态码:", error.response.status)
              }
            }
          })

        }
      })


    }, changeIconStatus(iconStatus) {
      console.log(" changing the icon status: ", iconStatus)
      if (iconStatus == 'loading') {
        this.myicn.type = 'default'
        this.myicn.name = 'el-icon-loading'
        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'error') {
        this.myicn.type = 'default'
        this.myicn.name = 'el-icon-error'
        this.myicn.style = 'color: red;'
        this.$message.success("请求失败！")
      } else if (iconStatus == 'success') {
        this.myicn.type = 'success'
        this.myicn.name = 'el-icon-success'
        this.myicn.style = 'color: green;'
        this.$message.success("请求成功")
      } else if (iconStatus == 'default') {
        this.myicn.type = ''
        this.myicn.name = ''
        this.myicn.style = 'color: green;'
      }
    }
  }, updated() {

    this.borrowSubmitForm.adminId = this.$cookies.get('currentUser').id;
    this.borrowSubmitForm.libraryId = this.$cookies.get('place');
  }
}
</script>

<style scoped>

</style>