package com.yb.aiot.module.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.yb.aiot.module.common.config.FileConfig;
import com.yb.aiot.common.Result;
import org.pf4j.JarPluginManager;
import yslz.aiot.plugin.api.ProtocolInterface;

import java.nio.file.Paths;
import java.util.List;


/**
 * <p>
 * 动态jar包插件调用工具类
 * <p>
 *
 * @author CXY
 * @date 2022/12/21 17:34
 */
public class Pf4jUtil {

    static JarPluginManager jarPluginManager;
    static JarPluginManager getPluginManager(){
        if (jarPluginManager == null){
            jarPluginManager = new JarPluginManager();
        }
        return jarPluginManager;
    }

    public static Result protocolExecuteTest(String path, String pluginId){
        System.out.println(">>> Test Call Plugin --------- \n>>> path --- "+ path + "\n>>> pluginId --- " + pluginId);
        try {
            // create the plugin manager
            JarPluginManager pluginManager = getPluginManager();//new JarPluginManager(); // or "new ZipPluginManager() / new DefaultPluginManager()"

            // start and load all plugins of application
            // pluginManager.loadPlugins();
            // pluginManager.startPlugins();

            // start and load singleton plugin by plugin-id and path
            pluginManager.loadPlugin(Paths.get(String.format("%s%s", FileConfig.rootFolder, path)));
            pluginManager.startPlugin(pluginId);

            // retrieve all extensions for "Greeting" extension point
            List<ProtocolInterface> ptInterfaces = pluginManager.getExtensions(ProtocolInterface.class);
            for (ProtocolInterface ptInterface : ptInterfaces) {
                System.out.println(">>> " + ptInterface.isReady());
            }

            // stop and unload all plugins
            // pluginManager.stopPlugins();
            // pluginManager.unloadPlugins();

            // stop and unload singleton plugin by plugin-id
            pluginManager.stopPlugin(pluginId);
            pluginManager.unloadPlugin(pluginId);

            return Result.ok("操作成功");
        }
        catch (Exception e){
            System.err.println(">>> 加载调用插件失败!");
            System.err.println(">>> "+e);
        }
        return Result.fail("加载调用插件失败!请联系管理员!");
    }

    public static Result protocolExecute(String path, String pluginId, JSONObject commandJson){
        try {

            JarPluginManager pluginManager = getPluginManager();

            String filePath = String.format("%s%s", FileConfig.rootFolder, path);
            pluginManager.loadPlugin(Paths.get(filePath));
            pluginManager.startPlugin(pluginId);

            List<ProtocolInterface> protocolInterfaces = pluginManager.getExtensions(ProtocolInterface.class);
            for (ProtocolInterface protocolInterface : protocolInterfaces) {
                JSONObject result = protocolInterface.protocolExecute(commandJson);
                System.out.println(">>> 调用插件(plugin-id = "+pluginId+")执行操作成功,返回结果 --- "+result.toString());
            }

            pluginManager.stopPlugin(pluginId);
            pluginManager.unloadPlugin(pluginId);

            return Result.ok("操作成功");
        }
        catch (Exception e){
            System.err.println(">>> 加载调用插件失败!");
            System.err.println(">>> "+e);
        }
        return Result.fail("加载调用插件失败!请联系管理员!");
    }
}
