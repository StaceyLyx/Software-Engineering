<template>
  <el-container class="mainWindow">
    <el-main>


      <div style="width: fit-content;margin: auto">


        <el-form :model="searchForm" :rules="searchFormRule"
                 ref="searchForm" style="width:500px;" label-position="top">
          <el-form-item label="输入查询信息进行书本查询：" prop="searchInfo">
            <el-col :span="20">
              <el-input v-model="searchForm.searchInfo" placeholder="输入ISBN、作者名、图书名或者相关信息"></el-input>
            </el-col>
            <el-col :span="1">
              <i :class="myicn.name" :style="'margin-left: 2px;'+myicn.style"> </i></el-col>
            <el-col :span="3">
              <el-button :type="myicn.type" @click="doSearch('searchForm')">Search</el-button>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-col :span="24">
            </el-col>
          </el-form-item>


        </el-form>

      </div>
      <hr/>

      <div>
        <div v-for="(aBook, index) in tableData">
          <!--          P1 图片展示页面 -->
          <el-row style="padding: 0">
            <div class="books-list-item">

              <div class="img-view-div">
                <img :alt="'book[@'+aBook.copyId+']'" class="img-book" :src="aBook.coverUrl"/>
              </div>
              <div class="my-table-content">
                <div class="my-table-para">
                  <el-row :gutter="10">
                    <el-col :span="4" :href="'/books/'+aBook.bookId" class="title-bold">书名:
                    </el-col>
                    <el-col :span="20">
                      <a class="col-book-link" :href="'/books/'+aBook.bookId">
                        {{ aBook.title }}
                      </a>
                    </el-col>
                  </el-row>

                  <el-row :gutter="10">
                    <el-col :span="4" class="title-bold">作者:</el-col>
                    <el-col :span="20">{{ aBook.authorName }}</el-col>
                  </el-row>
                </div>

                <div class="my-table-para">
                  <el-row :gutter="10">
                    <el-col :span="4" class="title-bold">ISBN:</el-col>
                    <el-col :span="20">{{ aBook.isbn }}</el-col>
                  </el-row>

                  <el-row :gutter="10">
                    <el-col :span="4" class="title-bold">出版日期:</el-col>
                    <el-col :span="20">{{ aBook.publishTime }}</el-col>
                  </el-row>
                </div>

              </div>
            </div>
          </el-row>

        </div>
      </div>

      <!--      <el-table :data="tableData" class="mainTable">-->
      <!--        <el-table-column>-->
      <!--          <template slot-scope="scope">-->
      <!--            <img :src="scope.row.coverUrl" style="height: 50px"/>-->
      <!--          </template>-->
      <!--        </el-table-column>-->
      <!--        <el-table-column label="书名" width="100">-->
      <!--          <template slot-scope="scope">-->
      <!--            <el-link :href="'/books/'+scope.row.bookId">{{ scope.row.title }}</el-link>-->
      <!--          </template>-->
      <!--        </el-table-column>-->

      <!--        <el-table-column prop="authorName" label="作者" width="200">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column prop="isbn" label="ISBN" width="200">-->
      <!--        </el-table-column>-->
      <!--        <el-table-column prop="publishTime" label="出版日期" width="200">-->
      <!--        </el-table-column>-->
      <!--        &lt;!&ndash;        <el-table-column prop="holdPlace" label="所在图书馆" width="200">&ndash;&gt;-->
      <!--        &lt;!&ndash;        </el-table-column>&ndash;&gt;-->
      <!--      </el-table>-->

      <br/>
      <el-pagination background class="pageBar"
                     :page-size="pageSize"
                     :pager-count="5"
                     @current-change="page"
                     :total="bookTotal"
                     layout="prev, pager, next">
      </el-pagination>

    </el-main>
    <el-footer style="height: 0"></el-footer>

  </el-container>

</template>

<script>
import axios from "axios";

export default {
  name: "SearchBooks",
  props: {
    msg: String

  }, data() {
    return {
      // 模糊查询：
      searchForm: {searchInfo: ''},
      searchFormRule: {searchInfo: [{min: 3, max: 100, message: '查询信息应该是3-100个字符！', trigger: 'blur'}]},
      myicn: {
        type: '',
        name: '', // el-icon-success
        style: '' // color: green / color: red
      },

      // 分页
      currentPage: 1,
      pageSize: 10,
      bookTotal: 20,
      tableData: [[]]
    }
  }, methods: {
    page(currentPage) {
      this.currentPage = currentPage;
      axios({
        method: 'get',
        url: '/resources/books/' + this.currentPage + "/" + this.pageSize
      }).then(resp => {
        console.log("change page!")
        this.tableData = resp.data.records;
      }).catch(error => {
        console.log(error);
      });
      console.log("[SearchBook/]当前页码：", currentPage);
    },
    doSearch(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.changeIconStatus('loading');
          axios({
            method: 'get',
            url: '/resources/books/searchBooks/' + this.searchForm.searchInfo
          }).then(resp => {
            if (resp.status == 200) {
              this.changeIconStatus('success');
              if (resp.data.result && resp.data.result[0]) {

                this.tableData = resp.data.result;
              } else {
                this.$message.warning("没有查询到符合条件的图书！")
                this.tableData = []
              }
            }
          }).catch(error => {
            if (error.response && error.response.status == 400) {
              this.$message.warning("没有查询到符合条件的图书！")
              this.tableData = []
              this.changeIconStatus('success');
            } else {
              console.log("查询图书出错！", error);
            }
          })
        } else {
          this.changeIconStatus('error')
        }
      })
    },
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
      }

    }
  }, mounted() {
    // 查询总页数
    axios({
      method: 'get',
      url: '/resources/books/totalNumber'
    }).then(resp => {
      console.log("查询总页数")
      this.bookTotal = resp.data.totalNumber;
    }).catch(error => {
      console.log(error);
    })

    // 分页查询
    axios({
      headers: {token: this.$cookies.get('token')},
      method: 'get',
      url: '/resources/books/' + this.currentPage + "/" + this.pageSize
    }).then(resp => {
      this.tableData = resp.data.records;
    }).catch(error => {
      console.log(error);
    });
  }, updated() {
    axios({
      headers: {token: this.$cookies.get('token')},
      method: 'get',
      url: '/resources/books/totalNumber'
    }).then(resp => {
      console.log("查询总页数")
      this.bookTotal = resp.data.totalNumber;
    }).catch(error => {
      console.log(error);
    })
  }

}
</script>
<style src="../../assets/my_css/app.css"></style>
<style src="../../assets/my_css/booksList.css"></style>

<style scoped>

.mainWindow {
  width: 100%;
  text-align: center;
}

.mainTable {
  width: fit-content;
  margin: auto;
}

el-table.mainTable el-table-column {

  height: 200px;

}

.pageBar {
  width: 100%;
}
</style>