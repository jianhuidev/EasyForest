package com.example.kys_8.easyforest.bean;

/**
 * Created by kys-8 on 2018/9/13.
 */

public class SortBean {
    private String name;
    private int res;

    public SortBean(String name, int res) {
        this.name = name;
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
