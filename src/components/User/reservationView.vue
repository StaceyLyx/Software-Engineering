<template>
  <el-container>
    <!--    输入框-->
    <el-main>
      <div style="width: fit-content; margin: auto">
        <el-button @click="checkReserve" class="around-button"
                   style="border: transparent; width: fit-content; height: fit-content;padding: 0; margin-bottom: 15px">
          <el-input class="my-info-input" readonly :value="this.$cookies.get('currentUser').name">
            <template slot="append"><i class="el-icon-search"></i></template>
          </el-input>
        </el-button>
        <i :class="myicn.name" :style="'margin-left: 2px;'+myicn.style"> </i>

        <!--        <el-button size="mini"-->
        <!--                   class="my-info-input-button" @click="checkReserve" icon="el-icon-search">-->
        <!--          {{  }}-->
        <!--        </el-button>-->

      </div>
      <!--    显示查询结果-->
      <!--      // reserveId: 2, copyId: 3, userId: 2, state: "已预约"-->
      <div class="my-table-list">

        <template v-for="(scope, index) in reserveData">

          <div class="my-table-pane">

            <reservation-table-view :scope="scope" style="width: fit-content"/>

            <div class="my-table-operation">
              <el-row :gutter="10">
                <el-button @click="deleteReserve(scope.reserveId,scope.copyId,scope.userId);"
                           size="mini" class="my-info-input-button"
                           :icon="typeDelete[scope.reserveId]?'el-icon-success':''">
                  {{ typeDelete[scope.reserveId] ? "已取消　" : "取消预约" }}
                </el-button>
              </el-row>
              <el-row :gutter="10" style="height: 5px">
                <br/>
              </el-row>
              <el-row :gutter="10">
                <el-button size="mini" :href="'/book/'+scope.info.bookId" class="my-info-input-button">
                  图书详情
                </el-button>
              </el-row>
            </div>

          </div>
        </template>
      </div>


    </el-main>
    <el-footer style="height: 0"/>

  </el-container>
</template>

<script>
import axios from "axios";
import reservationTableView from "@/components/Reserve/reservationTableView";

export default {
  name: "reservationView",
  props: {isSearch: Boolean},
  components: {reservationTableView},
  data() {
    return {
      loadingTimer: '',
      timer: '',

      typeDelete: [],
      debug_append_info: 1,

      myicn: {
        name: '',//'el-icon-check', //el-icon-warning-outline // el-icon-loading
        style: '', // color: red // color: green
        type: ''
      },
      mediaData: [],
      reserveData: [], // reserveId: 2, copyId: 3, userId: 2, state: "已预约"
    }
  }
  , methods: {
    checkReserve() {

      var userId = this.$cookies.get('currentUser').id
      if (!userId) {
        this.$message.warning('您的登陆已过期')
        this.$router.replace("/login")
      }
      axios({
        method: 'post',
        url: '/user/book/allReservation',
        data: {
          userId: userId
        }
      }).then(resp => {
        console.log("预约查询: 状态码为", resp.status, "; data:", resp.data, "; data.record:", resp.data.record);
        this.mediaData = resp.data.result;
        this.searchInfo()

      }).catch(error => {
        console.log("Error: 查询预约出错， ", error);
      })

      // 查询copybook的完整信息用于显示
    }, appendInfo() {
      if (this.debug_append_info)
        console.log("Append info: media data:", this.mediaData)

      for (var i in this.mediaData) {

        this.mediaData[i]['test'] = {info: i + "test"}
        if (this.debug_append_info)
          console.log("Append info: for loop , data[i]: ", this.mediaData[i],
              "; mediaData[i].test: ", this.mediaData[i]['test'])

      }

      if (this.debug_append_info)
        console.log("Append info:　out of the for loop", this.mediaData)

    }, searchInfo() {
      if (this.debug_append_info)
        console.log("search info: before ths for loop")


      console.log("media:", this.mediaData)
      for (var i in this.mediaData) {
        if (this.debug_append_info)
          console.log("Search info: envoke asy com in loop i:", i);
        this.doAsyCom(i);
      }

    }, doAsyCom(i) {
      if (this.debug_append_info)
        console.log("doAsyCom : before ths axios, with index:", i)

      axios({
        method: 'post', url: '/resources/books/copybook/allInfo',
        data: {copyId: this.mediaData[i].copyId}
      }).then(resp => {
        if (this.debug_append_info)
          console.log("doAsycom : axios by ", i, " response.data:", resp.data);


        var info = {
          isbn: resp.data.copybook.isbn, bookId: resp.data.copybook.bookId,
          title: resp.data.book.title, authorName: resp.data.book.authorName,
          coverUrl: resp.data.book.coverUrl, holdPlace: resp.data.library.name
        }
        this.mediaData[i].info = info;
      })
      if (this.debug_append_info)
        console.log("doAsycom : after axios mediaData[0]['info']: ", this.mediaData[0]['info']);


      // console.log("reserVationData.length: ", this.reserveData.)
      // if (i == this.reserveData.length){
      this.myicn.name = 'el-icon-loading'
      this.loadingTimer = setTimeout(this.loadingFinished, 1000)
      this.timer = setTimeout(this.get, 1000);
      // }

    }, deleteReserve(reserveId, copyId, userId) {
      var id = this.$cookies.get('currentUser').id

      axios({
        method: 'post', url: 'user/book/cancel', data:
            {userId: id, copyId: copyId}
      }).then(resp => {
        console.log("取消预约成功！状态码：", resp.status)
        if (resp.status == 200) {

          this.$set(this.typeDelete, reserveId, 'danger')
          this.$set(this.reserveData.i, 'state', '已取消')

          // reserveData[i].state = '已取走';
          setTimeout(function () {
            reserveData.splice(i, 1); // index=1
          }, 3000)

          this.$message("您已成功取消预约!");
        } else {

        }

      }).catch(error => {
        console.log("取消预约出错！", error)
      })
      this.$message.info("功能待实现")


    }, loadingFinished() {
      this.myicn.name = 'el-icon-success'
      this.myicn.style = 'color: green;'


    },
    get() {
      if (this.debug_append_info)
        console.log("getter : after 2000ms test mediaData[0]: ", this.mediaData[0]
            , "mediaData[0].info:", this.mediaData[0]['info']);
      this.reserveData = this.mediaData
    }, mounted() {
      if (this.isSearch)
        this.checkReserve()
    }
  }, updated() {
  }
}
</script>

<style scoped src="../../assets/my_css/app.css"></style>
<style scoped>
.around-button:hover {
  background-color: transparent;
}
</style>