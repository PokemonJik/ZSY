package com.wangj.testproject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class Http_xqfbcc {
    public String result="";

    public int gotoConn(String name,String demand,String introduce,String connectUrl){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(connectUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name",name));
        params.add(new BasicNameValuePair("demand",demand));
        params.add(new BasicNameValuePair("introduce",introduce));
        try{
            httpRequest.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
            System.out.println("1");
            System.out.println("1"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.equals("success")){
            return 1;
        }else if(result.equals("用户信息不存在")){
            return 0;
        }else{
            return -1;
        }


    }
}
