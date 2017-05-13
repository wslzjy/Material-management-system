package cn.ilell.manage.activityaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseActionActivity;
import cn.ilell.manage.utils.ContactManage;

/**
 * 应急电话管理
 */
public class ContactListActivity extends BaseActionActivity {
    private ListView lv = null;
    private TextView tv = null;
    List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    SimpleAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_action_contact_list, "应急号码");
        initView();
        setListener();
    }

    public void initView() {
        lv = (ListView) findViewById(R.id.listview_contact);
        tv = (TextView) findViewById(R.id.contact_updatetime);
        SharedPreferences spf = getSharedPreferences("userInfo", 0);
        tv.setText("上次更新时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(spf.getLong("lastupdate", 0)));
    }

    /**
     * 设置监听器及数据
     */
    private void setListener() {
        adapter = new SimpleAdapter(this,
                list,
                R.layout.item_contact,
                new String[]{"contact_img", "contact_title", "contact_number"},
                new int[]{R.id.contact_img, R.id.contact_name, R.id.contact_number});
        setData();
        lv.setAdapter(adapter);
    }

    public void setData() {
        Map<String, String> map = new ContactManage(this).getAllContact();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            HashMap<String, Object> temp = new HashMap<String, Object>();
            temp.put("contact_img", R.drawable.ic_contact);
            temp.put("contact_number", entry.getKey());
            temp.put("contact_title", entry.getValue());
            list.add(temp);
        }
    }
}