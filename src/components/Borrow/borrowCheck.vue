<template>

  <el-container>
    <el-main>
      <div style="width: fit-content;margin: auto">
        <el-form :model="isbnForm" :rules="isbnFormRules"
                 ref="isbnForm" style="width:500px;" label-position="top">
          <el-form-item label="请输入ISBN，查询相应副本：" prop="isbn">
            <el-col :span="20">
              <el-input show-word-limit max-length="17" placeholder="请输入副本ISBN，例如0123456789012-001"
                        v-model="isbnForm.isbn"></el-input>
            </el-col>

            <el-col :span="1">
              <i :class="myicn.name" :style="'margin-left: 2px;'+myicn.style"> </i></el-col>

            <el-col :span="3">
              <el-button :type="myicn.type" @click="borrowCheck('isbnForm')">Check</el-button>
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

      <div v-for="(aCopy, index) in copybookData">
        <copybook-table-view :a-copy="aCopy"/>

        <div class="my-table-operation">

          <!--          显示copybook条目-->
          <el-row :gutter="10">
            <el-button @click="openDialog(aCopy)"
                       size="mini"
                       :type="typeDelete[aCopy.reserveId]?'success':'default'"
                       :icon="typeDelete[aCopy.reserveId]?'el-icon-success':'el-icon-thumb'">
              <!--                    :type="typeDelete[scope.reserveId]"-->
              {{ typeDelete[aCopy.reserveId] ? "已 借 出" : "借　　取" }}
            </el-button>
          </el-row>

          <el-row :gutter="10" style="height: 5px">
            <br/>
          </el-row>
        </div>


      </div>

      <el-table :data="copybookData" class="mainTable" align="center">
      </el-table>

      <el-dialog title="现场借书" :visible.sync="showDialog" width="50%" center>
        <borrow-submit :copy-id="targetCopyId" :isbn="this.isbnForm.isbn"
                       :library-id="targetLibraryId" :title="targetTitle"
                       style="width: fit-content; margin: auto"/>
      </el-dialog>


    </el-main>


  </el-container>
</template>

<script>

import axios from "axios";
import copybookTableView from "@/components/copybook/copybookTableView";
import borrowSubmit from "@/components/Borrow/borrowSubmit";

