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



public class Http_register {
    public String result="";

    public int gotoConn(String phonenum,String paswd,String connectUrl){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(connectUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("phone",phonenum));
        params.add(new BasicNameValuePair("paswd",paswd));
        try{
            httpRequest.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
            System.out.println("1");
            System.out.println("1"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result.equals("该用户已经注册")){
            return -1;
        }else if(result.equals("学信网账号有误")){
            return 0;
        }else{
            return 1;
        }


    }
}

