package com.yb.aiot.module.common.config;

import lombok.Data;

/**
 * 文件上传配置类  (基于应用服务器的文件上传)
 *
 * @author kong
 */
@Data
public final class FileConfig {

    private FileConfig() {
    }

    // 文件保存的根目录，所有文件都保存在这个目录下
    public static String rootFolder = System.getProperty("user.dir") + "/file-folder";

    // 文件下载路径
    public static String downPrefix = "/file/down/";

    // 用户头像保存文件夹
    public static String avatarFolder = "/avatar/";

    //协议插件保存文件夹
    public static String protocolFolder = "/protocol/";

    // 视频保存文件夹
    public String videoFolder = "/video";
    // 音频保存地址
    public String audioFolder = "/audio";
    // apk保存地址
    public String apkFolder = "/apk";
    // file保存地址 (任意类型文件)
    public String fileFolder = "/common";
    // 图片允许的后缀
    public String imageSuffix = "jpg,jpeg,png,gif,ico,bmp,tiff,raw";
    // 视频允许的后缀
    public String videoSuffix = "mp4,avi,rmvb,mov,flv";
    // 音频允许的后缀
    public String audioSuffix = "mp3,aac,wav,wma,cda,flac,m4a,mid,mka,mp2,mpa,mpc,ape,ofr,ogg,ra,wv,tta,ac3,dts";
    // apk允许的后缀
    public String apkSuffix = "apk";
    // file允许的后缀 (为防止上传恶意文件，这里必须手动指定可上传的类型)
    public String fileSuffix = "jpg";
    // 文件最大大小,单位/B , 此为1G
    public long maxSize = 1024 * 1024 * 1024;

}
