package cn.ilell.manage.base;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.veryfit.multi.share.BleSharedPreferences;

import java.util.List;

import cn.ilell.manage.R;
import cn.ilell.manage.adapter.MyViewPagerAdapter;
import cn.ilell.manage.utils.User;
import cn.ilell.manage.versionupdate.UpdateManager;

/**
 * 基类
 * Created by WSL
 */
public class BaseActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    //初始化各种控件，照着xml中的顺序写
    protected DrawerLayout mDrawerLayout;
    protected CoordinatorLayout mCoordinatorLayout;
    protected AppBarLayout mAppBarLayout;
    protected Toolbar mToolbar;
    //    protected TabLayout mTabLayout;
    protected ViewPager mViewPager;
    protected View headView;
    //protected FloatingActionButton mFloatingActionButton;
    protected NavigationView mNavigationView;

    protected Context mContext;

    // TabLayout中的tab标题
    protected String[] mTitles;
    // 填充到ViewPager中的Fragment
    protected List<Fragment> mFragments;
    // ViewPager的数据适配器
    protected MyViewPagerAdapter mViewPagerAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_check_update) {//检查版本版本更新
            UpdateManager manager = new UpdateManager(mContext);
            manager.checkUpdate();
            return true;
        }
//        if (id == R.id.action_band) {//手环
//            Intent intent = new Intent();
//            //如果绑定
//            if (BleSharedPreferences.getInstance().getIsBind())
//                intent.setClass(mContext, BandUnBindActivity.class);
//            else
//                intent.setClass(mContext, BandBindActivity.class);
//            mContext.startActivity(intent);
//            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            return true;
//        } else
//        if (id == R.id.action_contact) {//联系人
//            Intent intent = new Intent();
//            //制定intent要启动的类
//            intent.setClass(mContext, ContactListActivity.class);
//            //启动一个新的Activity
//            mContext.startActivity(intent);
//            overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//            return true;
//        } else if (id == R.id.action_low_battery) {//低电量提醒
//            Intent intent = new Intent();
//            //制定intent要启动的类
//            intent.setClass(mContext, LowBatteryActivity.class);
//            //启动一个新的Activity
//            mContext.startActivity(intent);
//            overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//            return true;
//        } else if (id == R.id.action_update_passwd) {//修改密码
//            Intent intent = new Intent();
//            //制定intent要启动的类
//            intent.setClass(mContext, UpdatePasswdActivity.class);
//            //启动一个新的Activity
//            mContext.startActivity(intent);
//            overridePendingTransition(R.anim.push_left_in,
//                    R.anim.push_left_out);
//            return true;
//        } else if (id == R.id.action_logout) {//注销
//            SharedPreferences.Editor editor = getSharedPreferences("userInfo", 0).edit();
//            editor.clear();
//            editor.commit();
//            Intent intent = new Intent();
//            intent.setClass(mContext, LoginActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 初始化视图
     */
    public void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinatorlayout);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbarlayout);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //mTabLayout = (TabLayout) findViewById(R.id.main_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigationview);
    }

    /**
     * 更改acitvity
     *
     * @param mCalss
     */
    public void changeActivity(final Class mCalss) {
        new Thread() {
            public void run() {
                //休眠0.256
                try {
                    Thread.sleep(256);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                //制定intent要启动的类
                intent.setClass(mContext, mCalss);
                //启动一个新的Activity
                startActivity(intent);
                //关闭当前的
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            ;
        }.start();
    }

    public void onNavgationViewMenuItemSelected(NavigationView mNav) {

    }

    /**
     * 设置菜单头部的信息
     */
    protected void initNavHead() {
        TextView text_name = (TextView) headView.findViewById(R.id.id_header_authorname);
        text_name.setText(User.userName);
    }

    /**
     * 配置视图
     *
     * @param n
     */
    public void configViews(int n, int index) {

        // 设置显示Toolbar
        setSupportActionBar(mToolbar);

        // 设置Drawerlayout开关指示器，即Toolbar最左边的那个icon
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        //给NavigationView填充顶部区域，也可在xml中使用app:headerLayout="@layout/header_nav"来设置
        headView = mNavigationView.inflateHeaderView(R.layout.header_nav);
        initNavHead();
        //给NavigationView填充Menu菜单，也可在xml中使用app:menu="@menu/menu_nav_reduce"来设置
        mNavigationView.inflateMenu(R.menu.menu_nav);
        mNavigationView.setBackgroundColor(mNavigationView.getResources().getColor(R.color.main_blue_light));
//        mNavigationView.setItemTextColor(ColorStateList.valueOf(mNavigationView.getResources().getColor(R.color.menu_item_white)));
        // 自己写的方法，设置NavigationView中menu的item被选中后要执行的操作
        onNavgationViewMenuItemSelected(mNavigationView);
//
//        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                return true;
//            }
//        });

        // 初始化ViewPager的适配器，并设置给它
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(n);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

//        mTabLayout.setTabMode(MODE_SCROLLABLE);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
//        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
//        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);

        //设置显示的标题
        mToolbar.setTitle(mNavigationView.getMenu().getItem(index).getTitle());

        if (User.role == 2) {//个人，隐藏领导的
            mNavigationView.getMenu().getItem(0).setVisible(false);
            mNavigationView.getMenu().getItem(6).setVisible(false);
        }


        if (BleSharedPreferences.getInstance().getIsBind()) //绑定
            mNavigationView.getMenu().getItem(2).setVisible(false);
        else
            mNavigationView.getMenu().getItem(3).setVisible(false);

        mNavigationView.getMenu().getItem(4).setVisible(false);
    }


//    /**
//     * 设置菜单样式
//     * @param context
//     */
//    public static void setFragmentActivityMenuColor(FragmentActivity context) {
//        final LayoutInflater layoutInflater = context.getLayoutInflater();
//        final LayoutInflater.Factory existingFactory = layoutInflater.getFactory();
//        try {
//            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//            field.setAccessible(true);
//            field.setBoolean(layoutInflater, false);
//            context.getLayoutInflater().setFactory(new LayoutInflater.Factory() {
//                @Override
//                public View onCreateView(String name, final Context context, AttributeSet attrs) {
//                    if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
//                            || name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
//                        View view = null;
//                        if (existingFactory != null) {
//                            view = existingFactory.onCreateView(name, context, attrs);
//                            if (view == null) {
//                                try {
//                                    view = layoutInflater.createView(name, null, attrs);
//                                    final View finalView = view;
//                                    if (view instanceof TextView) {
//                                        new Handler().post(new Runnable() {
//                                            public void run() {
//                                                ((TextView) finalView).setTextColor(context.getResources().getColor(R.color.white));
//                                            }
//                                        });
//                                    }
//                                    return finalView;
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        return view;
//                    }
//                    return null;
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
