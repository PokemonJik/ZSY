package com.wangj.testproject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class ImageService {
    /**
     * 获取网络图片的数据
     * @param path 网络图片路径
     * @return
     * @throws Exception
     */
    public static byte[] getImage(String path) throws Exception {
        URL url=new URL(path);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();//得到基于HTTP协议的连接对象
        conn.setConnectTimeout(5000);//设置超时时间
        conn.setRequestMethod("GET");//请求方式
        if(conn.getResponseCode()==200){//判断是否请求成功
            InputStream inputStream=conn.getInputStream();
            return read(inputStream);
        }
        return null;
    }
    /**
     * 读取流中的数据
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        byte[] b=new byte[1024];
        int len=0;
        while((len=inputStream.read(b))!=-1){
            outputStream.write(b);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }
}
