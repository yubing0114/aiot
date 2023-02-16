package com.yb.aiot.module.common.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.yb.aiot.module.common.entity.Orginfo;
import com.yb.aiot.common.Result;
import com.yb.aiot.utils.YmlUtil;
import org.springframework.util.ObjectUtils;

import java.io.FileReader;
import java.io.FileWriter;

/**
 * <p>
 * 机构信息工具类
 * <p>
 *
 * @author author
 * @date 2022/4/28 16:30
 */
public class OrginfoUtil {

    private static final String ymlName = "orginfo.yml";

    /**
     * 获取机构信息
     *
     * @return entity.common.module.com.yb.aiot.Orginfo
     */
    public static Orginfo getOrginfo() {
        String ymlPath = YmlUtil.getYmlPath(ymlName);
        try {
            YamlReader reader = new YamlReader(new FileReader(ymlPath));
            return reader.read(Orginfo.class);
        } catch (Exception e) {
            System.err.println("读取配置文件失败!");
        }
        return null;
    }

    /**
     * 获取机构信息
     *
     * @return common.com.yb.aiot.Result
     */
    public static Result readAppConf() {
        String ymlPath = YmlUtil.getYmlPath(ymlName);
        try {
            YamlReader reader = new YamlReader(new FileReader(ymlPath));
            Orginfo appConf = reader.read(Orginfo.class);
            if (!ObjectUtils.isEmpty(appConf.getAppkey())) {
                return Result.ok(appConf);
            }
        } catch (Exception e) {
            System.err.println("读取配置文件失败!");
        }
        return Result.fail(202, "请初始化机构信息!");
    }

    /**
     * 配置机构信息
     *
     * @param orginfo 机构信息
     * @return common.com.yb.aiot.Result
     */
    public static Result writeAppConf(Orginfo orginfo) {
        String ymlPath = YmlUtil.getYmlPath(ymlName);
        try {
            YamlWriter writer = new YamlWriter(new FileWriter(ymlPath));
            writer.getConfig().writeConfig.setWriteRootTags(false);
            writer.write(orginfo);
            writer.close();
            return Result.ok("初始化机构信息成功");
        } catch (Exception e) {
            System.err.println("写配置文件失败!");
        }
        return Result.fail("初始化机构信息失败");
    }

}
