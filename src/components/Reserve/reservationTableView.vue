<template>
  <div class="inner-pane">
    <div class="img-view-div">
      <img :alt="'book[@'+scope.copyId+']'" class="img-book" :src="scope.info.coverUrl"/>
    </div>

    <div class="my-table-content" :ref="scope.reserveId">
      <div class="my-table-para">
        <el-row :gutter="10">
          <el-col :span="4" class="title-bold">预约编号:</el-col>
          <el-col :span="20">{{ scope.reserveId }}</el-col>
        </el-row>

        <el-row :gutter="10">
          <el-col :span="4" class="title-bold">馆藏地址:</el-col>
          <el-col :span="10">{{ scope.info.holdPlace }}</el-col>
        </el-row>
      </div>

      <div class="my-table-para">

      <el-row :gutter="10">
        <el-col :span="4" class="title-bold">预约时间:</el-col>
        <el-col :span="20">{{ scope.time }}</el-col>
      </el-row>

      <el-row :gutter="10">
        <el-col :span="4" class="title-bold">预约状态:</el-col>
        <el-col :span="20">{{ scope.status }}</el-col>
      </el-row>
      </div>

      <div class="my-table-para">
        <!-- 只有在info定义的时候才显示                  -->
        <div v-if="scope.info">
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">副本ISBN:</el-col>
            <el-col :span="10"> {{ scope.info.isbn }}</el-col>
          </el-row>

          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">作者:</el-col>
            <el-col :span="10">{{ scope.info.authorName }}</el-col>
          </el-row>


        </div>
    </div>
    </div>

  </div>

</template>

<script>
export default {
  name: "reservationTableView",
  props: {
    scope: Object,
    toDelete: Number
  }, methods:{
    delete(){


    }
  }
}
</script>

<style scoped>

</style>