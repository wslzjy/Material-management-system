package cn.ilell.manage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.share.BleSharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

import cn.ilell.manage.activitymain.ActivityLeader;
import cn.ilell.manage.activitymain.ActivityPersonal;
import cn.ilell.manage.service.BandService;
import cn.ilell.manage.service.BatteryService;
import cn.ilell.manage.service.IncomingCallService;
import cn.ilell.manage.service.EmergencyCallService;
import cn.ilell.manage.utils.MyConfig;
import cn.ilell.manage.utils.User;
import cn.ilell.manage.utils.Utils;

/**
 * Created by WSL
 */
public class WelcomeActivity extends AppCompatActivity {

    private WebView web;
    private SharedPreferences spf;
    private String account = "";
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //启动后台服务
        // 启动来电提醒后台服务
        Intent startIncomingService = new Intent(WelcomeActivity.this, IncomingCallService.class);
        startService(startIncomingService);
        // 启动低电量后台服务
        Intent startBatteryService = new Intent(WelcomeActivity.this, BatteryService.class);
        startService(startBatteryService);
        //如果绑定了手环，启动双向防丢服务
        if (BleSharedPreferences.getInstance().getIsBind()) {
            Intent bandService = new Intent(WelcomeActivity.this, BandService.class);
            startService(bandService);
        }

        spf = getSharedPreferences("userInfo", 0);

        //非自动登录，跳转到登录界面
        if (!spf.getBoolean("autologin", false) || !spf.getBoolean("islogin", false)) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    this.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent();
                            intent.setClass(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }, 1500, 100000);
        } else {
            initViewAndListener();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    account = spf.getString("account", "");
                    password = spf.getString("password", "");
                    User.role = spf.getInt("userrole", 1);
                    if ('1' == Utils.checkLogin(account, password).charAt(0)) {//登录成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putBoolean("islogin", true);
                                editor.commit();
                                web.loadUrl(MyConfig.SERVER_URL + "adminLoginGet?username=" + account + "&password=" + password + "&user1=" + User.role);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(WelcomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void initViewAndListener() {
        web = (WebView) findViewById(R.id.webView);
        web.setVisibility(View.INVISIBLE);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
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
                Intent startUpdate = new Intent(WelcomeActivity.this, EmergencyCallService.class);
                startService(startUpdate);

                Intent intent;
                if (User.role == 1)
                    intent = new Intent(WelcomeActivity.this, ActivityLeader.class);
                    //  intent = new Intent(WelcomeActivity.this, Main2Activity.class);
                else
                    intent = new Intent(WelcomeActivity.this, ActivityPersonal.class);
                //   intent = new Intent(WelcomeActivity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
