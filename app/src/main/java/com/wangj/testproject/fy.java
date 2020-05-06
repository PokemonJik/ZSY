package com.wangj.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class fy extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fy);

        ImageButton back=(ImageButton) findViewById(R.id.gr4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(fy.this,login2.class);
                startActivity(intent);

            }
        });

        ImageButton back2=(ImageButton) findViewById(R.id.news);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(fy.this,mymessage.class);
                startActivity(intent);

            }
        });

        Button bn=(Button) findViewById(R.id.button7);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(fy.this,fyfb.class);
                startActivity(intent);

            }
        });
        Button bn1=(Button) findViewById(R.id.button8);
        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(fy.this,search.class);
                startActivity(intent);

            }
        });
        Button bn2=(Button) findViewById(R.id.button9);
        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(fy.this,myhouse.class);
                startActivity(intent);

            }
        });
//
//        Button bn1=(Button) findViewById(R.id.button8);
//        bn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View source) {
//                Intent intent = new Intent(fy.this,fjss.class);
//                startActivity(intent);
//
//            }
//        });
    }
}
