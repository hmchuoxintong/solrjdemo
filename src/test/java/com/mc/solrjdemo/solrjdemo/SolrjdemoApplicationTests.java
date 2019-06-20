package com.mc.solrjdemo.solrjdemo;

import com.mc.solrjdemo.solrjdemo.entity.SysFloor;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SolrjdemoApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Test
    public void add(){
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        //创建对象
        SysFloor floor = new SysFloor();
        floor.setId(521+"");
        floor.setFname("枫叶城不下雨公寓");
        floor.setFinfo("就是用来测试solr的");
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
    }
    @Test
    public void solrQuery() throws Exception {
        System.out.println("进入solr查询 query： ");
        String solrUrl = "http://127.0.0.1:8081/solr/core_demo";
        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
        String q = "floor_name:公寓";
        SolrQuery sq = new SolrQuery(q);
        QueryResponse queryResponse = client.query(sq);
        List<SysFloor> fs = queryResponse.getBeans(SysFloor.class);
        System.out.println("返回的结果集 size ： " + fs.size());
        client.commit();
        client.close();
    }
    @Test
    public void testsplit(){
        String str = "-999";
        String[] arrs = str.split("-");
        System.out.println("arrs 长度 ： " + arrs.length);
        for (String arr : arrs) {
            System.out.println("arr ： " + arr);
        }

    }

}
