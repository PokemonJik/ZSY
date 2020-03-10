package com.wangj.testproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class fyfb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fyfb);

        final EditText housename = findViewById(R.id.E1);
        final EditText place = findViewById(R.id.E2);
        final EditText intro = findViewById(R.id.E3);
        final EditText demand = findViewById(R.id.E4);
        Button back = findViewById(R.id.gr3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fyfb.this,fy.class);
                startActivity(intent);
            }
        });
        Button bn = findViewById(R.id.B1);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fyfb.this,fyuploadimg.class);
                Bundle bundle = new Bundle();//定义Bundle对象存储要传递的值
                String name = housename.getText().toString();//获取EditText中输入的值，toString（）是指将其转换成String型
                String pl = place.getText().toString();
                String it = intro.getText().toString();
                String dm = demand.getText().toString();
                bundle.putString("housename",name);
                bundle.putString("place",pl);
                bundle.putString("intro",it);
                bundle.putString("demand",dm);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
