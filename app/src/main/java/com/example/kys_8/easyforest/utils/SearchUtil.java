package com.example.kys_8.easyforest.utils;

import com.example.kys_8.easyforest.GlobalVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kys-8 on 2018/11/9.
 */

public class SearchUtil {

    public static List<String> regexSearch(String tag, String[] arr){
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(tag); // 生成规则
        for (String str : arr){
            Matcher matcher = pattern.matcher(str); // 包含规则匹配字符串结果的实体
            if (matcher.find())
                results.add(str);
        }
        return results;
    }
    public static List<String> recommendSearch(int size){
        List<String> results = new ArrayList<>();
        int skip = new Random().nextInt(86);
        for (int i = 0; i<size;i++){
            results.add(GlobalVariable.trees[skip+i]);
        }
        return results;
    }

}
