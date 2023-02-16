package com.yb.aiot.module.common.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.yb.aiot.common.AuthConst;
import com.yb.aiot.common.Result;
import com.yb.aiot.module.common.config.FileConfig;
import com.yb.aiot.module.common.utils.MyFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-11-09
 */
@Api(tags = "文件")
@RestController
@RequestMapping("/file")
public class FileController {

    @ApiOperation("文件下载或访问")
    @GetMapping("/down/{filename}")
    public void downFile(@PathVariable("filename") String filename, HttpServletResponse response) {
        MyFileUtil.downFile(response, filename);
    }

    @ApiOperation("上传头像")
    @PostMapping("upload/avatar")
    @SaCheckPermission(AuthConst.USER)
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        return MyFileUtil.uploadFile(file, FileConfig.avatarFolder);
    }

    @ApiOperation("上传插件Jar包")
    @PostMapping("upload/jar")
    @SaCheckPermission(AuthConst.ADMIN)
    public Result uploadJarFile(@RequestParam("file") MultipartFile file){
        return MyFileUtil.uploadJarFile(file, FileConfig.protocolFolder);
    }
}
