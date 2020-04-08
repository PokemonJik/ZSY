package com.wangj.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.spark.submitbutton.SubmitButton;

public class grxx extends AppCompatActivity{
    ImageView imageView;
    public boolean flag=false;
    String message[]={};
    String imgpath,name;String sex,school;
    Bitmap bitmap;
    connectURL url = new connectURL();
    int flag1=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.grxx);

        imageView = findViewById(R.id.tx);
        Thread thread = new Thread(runnable);
        thread.start();

        while(thread.isAlive()){
        }
        imageView.setImageBitmap(bitmap);

        Map<String,String>user = getuser_mes(grxx.this);
        String a = user.get("username");
        String b = user.get("sex");
        String c = user.get("school");

        EditText Name = (EditText) findViewById(R.id.E1);
        EditText Sex = (EditText) findViewById(R.id.E2);
        EditText School = (EditText) findViewById(R.id.E3);

        findViewById(R.id.gr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(grxx.this,gr.class);
                startActivity(intent);
            }
        });
        SubmitButton btn=(SubmitButton) findViewById(R.id.update);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(runnable2);
                thread.start();
                flag1=1;
            }
        });
//
//        if(flag1==1){
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Intent intent = new Intent(grxx.this,gr.class);
//            startActivity(intent);
//
//        }

        findViewById(R.id.tx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(grxx.this,uploadimg.class);
                startActivity(intent);
            }
        });

        Name.setText(a);
        Sex.setText(b);
        School.setText(c);

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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_getimg httpconn = new Http_getimg();
            Map<String,String>user = getuser_mes(grxx.this);
            String own= user.get("username");
            String connectURL = "http://"+url.URL+"/getimg.php";
            Boolean flag = httpconn.gotoConn(own,connectURL);
            if(flag){
                message=httpconn.result.split(",");
                System.out.println(message[0]);
                imgpath = "http://"+url.URL+"/"+message[0];
                System.out.println(imgpath);
                Bitmap source=getImage(imgpath);
                int size = Math.min(source.getWidth(), source.getHeight());

                int x = (source.getWidth() - size) / 2;
                int y = (source.getHeight() - size) / 2;

                Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                if (squaredBitmap != source) {
                    source.recycle();
                }

                bitmap = Bitmap.createBitmap(size, size, source.getConfig() != null
                        ? source.getConfig() : Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                BitmapShader shader = new BitmapShader(squaredBitmap,
                        BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                paint.setShader(shader);
                paint.setAntiAlias(true);

                float r = size / 2f;
                canvas.drawCircle(r, r, r, paint);

                squaredBitmap.recycle();
            }
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            Http_grxx httpconn = new Http_grxx();
            Map<String,String>user = getuser_mes(grxx.this);
            String oname= user.get("username");
            EditText e1,e2,e3;
            e1=findViewById(R.id.E1);
            e2=findViewById(R.id.E2);
            e3=findViewById(R.id.E3);


            name=e1.getText().toString();
            sex=e2.getText().toString();
            school=e3.getText().toString();
            save_userMes(grxx.this,name,sex,school);
            System.out.println("name:"+name);
            System.out.println("sex:"+sex);
            System.out.println("school:"+school);

            String connectURL = "http://"+url.URL+"/grxx.php";
            Boolean flag = httpconn.gotoConn(oname,name,sex,school,connectURL);
        }
    };

    private boolean save_userMes(Context context,String name,String sex,String school){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_mes",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",name);
        editor.putString("sex",sex);
        editor.putString("school",school);
        editor.commit();
        return true;
    }
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
}


