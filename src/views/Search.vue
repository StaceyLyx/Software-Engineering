<template>
  <el-container>
<!--    <SearchAuthor/>-->
    <SearchBook/>
    <el-footer></el-footer>
<!--    footer是用于保证页面垂直排列-->
  </el-container>
</template>

<script>

import SearchBook from "@/components/Books/SearchBook";
import SearchAuthor from "@/components/Authors/SearchAuthor";

export default {
  name: "Search",
  components: {
    SearchBook,
    SearchAuthor
  }
}
</script>

<style scoped>

</style>