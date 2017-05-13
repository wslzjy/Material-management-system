package cn.ilell.manage.base;

import android.bluetooth.BluetoothGatt;
import android.os.Bundle;
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
 * Created by WSL on 2017/1/14.
 */

public class BaseBandFragment extends BaseFragment implements ProtocalCallBack, AppBleListener, BleScanTool.ScanDeviceListener, View.OnClickListener {

    @Override
    public void onBlueToothError(int i) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProtocolUtils.getInstance().setProtocalCallBack(this);
        ProtocolUtils.getInstance().setBleListener(this);
        ProtocolUtils.getInstance().setScanDeviceListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProtocolUtils.getInstance().removeProtocalCallBack(this);
        ProtocolUtils.getInstance().removeListener(this);
        ProtocolUtils.getInstance().removeScanDeviceListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ProtocolUtils.getInstance().removeProtocalCallBack(this);
        ProtocolUtils.getInstance().removeListener(this);
        ProtocolUtils.getInstance().removeScanDeviceListener(this);
    }

    @Override
    public void onBLEConnecting(String s) {

    }

    @Override
    public void onBLEConnected(BluetoothGatt bluetoothGatt) {

    }

    @Override
    public void onServiceDiscover(BluetoothGatt bluetoothGatt, int i) {

    }

    @Override
    public void onBLEDisConnected(String s) {

    }

    @Override
    public void onBLEConnectTimeOut() {

    }

    @Override
    public void onDataSendTimeOut(byte[] bytes) {

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
    public void onSysEvt(int i, int i1, int i2, int i3) {

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

    @Override
    public void onFind(BleDevice bleDevice) {

    }

    @Override
    public void onFinish() {

    }
}
