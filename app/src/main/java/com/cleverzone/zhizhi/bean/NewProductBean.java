package com.cleverzone.zhizhi.bean;

import java.io.Serializable;

/**
 * Created by WANGZHENGZE on 2015/7/30.
 */
public class NewProductBean implements Serializable {
    /**
     * id
     */
    public int id = -1;

    /**
     * 商品名字
     */
    public String name = "";

    /**
     * 图片路径
     */
    public String picPath = "";

    /**
     * 生产日期
     */
    public int prDate = 0;

    /**
     * 保质期
     */
    public int shelfLife = 0;

    /**
     * 保质期类型（年、月、日）
     */
    public int shelfLifeType = 0;

    /**
     * 截止日期
     */
    public int exDate = 0;

    /**
     * 提醒日期
     */
    public int hintDate = 0;

    /**
     * 总数
     */
    public int count = 0;

    /**
     * 存放位置
     */
    public String position = "";

    /**
     * 提前提醒天数
     */
    public int advance = 0;

    /**
     * 主分类
     */
    public String mainClassify = "";

    /**
     * 子分类
     */
    public String subClassify = "";

    /**
     * 备注
     */
    public String backup = "";

    /**
     * 频率
     */
    public int frequency = 0;

    @Override
    public String toString() {
        return "NewProductBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picPath='" + picPath + '\'' +
                ", prDate=" + prDate +
                ", shelfLife=" + shelfLife +
                ", shelfLifeType=" + shelfLifeType +
                ", exDate=" + exDate +
                ", hintDate=" + hintDate +
                ", count=" + count +
                ", position='" + position + '\'' +
                ", advance=" + advance +
                ", mainClassify='" + mainClassify + '\'' +
                ", subClassify='" + subClassify + '\'' +
                ", backup='" + backup + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
