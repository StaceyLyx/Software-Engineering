<template>
  <el-container>
    <el-main>
      <el-col :span="24">
        <el-form :model="ruleForm" :rules="rules"
                 ref="ruleForm" label-width="100px" class="demo-ruleForm" id="defined-pane">

          <el-col :span="3">
            <el-button size="mini" disabled>
              <span style="font-size: x-small"> 图书基本信息</span>
            </el-button>
          </el-col>
          <el-col :span="21">
            <hr>
            <br/>
          </el-col>

          <el-form-item label="标题" prop="title">
            <el-col :span="24">
              <el-input v-model="ruleForm.title" show-title></el-input>
            </el-col>
            <br/>
            <el-col :span="24">
            </el-col>
          </el-form-item>

          <el-form-item label="作者" prop="authorName">
            <el-col :span="10">
              <el-input v-model="ruleForm.authorName"></el-input>

            </el-col>
          </el-form-item>

          <!--          也就是说，此处用户的行为在后端对应两次插入操作：新建book元组、建立author和该book的关系-->
          <!--          未实现-->
          <el-tooltip :content="!seeAddAuthor?'点击打开添加作者页':'关闭添加作者页'"
                      placement="bottom" effect="dark">
            <el-col :span="1">
              <el-button @click="seeAddAuthor = !seeAddAuthor" size="mini" circle
                         :icon="!seeAddAuthor?'el-icon-circle-plus-outline':'el-icon-remove-outline'">
              </el-button>
            </el-col>
          </el-tooltip>
          <el-col :span="23">
            <hr/>
            <br>
          </el-col>

          <el-dialog title="添加作者" :visible.sync="seeAddAuthor" width="50%" center>
            <AddAuthor/>
          </el-dialog>

          <el-form-item label="ISBN" prop="ISBN">
            <el-col :span="24">
              <el-input show-word-limit maxlength="13"
                        v-model="ruleForm.ISBN"></el-input>
            </el-col>
          </el-form-item>


          <el-form-item label="出版时间" prop="publishTime">
            <el-col :span="10">
              <el-date-picker value-format="yyyy-MM" placeholder="选择日期"
                              v-model="ruleForm.publishTime" type="date">
              </el-date-picker>
            </el-col>
            <el-form-item label="所在图书馆" prop="holdPlace">
              <el-col :span="12">
                <el-select v-model="ruleForm.holdPlace" placeholder="请选择图书馆">
                  <el-option v-for="(library ,index) in libraries"
                             :label="library" :value="index+1">
                  </el-option>
<!--                  <el-option label="邯郸图书馆" value="邯郸图书馆"></el-option>-->
<!--                  <el-option label="张江图书馆" value="张江图书馆"></el-option>-->
<!--                  <el-option label="江湾图书馆" value="江湾图书馆"></el-option>-->
<!--                  <el-option label="枫林图书馆" value="枫林图书馆"></el-option>-->
                </el-select>
              </el-col>
            </el-form-item>

          </el-form-item>

          <el-form-item label="封面图片" prop="coverUrl">
            <el-col :span="19">
              <el-input placeholder="请输入图片网址或选择本地图片并上传" v-model="ruleForm.coverUrl">
                <template slot="prepend">https://</template>
              </el-input>
            </el-col>
            <el-col :span="5">

              <el-button @click="chooseImg" style="margin-left: 10px">选择</el-button>
              <el-button :type="(needPrepare && isPrepared)?'success':(needPrepare && !isPrepared)?'warning':''"
                         v-if="needPrepare"
                         style="margin-left: 10px"
                         @click="postResource">上传
              </el-button>

            </el-col>
          </el-form-item>
          <div class="center imgView">
            <el-avatar shape="square" v-if="isImgShow"
                       :size="300" fit="fill"
                       :src="imgPath"></el-avatar>
          </div>


          <el-tooltip content="点击自动填写简介" placement="bottom" effect="dark">
            <el-col :span="1">
              <el-button content="点击自动填写简介" @click="generateDescription"
                         size="mini" icon="el-icon-edit" circle></el-button>
            </el-col>
          </el-tooltip>
          <el-col :span="23">
            <hr/>
            <br/>
          </el-col>

          <el-form-item label="简介" prop="description">
            <el-col :span="24">
              <el-input type="textarea" v-model="ruleForm.description"></el-input>
            </el-col>
          </el-form-item>

          <el-col :span="24">
            <br>
            <hr/>
            <br>
          </el-col>

          <el-form-item>
            <el-col :span="24">
              <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-col>
            <el-col :span="24" style="text-align: center">{{ alertMsg }}</el-col>
          </el-form-item>
        </el-form>
      </el-col>
      <el-dialog title="添加图书成功！" :visible.sync="centerDialogVisible" width="30%" center>
        <span>名称：{{ ruleForm.title }}</span>
        <hr/>
        <span>作者：{{ ruleForm.authorName }}</span><br/>
        <span>简介：{{ ruleForm.description }}</span>
        <hr/>
        <span>ISBN：{{ ruleForm.ISBN }}</span><br/>
        <span>出版时间：{{ ruleForm.publishTime }}</span><br/>
        <span>封面图片：{{ ruleForm.coverUrl }}</span><br/>
        <span>所在图书馆：{{ ruleForm.holdPlace }}</span><br/>


        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="goToDetails">查看图书详情</el-button>
  </span>
      </el-dialog>
    </el-main>
  </el-container>
