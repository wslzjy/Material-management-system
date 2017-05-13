package cn.ilell.manage.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.entity.SportData;
import com.veryfit.multi.entity.SwitchDataAppEndReply;
import com.veryfit.multi.entity.SwitchDataAppIngReply;
import com.veryfit.multi.entity.SwitchDataAppStartReply;
import com.veryfit.multi.entity.SwitchDataBleEnd;
import com.veryfit.multi.entity.SwitchDataBleIng;
import com.veryfit.multi.entity.SwitchDataBleStart;
import com.veryfit.multi.nativedatabase.BasicInfos;
import com.veryfit.multi.nativedatabase.FunctionInfos;
import com.veryfit.multi.nativedatabase.GsensorParam;
import com.veryfit.multi.nativedatabase.HealthHeartRate;
import com.veryfit.multi.nativedatabase.HealthHeartRateAndItems;
import com.veryfit.multi.nativedatabase.HealthSport;
import com.veryfit.multi.nativedatabase.HealthSportAndItems;
import com.veryfit.multi.nativedatabase.HrSensorParam;
import com.veryfit.multi.nativedatabase.RealTimeHealthData;
import com.veryfit.multi.nativedatabase.healthSleep;
import com.veryfit.multi.nativedatabase.healthSleepAndItems;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.R;
import cn.ilell.manage.utils.ContactManage;
import cn.ilell.manage.utils.MyConfig;

/**
 * 服务基类
 */
public class BaseService extends Service implements ProtocalCallBack {

    /*通知栏*/
    protected NotificationCompat.Builder mBuilder;
    protected NotificationManager mNotificationManager;
    protected int notifyId_WARNING = 102;
    protected SharedPreferences spf;
    protected Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        initNotify();
        spf = getSharedPreferences("userInfo", 0);
        ProtocolUtils.getInstance().setProtocalCallBack(this);
        Log.i("abc", "parent");
    }

    /**
     * 初始化通知栏
     */
    protected void initNotify() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("")
                .setContentText("")
                .setTicker("")//通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                .setSmallIcon(R.mipmap.ic_launcher);
    }

    /**
     * 显示通知栏
     */
    public void showNotify(String title, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(content);
        Notification mNotification = mBuilder.build();
        mNotification.icon = R.mipmap.ic_launcher;
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        mNotification.tickerText = title;
        mNotification.when = System.currentTimeMillis();
        mNotificationManager.notify(notifyId_WARNING, mNotification);
    }

    /**
     * 处理手环的事件消息
     *
     * @param evt_base
     * @param evt_type
     * @param error
     * @param value
     */
    @Override
    public void onSysEvt(int evt_base, int evt_type, int error, int value) {
        if (evt_type == ProtocolEvt.BLE_TO_APP_FIND_PHONE_START.toIndex()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("abc", "寻找设备开始");
                    showNotify("通知", "正在寻找设备");
                }
            });
        } else if (evt_type == ProtocolEvt.BLE_TO_APP_FIND_PHONE_STOP.toIndex()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("abc", "寻找设备结束");
                }
            });
        } else if (evt_type == ProtocolEvt.BLE_TO_APP_ANTI_LOST_START.toIndex()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("abc", "手机防丢开始");
                    showNotify("警告", "您与手环断开连接");
                }
            });
        } else if (evt_type == ProtocolEvt.APP_TO_BLE_FIND_DEVICE_START.toIndex()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("abc", "手机防丢开始");
                    showNotify("警告", "您与手环断开连接");
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onWriteDataToBle(byte[] bytes) {

    }

    @Override
    public void onDeviceInfo(BasicInfos basicInfos) {

    }

    @Override
    public void healthData(byte[] bytes) {

    }

    @Override
    public void onSensorData(byte[] bytes) {

    }

    @Override
    public void onFuncTable(FunctionInfos functionInfos) {

    }

    @Override
    public void onSleepData(healthSleep healthSleep, healthSleepAndItems healthSleepAndItems) {

    }

    @Override
    public void onHealthSport(HealthSport healthSport, HealthSportAndItems healthSportAndItems) {

    }

    @Override
    public void onHealthHeartRate(HealthHeartRate healthHeartRate, HealthHeartRateAndItems healthHeartRateAndItems) {

    }

    @Override
    public void onLiveData(RealTimeHealthData realTimeHealthData) {

    }

    @Override
    public void onGsensorParam(GsensorParam gsensorParam) {

    }

    @Override
    public void onHrSensorParam(HrSensorParam hrSensorParam) {

    }

    @Override
    public void onMacAddr(byte[] bytes) {

    }

    @Override
    public void onLogData(byte[] bytes, boolean b) {

    }

    @Override
    public void onSwitchDataAppStart(SwitchDataAppStartReply switchDataAppStartReply, int i) {

    }

    @Override
    public void onSwitchDataAppIng(SwitchDataAppIngReply switchDataAppIngReply, int i) {

    }

    @Override
    public void onSwitchDataAppEnd(SwitchDataAppEndReply switchDataAppEndReply, int i) {

    }

    @Override
    public void onSwitchDataBleStart(SwitchDataBleStart switchDataBleStart, int i) {

    }

    @Override
    public void onSwitchDataBleIng(SwitchDataBleIng switchDataBleIng, int i) {

    }

    @Override
    public void onSwitchDataBleEnd(SwitchDataBleEnd switchDataBleEnd, int i) {

    }

    @Override
    public void onActivityData(SportData sportData, int i) {

    }
}
