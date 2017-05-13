package cn.ilell.manage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseFragment;
import cn.ilell.manage.utils.ContactManage;

/**
 * Created by WSL
 */
public class FragPhoneNum extends BaseFragment {

    private ListView lv = null;
    private List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_action_contact_list_frag, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        setListener();
    }

    public void initView() {
        lv = (ListView) mView.findViewById(R.id.listview_contact_f);
    }

    /**
     * 设置监听器及数据
     */
    private void setListener() {
        adapter = new SimpleAdapter(mView.getContext(),
                list,
                R.layout.item_contact,
                new String[]{"contact_img", "contact_title", "contact_number"},
                new int[]{R.id.contact_img, R.id.contact_name, R.id.contact_number});
        setData();
        lv.setAdapter(adapter);
    }

    public void setData() {
        Map<String, String> map = new ContactManage(mView.getContext()).getAllContact();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("contact_img", R.drawable.ic_contact);
            temp.put("contact_number", entry.getKey());
            temp.put("contact_title", entry.getValue());
            list.add(temp);
        }
    }
}