export default {
  name: "borrowCheck",
  components: {copybookTableView, borrowSubmit},
  data() {

    const checkCopybookIsbn = (rule, value, callback) => {
      var ISBN_success1 = /^[0-9]{10}-[0-9]{3}$/;
      var ISBN_success2 = /^[0-9]{13}-[0-9]{3}$/;

      if ((value.match(ISBN_success1)) ||
          (value.match(ISBN_success2))) {
        return callback();
      } else {
        var ISBN_success3 = /^[0-9]{10}-[0-9]/;
        var ISBN_success4 = /^[0-9]{13}-[0-9]/;
        var ISBN_success5 = /^[0-9]{10}/;
        var ISBN_success6 = /^[0-9]{13}/;
        if (value.match(ISBN_success3) || value.match(ISBN_success4)) {
          return callback(new Error("流水号应为001格式"))
        } else if (value.match(ISBN_success5) || value.match(ISBN_success6)) {
          return callback(new Error("需要流水号"))
        }
        return callback(new Error("ISBN应为10或13个数字"))
      }

    }


    return {
      // 传入子组件显示的数据
      targetCopyId: Number,
      targetLibraryId: Number,
      targetTitle: String,

      // 控制借阅上传的界面显示
      showDialog: false,
      // 控制操作按钮属性
      typeDelete: [],
      // 控制交互按钮
      myicn: {
        name: 'el-icon-warning-outline',//'el-icon-check', //el-icon-warning-outline
        style: 'color: red', // green
        type: ''
      },

      //表单
      isbnForm: {'isbn': ''},
      isbnFormRules: {
        isbn: [{require: true, message: "请输入书籍的isbn进行查询", trigger: 'blur'},
          {validator: checkCopybookIsbn, trigger: 'change'}]
      },

      //数据查找和展示
      copybookData: [],
      mediaData: [],
      // verbose
      debug_append_info: 1
    }

  }, methods: {
    openDialog(aCopy) {
      // 前提：已经有copybook显示和对应操作按钮

      // 控制提交表单子组件的数据
      this.targetCopyId = aCopy.id
      this.targetLibraryId = aCopy.libraryId
      this.targetTitle = aCopy.info.title
      // 显示
      this.showDialog = !this.showDialog
    }, // 点击借书 应当在提交子组件中完成
    borrowBook(copyId) {
      // var userId
    }, // 检查isbn的副本是否可借阅
    borrowCheck(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeIconStatus('loading')
          // 开始查询copy元组

          this.$axios({
            method: "post",
            url: "/admin/resources/books/borrowCheck",
            data: {
              isbn: this.isbnForm.isbn
            }
          }).then(resp => {
            console.log("请求检查borrowbook成功！状态码：", resp.status)
            // 使用中间数据存储不完整信息，防止组件动态改变

            // 如果中间数据（copybook）不止一个
            if (resp.data[1])
              this.mediaData = resp.data;
            // 中间数据只有一个
            else this.mediaData[0] = resp.data

            console.log("mediaData：", this.mediaData)


            if (this.mediaData[0]) {
              // 如果数据不为空，那么调用方法查询完整的数据
              this.searchInfo();
              // get copybook object:[bookId, id, isbn, libraryId, status]
            } else {
              this.$message.warning("没有数据！")
              this.copybookData = [] // 清空data
            }

          }).catch(error => {
            console.log(error)

            if (error.response.status == 400) {
              this.$message.warning("输入的isbn副本不存在！")
            } else if (error.response.status == 401) {
              this.$message.warning("该副本不是空闲状态，不允许借出！")
            } else {
              this.$message.warning("请再次输入isbn")
            }
            // 注意：后面的warning会覆盖前面的，所以必须放在一组if else clause中
            console.log("查询副本的isbn出错！", error)
          })
        }
      })
    }, searchInfo() {
      if (this.debug_append_info)
        console.log("search info: before ths for loop")

      // 对每一个中间数据的元组作查询
      // 实际上only 1
      for (var i in this.mediaData) {
        if (this.debug_append_info)
          console.log("Search info: envoke asy com in loop i:", i);
        this.doAsyCom(i);
      }

    }, // 进行异步查询
    doAsyCom(i) {
      if (this.debug_append_info)
        console.log("doAsyCom : before ths axios, with index:", i, "mediaData。id:", this.mediaData[i].id)

      axios({
        method: 'post', url: '/resources/books/copybook/allInfo',
        data: {copyId: this.mediaData[i].id}
      }).then(resp => {
        if (this.debug_append_info)
          console.log("doAsycom : axios by ", i, " response.data:", resp.data);
        var info = {
          isbn: resp.data.copybook.isbn, bookId: resp.data.copybook.bookId, //  redundent data
          title: resp.data.book.title, authorName: resp.data.book.authorName,
          coverUrl: resp.data.book.coverUrl,
          holdPlace: resp.data.library.name
        }
        this.mediaData[i]['info'] = info
      }).catch(error => {
        if (error.response) {
          var status = error.response.status
          if (status == 400) {
            this.$message.info("副本信息不存在")
            console.log("查找副本失败！副本is null，不存在")
          }
        }
      })
      if (this.debug_append_info)
        console.log("doAsycom : after axios mediaData[0]['info']: ", this.mediaData[0]['info']);


      // console.log("reserVationData.length: ", this.reserveData.)
      // if (i == this.reserveData.length){
      this.loadingTimer = setTimeout(this.loadingFinished, 1000)
      this.timer = setTimeout(this.get, 1000);


    }
    ,
    changeIconStatus(iconStatus) {
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

    }, loadingFinished() {
      this.changeIconStatus('success')
    },
    get() {
      if (this.debug_append_info)
        console.log("Lastly: getter : after 2000ms test mediaData[0]: ", this.mediaData[0]
            , "mediaData[0].info:", this.mediaData[0]['info']);
      this.copybookData = this.mediaData
      console.log("this.copybookdata.info:", this.copybookData[0])
    },

  }
  ,
  mounted() {

  }
}
</script>


<style scoped>

</style>