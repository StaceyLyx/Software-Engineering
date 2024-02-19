<template>
  <el-container>
    <el-main>
      <div style="width: fit-content;margin: auto">
        <el-form :model="returnIsbnForm" :rules="returnIsbnRule"
                 ref="returnIsbnForm" style="width:500px;" label-position="top">
          <el-form-item label="请输入归还的副本ISBN，并点击Check查询：" prop="isbn">
            <el-col :span="20">
              <el-input show-word-limit max-length="17" placeholder="请输入副本ISBN，例如0123456789012-001"
                        v-model="returnIsbnForm.isbn"></el-input>
            </el-col>

            <el-col :span="1">
              <i :class="myicn.name" :style="'margin-left: 2px;'+myicn.style"> </i></el-col>

            <el-col :span="3">
              <el-button :type="myicn.type" @click="returnCheck('returnIsbnForm')">Check</el-button>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-col :span="24">
            </el-col>
          </el-form-item>
        </el-form>
      </div>

      <hr/>

      <!--    显示查询结果-->

      <div v-for="(aBorrow, index) in borrowForReturnData">
        <borrow-table-view :a-borrow="aBorrow"/>
        <!--          显示borrow条目-->
        <div class="my-table-operation">
          <el-row :gutter="10">
            <el-button @click="openDialog(aBorrow)"
                       size="mini"
                       :type="typeDelete[aBorrow.borrowId]?'success':'default'"
                       :icon="typeDelete[aBorrow.borrowId]?'el-icon-success':'el-icon-thumb'">
              <!--                    :type="typeDelete[scope.reserveId]"-->
              {{ typeDelete[aCopy.borrowId] ? "已 归 还" : "还　　书" }}
            </el-button>
          </el-row>

          <!--          <el-row :gutter="10" style="height: 5px">-->
          <!--            <br/>-->
          <!--          </el-row>-->
        </div>

      </div>
      <el-table :data="borrowForReturnData" class="mainTable" align="center">
      </el-table>


    </el-main>

    <el-dialog title="还书" :visible.sync="showDialog" width="50%" center>
      <return-submit :isbn="this.returnIsbnForm.isbn"
                     :borrow-id="targetBorrowId"
                     style="width: fit-content; margin: auto"/>
    </el-dialog>
  </el-container>
</template>

<script>
import BorrowTableView from "@/components/Borrow/BorrowTableView";
import ReturnSubmit from "@/components/Return/ReturnSubmit";

export default {
  name: "ReturnCheck",
  components: {BorrowTableView, ReturnSubmit},

  data() {
    return {
      showDialog: false,
      targetBorrowId: Number,
      returnIsbnForm: {isbn: ''},
      typeDelete: [],

      returnIsbnRule: {
        isbn: [{required: true, message: '请输入副本的ISBN', trigger: 'blur'}]
      },
      myicn: {
        type: 'default', // success
        name: 'el-icon-warning', // el-icon-success / error / loading
        style: 'color: red' // color: green / red
      },

      borrowForReturnData: [],

    }
  }, methods: {
    openDialog(aBorrow) {
      this.targetBorrowId = aBorrow.borrowId
      // pass to the return submit

      this.showDialog = !this.showDialog

    },
    returnCheck(formName) {
      console.log("form name：", formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeIconStatus('loading')


          console.log("发送给请求")
          axios({
            method: 'post',
            url: '/admin/resources/books/returnCheck',
            data: {
              isbn: this.returnIsbnForm.isbn
            }
          }).then(resp => {
            console.log("还书请求成功！状态码：", resp.status)
            if (resp.status == 200) {
              this.changeIconStatus('success')
              this.borrowForReturnData = resp.data
            }
          }).catch(error => {
            if (error.response) {
              var status = error.response.status
              if (status == 404) {
                console.log("还书失败：ISBN 不存在")
                this.$message.warning("ISBN 不存在")
              } else if (status == 400) {
                console.log("还书失败：副本状态不是已借出，或不存在该副本的借书记录")
                this.$message.warning("副本状态不是已借出，或不存在该副本的借书记录")
              }
            }

            console.log("检查可还书出错！", error)
            this.changeIconStatus('error')
          })
        } else {
          this.changeIconStatus('error')
        }
      })
    }, changeIconStatus(iconStatus) {
      console.log(" changing the icon status: ", iconStatus)
      if (iconStatus == 'loading') {
        this.myicn.name = 'el-icon-loading'
        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'error') {
        this.myicn.name = 'el-icon-error'
        this.myicn.style = 'color: red;'
      } else if (iconStatus == 'success') {
        this.myicn.name = 'el-icon-success'
        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'default') {
        this.myicn.name = ''
        this.myicn.style = ''
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

</style>/