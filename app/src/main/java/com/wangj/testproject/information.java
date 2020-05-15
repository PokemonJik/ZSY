package com.wangj.testproject;

import android.animation.ObjectAnimator;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.spark.submitbutton.SubmitButton;

import cn.refactor.lib.colordialog.PromptDialog;

public class information extends AppCompatActivity{
    ImageView imageView;

    String message[]={};
    String imgpath,name;String sex,school,introduce,demand,id,hostid,customerid;
    Bitmap bitmap;
    connectURL url = new connectURL();

    LinearLayout myscrollLinearlayout;

    LinearLayout mainheadview;  //顶部个人资料视图

    RelativeLayout mainactionbar; //顶部菜单栏

    int flag1=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.information);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        hostid=intent.getStringExtra("hostid");
        customerid=intent.getStringExtra("customerid");
        imageView = findViewById(R.id.tx);
        Thread thread = new Thread(runnable);
        thread.start();

        while(thread.isAlive()){
        }
        Thread thread2 = new Thread(rundemand);
        thread2.start();

        while(thread2.isAlive()){
        }
        imageView.setImageBitmap(bitmap);
        initView();
        EditText Name = (EditText) findViewById(R.id.E1);
        EditText Sex = (EditText) findViewById(R.id.E2);
        EditText School = (EditText) findViewById(R.id.E3);
        EditText Demand = (EditText) findViewById(R.id.E5);
        EditText Introduce = (EditText) findViewById(R.id.E4);
        findViewById(R.id.gr2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(information.this,chat.class);
                intent.putExtra("hostid",hostid);
                intent.putExtra("myid",customerid);
                startActivity(intent);
            }
        });




//        findViewById(R.id.tx).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(grxx.this,uploadimg.class);
//                startActivity(intent);
//            }
//        });

        Name.setText(name);
        Sex.setText(sex);
        School.setText(school);
        Demand.setText(demand);
        Introduce.setText(introduce);
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
    String message2[]={};
    Runnable rundemand = new Runnable() {
        @Override
        public void run() {
            Http_getdemand httpconn = new Http_getdemand();

            String connectURL = "http://"+url.URL+"/demand.php";
            httpconn.gotoConn(id,connectURL);
            message2=httpconn.result.split(",");
            System.out.println(message2[0]+"  "+message2[1]+"  "+message2[2]+"  "+message2[3]+"  "+message2[4]);
            demand=message2[0];
            introduce=message2[1];
            name=message2[2];
            sex=message2[3];
            school=message2[4];
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Http_getimg httpconn = new Http_getimg();
            String connectURL = "http://"+url.URL+"/getimg.php";
            Boolean flag = httpconn.gotoConn(id,connectURL);
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

    int Y;
    int position = 0;       //拖动Linearlayout的距离Y轴的距离
    int scrollviewdistancetotop = 0;   //headView的高
    int menubarHeight = 0;
    int chufaHeight = 0; //需要触发动画的高
    float scale;    //像素密度
    int headViewPosition = 0;

    ImageView userinfo_topbar;


    static boolean flag = true;

    static boolean topmenuflag = true;

    private void initView() {
        userinfo_topbar = (ImageView) findViewById(R.id.userinfo_topbar);

        //获得像素密度
        scale = this.getResources().getDisplayMetrics().density;
        mainheadview = (LinearLayout) findViewById(R.id.mainheadview);
        mainactionbar = (RelativeLayout) findViewById(R.id.mainactionbar);

        menubarHeight = (int) (55 * scale);
        chufaHeight = (int) (110 * scale);

        scrollviewdistancetotop = (int) ((260 )*scale);
        position = scrollviewdistancetotop;
        myscrollLinearlayout = (LinearLayout) findViewById(R.id.myscrollLinearlayout);
        myscrollLinearlayout.setY( scrollviewdistancetotop);    //要减去Absolote布局距离顶部的高度
        myscrollLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myscrollLinearlayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下的Y的位置
                        Y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int nowY = (int) myscrollLinearlayout.getY();   //拖动界面的Y轴位置
                        int tempY = (int) (event.getRawY() - Y);    //手移动的偏移量
                        Y = (int) event.getRawY();
                        if ((nowY + tempY >= 0) && (nowY + tempY <= scrollviewdistancetotop)) {
                            if ((nowY + tempY <= menubarHeight)&& (topmenuflag == true) ){
                                userinfo_topbar.setVisibility(View.VISIBLE);
                                topmenuflag = false;
                            } else if ((nowY + tempY > menubarHeight) && (topmenuflag == flag)) {
                                userinfo_topbar.setVisibility(View.INVISIBLE);
                                topmenuflag = true;
                            }
                            int temp = position += tempY;
                            myscrollLinearlayout.setY(temp);
                            int headviewtemp = headViewPosition += (tempY/5);
                            mainheadview.setY(headviewtemp);
                        }
                        //顶部的动画效果
                        if ((myscrollLinearlayout.getY() <= chufaHeight) && (flag == true)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 1, 0.0f);
                            anim.setDuration(500);
                            anim.start();
                            flag = false;
                        } else if ((myscrollLinearlayout.getY() > chufaHeight + 40) && (flag == false)) {
                            ObjectAnimator anim = ObjectAnimator.ofFloat(mainheadview, "alpha", 0.0f, 1f);
                            anim.setDuration(500);
                            anim.start();
                            flag = true;
                        }
                        break;
                }
                return false;
            }
        });
    }

}


