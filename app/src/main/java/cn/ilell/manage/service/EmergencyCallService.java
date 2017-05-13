package cn.ilell.manage.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import cn.ilell.manage.utils.ContactManage;

/**
 * 更新紧急电话服务
 * Created by WSL on 2017/2/22.
 */

public class EmergencyCallService extends BaseService {
    @Override
    public void onCreate() {
        super.onCreate();
        //更新应急电话数据库
        //SharedPreferences spf = getSharedPreferences("userInfo", 0);
        //缓存时间30min
        //    if (System.currentTimeMillis() - spf.getLong("lastupdate", 0) > 1800000)
        new ContactManage(this).syncContact();
        //    this.stopSelf();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}
