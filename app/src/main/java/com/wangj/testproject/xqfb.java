package com.wangj.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class xqfb extends AppCompatActivity {

    private EditText demand,introduce;
    private Button tj;
    String a;
    public int flag=0;
    String message[]={};
    connectURL url = new connectURL();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zfxq);
        getSupportActionBar().hide();

        findViewById(R.id.gr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(xqfb.this,gr.class);
                startActivity(intent);
            }
        });


        Map<String,String>user = getuser_mes(xqfb.this);
        a = user.get("username");

        Thread thread = new Thread(runf);
        thread.start();

    }


    private void initc(){
        demand = findViewById(R.id.E1);
        introduce = findViewById(R.id.E2);
        tj = findViewById(R.id.B1);
        demand.setText(message[0]);
        introduce.setText(message[1]);
        tj.setText("修改");
        tj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(runff);
                thread.start();

            }
        });



//        tj.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Thread thread = new Thread(runnable);
//                thread.start();
//
//            }
//        });
    }


    private void init(){
        demand = findViewById(R.id.E1);
        introduce = findViewById(R.id.E2);
        tj = findViewById(R.id.B1);
        tj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(runnable);
                thread.start();

            }
        });
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_xqfb httpxqfb = new Http_xqfb();
            String Demand = demand.getText().toString();
            String Introduce = introduce.getText().toString();
//            demand.setText(Demand);
//            introduce.setText(Introduce);
            String connectURL = "http://"+url.URL+"/xqfb.php";
            flag = httpxqfb.gotoConn(a,Demand,Introduce,connectURL);
            if(flag==1){
                Intent intent = new Intent(xqfb.this,gr.class);
                startActivity(intent);
                Looper.prepare();
                Toast.makeText(xqfb.this,"住房需求更新成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(xqfb.this,"住房需求更新失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    };
    Runnable runf = new Runnable() {
        @Override
        public void run() {
            Http_xqfbc httpcxqfb = new Http_xqfbc();
            String connectURL = "http://"+url.URL+"/xqfbc.php";
            flag = httpcxqfb.gotoConn(a,connectURL);
            if(flag==1){
                message=httpcxqfb.result.split(",");
                System.out.println(message[0]+message[1]);
                initc();
            }else{
                init();
            }
        }
    };

    Runnable runff = new Runnable() {
        @Override
        public void run() {
            Http_xqfbcc httpxqfbc = new Http_xqfbcc();
            String Demand = demand.getText().toString();
            String Introduce = introduce.getText().toString();
            String connectURL = "http://"+url.URL+"/xqfbcc.php";
            flag = httpxqfbc.gotoConn(a, Demand, Introduce, connectURL);
            if (flag == 1) {
                Intent intent = new Intent(xqfb.this, gr.class);
                startActivity(intent);
                Looper.prepare();
                Toast.makeText(xqfb.this, "住房需求发布成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(xqfb.this, "住房需求发布失败", Toast.LENGTH_SHORT).show();
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
}
