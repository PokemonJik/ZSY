package com.wangj.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login2 extends AppCompatActivity {
    private List<book> bookList = new ArrayList<>();
    private BookAdapter recycleAdapter;
    ImageView imageView;
    public boolean flag=false;
    String message[]={};
    String imgpath;
    Bitmap bitmap;
    connectURL url = new connectURL();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login2);
        initBooks();

        imageView = findViewById(R.id.button7);
        Thread thread = new Thread(runnable);
        thread.start();

        while(thread.isAlive()){
        }
        imageView.setImageBitmap(bitmap);

        ImageView bn=findViewById(R.id.button7);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(login2.this,gr.class);
                startActivity(intent);

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);  //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了

        //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recycleAdapter=new BookAdapter(login2.this,bookList);
        recyclerView.setAdapter(recycleAdapter);


        recyclerView.setItemAnimator(new DefaultItemAnimator());



        recycleAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {

            @Override
            public void onLongClick(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(login2.this,tj.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(login2.this,fy.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(login2.this,gwc.class);
                        startActivity(intent2);
                        break;
                }
            }

            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(login2.this,tj.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(login2.this,fy.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(login2.this,gwc.class);
                        startActivity(intent2);
                        break;
                }
            }
        });


    }

    private void initBooks() {
        book book1 = new book(R.mipmap.tj);
        bookList.add(book1);

        book book2 = new book(R.mipmap.fy);
        bookList.add(book2);

        book book3 = new book(R.mipmap.pyq);
        bookList.add(book3);
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
            Map<String,String>user = getuser_mes(login2.this);
            String own = user.get("username");
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


