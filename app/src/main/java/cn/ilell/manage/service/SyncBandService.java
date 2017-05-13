package cn.ilell.manage.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.config.Constants;
import com.veryfit.multi.nativedatabase.NoticeOnOff;
import com.veryfit.multi.nativeprotocol.Protocol;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.activitymain.ActivityUnBind;

/**
 * 绑定成功以后同步手环
 * Created by WSL on 2017/1/9
 */
public class SyncBandService extends BaseService implements ProtocalCallBack {

    private Timer timer = null;
    private boolean isSetSync = false;
    private boolean isSetLost = false;
    private Handler mHandler = new Handler();
    private Handler mHandler2 = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        startSyncBand();
        Log.i("abc", "启动同步服务");
    }

    /**
     * 发送心跳包到手环
     */
    public void startSyncBand() {
        try {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.sendMessage(new Message());
                }
            }, 0, 15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // 同步信息完成
        if (evt_type == ProtocolEvt.SYNC_EVT_CONFIG_SYNC_COMPLETE.toIndex() && error == ProtocolEvt.SUCCESS) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    isSetSync = true;
                    if (isSetSync && isSetLost) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        //启动手环双向防丢服务
                        Intent service = new Intent(SyncBandService.this, BandService.class);
                        startService(service);
                        stopSelf();
                        Log.i("abc", "同步完成");
                    }
                }
            });
        }//设置手机防丢完成
        else if (evt_type == ProtocolEvt.SET_CMD_LOST_FIND.toIndex() && error == ProtocolEvt.SUCCESS) {
            mHandler2.post(new Runnable() {

                @Override
                public void run() {
                    isSetLost = true;
                    if (isSetSync && isSetLost) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        //启动手环双向防丢服务
                        Intent service = new Intent(SyncBandService.this, BandService.class);
                        startService(service);
                        stopSelf();
                        Log.i("abc", "同步配置完成");
                    }
                }
            });
        }
    }

    /**
     * 处理定时同步信息与配置
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Log.i("abc", "连接成功，设置手环绑定模式，同步配置，来电提醒，手机防丢");
            //设置绑定模式
            ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_BIND);
            //同步配置
            ProtocolUtils.getInstance().StartSyncConfigInfo();
            // 设置来电提醒
            NoticeOnOff onOff = ProtocolUtils.getInstance().getNotice();
            onOff.setCallonOff(true);
            ProtocolUtils.getInstance().addNotice(onOff);
            //设置手机防丢
            ProtocolUtils.getInstance().setAntilost(Constants.LOSE_MODE_NEAR_ANTI);
        }

        ;
    };

    @Override
    public void onDestroy() {
//        Log.i("abc", "退出服务");
    }
}
