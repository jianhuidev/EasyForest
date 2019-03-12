package com.example.kys_8.easyforest.utils;

import com.alibaba.fastjson.JSON;
import com.example.kys_8.easyforest.bean.InfoBean;
import com.example.kys_8.easyforest.ui.fragment.SortFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kys-8 on 2018/11/8.
 */

public class RequestUtil {

    public static void httpRequest(final String path, final SortFragment.InfoCallBack callBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    InputStream in = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    LogUtil.e("RequestUtil",response.toString());
                    callBack.infoResult(JSON.parseObject(response.toString(),InfoBean.class));
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (reader != null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if (conn != null){
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

}
