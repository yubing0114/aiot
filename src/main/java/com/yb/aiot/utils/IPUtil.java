package com.yb.aiot.utils;

import cn.hutool.system.SystemUtil;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 基础工具类
 * <p>
 *
 * @author author
 * @date 2022/11/11 16:35
 */
public class IPUtil {

    /**
     * 获取本机服务地址
     *
     * @return java.lang.String
     */
    public static String getServeUrl() {
        return String.format("http://%s:%s", getHostIp(), YmlUtil.getString("server", "port"));
    }

    /**
     * 获取本机ip地址
     *
     * @return java.lang.String
     */
    public static String getHostIp() {
        List<Enumeration<InetAddress>> enumerationList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface network = enumeration.nextElement();
                if (network.isUp() && !network.isVirtual()) {
                    enumerationList.add(network.getInetAddresses());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (Enumeration<InetAddress> inetAddress : enumerationList) {
            while (inetAddress.hasMoreElements()) {
                InetAddress address = inetAddress.nextElement();
                if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                    return address.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }

    /**
     * ping
     *
     * @param ipAddress ip
     * @return boolean
     */
    public static boolean ping(String ipAddress) {
        boolean status;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(3000);
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }
        if (!status) {
            status = ping(ipAddress, 2, 8000);
        }
        return status;
    }

    /**
     * ping
     *
     * @param ipAddress ip
     * @param pingTimes ping的次数
     * @param timeOut   ping超时时间
     * @return boolean
     */
    @SneakyThrows
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        // 将要执行的ping命令
        String pingCommand = String.format("ping -c %s -w %s %s", pingTimes, timeOut, ipAddress);
        if (SystemUtil.getOsInfo().isWindows()) {
            pingCommand = String.format("ping %s -n %s -w %s", ipAddress, pingTimes, timeOut);
        }
        Process process = Runtime.getRuntime().exec(pingCommand);
        if (process == null) {
            return false;
        }
        // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int connectedCount = 0;
        String line;
        while ((line = in.readLine()) != null) {
            connectedCount += getCheckResult(line);
        }
        in.close();
        // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
        return connectedCount == pingTimes;
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否则返回0.
     *
     * @param line line
     * @return int
     */
    private static int getCheckResult(String line) {
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return 1;
        }
        return 0;
    }

}
