<template>
  <el-container>
<!--    <h1>This is return</h1>-->
    <router-view/>
  </el-container>
</template>

<script>
import returnCheck from "@/components/Return/ReturnCheck";
import returnSubmit from "@/components/Return/ReturnSubmit";

export default {
  name: "Return",
  components:{
    // returnCheck,
    // returnSubmit

  }
}
</script>

<style scoped>

</style>