package com.wangj.testproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class fwxq extends Activity {

    public boolean flag=false;
    String message[]={};
    ArrayList list = new ArrayList<>();
    TextView uname;
    TextView hname;
    TextView intro;
    TextView dm;
    String username;
    String housename;
    String hostid,id;
    ImageView imageView;
    String imgpath;
    Bitmap bitmap;
    connectURL url = new connectURL();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fwxq);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        housename = intent.getStringExtra("housename");
        String place = intent.getStringExtra("place");
        hname = findViewById(R.id.place);
        hname.setText("地址:"+place);
        System.out.println("这里是fwxq\n");
        TextView jrgwc = findViewById(R.id.jrgwc);
        jrgwc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Thread thread = new Thread(runnablef);
                thread.start();
            }
        });

        TextView lxfbr = findViewById(R.id.lxfbr);
        lxfbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Map<String,String>user = getuser_mes(fwxq.this);
                String id = user.get("id");
                if(hostid.equals(id)){
                    Toast.makeText(fwxq.this,"不要和自己对话哦！",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(fwxq.this,chat.class);
                    intent.putExtra("hostid",hostid);
                    intent.putExtra("myid",id);
                    startActivity(intent);
                }

            }
        });

        ImageView imageView = findViewById(R.id.zf_pic);

        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }
        if(bitmap == null){
            imageView.setImageResource(R.mipmap.error);
        }else{
            imageView.setImageBitmap(bitmap);
        }

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            uname = findViewById(R.id.fbr);
            intro = findViewById(R.id.fwjj);
            dm = findViewById(R.id.demand);
            Http_fwxq httpconn = new Http_fwxq();
            String connectURL = "http://"+url.URL+"/fwxq.php";
            flag = httpconn.gotoConn(username,housename,connectURL);
            if(flag){
                message=httpconn.result.split(",");
                uname.setText("发布人:"+message[0]);
                intro.setText("房屋简介:"+message[1]);
                dm.setText("房屋要求:"+message[3]);
                hostid=message[4];
                imgpath = "http://"+url.URL+"/"+message[2];
                System.out.println(imgpath);
                System.out.println("发布人："+message[0]);
                System.out.println("简介："+message[1]);
                System.out.println("大小："+message.length);
                System.out.println("需求："+message[3]);
                System.out.println("userid："+message[4]);
                bitmap=getImage(imgpath);
            }else{
                Looper.prepare();
                Toast.makeText(fwxq.this,"查询失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

    };
    Runnable runnablef = new Runnable() {
        @Override
        public void run() {
            Map<String,String>user = getuser_mes(fwxq.this);
            String own = user.get("username");
            Http_fwxqf httpconn = new Http_fwxqf();
            String connectURL = "http://"+url.URL+"/fwxqf.php";
            flag = httpconn.gotoConn(own,username,housename,connectURL);
            if (flag) {
                Looper.prepare();
                Toast.makeText(fwxq.this,"加入购物车成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(fwxq.this, "查询失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    };


    private Map<String,String>getuser_mes(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_mes",MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        String sex = sharedPreferences.getString("sex",null);
        String school = sharedPreferences.getString("school",null);
        String id = sharedPreferences.getString("id",null);
        Map<String,String>user = new HashMap<String,String>();
        user.put("username",username);
        user.put("sex",sex);
        user.put("school",school);
        user.put("id",id);
        return user;
    }

    public static Bitmap getImage(String path){

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            System.out.println("tdw1");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
