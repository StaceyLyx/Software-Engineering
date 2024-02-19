<template>
  <el-container style="height: 500px; border: 1px solid transparent" class="">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu router :default-active="activeIndex"
               @select="handleSelect">
        <!--        遍历routes中的元素所有-->
        <div v-for="(item,index) in $router.options.routes">

          <div v-if="item.name==='管理'">
            <!-- key 和 index 与 标题头部 pageHeaderContent 有关-->
            <el-submenu v-for="(item2,index2) in item.children" :index="item2.path" :key="item2.path">

              <template slot="title"><i class="el-icon-folder-add"></i>{{ item2.name }}</template>

              <el-menu-item-group>
                <el-menu-item v-for="(item3,index3) in item2.children" :index="item3.path" :key="item3.path">
                  {{ item3.name }}
                </el-menu-item>
              </el-menu-item-group>

            </el-submenu>
          </div>
        </div>
      </el-menu>
    </el-aside>
    <el-main>
      <el-page-header :activeIndex="activeIndex" :content="pageHeaderContent" @back="goBack"></el-page-header>
      <router-view/>
    </el-main>


  </el-container>
</template>

<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>


<script>

import router from "@/router";

export default {
  name: "Books",
  data() {
    return {
      activeIndex: this.$route.path,
      pageHeaderContent: ''
    }
  }, methods: {
    goBack() {
      this.$router.back();
      console.log("back!");
    }, handleSelect(key, keyPath) {
      console.log("key:", key, ";keyPath:", keyPath);
      this.setPageHeader(key);
    }, setPageHeader(key) {
      var theRouter = router.options.routes;

      if (key === '/manage')
        this.pageHeaderContent = '查找书籍';
      else if (key === '/manage/addBook')
        this.pageHeaderContent = '添加书籍';
      else if (key === '/manage/modifyBook')
        this.pageHeaderContent = '修改书籍';
      else if (key === '/manage/deleteBook')
        this.pageHeaderContent = '删除书籍';
      // add book

      else if (key === '/manage/addAuthor')
        this.pageHeaderContent = '添加作者';
      else if (key === '/manage/modifyAuthor')
        this.pageHeaderContent = '修改作者';
      else if (key === '/manage/searchAuthor')
        this.pageHeaderContent = '查找作者';
      else if (key === '/manage/deleteAuthor')
        this.pageHeaderContent = '删除作者';
      // author

      else if (key === '/manage/borrowBook')
        this.pageHeaderContent = '普通借书';
      else if (key === '/manage/borrowReservedBook')
        this.pageHeaderContent = '预约借书';
       // 借书

      else if (key === '/manage/returnBook')
        this.pageHeaderContent = '普通还书';
      else if (key === '/manage/addAdmin')
        this.pageHeaderContent = '添加管理员';




    }
  }, mounted() {
    var key = this.$route.path;
    this.activeIndex = key;
    this.setPageHeader(key);
  }, updated() {
    var key = this.$route.path;
    this.activeIndex = key;
    this.setPageHeader(key);
  }
}
;
</script>