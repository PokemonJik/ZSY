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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.ColorDialog;


public class myhome extends Activity {

    public boolean flag=false;
    String message[]={};
    ArrayList list = new ArrayList<>();
    EditText hname;
    EditText Place;
    EditText intro;
    EditText dm;
    String username,place,introduction,demand;
    String housename,oname,oplace;
    ImageView imageView;
    String imgpath;
    Bitmap bitmap;
    connectURL url = new connectURL();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhome);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        housename = intent.getStringExtra("housename");
        String place = intent.getStringExtra("place");
        Place = findViewById(R.id.place);
        Place.setText(place);
        oplace=place;
        oname=housename;
        System.out.println("这里是myhome\n");

        Button del=findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                ColorDialog dialog = new ColorDialog(source.getContext());
                dialog.setTitle("删除提示：");
                dialog.setContentText("       确认您要删除该发布信息吗？");
                dialog.setColor("#2b4490");
                dialog.setContentImage(getResources().getDrawable(R.drawable.pkq));
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Thread thread = new Thread(runnable2);
                        thread.start();
                        Intent intent = new Intent(myhome.this,myhouse.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                        .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Toast.makeText(myhome.this, dialog.getNegativeText().toString(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        Button Back=findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(myhome.this,myhouse.class);
                startActivity(intent);
            }
        });
        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                ColorDialog dialog = new ColorDialog(source.getContext());
                dialog.setTitle("修改提示：");
                dialog.setContentText("       确认您要修改该发布信息吗？");
                dialog.setColor("#2b4490");
                dialog.setContentImage(getResources().getDrawable(R.drawable.cat));
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        Thread thread = new Thread(runnable3);
                        thread.start();
                        Intent intent = new Intent(myhome.this,myhouse.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                        .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                Toast.makeText(myhome.this, dialog.getNegativeText().toString(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(myhome.this,select.class);
                intent.putExtra("housename",housename);
                startActivity(intent);
            }
        });

        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Thread thread = new Thread(runnable4);
                thread.start();
                Intent intent = new Intent(myhome.this,myhouse.class);
                startActivity(intent);

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
            hname = findViewById(R.id.homename);
            intro = findViewById(R.id.fwjj);
            dm = findViewById(R.id.demand);
            Http_fwxq httpconn = new Http_fwxq();
            String connectURL = "http://"+url.URL+"/fwxq.php";
            flag = httpconn.gotoConn(username,housename,connectURL);
            if(flag){
                message=httpconn.result.split(",");
                hname.setText(housename);
                intro.setText(message[1]);
                dm.setText(message[3]);
                imgpath = "http://"+url.URL+"/"+message[2];
//                System.out.println(imgpath);
//                System.out.println("发布人："+message[0]);
//                System.out.println("简介："+message[1]);
//                System.out.println("大小："+message.length);
//                System.out.println("需求："+message[3]);
                bitmap=getImage(imgpath);
            }else{
//                Looper.prepare();
//                Toast.makeText(myhome.this,"查询失败",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        }

    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Http_homedelete httpconn = new Http_homedelete();
            String connectURL = "http://"+url.URL+"/homedelete.php";
            flag = httpconn.gotoConn(housename,connectURL);
            System.out.println("房间名"+housename+"\n");
            if(flag){
                Looper.prepare();
                Toast.makeText(myhome.this,"删除成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(myhome.this,"删除失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

    };
    Runnable runnable3 = new Runnable() {
        @Override
        public void run() {
            hname = findViewById(R.id.homename);
            Place = findViewById(R.id.place);
            intro = findViewById(R.id.fwjj);
            dm = findViewById(R.id.demand);
            place=Place.getText().toString();
            introduction=intro.getText().toString();
            demand=dm.getText().toString();
            housename=hname.getText().toString();
            System.out.println("房名："+housename);
            System.out.println("地址："+place);
            System.out.println("简介："+introduction);
            System.out.println("要求："+demand);
            Http_homeupdate httpconn = new Http_homeupdate();
            String connectURL = "http://"+url.URL+"/homeupdate.php";
            flag = httpconn.gotoConn(oname,oplace,housename,place,introduction,demand,connectURL);
            if(flag){
                Looper.prepare();
                Toast.makeText(myhome.this,"修改成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(myhome.this,"修改失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

    };

    Runnable runnable4 = new Runnable() {
        @Override
        public void run() {
            Http_refresh httpconn = new Http_refresh();
            String connectURL = "http://"+url.URL+"/refresh.php";
            flag = httpconn.gotoConn(oname,oplace,connectURL);
            System.out.println("房间名"+housename+"\n");
            if(flag){
                Looper.prepare();
                Toast.makeText(myhome.this,"刷新成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(myhome.this,"刷新失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }

    };

    private Map<String,String> getuser_mes(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_mes",MODE_PRIVATE);
        String username = sharedPreferences.getString("username",null);
        String sex = sharedPreferences.getString("sex",null);
        String school = sharedPreferences.getString("school",null);
        Map<String,String>user = new HashMap<String,String>();
        user.put("username",username);
        user.put("sex",sex);
        user.put("school",school);
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
