package cn.ilell.manage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.ilell.manage.base.BaseActionActivity;

public class ForgetPasswdActivity extends BaseActionActivity {

    protected WebView web;
    //直接加载找回密码的网页，这里声明默认地址测试用
    protected String url = "https://www.baidu.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_forget_passwd, "找回密码");

        initView();
        web.loadUrl(url);
    }

    public void initView() {
        web = (WebView) findViewById(R.id.webView);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        web.loadUrl("about:blank");
    }
}
