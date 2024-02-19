<template>
  <router-view/>

</template>

<script>

import AddAuthor from "@/components/Authors/AddAuthor";
import SearchAuthor from "@/components/Authors/SearchAuthor";
import ModifyAuthor from "@/components/Authors/ModifyAuthor";

export default {
  name: "Author",
  components: {
    AddAuthor,
    ModifyAuthor,
    SearchAuthor
  },
  data: {}
}
</script>

<style scoped>

</style>