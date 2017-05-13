package cn.ilell.manage;

import android.app.Application;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;

/**
 * Created by WSL on 2017/3/1.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化手环
        ProtocolUtils.getInstance().init(this);
        ProtocolUtils.getInstance().setCanConnect(true);
    }
}
