<template>
  <el-container>
    <!--    输入框-->
    <el-main>
      <div style="width: fit-content;margin: auto">
        <el-form :model="usernameForm" :rules="usernameRule"
                 ref="checkReservationForm" style="width:500px;" label-position="top">
          <el-form-item label="请输入学工号，查询预约记录：" prop="username">

            <el-col :span="20">
              <el-input maxlength="11"
                        show-word-limit
                        v-model="usernameForm.username"></el-input>
            </el-col>
            <el-col :span="1">
              <i :class="myicn.name" :style="'margin-left: 2px;'+myicn.style"> </i></el-col>

            <el-col :span="3">
              <el-button :type="myicn.type" @click="checkReserve('checkReservationForm')">Check</el-button>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-col :span="24">
            </el-col>
          </el-form-item>


        </el-form>

      </div>
      <hr/>
      <div>


        <div class="my-table-list">

          <template v-for="(scope, index) in reserveData">

            <div class="my-table-pane">
              <reservation-table-view :scope="scope" style="width: fit-content"/>


              <div class="my-table-operation">
                <el-row :gutter="10">
                  <el-button @click="borrowByReserve(scope.reserveId,scope.copyId,scope.userId);"
                             size="mini"
                             :type="typeDelete[scope.reserveId]?'success':'default'"
                             :icon="typeDelete[scope.reserveId]?'el-icon-success':'el-icon-thumb'">
                    <!--                    :type="typeDelete[scope.reserveId]"-->
                    {{ typeDelete[scope.reserveId] ? "已　　取" : "借　　取" }}
                  </el-button>
                </el-row>

                <el-row :gutter="10" style="height: 5px">
                  <br/>
                </el-row>

                <el-row :gutter="10">
                  <el-button size="mini" type="primary">
                    <a :href="'/books/'+scope.info.bookId" class="bt-link">
                      图书详情</a>
                  </el-button>
                </el-row>
              </div>
            </div>
          </template>
        </div>
      </div>

      <!--    显示查询结果-->
      <!--      // reserveId: 2, copyId: 3, userId: 2, state: "已预约"-->
      <el-table :data="reserveData" class="mainTable" align="center">
        <!--        <el-table-column label="预约查询列表" width="1080px">-->

        <!--          <template slot-scope="scope">-->
        <!--            <div class="my-table-pane">-->
        <!--              <div class="img-view-div">-->
        <!--                <img :alt="'book[@'+scope.row.copyId+']'" class="img-book" :src="scope.row.info.coverUrl"/>-->
        <!--              </div>-->

        <!--              <div class="my-table-content">-->
        <!--                <div class="my-table-para">-->
        <!--                  <el-row :gutter="10">-->
        <!--                    <el-col :span="4" class="title-bold">预约编号:</el-col>-->
        <!--                    <el-col :span="20">{{ scope.row.reserveId }}</el-col>-->
        <!--                  </el-row>-->
        <!--                  &lt;!&ndash; 只有在info定义的时候才显示                  &ndash;&gt;-->
        <!--                  <div v-if="scope.row.info">-->
        <!--                    <el-row :gutter="10">-->
        <!--                      <el-col :span="4" class="title-bold">ISBN:</el-col>-->
        <!--                      <el-col :span="10"> {{ scope.row.info.isbn }}</el-col>-->
        <!--                    </el-row>-->

        <!--                    <el-row :gutter="10">-->
        <!--                      <el-col :span="4" class="title-bold">作者:</el-col>-->
        <!--                      <el-col :span="10">{{ scope.row.info.authorName }}</el-col>-->
        <!--                    </el-row>-->

        <!--                    <el-row :gutter="10">-->
        <!--                      <el-col :span="4" class="title-bold">馆藏地址:</el-col>-->
        <!--                      <el-col :span="10">{{ scope.row.info.holdPlace }}</el-col>-->
        <!--                    </el-row>-->
        <!--                  </div>-->
        <!--                  <el-row :gutter="10">-->
        <!--                    <el-col :span="4" class="title-bold">预约时间:</el-col>-->
        <!--                    <el-col :span="20">{{ scope.row.time }}</el-col>-->
        <!--                  </el-row>-->

        <!--                  <el-row :gutter="10">-->
        <!--                    <el-col :span="4" class="title-bold">预约状态:</el-col>-->
        <!--                    <el-col :span="20">{{ scope.row.status }}</el-col>-->
        <!--                  </el-row>-->
        <!--                </div>-->

        <!--              </div>-->

        <!--              <div class="my-table-operation">-->
        <!--                <el-row :gutter="10">-->
        <!--                  <el-button @click="borrowByReserve(scope.row.reserveId,scope.row.copyId,scope.row.userId);"-->
        <!--                             size="mini" :icon="typeDelete[scope.row.reserveId]?'el-icon-success':'el-icon-thumb'">-->
        <!--                    &lt;!&ndash;                    :type="typeDelete[scope.row.reserveId]"&ndash;&gt;-->
        <!--                    {{ typeDelete[scope.row.reserveId] ? "已　　取" : "借　　取" }}-->
        <!--                  </el-button>-->
        <!--                </el-row>-->

        <!--                <el-row :gutter="10" style="height: 5px">-->
        <!--                  <br/>-->
        <!--                </el-row>-->

        <!--                <el-row :gutter="10">-->
        <!--                  <el-button :href="'/book/'+scope.row.info.bookId" size="mini" type="primary">-->
        <!--                    图书详情-->
        <!--                  </el-button>-->
        <!--                </el-row>-->
        <!--              </div>-->
        <!--            </div>-->
        <!--          </template>-->
        <!--        </el-table-column>-->


      </el-table>
    </el-main>
    <el-footer style="height: 0px"/>

  </el-container>
