<template>
  <div class="inner-pane">
    <div class="img-view-div">
      <img :src="aCopy.info.coverUrl" :alt="'copybook[@' + aCopy.id+']'" class="img-book"/>
    </div>

    <div class="my-table-content">
      <div class="my-table-para">
        <!-- 只有在info定义的时候才显示                  -->
        <div v-if="aCopy.info">
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">标题:</el-col>
            <el-col :span="10">{{ aCopy.info.title }}</el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">作者:</el-col>
            <el-col :span="10">{{ aCopy.info.authorName }}</el-col>
          </el-row>

        </div>
        <div class="my-table-para">
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">副本编号:</el-col>
            <el-col :span="20">{{ aCopy.id }}</el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">副本ISBN:</el-col>
            <el-col :span="10"> {{ aCopy.isbn }}</el-col>
          </el-row>

        </div>
        <div class="my-table-para">
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">副本状态:</el-col>
            <el-col :span="20">{{ aCopy.status }}</el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">馆藏地址:</el-col>
            <el-col :span="10">{{ aCopy.info.holdPlace }}</el-col>
          </el-row>
        </div>


      </div>
    </div>

  </div>

</template>

<script>
export default {
  name: "copybookTableView",
  props: {
    aCopy: Object
  }
}
</script>

<style scoped>

</style>