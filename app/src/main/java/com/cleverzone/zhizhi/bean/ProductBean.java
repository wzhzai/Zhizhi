package com.cleverzone.zhizhi.bean;

import java.io.Serializable;

/**
 * Created by WANGZHENGZE on 2015/4/26.
 */
public class ProductBean implements Serializable {
    public int id = -1;
    public String name = "";
    public String picPath = "";
    public String prDate = "";
    public int shelfLifeDay = 0;
    public int shelfLifMonth = 0;
    public String exDate = "";
    public String hintDate = "";
    public int count = 0;
    public String position = "";
    public int advance = 0;
    public String mainClassify = "";
    public String subClassify = "";
    public String backup = "";

    @Override
    public String toString() {
        return "ProductBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picPath='" + picPath + '\'' +
                ", prDate='" + prDate + '\'' +
                ", shelfLifeDay=" + shelfLifeDay +
                ", shelfLifMonth=" + shelfLifMonth +
                ", exDate='" + exDate + '\'' +
                ", hintDate='" + hintDate + '\'' +
                ", count=" + count +
                ", position='" + position + '\'' +
                ", advance=" + advance +
                ", mainClassify='" + mainClassify + '\'' +
                ", subClassify='" + subClassify + '\'' +
                ", backup='" + backup + '\'' +
                '}';
    }
}
