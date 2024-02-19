<template>
  <el-container>
    <el-main>
      <!--    先是查询item: 输入isbn,查询copybook信息, table展示-->
      <div style="width: fit-content;margin: auto">
        <el-form :model="returnSubmitForm" :rules="returnSubmitRules"
                 ref="returnSubmitForm" style="width:500px;" label-position="top">


          <el-col :span="12" class="my-info-input">
            <el-input v-model="returnSubmitForm.borrowId" readonly>
              <template class="my-info-input-ele" slot="prepend">借阅号:</template>
            </el-input>
          </el-col>


          <el-col :span="12" class="my-info-input">
            <el-input v-model="returnSubmitForm.isbn" readonly>
              <template class="my-info-input-ele" slot="prepend">副本ISBN:</template>
            </el-input>
          </el-col>


          <el-col :span="12" class="my-info-input">
            <el-input v-model="returnSubmitForm.adminId" readonly>
              <template class="my-info-input-ele" slot="prepend">管理员ID:</template>
            </el-input>
          </el-col>


          <el-col :span="12" class="my-info-input">
            <el-input v-model="returnSubmitForm.libraryId" readonly>
              <template class="my-info-input-ele" slot="prepend">
                {{ this.$store.getters.library(returnSubmitForm.libraryId) }}ID:
              </template>
            </el-input>
          </el-col>



          <el-col :span="3">
            <el-button :type="myicn.type" @click="returnBook('returnSubmitForm')"
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
export default {
  name: "ReturnSubmit",
  props: {
    borrowId: Number,
    // adminId: Number,
    // libraryId: Number,
    isbn: String
  },
  data() {
    return {
      myicn: {
        type: '',
        name: '',
        style: ''
      },
      //Integer borrowId, Integer adminId, Integer libraryId, String isbn)
      returnSubmitForm: {borrowId: '', adminId: '', libraryId: '', isbn: ''},
      returnSubmitRules: {
        borrowId: [{required: true, message: '借阅号不能为空', trigger: 'blur'}],
        adminId: [{required: true, message: '管理员信息不能为空', trigger: 'blur'}],
        libraryId: [{required: true, message: '图书馆信息不能为空', trigger: 'blur'}],
        isbn: [{required: true, message: '副本ISBN不能为空', trigger: 'blur'}],
      },

    }
  }, methods: {
    returnBook(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeIconStatus('loading')

          var adminId = this.$cookies.get('currentUser').id;
          var libraryId = this.$cookies.get('place');


          axios({
            method: 'post', url: '/admin/resources/books/returnBook',
            data: {
              borrowId: this.borrowId,
              adminId: adminId,
              libraryId: libraryId,
              isbn: this.isbn
            }
          }).then(resp => {
            console.log("还书请求成功！状态码：", resp.status);

            if (resp.status==200) {
              this.changeIconStatus('success')
            }
          }).catch(error => {
            console.log("还书出错！", error)
            this.changeIconStatus('error')

            if (error.response) {
              var status = error.response.status
              if (status == 404) {
                this.$message.warning("还书失败")
              }
            }
          })
        }
      })
    },changeIconStatus(iconStatus) {
      console.log(" changing the icon status: ", iconStatus)
      if (iconStatus == 'loading') {
        this.myicn.name = 'el-icon-loading'
        this.myicn.type = ''

        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'error') {
        this.myicn.name = 'el-icon-error'
        this.myicn.type = ''

        this.myicn.style = 'color: red;'
      } else if (iconStatus == 'success') {
        this.myicn.name = 'el-icon-success'
        this.myicn.type = 'success'
        this.myicn.style = 'color: green;'
        this.$message.success("还书成功")

      } else if (iconStatus == 'default') {
        this.myicn.name = ''
        this.myicn.style = ''
        this.myicn.type = ''
      } else if (iconStatus == 'check') {
        this.myicn.name = 'el-icon-check';
        this.myicn.style = 'color: green';
        this.myicn.type = 'success'
      }

    }

  }

}
</script>

<style scoped>

</style>