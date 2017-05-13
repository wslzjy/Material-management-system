package cn.ilell.manage.utils;

/**
 * Created by WSL on 2017/1/9. 后台服务配置文件
 */
public class MyConfig {
    //服务器ip
    public static final String SERVER_IP = "114.215.27.226:8080";
    // 服务器端口
    public static final int PORT = 8888;
    // 关机发送通知
    public static final String MSG_SHUTDOWN = "shutdown";
    // 心跳包
    public static final String MSG_HEARTBEAT = "hello";
    // 低电量通知
    public static final String MSG_LOW_BATTERY = "low battery";
    // 保存联系人数据库名
    public static final String DB_CONTACT_NAME = "contect.db";
    // 保存联系人数据库表名
    public static final String CONTACT_DB_TABLE_NAME = "t_contact";
    //server url
    public static final String SERVER_URL = "http://114.215.27.226:8080/zzwz/";
    //package name
    public static final String PACKAGE_NAME = "cn.ilell.manage";
    //检查版本信息地址
    public static final String VERSIONUPDATE_URL = SERVER_URL + "static/download/version.xml";
    // 保存联系人数据库位置
    public static String DB_CONTACT_PATH = "/data/data/" + PACKAGE_NAME + "/databases/" + DB_CONTACT_NAME;
    //默认低电量阈值
    public static int BATTERY = 20;
}
