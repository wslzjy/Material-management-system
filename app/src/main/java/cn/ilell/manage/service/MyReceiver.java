package cn.ilell.manage.service;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Iterator;
import java.util.List;

import cn.ilell.manage.utils.HttpXmlClient;
import cn.ilell.manage.utils.MyConfig;
import cn.ilell.manage.utils.User;

/**
 * Created by WSL on 2017/1/9.
 *
 * @处理广播消息
 * @关机上报
 * @低电量提醒
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {// 关机
            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_SHUTDOWN, MyConfig.MSG_SHUTDOWN);// 发送到手环
            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_SHUTDOWN, MyConfig.MSG_SHUTDOWN);// 发送到手环
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpXmlClient.get(MyConfig.SERVER_URL + "admin/addshutdowninfo?userid=" + User.userId + "&username=" + User.userName);
                }
            }).start();
        } else if (intent.getAction().equals("android.intent.action.BATTERY_LOW")) {// 低电量
            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_LOW_BATTERY, MyConfig.MSG_LOW_BATTERY);// 发送到手环
            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_LOW_BATTERY, MyConfig.MSG_LOW_BATTERY);// 发送到手环
        }
//      else if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
//            //    如需发送给心跳包，取消注释
//            //   如果服务不运行的话，启动服务
//            if (!isServiceRunning(context, MyConfig.PACKAGE_NAME + ".service.BandService")) {
//                // 启动后台服务,发送心跳包,监听电话状态
//                Intent startBackService = new Intent(context, BandService.class);
//                context.startService(startBackService);
//            }
//        }
    }

//    //判断服务是否运行
//    public static boolean isServiceRunning(Context mContext, String className) {
//
//        ActivityManager activityManager = (ActivityManager) mContext
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
//                .getRunningServices(100);
//        if (serviceList.size() <= 0)
//            return false;
//        Iterator<ActivityManager.RunningServiceInfo> it = serviceList.iterator();
//        while (it.hasNext()) {
//            if (it.next().service.getClassName().equals(className))
//                return true;
//        }
//        return false;
//    }
}
