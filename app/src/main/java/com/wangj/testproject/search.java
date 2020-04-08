package com.wangj.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class search extends AppCompatActivity implements
        SearchView.OnQueryTextListener {
    private connectURL url = new connectURL();
    private String message[]={};
    private SearchView sv;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, message));
        lv.setTextFilterEnabled(true);//设置lv可以被过虑
        sv = (SearchView) findViewById(R.id.sv);
        // 设置该SearchView默认是否自动缩小为图标
        sv.setIconifiedByDefault(false);
        // 为该SearchView组件设置事件监听器
        sv.setOnQueryTextListener(this);
        // 设置该SearchView显示搜索按钮
        sv.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        sv.setQueryHint("按地址查找");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(search.this,result.class);
                String item2 = (String) parent.getAdapter().getItem(position) ;
                System.out.println("地址： "+item2);
                intent.putExtra("place",item2);
                startActivity(intent);
            }
        });


    }
    // 用户输入字符时激发该方法
    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(search.this, "textChange--->" + newText, 1).show();
        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            lv.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            lv.setFilterText(newText);
        }
        return true;
    }

    // 单击搜索按钮时激发该方法
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 实际应用中应该在该方法内执行实际查询
        // 此处仅使用Toast显示用户输入的查询内容
        Toast.makeText(this, "您的选择是:" + query, Toast.LENGTH_SHORT).show();
        return false;
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_tj httpconn = new Http_tj();
            String connectURL = "http://"+url.URL+"/search.php";
            boolean flag = httpconn.gotoConn(connectURL);
            if(flag){
                message=httpconn.result.split(",");
                for(int i=0;i<message.length;i++) {
                   System.out.println(message[i]);
                }
            }else{
                Looper.prepare();
                Toast.makeText(search.this,"查询失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        }
    };
}
