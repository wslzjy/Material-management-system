package cn.ilell.manage.activityaction;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ilell.manage.LoginActivity;
import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseActionActivity;
import cn.ilell.manage.utils.HttpXmlClient;
import cn.ilell.manage.utils.MyConfig;

public class UpdatePasswdActivity extends BaseActionActivity {

    private Button btn;
    private EditText et1;
    private EditText et2;
    private EditText et3;


    String result;//返回结果字符串
    JSONObject json;//返回结果json
    String message;//修改结果


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_update_passwd, "修改密码");
        initView();
        setListener();

    }

    public void initView() {
        btn = (Button) findViewById(R.id.btn_update_passwd);
        et1 = (EditText) findViewById(R.id.old_passwd);
        et2 = (EditText) findViewById(R.id.new_passwd);
        et3 = (EditText) findViewById(R.id.conf_passwd);
    }

    public void setListener() {
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_passwd:
                updatePasswd();
                break;
        }
    }


    /**
     * 更新密码
     */
    public void updatePasswd() {
        final String oldPass = et1.getText().toString();
        final String newPass = et2.getText().toString();
        final String confPass = et3.getText().toString();
        if (null == oldPass || null == newPass || null == confPass || oldPass.equals("") || newPass.equals("") || confPass.equals(""))
            return;
        if (!newPass.equals(confPass)) {
            Toast.makeText(this, "新密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        btn.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    //向服务器发送请求，获取更新结果
                    result = HttpXmlClient.get(MyConfig.SERVER_URL + "admin/modifyAdminPwd?oldPassword=" + oldPass + "&password=" + newPass);
                    json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", ""));
                    message = json.getString("message");

                    msg.what = json.getInt("code");
                } catch (JSONException e) {
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            btn.setEnabled(true);
            if (msg.what == 0) {//操作成功
                Toast.makeText(UpdatePasswdActivity.this, message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == -1) {//出现异常
                Toast.makeText(UpdatePasswdActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
            } else//修改失败
                Toast.makeText(UpdatePasswdActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };
}
