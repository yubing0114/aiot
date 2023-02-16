package com.yb.aiot.utils;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileReader;

/**
 * <p>
 * 读取配置文件工具类
 * <p>
 *
 * @author: author
 * @date: 2022/4/28 16:30
 */
public class YmlUtil {

    private static JSONObject ymlJSONObject;

    /**
     * 加载配置文件
     */
    public static void loadYml() {
        String ymlPath = getYmlPath("application.yml");
        try {
            YamlReader reader = new YamlReader(new FileReader(ymlPath));
            ymlJSONObject = reader.read(JSONObject.class);
        } catch (Exception e) {
            System.err.println("读取配置文件失败!");
        }
    }


    /**
     * 根据配置文件的key获取对应的value
     *
     * @param args 不定参数key
     * @return java.lang.String
     */
    public static String getString(String... args) {
        if (ymlJSONObject == null) loadYml();
        JSONObject jsonObject = ymlJSONObject;
        String result = null;
        for (int i = 0; i < args.length; i++) {
            if (i != (args.length - 1)) {
                jsonObject = jsonObject.getJSONObject(args[i]);
            } else {
                result = jsonObject.getString(args[i]);
            }
        }
        return result;
    }

    /**
     * 根据配置文件appconf下的key获取对应的JSONObject
     *
     * @param args 不定参数key
     * @return java.lang.String
     */
    public static JSONObject getJSONObject(String... args) {
        if (ymlJSONObject == null) loadYml();
        JSONObject jsonObject = ymlJSONObject;
        for (int i = 0; i < args.length; i++) {
            jsonObject = jsonObject.getJSONObject(args[i]);
        }
        return jsonObject;
    }

    /**
     * 获取yml路径
     *
     * @return java.lang.String
     */
    public static String getYmlPath(String fileName) {
        String path = String.format("%s/%s/%s", System.getProperty("user.dir"), "%s", fileName);
        String ymlPath = String.format(path, "/config");
        File file = new File(ymlPath);
        if (!file.exists()) {
            ymlPath = String.format(path, "/src/main/resources");
        }
        return ymlPath;
    }

}
