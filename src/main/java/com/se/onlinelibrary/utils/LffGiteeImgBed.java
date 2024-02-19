package com.se.onlinelibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LffGiteeImgBed {
    public static void setAccessToken(String newToken){
        ACCESS_TOKEN  = newToken;

    }
    /**
     * 配置码云图床
     */
    public static String getNowDate() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(now);
    }

    public static  String ACCESS_TOKEN = "8f6c56d6d0e28f9b58966f156436c160";
    public static final String OWNER = "lifeife";
    public static final String REPO_NAME = "sebms2021";
    public static final String FOLDER_NAME = "img";
    public static final String PATH = FOLDER_NAME + "/" + getNowDate() + "/";

    public static final String ADD_MESSAGE = "add img";
    public static final String DEl_MESSAGE = "del img";
    public static String CREATE_REPOS_URL = "https://gitee.com/api/v5/repos/%s/%s/contents/%s";
    public static String DEL_IMG_URL = "https://gitee.com/api/v5/repos/%s/%s/contents/%s";
    public static String getDownload(){
        String ret = "https://gitee.com/" + OWNER +
                "/" +REPO_NAME +
                "/raw/master/img/" + LffGiteeImgBed.getNowDate() + "/";
        return ret;
    }
}

