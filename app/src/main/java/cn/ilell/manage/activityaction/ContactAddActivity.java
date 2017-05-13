package cn.ilell.manage.activityaction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.ilell.manage.R;
import cn.ilell.manage.base.BaseActionActivity;
import cn.ilell.manage.utils.DataBaseHelper;
import cn.ilell.manage.utils.MyConfig;

public class ContactAddActivity extends BaseActionActivity {

    private Button btn;
    private EditText contactName;
    private EditText contactNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_action_contact_add, "添加应急电话");

        initView();
        setListener();
    }

    public void initView() {
        btn = (Button) findViewById(R.id.contact_add);
        contactName = (EditText) findViewById(R.id.contact_name);
        contactNum = (EditText) findViewById(R.id.contact_phone);
    }

    private void setListener() {
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_add:
                String result = addContact(contactName.getText().toString(), contactNum.getText().toString());
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     * 添加联系人
     */
    public String addContact(String name, String num) {
        if (null == name || null == num || name.equals("") || num.equals(""))
            return "用户名或手机号不能为空";
        DataBaseHelper dbUtil = new DataBaseHelper(this, MyConfig.DB_CONTACT_NAME);
        SQLiteDatabase database = dbUtil.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='t_contact'", null);
        boolean result = false;
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0)
                result = true;
        }
        if (!result) {
            String sql = "create table t_contact (contact_number varchar PRIMARY KEY NOT NULL, contact_name varchar NOT NULL)";
            database.execSQL(sql);
        }

        ContentValues cv = new ContentValues();

        cv.put("contact_number", num);
        cv.put("contact_name", name);

        Long l = database.insert("t_contact", null, cv);

        cursor = database.query("t_contact", new String[]{"contact_number", "contact_name"}, null,
                null, null, null, null);
        String str = "";
        while (cursor.moveToNext())
            str += cursor.getString(cursor.getColumnIndex("contact_number")) + cursor.getString(cursor.getColumnIndex("contact_name"));
        Log.i("abc", str);

        cursor.close();
        database.close();

        if (-1 == l)
            return "操作失败，手机号已存在";
        return "操作成功";
    }
}
