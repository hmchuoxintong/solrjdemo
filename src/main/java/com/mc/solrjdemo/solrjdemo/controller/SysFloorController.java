package com.mc.solrjdemo.solrjdemo.controller;


import com.mc.solrjdemo.solrjdemo.entity.SysFloor;
import com.mc.solrjdemo.solrjdemo.service.ISysFloorService;
import com.mc.solrjdemo.solrjdemo.util.PageUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.StringUtils;
//import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.solr.repository.query.SolrParameters;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 公寓 前端控制器
 * </p>
 *
 * @author mc
 * @since 2019-06-16
 */
@RestController
@RequestMapping("/floor")
public class SysFloorController  {
    @Autowired
    private ISysFloorService sysFloorService;
    @Autowired
    private SolrClient solrClient;
    @RequestMapping("/get")
    public Object get(){

        List<SysFloor> list = sysFloorService.list();
        return list;
    }
    /**
     * 保证双方的数据一致性（同步）
     * 1、功能：针对于mysql 中的 sys_floor 表 完成的一系列数据增删改查
     * 2、功能：solr表索引库也要完成相应的增删改查。solrj技术
     */



    //已测试可用
    public void solrAdd(Object floor){
        System.out.println("进入添加或修改solr");
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        //创建对象
//        SysFloor floor = new SysFloor();
//        floor.setId(521);
//        floor.setFname("枫叶城不下雨公寓");
//        floor.setFinfo("就是用来测试solr的");
        UpdateResponse response;
        try {
            response = client.addBean(floor);
            client.commit();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
        System.out.println("添加或修改solr成功");
    }

    public void solrDelete(@NotNull Integer id) throws Exception{
        System.out.println("进入solr删除 id： " + id);
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        client.deleteById(id + "");
        client.commit();
        client.close();
    }

    public void solrDelete(String query) throws Exception {
        System.out.println("进入solr删除 query： " + query);
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        client.deleteByQuery(query);
        client.commit();
        client.close();
    }

    public List<SysFloor> solrQuery(String query) throws Exception {
        System.out.println("进入solr查询 query： " + query);
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        String q = "*:*";
        SolrQuery sq = new SolrQuery(q);
        QueryResponse queryResponse = client.query(sq);
        List<SysFloor> fs = queryResponse.getBeans(SysFloor.class);
        System.out.println("返回的结果集 size ： " + fs.size());
        client.commit();
        client.close();
        return fs;
    }

    /**
     * 复杂查询solr
     */
    public void solrQuerys(String keyword) throws Exception {
        System.out.println("进入solr查询 query： ");
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();

        SolrQuery sq = new SolrQuery();
        //1、设置 q 标题
        if(StringUtils.isEmpty(keyword)){
//            sq.set("q","*:*");//设置默认域之后不需要
            sq.set("q","*");
        }else{
//            sq.set("q","floor_name:" + keyword);
            sq.set("q",keyword);//设置默认域之后不需要
        }

        //2、设置 fq 类别
        String floor_info = "哈哈";
        if(!StringUtils.isEmpty(floor_info)){//非空的时候才加类别查询
            sq.addFilterQuery("floor_info:" + floor_info);
        }
        //2.2、价格赛选
        String price_str = "1-10";
        if(!StringUtils.isEmpty(price_str)){
            String[] arrs = price_str.split("-");
            if(arrs.length == 1){
                sq.addFilterQuery("prod_price:["+arrs[0]+" TO *]");
            }
            if(StringUtils.isEmpty(arrs[0])){
                sq.addFilterQuery("prod_price:[* TO "+arrs[1]+"]");
            }
            sq.addFilterQuery("prod_price:["+arrs[0]+" TO "+arrs[1]+"]");
        }
        //3、价格排序 psort=1升序 psort=2降序
        int psort = 0;
        if(psort == 1){
            sq.addSort("prod_price", SolrQuery.ORDER.asc);
        }else if(psort == 2){
            sq.addSort("prod_price", SolrQuery.ORDER.desc);
        }

        //4、分页功能 start=0, rows=10
        /**
         * mysql 分页功能相似
         * start offset 偏移量
         * rows rows 返回的最大记录数
         * start = rows*(page -1)
         */
        Integer start = 0;
        Integer rows = 60;
        sq.setStart(0);
        sq.setRows(60);
        //5.设置回显， 返回页面的字段 保护隐私 ， 或者是不想看某些字段的数据
        //设置要显示的字段 不设置的 显示null
        sq.setFields("floor_name","floor_info");

        //6、设置默认域 默认查询哪个字段
        sq.set("df","floor_name");

        //7、设置高亮显示
        //启动高亮设置
        sq.setHighlight(true);
        sq.addHighlightField("floor_name");//设置高亮字段
        sq.setHighlightSimplePre("<front color='red'>");//设置高亮前缀
        sq.setHighlightSimplePost("</front>");//设置高亮后缀


        QueryResponse queryResponse = client.query(sq);
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        if(highlighting != null){
            highlighting.keySet().forEach(key->
                            System.out.println(highlighting.get(key).get("floor_name"))
            );
        }else{
            List<SysFloor> fs = queryResponse.getBeans(SysFloor.class);
            fs.stream().forEach(f->{
                System.out.println(f.getFname());
                System.out.println(f.getFinfo());
            });
        }

        client.commit();
        client.close();
    }

    /**
     *主页查询方法 （全文检索）
     * @param keyWord 查询关键字
     * @param catalogName 类别名称
     * @return
     */
    /**
     * 主页查询方法 （全文检索）
     * @param keyword 主页关键字检索
     * @param catalogName 类别
     * @param price_str 价格区间 0-10
     * @param psort 排序类别 1顺序 2降序
     * @param pageNum 目标页
     * @return
     */
    @RequestMapping("/query")
    public Object query(String keyword, String catalogName, String price_str,
                           String psort, Integer pageNum) throws IOException, SolrServerException {
        SolrQuery sq = new SolrQuery();
        //1、设置 q 标题
        if(StringUtils.isEmpty(keyword)){
//            sq.set("q","*:*");//设置默认域之后不需要
            sq.set("q","*");
        }else{
//            sq.set("q","floor_name:" + keyword);
            sq.set("q",keyword);//设置默认域之后不需要
        }
        //2、设置 fq 类别
        if(!StringUtils.isEmpty(catalogName)){//非空的时候才加类别查询
            sq.addFilterQuery("floor_info:" + catalogName);
        }
        //2.2、价格赛选
//        String price_str = "1-10";
//        if(!StringUtils.isEmpty(price_str)){
//            String[] arrs = price_str.split("-");
//            if(arrs.length == 1){
//                sq.addFilterQuery("prod_price:["+arrs[0]+" TO *]");
//            }
//            if(StringUtils.isEmpty(arrs[0])){
//                sq.addFilterQuery("prod_price:[* TO "+arrs[1]+"]");
//            }
//            sq.addFilterQuery("prod_price:["+arrs[0]+" TO "+arrs[1]+"]");
//        }
        //3、价格排序 psort=1升序 psort=2降序
//        int psort = 0;
//        if(psort == 1){
//            sq.addSort("prod_price", SolrQuery.ORDER.asc);
//        }else if(psort == 2){
//            sq.addSort("prod_price", SolrQuery.ORDER.desc);
//        }
        //分页
        sq.setStart(0);
        sq.setRows(15);
        //设置要显示的字段 不设置的 显示null
        sq.setFields("id", "floor_name", "floor_info");
        //6、设置默认域 默认查询哪个字段
        sq.set("df","floor_name");
        //7、设置高亮显示
        //启动高亮设置
        sq.setHighlight(true);
        sq.addHighlightField("floor_name");//设置高亮字段
        sq.setHighlightSimplePre("<front color='red'>");//设置高亮前缀
        sq.setHighlightSimplePost("</front>");//设置高亮后缀
        QueryResponse queryResponse = solrClient.query(sq);
        List<SysFloor> fs = queryResponse.getBeans(SysFloor.class);
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        boolean b
                = highlighting.isEmpty()?true:false;
        if(!b)
            fs.stream().forEach(f->{
                Map<String, List<String>> stringListMap = highlighting.get(f.getId() + "");
                if(!stringListMap.isEmpty())
                f.setFname(stringListMap.get("floor_name").get(0));
            });

        return fs;
    }






}
