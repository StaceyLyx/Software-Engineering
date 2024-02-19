package com.se.onlinelibrary.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.se.onlinelibrary.utils.LffGiteeImgBed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class ImageController {

    @ResponseBody
    @RequestMapping("/uploadUrl")
    public ResponseEntity<Object> uploadUrl(String name, String binaryImg) throws Exception {
        log.info("请求成功");
        String originaFileName = name;

        if (originaFileName == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //设置图片名字： xxxxx_xxxxx.xxx
        String suffix = originaFileName.substring(originaFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + suffix;

        // 返回给前端的参数:
        //  生成请求地址；
        //  处理base64字符串（enabled，后端传输的数据大小受限，改为在前端进行处理）
        //  提前生成下载地址给数据库存储
        Map<String, Object> paramMap = new HashMap<>();

        //文件夹路径： ../img/todayDate/
        String targetDir = LffGiteeImgBed.PATH + fileName;

        //generate request path
        // https://gitee.com/api/v5/repos/lifeife/sebms2021/contents/img/20210414/1618371069455_d3accb9d-7ab8-4aad-a63e-8c295b1a3f51.jpg
        String requestUrl = String.format(LffGiteeImgBed.CREATE_REPOS_URL, LffGiteeImgBed.OWNER,
                LffGiteeImgBed.REPO_NAME, targetDir);
        log.info("请求Gitee仓库路径:{}", requestUrl);
        paramMap.put("requestUrl", requestUrl);

        // 处理输入的图片base64编码，去掉base64前缀
        String[] splitStr = binaryImg.split("base64,");
        System.out.println("split lenth : " + splitStr.length);
        if (splitStr.length > 2)
            binaryImg = splitStr[1];
        paramMap.put("binaryImg", binaryImg);

        String downloadUrl = LffGiteeImgBed.getDownload() + fileName;
        paramMap.put("downloadUrl", downloadUrl);
        // System.out.println(downloadUrl);
        // https://gitee.com/lifeife/sebms2021/raw/master/img/20210414/filename_dhsajd.png

        return ResponseEntity.status(200).body(paramMap);
    }


    /**
     * 上传图片
     *
     * @param
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/uploadImg")
    public ResponseEntity<Object> uploadImg(String name, String binaryImg) throws Exception {

//    public ResponseEntity<Object> uploadImg(@RequestParam("file") MultipartFile file) throws Exception {
//        System.out.println(file);
//        String originaFileName = file.getOriginalFilename();
        log.info("请求成功");

        System.out.println("name: " + name + " binaryImg: " + binaryImg);
        String originaFileName = name;
        //上传图片不存在时
        if (originaFileName == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String suffix = originaFileName.substring(originaFileName.lastIndexOf("."));
        //设置图片名字
        String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + suffix;

//        String paramImgFile = Base64.encode(file.getBytes());
//        String paramImgFile = binaryImg;
//
//        paramImgFile = binaryImg.split("base64,")[1];
//        System.out.println(paramImgFile);

        //设置转存到Gitee仓库参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("access_token", LffGiteeImgBed.ACCESS_TOKEN);
        paramMap.put("message", LffGiteeImgBed.ADD_MESSAGE);
//        paramMap.put("content", paramImgFile);

        //转存文件路径
        String targetDir = LffGiteeImgBed.PATH + fileName;
        //设置请求路径
        String requestUrl = String.format(LffGiteeImgBed.CREATE_REPOS_URL, LffGiteeImgBed.OWNER,
                LffGiteeImgBed.REPO_NAME, targetDir);
        log.info("请求Gitee仓库路径:{}", requestUrl);

        System.out.println(paramMap);
        String resultJson = HttpUtil.post(requestUrl, paramMap);
        JSONObject jsonObject = JSONUtil.parseObj(resultJson);
        //表示操作失败
        if (jsonObject == null || jsonObject.getObj("commit") == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        JSONObject content = JSONUtil.parseObj(jsonObject.getObj("content"));
        return ResponseEntity.status(200).body(content.getObj("download_url"));
    }


    @ResponseBody
    @RequestMapping(path = "/savePhoto", method = RequestMethod.GET)
    public ResponseEntity<Object> addDish(@RequestParam("imgFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("addDish!");
        String path = null;// 文件路径
        Map<String, Object> map = new HashMap<>();
        if (file != null) {// 判断上传的文件是否为空

            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            System.out.println("上传的文件原名称:" + fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase())
                        || "PNG".equals(type.toUpperCase())
                        || "JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + "." + type;
                    // 设置存放图片文件的路径
                    path = realPath +/*System.getProperty("file.separator")+*/trueFileName;
                    System.out.println("存放图片文件的路径:" + path);
                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                    System.out.println("文件成功上传到指定目录下");
                    map.put("res", 1);
                    map.put("path", path);
                } else {
                    System.out.println("不是我们想要的文件类型,请按要求重新上传");
                    //return null;
                    map.put("res", 0);
                }
            } else {
                System.out.println("文件类型为空");
                //return null;
                map.put("res", 0);
            }
        } else {
            System.out.println("没有找到相对应的文件");
            map.put("res", 0);
            //return null;
        }

        response.getWriter().println("fkasgkfa");
        response.setContentType("application/json;charset=UTF-8");
        if (map.get("res").equals(0)) {
            return ResponseEntity.badRequest().body(map);
        } else
            return ResponseEntity.status(200).body(map);
    }
}
