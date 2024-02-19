<template>
  <div class="inner-pane">
    <div class="img-view-div">
      <img :alt="'book[@'+aBorrow.borrowId+']'" class="img-book" :src="aBorrow.info.coverUrl"/>
    </div>

    <div class="my-table-content">
      <div class="my-table-para">
        <el-row :gutter="10">
          <el-col :span="4" class="title-bold">借阅记录编号:</el-col>
          <el-col :span="20">{{ aBorrow.borrowId }}</el-col>
        </el-row>

      <div class="my-table-para">
        <el-row :gutter="10">
          <el-col :span="4" class="title-bold">借阅时间:</el-col>
          <el-col :span="20">{{ aBorrow.time }}</el-col>
        </el-row>

        <!--        <el-row :gutter="10">-->
        <!--          <el-col :span="4" class="title-bold">借阅人ID:</el-col>-->
        <!--&lt;!&ndash;          此处多写一个username接口，查找username&ndash;&gt;-->
        <!--          <el-col :span="20">{{ aBorrow.userId }}</el-col>-->
        <!--        </el-row>-->

        <div class="my-table-para">
          <!-- 只有在info定义的时候才显示                  -->
          <div v-if="aBorrow.info">


            <el-row :gutter="10">
              <el-col :span="4" class="title-bold">作者:</el-col>
              <el-col :span="10">{{ aBorrow.info.authorName }}</el-col>
            </el-row>
            <el-row :gutter="10">
              <el-col :span="4" class="title-bold">标题:</el-col>
              <el-col :span="10">{{ aBorrow.info.title }}</el-col>
            </el-row>

            <el-row :gutter="10">
              <el-col :span="4" class="title-bold">借阅人学号:</el-col>
              <!--          此处多写一个username接口，查找username-->
              <el-col :span="20">{{ aBorrow.info.username }}</el-col>
            </el-row>
          </div>
        </div>


       <div class="my-table-para">
          <!-- 只有在info定义的时候才显示                  -->
          <div v-if="aBorrow.info">

            <el-row :gutter="10">
              <el-col :span="4" class="title-bold">副本状态:</el-col>
              <el-col :span="20">{{ aBorrow.info.status }}</el-col>
            </el-row>
          </div>
          <el-row :gutter="10">
            <el-col :span="4" class="title-bold">副本ISBN:</el-col>
            <el-col :span="10"> {{ aBorrow.info.isbn }}</el-col>
          </el-row>


         <el-row :gutter="10">
           <el-col :span="4" class="title-bold">副本原馆藏:</el-col>
           <el-col :span="10">{{ aBorrow.info.holdPlace }}</el-col>
         </el-row>
       </div>

        </div>
      </div>
    </div>

  </div>

</template>

<script>
export default {
  name: "BorrowTableView",
  props: {
    aBorrow: Object
  }
}
</script>

<style scoped>

</style>