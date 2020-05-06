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

public class userLogin extends AppCompatActivity {
    private EditText name,paswd;
    private Button login;
    public boolean flag=false;
    String message[]={};
    connectURL url = new connectURL();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.loginact);
        init();

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userLogin.this,user_register.class);
                startActivity(intent);
            }
        });

    }

    private void init(){
        name = findViewById(R.id.et_name);
        paswd = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(runnable);
                thread.start();

            }
        });
    }

    private boolean save_userMes(Context context,String id,String name,String sex,String school){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_mes",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("username",name);
        editor.putString("sex",sex);
        editor.putString("school",school);
        editor.commit();
        return true;
    }

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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_Conn httpconn = new Http_Conn();
            String userid = name.getText().toString();
            String password = paswd.getText().toString();
            String connectURL = "http://"+url.URL+"/user_login.php";
            flag = httpconn.gotoConn(userid,password,connectURL);
            if(flag){
                message=httpconn.result.split(",");
                System.out.println(message[0]+message[1]+message[2]);
                boolean savaflag = save_userMes(userLogin.this,message[3],message[0],message[1],message[2]);
                Map<String,String>user = getuser_mes(userLogin.this);
                System.out.println(user.get("school"));
                Intent intent = new Intent(userLogin.this,login2.class);
                startActivity(intent);
                Looper.prepare();
                Toast.makeText(userLogin.this,"登陆成功",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(userLogin.this,"登陆失败,密码错误",Toast.LENGTH_SHORT).show();
                Looper.loop();

            }
        }
    };
}
