<template>
  <el-container class="mainWindow">
    <el-main>
      <el-table :data="tableData" class="mainTable">
        <el-table-column prop="id" label="ID" width="100">
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="300">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="100">
        </el-table-column>
        <el-table-column prop="country" label="国家" width="200">
        </el-table-column>
        <el-table-column prop="birth" label="出生日期" width="200">
        </el-table-column>
      </el-table>
    <hr/>
    <el-pagination background class="pageBar"
                   :page-size="pageSize"
                   :page-count="2"
                   @current-change="page"
                   layout="prev, pager, next" :total="authorTotal">
    </el-pagination>

    </el-main>
    <el-footer></el-footer>

  </el-container>

</template>

<script>
import axios from "axios";

export default {
  name: "SearchAuthors",
  props: {
    msg: String

  }, data() {
    return {
      currentPage: 1,
      pageSize: 10,
      authorTotal: 20,
      tableData: [[]]
    }
  }, methods: {
    page(currentPage) {
      this.currentPage = currentPage;
      axios({
        headers: {token: this.$cookies.get('token')},
        method: 'get',
        url: '/resources/authors/' + this.currentPage + "/" + this.pageSize
      }).then(resp => {
        this.tableData = resp.data.content;
      }).catch(error => {
        console.log(error);
      });
      console.log("[SearchAuthor/]当前页码：", currentPage);
    }
  }, created() {
    // 查询页面总条目数量
    axios({
      headers: {token: this.$cookies.get('token')},
      method: 'get',
      url: '/resources/authors/allNumber'
    }).then(resp => {
      this.authorTotal = resp.data.number;
    }).catch(error => {
      console.log(error);
    });

    this.page(this.currentPage);


  }


}
</script>

<style scoped>

.mainWindow {
  width: 100%;
  text-align: center;
}

.mainTable {
  width: fit-content;
  margin: auto;
}

.pageBar {
  width: 100%;
}
</style>