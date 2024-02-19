<template>
  <el-container id="show-details">
    <div id="detailsWindow">
      <div id="show-image">
        <img :src="bookDetailsInfo.coverUrl" class="image">
      </div>
      <div id="show-others">
        <p>
          <span class="title">图书Id:</span>
          <span>{{ bookDetailsInfo.bookId }}</span>
          <br>
        </p>
        <hr/>

        <p>
          <span class="title">书名:</span>
          <span>{{ bookDetailsInfo.title }}</span>
          <br>
          <span class="title">作者:</span>
          <span>{{ bookDetailsInfo.authorName }}</span>
          <br>
          <span class="title">出版日期:</span>
          <span>{{ bookDetailsInfo.publishTime }}</span>
          <br>
        </p>
        <hr/>
        <p>
          <span class="title">ISBN:</span>
          <span>{{ bookDetailsInfo.isbn }}</span>
          <br>
          <span class="title">介绍:</span>
        <p>{{ bookDetailsInfo.description }}</p>


        <hr/>
        <el-button @click="gotoModify" class="btn">修改</el-button>
        <el-button @click="gotoDelete" class="btn">删除</el-button>


      </div>
    </div>
  </el-container>

</template>


<script>
export default {
  name: "ShowDetails",
  props: {
    msg: String
  },
  data() {
    return {
      bookDetailsInfo: [{
        bookId: 1,
        title: '红楼梦',
        authorName: '',
        isbn: '11222',
        description: '',
        publishTime: '',
        holdPlace: ''
      }]
    }
  },
  methods: {
    gotoModify() {
      //指定到修改页面
      this.$router.replace('/manage/modifyBook')
    },
    gotoDelete() {
      //指定到删除页面
      this.$router.replace('/manage/deleteBook')
    }
  },
  updated() {

  }, created() {
    var tableData = []
    var _this = this;
    console.log("bookid is:", this.$route.params.bookid)
    this.$axios({
          method: 'get',
          url: '/resources/books/' + this.$route.params.bookid,
        }
    ).then(resp => {
      console.log("获取书籍详细信息完成!状态码：", resp.status);
      if (resp.status == 200) {
        this.bookDetailsInfo = resp.data.result
        console.log(tableData)
      }
    }).catch(error => {
      console.log(error)

      if (error.response && error.response.status == 400)
        this.$router.replace("/error400")
    })

    if (tableData[0])
      this.bookDetailsInfo = tableData;
  }
}
</script>

<style scoped src="../../assets/my_css/details.css"></style>