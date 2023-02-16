package com.yb.aiot.module.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.yb.aiot.module.common.config.FileConfig;
import com.yb.aiot.common.Result;
import com.yb.aiot.utils.IPUtil;
import lombok.SneakyThrows;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 文件工具类
 * <p>
 *
 * @author author
 * @date 2022/11/10 11:53
 */
public class MyFileUtil {


    /**
     * 获取下载/预览文件地址
     *
     * @param filename 文件名
     * @return java.lang.String
     */
    public static String getDownFileUrl(String filename) {
        if (StrUtil.isEmpty(filename)) return null;
        return IPUtil.getServeUrl() + FileConfig.downPrefix + filename;
    }

    /**
     * 下载/预览指定文件
     *
     * @param response 返回体
     * @param filename 文件名
     */
    @SneakyThrows
    public static void downFile(HttpServletResponse response, String filename) {
        String path = getAbsolutePath(filename);
        if (!FileUtil.exist(path)) {
            response.sendError(404, "文件不存在");
            return;
        }
        File file = FileUtil.file(path);
        final String mimeType = FileUtil.getMimeType(file.getName());
        final String contentType = ObjectUtil.defaultIfNull(mimeType, "application/octet-stream");
        BufferedInputStream in = FileUtil.getInputStream(file);
        ServletUtil.write(response, in, contentType, "");
        IoUtil.close(in);
    }

    /**
     * 上传头像
     *
     * @param file           文件
     * @param saveFolderName 文件保存目录
     * @return common.com.yb.aiot.Result
     */
    public static Result uploadFile(MultipartFile file, String saveFolderName) {
        if (ObjectUtils.isEmpty(file.getOriginalFilename())) {
            return Result.fail("请选择要上传的文件!");
        }
        String fileName = makeFileName(saveFolderName, file.getOriginalFilename());
        String saveFilePath = String.format("%s%s", FileConfig.rootFolder, saveFolderName);
        File saveFile = new File(saveFilePath, fileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (saveFile.exists()) {
            return Result.ok(getDownFileUrl(fileName));
        }
        return Result.fail("上传文件失败,请重试!");
    }

    /**
     * 上传jar文件
     *
     * @param file           文件
     * @param saveFolderName 文件保存目录
     * @return common.com.yb.aiot.Result
     */
    public static Result uploadJarFile(MultipartFile file, String saveFolderName) {
        String fileName = file.getOriginalFilename();
        String saveFilePath = String.format("%s%s", FileConfig.rootFolder, saveFolderName);
        File saveFile = new File(saveFilePath, fileName);
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (saveFile.exists()) {
            return Result.ok(saveFolderName+""+fileName);
        }
        return Result.fail("上传文件失败,请重试!");
    }

    /**
     * 生成文件名
     *
     * @param saveFolderName 文件保存目录
     * @param oldFileName    源文件名
     * @return java.lang.String
     */
    public static String makeFileName(String saveFolderName, String oldFileName) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        uuid = saveFolderName.replace("/", "") + uuid;
        return String.format("%s%s%s", uuid, ".", StringUtils.unqualify(oldFileName));
    }

    /**
     * 根据设备名称生成图片名称
     *
     * @param deviceName 设备名称
     * @return java.lang.String
     */
    public static String makeFileName(String deviceName) {
        String format = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        return String.format("%s@%s@%s.jpeg", deviceName, format, IdUtil.simpleUUID());
    }

    /**
     * 获取指定文件路径
     *
     * @param fileName 文件名
     * @return java.lang.String
     */
    public static String getAbsolutePath(String fileName) {
        String path = FileConfig.rootFolder;
        if (!fileName.contains("@")) {
            return String.format("%s%s%s", path, FileConfig.avatarFolder, fileName);
        }
        String[] names = fileName.split("@");
        String name = names[0];
        String ymd = names[1];
        ymd = ymd.substring(0, ymd.length() - 6);
        String ym = ymd.substring(0, (ymd.length() - 2));
        String year = ymd.substring(0, (ymd.length() - 4));
        path = String.format("%s/%s/%s/%s/%s", path, name, year, ym, ymd);
        FileUtil.mkdir(path);
        return String.format("%s/%s", path, fileName);
    }

    /**
     * 根据文件名删除文件
     *
     * @param filename 文件名
     */
    public static void deleteFile(String filename) {
        FileUtil.del(getAbsolutePath(filename));
        File file = new File(getAbsolutePath(filename));
        for (int i = 0; i < 3; i++) {
            file = FileUtil.file(file.getParent());
            deletePicturePath(file);
        }
    }

    /**
     * 删除空图片文件目录
     *
     * @param file file
     */
    public static void deletePicturePath(File file) {
        if (Objects.requireNonNull(file.list()).length == 0) {
            FileUtil.del(file);
        }
    }

}
