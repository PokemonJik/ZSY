package com.wangj.testproject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Http_Conn {
    public String result="";

    public boolean gotoConn(String phonenum,String paswd,String connectUrl){
        boolean isLoginSucceed = false;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(connectUrl);
        List<NameValuePair>params = new ArrayList<NameValuePair>();
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



        if(!result.equals("fail")){
            isLoginSucceed=true;
        }

        return isLoginSucceed;

    }
}
