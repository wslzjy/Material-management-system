package cn.ilell.manage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseWebFragment;
import cn.ilell.manage.utils.MyConfig;

/**
 * Created by WSL
 */
public class FragShutdownInfo extends BaseWebFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag_webonly, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        web.loadUrl(MyConfig.SERVER_URL + "admin/shutdowninfo");
    }
}