</template>

<script>
import axios from "axios";
import reservationTableView from "@/components/Reserve/reservationTableView";

export default {
  name: "BorrowCheckReserve",
  components: {reservationTableView},
  data() {
    const checkUser = (rule, value, callback) => {
      const user_success = /^[0-9]+/; // 11位学号
      const user_len = /[0-9]{11}/;
      // 异步通讯检测学工号存在，参数列表存疑
      if (value.match(user_success) && value.match(user_len) && value.toString().length == 11) {
        return axios({
          method: 'post',
          url: '/register/username',
          data: {username: value}
        }).then(resp => {
          let res = resp.status
          // for test
          res = 202;

          if (res == 202) {
            this.changeIconStatus('check')
            return callback();
          } else if (res == 200) return callback(new Error('学工号不存在！'));

          else return callback(new Error('...Loading'));

        }).catch(error => {
          if (error.response && error.response.status == 403) return callback(new Error('学工号非法！'));
          else {
            console.log("检查username时出错，", error);
            return callback(new Error('未知错误'));
          }
        });

      }

      return callback(new Error('学工号为11位数字'));
    };

    return {
      loadingTimer: '',
      timer: '',
      btValue: 'danger',

      typeDelete: [],
      debug_append_info: 1,

      myicn: {
        name: 'el-icon-warning-outline',//'el-icon-check', //el-icon-warning-outline
        style: 'color: red', // green
        type: ''
      },
      usernameForm: {
        username: '18307111111'
      },
      usernameRule: {
        username: [
          {min: 11, max: 11, message: "学工号为11位数字", trigger: 'change'},
          {validator: checkUser, trigger: 'blur'}]
      },
      mediaData: [],
      reserveData: [], // reserveId: 2, copyId: 3, userId: 2, state: "已预约"
      typeStatus: {
        "已预约": 'primary'
      }
    }
  },
  methods: {
    checkReserve(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid)
          axios({
            method: 'POST',
            url: '/admin/resources/books/reservationCheck',
            data: {
              username: this.usernameForm.username
            }
          }).then(resp => {
            console.log("预约查询: 状态码为", resp.status, "; data:", resp.data, "; data.record:", resp.data.record);
            this.mediaData = resp.data.record;
            if (this.mediaData[0]) {

              this.typeDelete = [];
              this.searchInfo();
            } else {
              this.$message.warning("该用户没有预约记录")
              this.reserveData = []
            }

          }).catch(error => {
            if (error.response && error.response.status == 500)
              this.$message.warning("输入的学工号不是合法用户：学生/老师")
            console.log("Error: 查询预约出错， ", error);
          })
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

      for (var i in this.mediaData) {
        if (this.debug_append_info)
          console.log("Search info: envoke asy com in loop i:", i);
        this.doAsyCom(i);
      }


      setTimeout(console.log("Search info : check finish ", 0, "  " +
          "mediaData[0].info:", this.mediaData[0]['info']), 3000)

    }, doAsyCom(i) {
      if (this.debug_append_info)
        console.log("doAsyCom : before ths axios, with index:", i, "mediaData[i].copyId:", this.mediaData[i].copyId)

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

        this.mediaData[i]['info'] = info
      })
      if (this.debug_append_info)
        console.log("doAsycom : after axios mediaData[0]['info']: ", this.mediaData[0]['info']);


      // console.log("reserVationData.length: ", this.reserveData.)
      // if (i == this.reserveData.length){
      this.changeIconStatus('loading')
      this.loadingTimer = setTimeout(this.loadingFinished, 1000)
      this.timer = setTimeout(this.get, 1000);


      // }

    }, borrowByReserve(reserveId, copyId, userId) {
      console.log("点击取出：预约号：", reserveId)
      let adminId = this.$cookies.get('currentUser').id;
      let libraryId = this.$cookies.get('place');

      axios({
        method: 'post', url: '/admin/resources/books/reservationBorrow',
        data: {
          reserveId: reserveId,
          copyId: copyId,
          userId: userId,
          adminId: adminId,
          libraryId: libraryId
        }
      }).then(resp => {
        console.log("预约取书成功！状态码：", resp.status)
        if (resp.status == 200) {
          var reserveData = this.reserveData;
          for (var i in reserveData) {
            if (reserveData[i].reserveId == reserveId) {

              this.$set(this.typeDelete, reserveId, 'danger')


              var a = this.reserveData[i]
              a.status = '已取走'
              this.reserveData[i] = a

              // reserveData[i].state = '已取走';
              setTimeout(function () {
                reserveData.splice(i, 1); // index=1
              }, 3000)

              this.$message("用户" + this.usernameForm.username + "借书成功!");

              console.log("遍历: [" + i + "]", reserveData[i], "typedelete['i'r]=", this.typeDelete[reserveId + 'r'])

              break;
            }
          }
        }
      }).catch(error => {
        console.log("预约借书出错！", error)
      })

    },
    loadingFinished() {
      this.changeIconStatus('success')


    },
    get() {
      if (this.debug_append_info)
        console.log("Lastly: getter : after 2000ms test mediaData[0]: ", this.mediaData[0]
            , "mediaData[0].info:", this.mediaData[0]['info']);
      this.reserveData = this.mediaData
      console.log("this.reservation.info:", this.reserveData[0])
    },
    changeIconStatus(iconStatus) {
      console.log(" changing the icon status: ", iconStatus)
      if (iconStatus == 'loading') {
        this.myicn.name = 'el-icon-loading'
        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'error') {
        this.myicn.name = 'el-icon-error'
        this.myicn.style = 'color: red;'
      } else if (iconStatus == 'success') {
        this.myicn.name = 'el-icon-success'
        this.myicn.style = 'color: green;'
      } else if (iconStatus == 'default') {
        this.myicn.name = ''
        this.myicn.style = ''
      } else if (iconStatus == 'check') {
        this.myicn.name = 'el-icon-check';
        this.myicn.style = 'color: green';
        this.myicn.type = 'success'
      }

    }
  }, mounted() {
  }
}
</script>

<style scoped>

</style>