package cn.ilell.manage.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseFragment;
import cn.ilell.manage.base.BaseWebFragment;
import cn.ilell.manage.utils.DataBaseHelper;
import cn.ilell.manage.utils.MyConfig;

/**
 * 原生界面
 * Created by WSL
 */
public class FragLeader1 extends BaseWebFragment {

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

        web.loadUrl(MyConfig.SERVER_URL + "admin/toDetailProduct");
    }
}
