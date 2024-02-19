<template>
  <el-container id="show-details">
    <div id="detailsWindow" v-model="data1">
      <div id="show-image">
        <img :src="item.coverUrl" class="image">
      </div>
      <div id="show-others">
        <p>
          <span class="title">图书Id:</span>
          <span>{{ item.bookId }}</span>
          <br>
        </p>
        <hr/>
        <p>
          <span class="title">书名:</span>
          <span>{{ item.title }}</span>
          <br>
          <span class="title">作者:</span>
          <span>{{ item.authorName }}</span>
          <br>
        </p>
        <hr/>
        <p>
          <span class="title">ISBN:</span>
          <span>{{ item.isbn }}</span>
          <br>
          <span class="title">介绍:</span>
        <p>{{ item.description }}</p>
        <hr/>
        <span class="title">出版日期:</span>
        <span>{{ item.publishTime }}</span>
        <br>
<!--        <span class="title">所在图书馆:</span>-->
<!--        <span>{{ item.holdPlace }}</span>-->
<!--        <br>-->
        <hr/>
        <span class="title">副本Id:</span>
        <span>{{ copyData.copyId}}</span>
        <br>
        <span class="title">副本状态:</span>
        <span>{{ copyData.status}}</span>
        <br>
        <span class="title">副本所在图书馆:</span>
        <span>{{ copyData.holdPlace}}</span>
        <br>
        <span class="title">借阅/预约用户:</span>
        <span>{{ copyData.userId}}</span>
        <br>
        <el-button @click="gotoModify" class="btn">修改</el-button>
        <el-button @click="gotoDelete" class="btn">删除</el-button>
        <hr/>
        <el-button @click="AddCopy" class="btn">添加副本</el-button>


      </div>
    </div>
  </el-container>

    <!--data1=1时显示添加副本的表单-->
    <el-container id=v-if="data1=='1'">
    <el-main>
      <el-col :span="24">
        <el-form :model="ruleForm" :rules="rules"
                 ref="ruleForm" label-width="100px" class="demo-ruleForm" id="defined-pane">
          <el-col :span="3">
            <el-button size="mini" disabled>
              <span style="font-size: x-small"> 副本信息</span>
            </el-button>
          </el-col>
          <el-col :span="21">
            <hr>
            <br/>
          </el-col>
          <el-form-item label="所在图书馆" prop="libraryId">
            <el-col :span="12">
              <el-select v-model="ruleForm.libraryId" placeholder="请选择图书馆">
                <el-option label="文科图书馆" value="1"></el-option>
                <el-option label="理科图书馆" value="2"></el-option>
                <el-option label="医科图书馆" value="3"></el-option>
                <el-option label="张江图书馆" value="4"></el-option>
                <el-option label="古籍部" value="5"></el-option>
                <el-option label="李兆基图书馆" value="6"></el-option>
              </el-select>
            </el-col>
          </el-form-item>

          <el-form-item label="数量" prop="number">
            <el-col :span="12">
              <el-input v-model="ruleForm.number"></el-input>
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-col :span="24">
              <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-col>
            <el-col :span="24" style="text-align: center">{{ alertMsg }}</el-col>
          </el-form-item>
        </el-form>
      </el-col>
      <el-dialog title="添加副本成功！" :visible.sync="centerDialogVisible" width="30%" center>
      </el-dialog>
    </el-main>
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
      item: [{
        bookId: 1,
        title: '红楼梦',
        authorName: '',
        isbn: '11222',
        description: '',
        publishTime: '',
        // holdPlace: '',
        //副本信息
        copyData:[
          {status:'在库',
          holdPlace:'邯郸图书馆',
          copyId:'123001',
          userId:''
        },
          {status:'borrowed',
            holdPlace:'邯郸图书馆',
            copyId:'123002',
            userId:'333'
          }]
      }]
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('http://localhost:8181/admin/resources/books/addCopybook', {
            bookId: this.ruleForm.bookId,
            isbn: this.ruleForm.isbn,
            number: this.ruleForm.number,
            libraryId: this.ruleForm.libraryId,
          }).then(resp => {
            if (resp.status == 201) // 注册成功
              this.centerDialogVisible = true;
          }).catch(error => {
            if (resp.status == 400) {
              this.alertMsg = "添加管理员失败"
              console.log(resp.data.message);
            } else {
              console.log(error)
              this.alertMsg = '请求错误';
            }
          })
        } else {
          console.log('填写有误');
          return false;
        }
      });
    },
    AddCopy() {
      //改变data1的状态，显示添加副本的表单
      this.data1 = 1;
    }
  },
    gotoModify() {
      //指定到修改页面
      this.$router.replace('/manage/modifyBook')
    },
    gotoDelete() {
      //指定到删除页面
      this.$router.replace('/manage/deleteBook')
    },
  updated() {
    // var _this = this;
    // console.log("bookid is:", this.$route.params.bookid)
    // this.$axios.post(
    //     'http://localhost:8181/resources/books/' + this.$route.params.bookid,
    //     {
    //       "bookId": this.$route.params.bookid
    //     }
    // ).then(function (resp) {
    //   _this.item = resp.data;
    //   console.log(this.tableData)
    // }).catch(error => {
    //   console.log(error)
    // })
  }, created() {
    var _this = this;
    console.log("bookid is:", this.$route.params.bookid)
    this.$axios.post(
        'http://localhost:8181/resources/books/' + this.$route.params.bookid,
        {
          "bookId": this.$route.params.bookid
        }
    ).then(function (resp) {
      _this.item = resp.data;
      console.log(this.item)
    }).catch(error => {
      if (error.response.status == 400)
        this.$router.replace("/error400")
      console.log(error)
    })


    this.$axios.post(
        'http://localhost:8181/resources/AllCopybooks' + this.$route.params.bookid,
    {
      "bookId": this.$route.params.bookid
    }).then(function (resp) {
      _this.copyData = resp.data;
      console.log(this.copyData)
    }).catch(error => {
      if (error.response.status == 400)
        this.$router.replace("/error400")
      console.log(error)
    })
  }
}
</script>

<style scoped>
#detailsWindow {
  box-shadow: 0 0 24px 12px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  border: 1px #2c3e50;

  background-color: #ffffff;
  width: fit-content;
  height: fit-content;

  margin-right: 200px;
  padding: 40px 30px 20px 20px;

  width: 90%;
  display: inline-block;
}

.image {
  height: 50%;
  /*padding-top: 50%;*/
}

#show-details {
  background-color: rgb(84, 92, 100);
  padding-top: 100px;
  padding-left: 80px;
}

#show-image {
  box-shadow: 0 0 24px 12px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  border: 1px #2c3e50;

  margin-left: 100px;

  display: inline-block;
}

#show-others {
  font-size: large;
  margin-right: 40px;
  width: 50%;
  float: right;

}

.btn {
  margin-top: 10px;
  margin-right: 20px;
}

.title {

  font-weight: bolder;
  margin-right: 10px;
}
</style>