package cn.ilell.manage.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by WSL
 */
public class HttpXmlClient {

    public static DefaultHttpClient httpclient = new DefaultHttpClient();

    public static String post(String url, Map<String, String> params) {
        try {
            HttpPost post = postForm(url, params);//没毛病
            HttpResponse response = httpclient.execute(post);
            if (null == response)
                return null;
            String body = EntityUtils.toString(response.getEntity());
            if (body.startsWith("\ufeff"))
                body = body.substring(1);
            Log.i("abc", "post result-->" + body);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String get(String url) {
        try {
            HttpGet httpGet = new HttpGet(url);
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                return result;
            }
        } catch (Exception e) {
        }
        return null;
    }

//    private static String invoke(HttpUriRequest httpost) {
//        try {
//            HttpResponse response = httpclient.execute(httpost);
//            HttpEntity entity = response.getEntity();
//            return EntityUtils.toString(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private static String paseResponse(HttpResponse response) {
//        try {
//            if (null == response)
//                return null;
//            HttpEntity entity = response.getEntity();
//            String charset = EntityUtils.getContentCharSet(entity);
//            String body = null;
//            body = EntityUtils.toString(entity);
//            return body;
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private static HttpResponse sendRequest(HttpUriRequest httpost) {
//        HttpResponse response = null;
//        try {
//            response = httpclient.execute(httpost);
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response;
//    }

    private static HttpPost postForm(String url, Map<String, String> params) {

        HttpPost httpost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();

        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key)));
        }

        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpost;
    }
}


