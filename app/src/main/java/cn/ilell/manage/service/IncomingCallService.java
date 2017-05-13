package cn.ilell.manage.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.utils.ContactManage;
import cn.ilell.manage.utils.MyConfig;

/**
 * 紧急电话服务
 * Created by WSL on 2017/2/22.
 */

public class IncomingCallService extends BaseService {
    // 电话服务管理器
    private TelephonyManager telephonyManager;
    // 电话状态监听器
    private MyPhoneStateListener myPhoneStateListener;
    // 来电定时器，持续提醒
    private Timer callTimer = null;
    private String name = "";
    private String phonenumber;
    /**
     * 手机振动器
     */
    private Vibrator vibrator;
    long[] pattern = {1000, 1500};

    @Override
    public void onCreate() {
        super.onCreate();

//        // 启动收发数据服务
//        MsgService.connectServer();

//        //心跳包服务
//        sendHeartBeatToServer();

        //来电提醒服务
        incomingCall();

//        //更新应急电话数据库
//        new ContactManage(this).syncContact();
    }

    /**
     * 紧急电话服务
     */
    public void incomingCall() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    /**
     * 电话状态监听器
     */
    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                // 判断是不是紧急号码
                name = new ContactManage(IncomingCallService.this).checkIsEmeCall(incomingNumber);
                if (!name.equals("")) {

                    //震动
                    vibrator.vibrate(pattern, 0);
                    //调整系统音量
                    AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_RING, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

                    ProtocolUtils.getInstance().setCallEvt(name, phonenumber);
                    phonenumber = incomingNumber;
                    // 设置定时器
                    if (null != callTimer)
                        callTimer.cancel();
                    callTimer = new Timer();
                    callTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ProtocolUtils.getInstance().setCallEvt(name, phonenumber);
                            handlerCall.sendMessage(new Message());
                        }
                    }, 0, 5000);
                }
            } else if (state == TelephonyManager.CALL_STATE_OFFHOOK ||
                    state == TelephonyManager.CALL_STATE_IDLE) {// 接起电话挂电话
                if (!name.equals("")) {//如果是应急号码
                    vibrator.cancel();
                    ProtocolUtils.getInstance().stopCall();
                    if (null != callTimer) {
                        callTimer.cancel();
                        callTimer = null;
                    }
                    name = "";
                }
            }
        }
    }

    /**
     * 定时发送来电数据，保证手环持续震动
     */
    @SuppressLint("HandlerLeak")
    Handler handlerCall = new Handler() {
        public void handleMessage(Message msg) {
            ProtocolUtils.getInstance().setCallEvt(name, phonenumber);
        }

        ;
    };

    /**
     * 发送心跳包到服务器，保持连接
     */
    public void sendHeartBeatToServer() {
        try {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    MsgService.sendMsgToServer(MyConfig.MSG_HEARTBEAT);
                }
            }, 0, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
