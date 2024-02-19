<template>
  <el-container style="height: 500px; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu router>
        <!--        遍历routes中的元素所有-->
        <div v-for="(item,index) in $router.options.routes">

          <el-submenu v-if="item.name==='书籍管理'" index="1">
            <!--如果遍历到了Book的路由，生成菜单和标题-->
            <template slot="title"><i class="el-icon-folder-add"></i>{{ item.name }}</template>

            <el-menu-item-group>
              <!--如果遍历到了book的路由，对其进行子元素的遍历，生成副菜单-->
              <el-menu-item v-for="(item2,index2) in item.children" :index="item2.path"
                            :class="$route.path==item2.path?'is-active':''">
                {{ item2.name }}
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>


          <el-submenu v-if="item.name==='Author'" index="2">
            <!--如果遍历到了Author的路由，生成菜单和标题-->
            <template slot="title"><i class="el-icon-s-custom"></i>{{ item.name }}</template>

            <!--如果遍历到了Author的路由，对其进行子元素的遍历，生成副菜单-->
            <el-menu-item v-for="(item2,index2) in item.children" :index="item2.path"
                          :class="$route.path==item2.path?'is-active':''">
              {{ item2.name }}
            </el-menu-item>
          </el-submenu>


        </div>
      </el-menu>


      <!--      <el-menu :default-openeds="['1','']">-->

      <!--        <el-submenu index="1">-->
      <!--          <template slot="title"><i class="el-icon-message"></i>书籍</template>-->
      <!--          <el-menu-item-group>-->
      <!--            <el-menu-item index="1-1" >添加书籍</el-menu-item>-->
      <!--            <el-menu-item index="1-2">删除书籍</el-menu-item>-->
      <!--            <el-menu-item index="1-3">修改书籍</el-menu-item>-->
      <!--            <el-menu-item index="1-4">查询书籍</el-menu-item>-->
      <!--          </el-menu-item-group>-->
      <!--        </el-submenu>-->

      <!--        <el-submenu index="2">-->
      <!--          <template slot="title"><i class="el-icon-menu"></i>作者</template>-->
      <!--          <el-menu-item-group>-->
      <!--            <el-menu-item index="2-1">增加作者</el-menu-item>-->
      <!--            <el-menu-item index="2-2">查询作者</el-menu-item>-->
      <!--            <el-menu-item index="2-3">修改作者</el-menu-item>-->
      <!--          </el-menu-item-group>-->
      <!--        </el-submenu>-->
      <!--      </el-menu>-->
    </el-aside>

    <el-main>
      <el-page-header @back="goBack" content="详情页面"></el-page-header>
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

export default {
  name: "Books",
  data() {
    const item = {
      bookID: '1',
      title: 'Java基础',
      authorID: '12',
      description: "书籍简介",
      ISBN: "123-123-23-12",
      publishTime: "2007-12-21",
      coverURL: "./Book/" + this.bookID
    };
    return {
      tableData: Array(20).fill(item)
    }
  }, methods: {
    goBack() {
      console.log("back!");
    }
  }
};
</script>