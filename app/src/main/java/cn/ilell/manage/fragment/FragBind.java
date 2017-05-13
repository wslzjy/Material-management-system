package cn.ilell.manage.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.veryfit.multi.config.Constants;
import com.veryfit.multi.entity.BleDevice;
import com.veryfit.multi.nativedatabase.NoticeOnOff;
import com.veryfit.multi.nativeprotocol.Protocol;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.R;
import cn.ilell.manage.activitymain.ActivityUnBind;
import cn.ilell.manage.adapter.BandScanAdapter;
import cn.ilell.manage.base.BaseBandFragment;
import cn.ilell.manage.service.BandService;
import cn.ilell.manage.service.SyncBandService;
import cn.ilell.manage.utils.BlueToothUtil;
import cn.ilell.manage.view.BandBufferDialog;

/**
 * Created by WSL
 */
public class FragBind extends BaseBandFragment {

    private ListView lView;
    private Button btnScan, btnBind;
    private BandScanAdapter adapter;
    private Handler mHandler = new Handler();
    private Handler mHandler1 = new Handler();
    private ArrayList<BleDevice> list = new ArrayList<BleDevice>();
    private int index;
    private BandBufferDialog mBufferDialog;
    private int bindTryTime = 0;
    private Timer scanTimer = null, bindTimer = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_action_bind_frag, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ProtocolUtils.getInstance().setProtocalCallBack(this);
        ProtocolUtils.getInstance().setBleListener(this);
        ProtocolUtils.getInstance().setScanDeviceListener(this);

        initView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        ProtocolUtils.getInstance().scanDevices(true);
    }

    @Override
    public void onServiceDiscover(BluetoothGatt gatt, int status) {
        super.onServiceDiscover(gatt, status);
        ProtocolUtils.getInstance().setBind();
    }

    public void initView() {
        mBufferDialog = new BandBufferDialog(mView.getContext(), R.layout.activity_scan_dialogr);
        lView = (ListView) mView.findViewById(R.id.lv_device_f);
        btnBind = (Button) mView.findViewById(R.id.btn_bind_f);
        btnScan = (Button) mView.findViewById(R.id.btn_scan_f);
        adapter = new BandScanAdapter(mView.getContext(), null);
        lView.setAdapter(adapter);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg2;
                adapter.setSelectItem(arg2);
            }
        });
    }

    public void setListener() {
        btnBind.setOnClickListener(this);
        btnScan.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //如果不支持蓝牙，提示对话框
        if (!BlueToothUtil.isBluetoothSupported()) {
            dialogBlueToothNotSupport();
        } else if (!BlueToothUtil.isBluetoothEnabled()) {//如果没打开，打开蓝牙对话框
            openBlueToothDialog();
        } else {
            switch (v.getId()) {
                case R.id.btn_bind_f:
                    bindDevice();
                    break;
                case R.id.btn_scan_f:
                    scanDevice();
                    break;
                default:
                    break;
            }
        }
    }

    public void scanDevice() {
        Log.i("abc", "开始搜索");
        mBufferDialog.show();
        // 取消原定时器
        if (null != scanTimer)
            scanTimer.cancel();
        // new新的定时器
        scanTimer = new Timer();
        scanTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handlerScan.sendMessage(new Message());
            }

        }, 15000, 15000);
        // 清空链表和监听器
        list.clear();
        adapter.clear();
        // 启动扫描
        ProtocolUtils.getInstance().scanDevices();
    }

    public void bindDevice() {
        // 取消原定时器
        if (bindTimer != null) {
            bindTimer.cancel();
            bindTimer = null;
        }
        // 判断链表是否为空
        if (!list.isEmpty()) {
            mBufferDialog.show();
            // new新的定时器，尝试连接2次
            bindTryTime = 0;
            bindTimer = new Timer();
            bindTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Message msg = new Message();
                    msg.what = bindTryTime++;
                    handlerBind.sendMessage(msg);
                }
            }, 0, 10000);
        }
    }

    @Override
    public void onFind(BleDevice device) {
        super.onFind(device);
        showList(device);
        list.add(device);
        Collections.sort(list);
        if (null != scanTimer) {
            scanTimer.cancel();
            scanTimer = null;
        }
        if (mBufferDialog.isShow())
            mBufferDialog.dismiss();
        Log.i("abc", "搜索到");
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Log.i("abc", "搜索完成");
    }

    private void showList(final BleDevice device) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.upDada(device);
            }
        });
    }


    @Override
    public void onSysEvt(int evt_base, int evt_type, int error, int value) {
        super.onSysEvt(evt_base, evt_type, error, value);
        // 连接成功，然后启动绑定模式和同步信息服务
        if (evt_type == ProtocolEvt.BIND_CMD_REQUEST.toIndex() && error == ProtocolEvt.SUCCESS) {
            mHandler1.post(new Runnable() {
                @Override
                public void run() {
                    if (mBufferDialog.isShow())
                        mBufferDialog.dismiss();
                    if (null != bindTimer) {
                        bindTimer.cancel();
                        bindTimer = null;
                    }

                    //启动同步配置服务
                    Intent service = new Intent(mView.getContext(), SyncBandService.class);
                    mView.getContext().startService(service);

                    //页面跳转
                    Intent intent = new Intent(mView.getContext(), ActivityUnBind.class);
                    startActivity(intent);
                    Log.i("abc", "手环已绑定");
                    Toast.makeText(mView.getContext(), "手环已绑定", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 处理扫描
     */
    @SuppressLint("HandlerLeak")
    Handler handlerScan = new Handler() {
        public void handleMessage(Message msg) {
            if (null != scanTimer) {
                scanTimer.cancel();
                scanTimer = null;
            }
            if (mBufferDialog.isShow())
                mBufferDialog.dismiss();
            Toast.makeText(mView.getContext(), "未搜索到", Toast.LENGTH_LONG).show();
            Log.i("abc", "未搜索到");
        }

        ;
    };

    /**
     * 处理连接
     */
    @SuppressLint("HandlerLeak")
    Handler handlerBind = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what < 2) {
                ProtocolUtils.getInstance().connect(list.get(index).mDeviceAddress);
                Log.i("abc", "尝试连接");
            } else {
                if (null != bindTimer) {
                    bindTimer.cancel();
                    bindTimer = null;
                }
                if (mBufferDialog.isShow())
                    mBufferDialog.dismiss();
                Log.i("abc", "连接失败，请重试");
                Toast.makeText(mView.getContext(), "连接失败，请重试", Toast.LENGTH_LONG).show();
            }
        }

        ;
    };

    /**
     * 不支持蓝牙对话框
     */
    public void dialogBlueToothNotSupport() {
        AlertDialog.Builder dlg = new AlertDialog.Builder(mView.getContext());
        dlg.setTitle("提示");
        dlg.setMessage("当前设备不支持蓝牙，无法绑定手环");
        dlg.setPositiveButton("确定", null);
        dlg.show();
    }

    /**
     * 打开蓝牙对话框
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
}
