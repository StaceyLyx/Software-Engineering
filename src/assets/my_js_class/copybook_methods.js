import axios from "axios";


export default {
    name: 'copybookUtils',
    functions: {
        getAllBookInfo: getAllBookInfo,
        getBook: getBook
    }
}


function getAllBookInfo(data){
    /**
     * 用 copyid 查找 单个copy对象
     * copy对象查找单个 bookid
     * 返回 result{ copybook: object, book: object}
     */
    console.log("进入函数 getallinfo， 传入data.copyId:", data.copyId)
    if (data.copyId){
        axios({
            method: 'get', url: "/resources/books/copybook/allInfo",
            data: {copyId: data.copyId}
        }).then(resp => {
            console.log("请求成功 data:", resp.data)
            if(resp.status == 200){
                console.log("data.copybook:", resp.data.copybook)
            var retVal = {
                isbn: resp.data.copybook.isbn,
                bookId : resp.data.copybook.bookId,
                title: resp.data.book.title,
                authorName: resp.data.book.authorName,
                coverUrl: resp.data.book.coverUrl,
                holdPlace: resp.data.library.name
            }

                console.log("函数返回值:", retVal)

                return retVal;

            }else{
                console.log("状态码不正确！")
            }
        }).catch(error => {
            console.log("appendinfo 出错！", error)
            return null
        })
        console.log("appendinfo 出错！")
        return null
    }else if(data.bookId){
        console.log("进入函数 getallinfo-if-bookId， 传入data.copyId:", data.bookId)

    }else if(data.reserveId){
        console.log("进入函数 getallinfo-if-reserveId， 传入data.copyId:", data.reserveId)

    }

}


function getBook(data){
    if(data.bookId){
        axios({
            method: 'get',
            url:'/resources/books/'+ data.bookId,
            data:{}
        }).then(resp=>{
            console.log("getBook成功!");
            resp

        }).catch(error=>{
            console.log("copybook_methods.js: getBook出错！", error)
        })
    }

}


function my_copybook_prototype() {
    var obj = {
        copyId: 1000,
        isbn: "0000000000000-001",

        bookId: "原件",
        book: Object,


        libraryId: 1000,
        libraryName: "图书馆",

        status: "空闲中", //  “被预约”， “已借出” “采购中”，“其他”

    }
    return obj;
}


function my_copybook(copyId, isbn, bookId, book, libraryId,
                     libraryName, status) {
    var obj= my_copybook_prototype();
    ob.assign('');
}