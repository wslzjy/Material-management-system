package cn.ilell.manage.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import cn.ilell.manage.utils.MyConfig;

/**
 * 低电量服务
 * Created by WSL on 2017/2/22.
 */

public class BatteryService extends BaseService {

    private int flag = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        startOnBatteryChangeService();
        Log.i("abc", "启动低电量服务");
    }

    /**
     * 启动定时检测电量服务
     */
    public void startOnBatteryChangeService() {
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    int intLevel = intent.getIntExtra("level", 0);
                    if (intLevel != flag) {
                        flag = intLevel;
                        if (intLevel % 4 == 0 && intLevel < spf.getInt("battery", MyConfig.BATTERY)) {
                            showNotify("通知", "电池电量过低，请及时充电");
                            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_LOW_BATTERY, MyConfig.MSG_LOW_BATTERY);// 发送到手环
                            ProtocolUtils.getInstance().setCallEvt(MyConfig.MSG_LOW_BATTERY, MyConfig.MSG_LOW_BATTERY);// 发送到手环
                        }
                    }
                }
            }
        }, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
