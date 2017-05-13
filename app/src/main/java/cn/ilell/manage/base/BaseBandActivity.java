package cn.ilell.manage.base;

import android.bluetooth.BluetoothGatt;
import android.view.View;

import com.veryfit.multi.ble.AppBleListener;
import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.entity.BleDevice;
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
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.BleScanTool;

/**
 * Created by WSL
 */
public class BaseBandActivity extends BaseActionActivity implements View.OnClickListener, ProtocalCallBack, AppBleListener, BleScanTool.ScanDeviceListener {

    @Override
    public void onActivityData(SportData sportData, int i) {

    }

    public void initView() {

    }

    public void addListener() {

    }

    @Override
    public void onBLEConnectTimeOut() {

    }

    @Override
    public void onBLEConnected(BluetoothGatt arg0) {

    }

    @Override
    public void onBLEConnecting(String arg0) {

    }

    @Override
    public void onBLEDisConnected(String arg0) {

    }

    @Override
    public void onBlueToothError(int arg0) {

    }

    @Override
    public void onDataSendTimeOut(byte[] arg0) {

    }

    @Override
    public void onServiceDiscover(BluetoothGatt arg0, int arg1) {

    }

    @Override
    public void healthData(byte[] arg0) {

    }

    @Override
    public void onDeviceInfo(BasicInfos arg0) {

    }

    @Override
    public void onFuncTable(FunctionInfos arg0) {

    }

    @Override
    public void onGsensorParam(GsensorParam arg0) {

    }

    @Override
    public void onHealthHeartRate(HealthHeartRate arg0, HealthHeartRateAndItems arg1) {

    }

    @Override
    public void onHealthSport(HealthSport arg0, HealthSportAndItems arg1) {

    }

    @Override
    public void onHrSensorParam(HrSensorParam arg0) {

    }

    @Override
    public void onLiveData(RealTimeHealthData arg0) {

    }

    @Override
    public void onLogData(byte[] arg0, boolean arg1) {

    }

    @Override
    public void onMacAddr(byte[] arg0) {

    }

    @Override
    public void onSensorData(byte[] arg0) {

    }

    @Override
    public void onSleepData(healthSleep arg0, healthSleepAndItems arg1) {

    }

    @Override
    public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onWriteDataToBle(byte[] arg0) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProtocolUtils.getInstance().removeProtocalCallBack(this);
        ProtocolUtils.getInstance().removeListener(this);
        ProtocolUtils.getInstance().removeScanDeviceListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProtocolUtils.getInstance().removeProtocalCallBack(this);
        ProtocolUtils.getInstance().removeListener(this);
        ProtocolUtils.getInstance().removeScanDeviceListener(this);
    }

    @Override
    public void onFind(BleDevice bleDevice) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSwitchDataAppEnd(SwitchDataAppEndReply arg0, int arg1) {

    }

    @Override
    public void onSwitchDataAppIng(SwitchDataAppIngReply arg0, int arg1) {

    }

    @Override
    public void onSwitchDataAppStart(SwitchDataAppStartReply arg0, int arg1) {

    }

    @Override
    public void onSwitchDataBleEnd(SwitchDataBleEnd arg0, int arg1) {

    }

    @Override
    public void onSwitchDataBleIng(SwitchDataBleIng arg0, int arg1) {

    }

    @Override
    public void onSwitchDataBleStart(SwitchDataBleStart arg0, int arg1) {

    }

    @Override
    public void onClick(View v) {

    }
}
