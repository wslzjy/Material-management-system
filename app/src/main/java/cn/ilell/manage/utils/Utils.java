package cn.ilell.manage.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WSL on 2017/1/14.
 */

public class Utils {

    /**
     * 登录
     *
     * @param account
     * @param password
     * @return
     */
    public static String checkLogin(String account, String password) {
        Log.i("abc", account + password);
        //检测非空
        if (null == account || null == password || account.equals("") || password.equals(""))
            return "0用户名或密码不能为空";

        Map<String, String> param = new HashMap<String, String>();
        param.put("username", account);
        param.put("password", password);
        param.put("user1", String.valueOf(User.role));
        try {
            String result = HttpXmlClient.post(MyConfig.SERVER_URL + "adminLogin/", param);
            if(null == result)
                return "0登录失败";
            JSONObject json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", ""));
            if (json.getString("code").equals("0")) {//登录成功
                if (User.role == 1) {//领导
                    User.userName = json.getJSONObject("data").getString("manName");
                    User.userId = json.getJSONObject("data").getString("manId");
                } else {
                    User.userName = json.getJSONObject("userData").getString("userName");
                    User.userId = json.getJSONObject("userData").getString("userId");
                }
                return "1登录成功";
            } else {//登录失败
                return "0" + json.getString("message");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "0登录失败";//出现异常，返回登录失败
    }

//    /**
//     * 最初登录方法
//     *
//     * @param account
//     * @param password
//     * @return
//     */
//    public static String checkLogin(String account, String password) {
//        Log.i("abc", account + password);
//        //检测非空
//        if (null == account || null == password || account.equals("") || password.equals(""))
//            return "0用户名或密码不能为空";
//
//        Map<String, String> param = new HashMap<String, String>();
//        param.put("username", account);
//        param.put("password", password);
//        param.put("user1", "1");
//        try {
//            String result = HttpXmlClient.post(MyConfig.SERVER_URL + "adminLogin/", param);
//            JSONObject json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", ""));
//            if (json.getString("code").equals("0")) {
//                User.role = 1;
//                User.userName = json.getJSONObject("data").getString("manName");
//                User.userId = json.getJSONObject("data").getString("manId");
//                return "1登录成功";
//            } else {
//                param.put("user1", "2");
//                result = HttpXmlClient.post(MyConfig.SERVER_URL + "adminLogin/", param);
//                json = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1).replaceAll("\\\\", ""));
//                if (json.getString("code").equals("0")) {
//                    User.role = 2;
//                    User.userName = json.getJSONObject("userData").getString("userName");
//                    User.userId = json.getJSONObject("userData").getString("userId");
//                    return "1登录成功";
//                } else
//                    return "0" + json.getString("message");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "0登录失败";
//    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile())
            return true;
        return false;
    }
}
