package cn.ilell.manage.base;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.ilell.manage.R;

/**
 * 子页面显示网页的基类
 * Created by WSL
 */
public class BaseWebFragment extends BaseFragment {
    protected Context mContext;
    protected WebView web;
    protected String url;
//    protected SwipeRefreshLayout mSwipeRefreshLayout;

    protected void initView() {
        mContext = this.getActivity();

        // 初始化刷新
//        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.main_swiperefreshlayout);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_blue_light, R.color.main_blue_dark);
//        mSwipeRefreshLayout.setOnRefreshListener(this);

        //初始化web
        web = (WebView) mView.findViewById(R.id.webView);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //网页加载完成时
            @Override
            public void onPageFinished(WebView view, String url) {
//                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        web.loadUrl("about:blank");
    }

    @Override
    public void onRefresh() {
//        web.loadUrl(url);
    }
}
