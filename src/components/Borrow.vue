<template>
  <el-container>
    <el-main>
<!--      <borrow-check/>-->
<!--      <h1>This is borrow</h1>-->
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>

import borrowCheck from "@/components/Borrow/BorrowCheck";
import borrowSubmit from "@/components/Borrow/borrowSubmit";

export default {
  name: "Borrow",
  components: {
    borrowCheck,
    borrowSubmit
  }
}
</script>

<style scoped>

</style>