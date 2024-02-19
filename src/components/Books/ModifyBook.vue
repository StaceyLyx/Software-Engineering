<template>
  <el-container>
    <el-main>
      <el-table :data="tableData">
        <el-table-column prop="bookID" label="bookID" width="140"></el-table-column>
        <el-table-column prop="title" label="名称" width="120"></el-table-column>

        <el-table-column prop="authorID" label="作者编号"></el-table-column>
        <el-table-column prop="description" label="简介"></el-table-column>

        <el-table-column prop="ISBN" label="ISBN"></el-table-column>
        <el-table-column prop="publishTime" label="发行时间"></el-table-column>
        <el-table-column prop="coverURL" label="封面"></el-table-column>

      </el-table>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "ModifyBook",
data(){
    return{
      tableData: [{
        bookID: '1',
        title: 'Java基础',
        authorID: '12',
        description: "书籍简介",
        ISBN: "123-123-23-12",
        publishTime: "2007-12-21",
        coverURL: "./Book/" + this.bookID
      }]
    }
}
}
</script>

<style scoped>

</style>