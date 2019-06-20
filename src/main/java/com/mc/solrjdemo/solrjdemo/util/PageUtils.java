package com.mc.solrjdemo.solrjdemo.util;

import lombok.Data;

import java.util.List;
@Data
public class PageUtils {

    private int pageSize = 5;//每页大小 ， 默认显示5条
    private int currPage;//当前页数
    private int offset;//偏移量
    private int count;//总记录数
    private int countPage;//总页数 count/countPage...

    private List<?> list;// ? 表示未知的数据类型 用于存放分页显示的数据信息

    public PageUtils(int count,int pageSize,int currPage){
        if(pageSize > 1){
            this.pageSize = pageSize;
        }else{
            this.pageSize = 5;
        }
        if(currPage > 1){
            this.currPage = currPage;
        }else {
            this.currPage = 1;
        }
        this.countPage = count%pageSize==0?count/pageSize:count/pageSize+1;
        this.count = count;
        if(currPage>this.countPage && this.countPage > 0){
            this.currPage = countPage;
        }
        //偏移量
        this.offset=(this.currPage -1) * this.pageSize;
    }
    public PageUtils(){

    }
}
