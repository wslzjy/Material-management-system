package cn.ilell.manage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import cn.ilell.manage.activitymain.ActivityLeader;
import cn.ilell.manage.activitymain.ActivityPersonal;
import cn.ilell.manage.adapter.SpinnerAdapter;
import cn.ilell.manage.service.EmergencyCallService;
import cn.ilell.manage.utils.MyConfig;
import cn.ilell.manage.utils.User;
import cn.ilell.manage.utils.Utils;

public class LoginActivity extends Activity {
    private EditText et_account;
    private EditText et_password;
    private CheckBox remember;
    private CheckBox autologin;
    private Button btn_login;
    private SharedPreferences sp;
    private String loginResult = "";
    private WebView web;
    private String account = "";
    private String password = "";

    private Spinner userrole;
    private SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initAutoLogin();
        addListener();
    }

    /**
     * 初始化控件
     */
    public void initView() {
        // 初始化用户名、密码、记住密码、自动登录、登录按钮
        et_account = (EditText) findViewById(R.id.username);
        et_password = (EditText) findViewById(R.id.userpassword);
        remember = (CheckBox) findViewById(R.id.remember);
        autologin = (CheckBox) findViewById(R.id.autologin);
        btn_login = (Button) findViewById(R.id.login);

        //下拉列表部分
        userrole = (Spinner) findViewById(R.id.userrole);
        spinnerAdapter = new SpinnerAdapter(this, getResources().getStringArray(R.array.userroles));
        userrole.setAdapter(spinnerAdapter);
        userrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User.role = position + 1;
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("userrole", User.role);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //设置网页部分
        web = (WebView) findViewById(R.id.webView);
        web.setWebViewClient(new WebViewClient() {
            //打开网页时不调用系统浏览器， 而是在本WebView中显示
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //网页加载完成时
            @Override
            public void onPageFinished(WebView view, String url) {
                // 启动更新应急号码数据库服务
                Intent startUpdate = new Intent(LoginActivity.this, EmergencyCallService.class);
                startService(startUpdate);
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent;
                if (User.role == 1)
                    intent = new Intent(LoginActivity.this, ActivityLeader.class);
                else
                    intent = new Intent(LoginActivity.this, ActivityPersonal.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 初始化自动登录
     */
    public void initAutoLogin() {
        sp = getSharedPreferences("userInfo", 0);
        String name = sp.getString("account", "");
        String pass = sp.getString("password", "");
        boolean choseRemember = sp.getBoolean("remember", false);
        boolean choseAutoLogin = sp.getBoolean("autologin", false);
        userrole.setSelection(sp.getInt("userrole", 1) - 1);
        // 如果上次选了记住密码或自动登录，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember || choseAutoLogin) {
            et_account.setText(name);
            et_password.setText(pass);
            remember.setChecked(true);
        }
        // 如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if (choseAutoLogin) {
            autologin.setChecked(true);
        }
    }

    /**
     * 添加监听器
     */
    public void addListener() {
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        account = et_account.getText().toString();
                        password = et_password.getText().toString();
                        loginResult = Utils.checkLogin(account, password);
                        char c = loginResult.charAt(0);
                        loginResult = loginResult.substring(1);
                        if ('1' == c) {//登录成功
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences.Editor editor = sp.edit();
                                    if (remember.isChecked() || autologin.isChecked()) {
                                        editor.putString("account", et_account.getText().toString());
                                        editor.putString("password", et_password.getText().toString());
                                    }
                                    editor.putBoolean("islogin", true);
                                    editor.commit();
                                    web.loadUrl(MyConfig.SERVER_URL + "adminLoginGet?username=" + account + "&password=" + password + "&user1=" + User.role);
                                }
                            });
                        } else
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, loginResult, Toast.LENGTH_LONG).show();
                                }
                            });
                    }
                }).start();

            }
        });
        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                SharedPreferences.Editor editor = sp.edit();
                if (arg1) {//如果选择自动登录，则记住密码选中并修改
                    remember.setChecked(true);
                    editor.putBoolean("remember", arg1);
                }
                editor.putBoolean("autologin", arg1);
                editor.commit();
            }
        });
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("remember", arg1);
                editor.commit();
            }
        });
    }
}
