package cn.ilell.manage.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by WSL on 2017/1/15.
 */

public class ContactManage {

    private SQLiteDatabase database;
    private Context context;

    public ContactManage(Context context) {
        this.context = context;
        DataBaseHelper dbUtil = new DataBaseHelper(context, MyConfig.DB_CONTACT_NAME);
        database = dbUtil.getReadableDatabase();
    }

    /**
     * 同步应急电话
     */
    public boolean syncContact() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //初始化，判断表是否存在，不存在则创建
                    initEmergencyCall();
                    //从服务器获取数据
                    String result = HttpXmlClient.post(MyConfig.SERVER_URL + "admin/getEmergencyNum", new HashMap<String, String>());
                    JSONObject json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", ""));
                    JSONArray array = json.getJSONArray("numList");
                    if (array.length() > 0) {
                        database.execSQL("delete from " + MyConfig.CONTACT_DB_TABLE_NAME);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject temp = array.getJSONObject(i);
                            String sql = "insert into " + MyConfig.CONTACT_DB_TABLE_NAME + " values ( '" + temp.getString("telnum") + "' , '" + temp.getString("name") + "')";
                            Log.i("abc", sql);
                            database.execSQL(sql);
                        }
                        SharedPreferences spf = context.getSharedPreferences("userInfo", 0);
                        SharedPreferences.Editor editor = spf.edit();
                        editor.putLong("lastupdate", System.currentTimeMillis());
                        editor.commit();
                    }
//                    if (true) {
//                        Iterator<String> it = array..keys();
//                        String key;
//                        while (it.hasNext()) {
//                            key = it.next();
//                            String sql = "insert into " + MyConfig.CONTACT_DB_TABLE_NAME + " values ( '" + key + "' , '" + json.getString(key) + "')";
//                            database.execSQL(sql);
//                            Log.i("abc", sql);
//                        }
//                        SharedPreferences spf = context.getSharedPreferences("userInfo", 0);
//                        SharedPreferences.Editor editor = spf.edit();
//                        editor.putLong("lastupdate", System.currentTimeMillis());
//                        editor.commit();
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    database.close();
                }
            }
        }).start();
        return true;
    }

    /**
     * 初始化应急电话数据库，表不存在则创建
     */
    public void initEmergencyCall() {
        if (!Utils.isFileExist(MyConfig.DB_CONTACT_PATH)) {
            Cursor cursor = database.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + MyConfig.CONTACT_DB_TABLE_NAME + "'", null);
            boolean result = false;
            if (cursor.moveToNext() && cursor.getInt(0) > 0)
                result = true;
            if (!result) {//表不存在
                String sql = "create table " + MyConfig.CONTACT_DB_TABLE_NAME + " (contact_number varchar PRIMARY KEY NOT NULL, contact_name varchar NOT NULL)";
                database.execSQL(sql);
            }
            cursor.close();
        }
    }

    /**
     * 获得所有数据
     *
     * @return
     */
    public Map<String, String> getAllContact() {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM " + MyConfig.CONTACT_DB_TABLE_NAME, null);
            while (cursor.moveToNext())
                map.put(cursor.getString(0), cursor.getString(1));
            cursor.close();
        } catch (Exception e) {
        } finally {
            database.close();
        }
        return map;
    }

    /**
     * 查询是否是紧急电话
     *
     * @param phonenumber
     * @return
     */
    public String checkIsEmeCall(String phonenumber) {
        String str = "";
        Cursor cursor = database.rawQuery("SELECT contact_name FROM " + MyConfig.CONTACT_DB_TABLE_NAME + " WHERE contact_number='" + phonenumber + "'", null);
        while (cursor.moveToNext()) {
            str = cursor.getString(0);
            break;
        }
        cursor.close();
        database.close();
        return str;
    }
}
