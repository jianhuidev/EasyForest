package com.example.kys_8.easyforest.plant;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by kys-8 on 2018/10/22.
 */

public class Plant {
    /**
     * @param filePath 本地文件路径
     * @param accessToken 调用鉴权接口获取的token
     * @return
     */
    public static String plant(String filePath,String accessToken){
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";

        try {
            byte[] imgData = FileUtil.readFileByBytes(filePath);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");
            String param = "image=" + imgParam;
            String baikeparam = "baike_num=" + 5;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            if (TextUtils.isEmpty(accessToken)){
                return "失效token";
            }
            String result = HttpUtil.post(url, accessToken, param+"&"+baikeparam);
            Log.e("plant ","result:" + result);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "识别失败";
    }
}
