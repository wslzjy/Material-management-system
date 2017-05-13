package cn.ilell.manage.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 手环
 * Created by WSL on 2017/1/9
 */
public class BandService extends BaseService implements ProtocalCallBack {

    private int breakConn = 0;
    private Timer keepConnBand = null;

    @Override
    public void onCreate() {
        super.onCreate();
        startHeartBeat();
        Log.i("abc", "启动服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * 发送心跳包到手环
     */
    public void startHeartBeat() {
        try {
            keepConnBand = new Timer();
            keepConnBand.schedule(new TimerTask() {
                @Override
                public void run() {
                    ProtocolUtils.getInstance().setFindPhone(true);
                    breakConn++;
                    handler.sendMessage(new Message());
                }
            }, 0, 5000);
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
        if (evt_type == ProtocolEvt.SET_CMD_FIND_PHONE.toIndex()) {//设置寻找手机
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    breakConn = 0;
                }
            });
        }
    }

    /**
     * 处理连接
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (breakConn > 3) {
                keepConnBand.cancel();
                showNotify("通知", "您与手环断开连接");
                stopSelf();
            }
        }

        ;
    };
}
