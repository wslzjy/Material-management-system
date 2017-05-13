package cn.ilell.manage.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.veryfit.multi.config.Constants;
import com.veryfit.multi.nativeprotocol.Protocol;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.share.BleSharedPreferences;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.R;
import cn.ilell.manage.activitymain.ActivityBind;
import cn.ilell.manage.base.BaseBandFragment;
import cn.ilell.manage.utils.BlueToothUtil;
import cn.ilell.manage.view.BandBufferDialog;

/**
 * Created by WSL
 */
public class FragUnBind extends BaseBandFragment {

    private Button btn_optoin;
    private Handler mHandler = new Handler();
    private BandBufferDialog mBufferDialog;
    int unBindTryTime = 0;
    Timer unBindTimer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_action_unbind_frag, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListener();
    }

    public void initView() {
        mBufferDialog = new BandBufferDialog(mView.getContext(), R.layout.activity_dialogr);
        mBufferDialog.setTitle("正在解绑，请稍后");
        btn_optoin = (Button) mView.findViewById(R.id.btn_band_unbind_frag);
    }

    private void setListener() {
        btn_optoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_band_unbind_frag:
                unBindWarnDialog();
                break;
        }
    }


    /**
     * 解绑设备
     */
    public void unBind() {
        //解绑方式1 强制解绑，不清除手环信息
//        if (BleSharedPreferences.getInstance().getIsBind()) {
            ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_NOBIND);
            Calendar mCalendar1 = Calendar.getInstance();
            int year = mCalendar1.get(Calendar.YEAR);
            int month = mCalendar1.get(Calendar.MONTH);
            int day = mCalendar1.get(Calendar.DAY_OF_MONTH);
            ProtocolUtils.getInstance().enforceUnBind(new Date(year, month, day));
            Toast.makeText(mView.getContext(), "操作成功", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setClass(mView.getContext(), ActivityBind.class);
            startActivity(intent);
//        } else { //解绑方式2，清除手环信息
//            if (!BlueToothUtil.isBluetoothEnabled()) //如果蓝牙没打开，打开蓝牙对话框
//                openBlueToothDialog();
//            else {
//                mBufferDialog.show();
//                if (null != unBindTimer)
//                    unBindTimer.cancel();
//                unBindTryTime = 0;
//                unBindTimer = new Timer();
//                unBindTimer.schedule(new TimerTask() {
//
//                    @Override
//                    public void run() {
//                        Message msg = new Message();
//                        msg.what = unBindTryTime++;
//                        handlerUnBind.sendMessage(msg);
//                    }
//                }, 0, 10000);
//            }
//        }
    }

    @Override
    public void onSysEvt(int evt_base, int evt_type, int error, int value) {
        if (evt_type == ProtocolEvt.BIND_CMD_REMOVE.toIndex() && error == ProtocolEvt.SUCCESS) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (unBindTimer != null) {
                        unBindTimer.cancel();
                        unBindTimer = null;
                    }
                    if (mBufferDialog.isShow())
                        mBufferDialog.dismiss();
                    Toast.makeText(mView.getContext(), "解绑成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(mView.getContext(), ActivityBind.class);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 处理解绑
     */
    @SuppressLint("HandlerLeak")
    Handler handlerUnBind = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what < 3) {
                ProtocolUtils.getInstance().setAntilost(Constants.LOSE_MODE_NEAR_ANTI);
                ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_NOBIND);
                ProtocolUtils.getInstance().setUnBind();
                Log.i("abc", "解绑");
            } else {
                if (null != unBindTimer) {
                    unBindTimer.cancel();
                    unBindTimer = null;
                }
                if (mBufferDialog.isShow())
                    mBufferDialog.dismiss();
                Log.i("abc", "解绑失败,请重试");
                Toast.makeText(mView.getContext(), "解绑失败,请重试", Toast.LENGTH_LONG).show();
            }
        }

        ;
    };

    /**
     * 打开蓝牙提示框
     */
    protected void openBlueToothDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
        builder.setMessage("绑定设备需要蓝牙，是否打开蓝牙？");
        builder.setTitle("打开蓝牙");
        builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                BlueToothUtil.turnOnBluetooth();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 接棒提醒对话框
     */
    protected void unBindWarnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
        builder.setMessage("解绑后全部功能将丢失，是否解绑？");
        builder.setTitle("提醒");
        builder.setPositiveButton("解绑", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                unBind();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
