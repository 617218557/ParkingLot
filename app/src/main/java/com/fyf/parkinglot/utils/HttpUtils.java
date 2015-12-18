package com.fyf.parkinglot.utils;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * Created by fengyifei on 15/11/23.
 */
public class HttpUtils {


    private static OkHttpClient client = new OkHttpClient();

    // post请求
    public static String httpPost(String url, RequestBody body) {

        try {
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = Utils.base64Decode(response.body().string());
                Log.e("HttpResult",result);
                return result;
            } else {
                return response.code() + "";
            }
        } catch (Exception e) {
            return "";
        }

    }

    public static String httpGet(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = Utils.base64Decode(response.body().string());
                Log.e("HttpResult",result);
                return result;
            } else {
                return response.code() + "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String httpGetWithOutBase64(String url) {
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.e("HttpResult",result);
                return result;
            } else {
                return response.code() + "";
            }
        } catch (Exception e) {
            return "";
        }
    }
}