</template>


<script>
import axios from "axios";
import AddAuthor from "@/components/Authors/AddAuthor";

let inputElement = null

export default {
  name: "AddBook",
  components: {AddAuthor},
  props: {
    msg: 'This is add book page',
  },
  data() {
    var checkISBN = (rule, value, callback) => {
      // 13位或10位数字
      var ISBN_success = /[0-9]{10,13}/;
      if (value.match(ISBN_success)) {
        return this.$axios.post(
            'http://localhost:8181/book/upload/isbn',
            {isbn: this.ruleForm.ISBN})
            .then(resp => {
              console.log('ISBN验证请求成功; 获取数据: ', resp);
              if (resp.status == 200) {
                return callback();
              }
              return callback(new Error('无法识别的后端连接'));
            }).catch(error => {
              console.log(error);
              if (error.response.status == 400) {
                return callback(new Error('ISBN已存在！'));
              } else {
                return callback(new Error('连接错误'));
              }
            });
      } else {
        return callback(new Error('只能包含数字或-'));
      }
    };
    var checkUrl = (rule, value, callback) => {
      console.log(value);
      if (value.toString().includes('.jpg') ||
          value.toString().includes('.jpeg') ||
          value.toString().includes('.png')) {

        this.imgPath = value;
        this.isImgShow = true;
        return callback();
      } else {
        this.imgPath = this.defaultImg;
        this.isImgShow = false;
        return callback(new Error('请以.jpg/.jpeg/.png格式结尾,默认图片:' + this.defaultImg));
      }
    }
    return {
      libraries: this.$store.getters.allLibrary,

      isPrepared: true,
      needPrepare: false,
      isImgShow: false,

      alertMsg: '',
      seeAddAuthor: false,
      searchAuthorResult: null,

      downloadUrl: '',
      binaryImg: '', // base64编码的img,用作content发送请求;注意传到后端做一下处理?
      imgName: '', // 文件名,用于axios向后端发送请求获得requestUrl
      requestUrl: '', // gitee请求上传图片资源\并获取download url?或直接把request设成ruleForm的src
      imgFile: null, //?
      imgPath: '', // 未形成网址时,显示给用户

      defaultImg: 'https://mingzihui.lianzhixiu.com/d/file/jingdian/2018-05-30/6dfdfadf8049ae4767eac0659e98c167.jpeg',

      bookID: Number,

      isExist: null,
      centerDialogVisible: false,
      ruleForm: {
        title: '',
        authorName: '',
        description: '',
        ISBN: '',
        publishTime: '',
        coverUrl: '',
        holdPlace: '',
      }, rules: {
        ISBN: [
          {required: true, message: 'ISBN不能为空', trigger: 'blur'},
          {validator: checkISBN, trigger: 'blur'},
          {min: 13, max: 13, message: '⻓度为13个字符', trigger: 'change'},
        ], title: [
          {required: true, message: '请输入书名', trigger: 'blur'}
        ], authorName: [
          {required: true, message: '请输入作者', trigger: 'blur'},
        ], publishTime: [
          {required: true, message: '不能为空', trigger: 'blur'},
        ], coverUrl: [
          {required: true, message: '请添加图片', trigger: 'blur'},
          {validator: checkUrl, trigger: 'blur'}
        ], holdPlace: [
          {required: true, message: '请选择图书馆', trigger: 'blur'},
        ]
      } // Ending the definition of rules.
    }; // Ending the return statement.
  } // Ending the data definition.
  , methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/book/upload', {
            title: this.ruleForm.title,
            authorName: this.ruleForm.authorName,
            description: this.ruleForm.description,
            isbn: this.ruleForm.ISBN,
            publishTime: this.ruleForm.publishTime,
            coverUrl: this.ruleForm.coverUrl,
          }).then(resp => {
            console.log("submit resp data: ", resp.data);
            if (resp.status == 200 || resp.status == 201) {
              // 需要后端返回一个bookID,方便跳转详情页 ok
              this.bookID = resp.data.bookId
              // console.log("submit result: ", resp.data.result);
              this.centerDialogVisible = true;
            } else {
              console.log("status:", resp.status);
              console.log("data:", resp.data);
              this.alertMsg = '无法识别的后端状态';
              this.$message.warning(this.alertMsg)
            }
          }).catch(error => {
            console.log(error.response)
            console.log(error)
            this.alertMsg = '上传出错啦';
          })
        } else {
          console.log('提交非法,数据错误');
          return false;
        }
      });
    }, resetForm(formName) {
      this.$refs[formName].resetFields();
    }, // 跳转到图书的详情页
    goToDetails() {
      this.centerDialogVisible = false;
      this.$router.replace({path: '/books/' + this.bookID}) // 跳转到图书详情页面
    },
    handleSelect(item) {
      console.log(item);
      this.ruleForm.authorName = item;
    }, generateDescription() {
      var desp = "本书" + this.ruleForm.title + "由作家" +
          this.ruleForm.authorName + "写成并于" + this.ruleForm.publishTime + "出版";
      this.ruleForm.description = desp;
    }, chooseImg() {
      if (inputElement === null) {
        // 生成文件上传的控件
        inputElement = document.createElement('input');
        inputElement.setAttribute('type', 'file');
        inputElement.setAttribute('accept', 'image/*');
        inputElement.style.display = 'none'

        inputElement.addEventListener('change', this.showImg, false)

        document.body.appendChild(inputElement)
      }
      inputElement.click()

      console.log(" 1- 点击创建元素/请选择文件", inputElement)

    }, showImg(el) {
      if (el && el.target && el.target.files && el.target.files.length > 0) {
        console.log(el);
        const files = el.target.files[0];
        this.imgName = files.name;
        console.log("文件名为:", this.imgName);
        this.imgFile = files;
        let _this = this;

        if (files.type.indexOf('image') === -1) { //如果不是图片格式
          // this.$dialog.toast({ mes: '请选择图片文件' });
          this.$message.error('请选择图片文件');
        } else {
          const reader = new FileReader(); // 创建读取文件对象
          reader.readAsDataURL(el.target.files[0]); // 发起异步请求，读取文件
          reader.onload = function () { // 文件读取完成后
            console.log(" 2- 成功转换为base64, 设置显示图片");
            //
            _this.imgPath = this.result;
            _this.binaryImg = this.result;
            console.log(_this.imgName, _this.binaryImg);
            //
            _this.isImgShow = true;
            _this.needPrepare = true;
            re
            _this.isPrepared = false;

          }
        }
      }
      this.getRequestUrl()
      // console.log("For sure: name: ", this.imgName, " result: ", this.binaryImg)
    }, getRequestUrl() {
      axios({
        method: 'post',
        url: '/uploadUrl',
        data: {name: this.imgName, binaryImg: this.binaryImg}
      }).then(resp => {
        console.log(" 2 - 后端请求request url成功:", resp);
        this.requestUrl = resp.data.requestUrl;
        // this.binaryImg = resp.data.binaryImg;
        this.downloadUrl = resp.data.downloadUrl

      }).catch(error => {

        console.log("请求URL出错啦:", error)
      })


    }, postResource() {

      var index = this.binaryImg.indexOf('base64,');
      if (index > 0) {
        index = index + 'base64,'.length;
        this.binaryImg = this.binaryImg.substring(index);
      }
      console.log(this.binaryImg)

      let formData = new FormData();
      formData.append('content', this.binaryImg);

      axios({
        url: this.requestUrl,
        method: 'post',
        data: {
          access_token: this.$cookies.get("imgBedToken"),
          content: this.binaryImg,
          message: "msg: upload " + this.requestUrl
        }
      }).then(resp => {
        console.log("请求成功! TO: ", this.requestUrl);
        console.log("获取资源: down url: ", resp)

        if (resp.status == 201) {
          console.log("创建资源成功! 设置url.....");
          this.ruleForm.coverUrl = this.downloadUrl;
          this.imgPath = this.downloadUrl;
          console.log("4 - 成功设置表单的value/成功设置显示图片的src! finished")
          this.isPrepared = true
        }
      }).catch(error => {
        console.log(error);
      })


    }
  }

}
</script>


<style scoped>

#defined-pane {
  width: 80%;
  height: fit-content;
  margin: auto;
  margin-top: 50px;
  border: 1px solid rgba(44, 62, 80, 0.2);
  box-shadow: 0 2px 12px 5px rgba(179, 192, 209, 0.2);
  color: #b3c0d1;
  padding: 50px 30px 20px 50px;
}

.hidden-component {
  display: none;
}

.imgView {
  padding: 10px;
  margin-bottom: 10px;
}

.center {
  width: fit-content;
  margin: auto;
}
</style>