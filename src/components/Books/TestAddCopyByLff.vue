<template>
  <el-container>
    <el-main>
      <el-form :model="">

      </el-form>
    </el-main>

    <el-footer style="height: 0"></el-footer>
  </el-container>

</template>

<script>
export default {
  name: "TestAddCopyByLff",
  data() {
    return {
      addCopyForm: {bookId: '', isbn: '', number: 1, libraryId: ''},


    }
    //
    // bookId: 1, // 原件id
    //     isbn: "435465768" // 原件isbn
    // number: 5,  // 5本
    //     libraryId:2 // 不同的图书馆有不同的id
    //
  }
}
</script>

<style scoped>

</style>