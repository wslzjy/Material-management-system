package cn.ilell.manage.activitymain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import cn.ilell.manage.LoginActivity;
import cn.ilell.manage.R;
import cn.ilell.manage.ScanActivity;
import cn.ilell.manage.base.BaseActivity;
import cn.ilell.manage.fragment.FragUnBind;

/**
 * Created by WSL
 */
public class ActivityUnBind extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = ActivityUnBind.this;

        // 初始化各种控件
        initViews();

        // 初始化mTitles、mFragments等ViewPager需要的数据
        //这里的数据都是模拟出来了，自己手动生成的，在项目中需要从网络获取数据
        initData();

        // 对各种控件进行设置、适配、填充数据
        configViews(1, 3);

    }

    private void initData() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.frag3_tab_titles);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt("flag", 0);
        FragUnBind fragment = new FragUnBind();
        fragment.setArguments(bundle);
        mFragments.add(0, fragment);
    }

    /**
     * 设置NavigationView中menu的item被选中后要执行的操作
     *
     * @param mNav
     */
    public void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_leader:
                        changeActivity(ActivityLeader.class);
                        break;
                    case R.id.nav_menu_personal:
                        changeActivity(ActivityPersonal.class);
                        break;
                    case R.id.nav_menu_bind:
                        changeActivity(ActivityBind.class);
                        break;
                    case R.id.nav_menu_bind_un:
                        break;
                    case R.id.nav_menu_phonenumber:
                        changeActivity(ActivityPhoneNum.class);
                        break;
                    case R.id.nav_menu_lowbattery:
                        changeActivity(ActivityLowBattery.class);
                        break;
                    case R.id.nav_menu_info:
                        changeActivity(ActivityInfo.class);
                        break;
                    case R.id.nav_menu_sys:
                        Intent i = new Intent(ActivityUnBind.this, ScanActivity.class);
                        startActivity(i);
                        break;
                    case R.id.nav_menu_logout:
                        SharedPreferences.Editor editor = getSharedPreferences("userInfo", 0).edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
