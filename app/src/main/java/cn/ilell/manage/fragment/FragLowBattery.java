package cn.ilell.manage.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseFragment;
import cn.ilell.manage.utils.MyConfig;

/**
 * Created by WSL
 */
public class FragLowBattery extends BaseFragment {
    private Button btn;
    private EditText et1;
    private SharedPreferences spf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_set_low_battery_frag, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListener();
    }

    public void initView() {
        btn = (Button) mView.findViewById(R.id.btn_set_low_battery_f);
        et1 = (EditText) mView.findViewById(R.id.et_low_battery_f);
        spf = mView.getContext().getSharedPreferences("userInfo", 0);
        Log.i("abc", String.valueOf(spf.getInt("battery", MyConfig.BATTERY)));
        et1.setText(String.valueOf(spf.getInt("battery", MyConfig.BATTERY)));
    }

    public void setListener() {
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_low_battery_f:
                setBattery();
                break;
        }
    }

    /**
     * 修改电量
     */
    public void setBattery() {
        try {
            int i = Integer.valueOf(et1.getText().toString());
            if (i > 0 && i <= 100) {
                SharedPreferences.Editor editor = spf.edit();
                editor.putInt("battery", i);
                editor.commit();
                showDialog("修改成功");
            } else {
                showDialog("修改失败，请输入正确的数值（1~100）");
            }
        } catch (Exception e) {
            showDialog("修改失败，请输入正确的数值（1~100）");
        }
    }

    /**
     * 对话框
     */
    public void showDialog(String msg) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(mView.getContext());
        dlg.setTitle("提示");
        dlg.setMessage(msg);
        dlg.setPositiveButton("确定", null);
        dlg.show();
    }
}
