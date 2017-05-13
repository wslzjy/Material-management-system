package cn.ilell.manage.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import cn.ilell.manage.adapter.MyRecyclerViewAdapter;
import cn.ilell.manage.adapter.MyStaggeredViewAdapter;

/**
 * 子页面父类，原生页面
 * Created by WSL
 */
public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MyRecyclerViewAdapter.OnItemClickListener, MyStaggeredViewAdapter.OnItemClickListener,
        View.OnClickListener {
    protected View mView;

    @Override
    public void onRefresh() {
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }

    @Override
    public void onClick(View v) {
    }
}
