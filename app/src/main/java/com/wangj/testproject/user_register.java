package com.wangj.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class user_register extends AppCompatActivity {

    private EditText name,paswd;
    private Button login;
    public int flag=0;
    connectURL url = new connectURL();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zc);
        getSupportActionBar().hide();
        init();

    }

    private void init(){
        name = findViewById(R.id.et_account);
        paswd = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_register);
        login.setOnClickListener(new View.OnClickListener(){
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
            Http_register httpconn = new Http_register();
            String userid = name.getText().toString();
            String password = paswd.getText().toString();
            String connectURL = "http://"+url.URL+"/user_register.php";
            flag = httpconn.gotoConn(userid,password,connectURL);
            if(flag==1){
                Intent intent = new Intent(user_register.this,userLogin.class);
                startActivity(intent);
                Looper.prepare();
                Toast.makeText(user_register.this,"注册成功请登录",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else if(flag==-1){
                Looper.prepare();
                Toast.makeText(user_register.this,"注册失败，该用户已经注册",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else if(flag==0){
                Looper.prepare();
                Toast.makeText(user_register.this,"学信网账号有误",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }


        }
    };
}
