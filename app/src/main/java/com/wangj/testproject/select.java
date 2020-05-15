package com.wangj.testproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wangj.testproject.CheckBoxAdapter.ViewCache;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class select extends Activity implements OnClickListener{

    public boolean flag=false;
    String message[]={};
    String housename;

    private TextView tvSelected;
    private Button btnAll;
    private Button btnFan;
    private Button btnCancle;
    private Button btnfin;
    private ListView lv;
    private List<HashMap<String,Object>> list;
    private CheckBoxAdapter cbAdapter;
    connectURL url = new connectURL();
    private List<String> listStr = new ArrayList<String>();
    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select);
        Intent intent = getIntent();
        housename = intent.getStringExtra("housename");
        InitViews();

    }

    private void InitViews() {
        tvSelected = (TextView) findViewById(R.id.tvselected);
        btnAll = (Button) findViewById(R.id.btn_all);
        btnFan = (Button) findViewById(R.id.btn_fan);
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnfin = (Button) findViewById(R.id.btn_finish);
        lv = (ListView) findViewById(R.id.lv);

        list = new ArrayList<HashMap<String,Object>>();

        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }

        cbAdapter = new CheckBoxAdapter(this,list);
        lv.setAdapter(cbAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                ViewCache viewCache = (ViewCache) view.getTag();
                viewCache.cb.toggle();
                list.get(position).put("boolean", viewCache.cb.isChecked());

                cbAdapter.notifyDataSetChanged();

                if(viewCache.cb.isChecked()){//被选中状态
                    listStr.add(list.get(position).get("name").toString());
                }else//从选中状态转化为未选中
                {
                    listStr.remove(list.get(position).get("name").toString());
                }

                tvSelected.setText("已选择了:"+listStr.size()+"项");
            }
        });
        btnAll.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnFan.setOnClickListener(this);
        btnfin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                for(int i=0;i<listStr.size()-1; i++){
                    res=res+listStr.get(i)+" ";
                }
                res+=listStr.get(listStr.size()-1);
                System.out.println(res);
                Thread thread = new Thread(runnablef);
                thread.start();
                break;
            case R.id.btn_all://全选,修改值为true
                for(int i=0;i<list.size() && !(Boolean)list.get(i).get("boolean") ;i++){
                    list.get(i).put("boolean", true);
                    listStr.add(list.get(i).get("name").toString());
                }
                cbAdapter.notifyDataSetChanged();

                tvSelected.setText("已选择了:"+listStr.size()+"项");
                break;
            case R.id.btn_fan:
                for(int i=0;i<list.size();i++){
                    if((Boolean)list.get(i).get("boolean")){//为true
                        list.get(i).put("boolean", false);
                        listStr.remove(list.get(i).get("name").toString());
                    }
                    else
                    {
                        list.get(i).put("boolean", true);
                        listStr.add(list.get(i).get("name").toString());
                    }
                }
                cbAdapter.notifyDataSetChanged();

                tvSelected.setText("已选择了:"+listStr.size()+"项");
                break;
            case R.id.btn_cancle://取消已选
                for(int i=0;i<list.size();i++){
                    if((Boolean)list.get(i).get("boolean")){
                        list.get(i).put("boolean", false);
                        listStr.remove(list.get(i).get("name").toString());
                    }
                }
                cbAdapter.notifyDataSetChanged();
                tvSelected.setText("已选择了:"+listStr.size()+"项");
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_select httpselect = new Http_select();
            String connectURL = "http://"+url.URL+"/selectroommate.php";
            Map<String,String>user = getuser_mes(select.this);
            String myuid = user.get("id");
            flag = httpselect.gotoConn(myuid,housename,connectURL);
            if(flag){
                message=httpselect.result.split(",");
                for(int i=0;i<message.length;i++){
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("name", message[i]);
                    map.put("boolean", false);//初始化为未选
                    list.add(map);
                }
            }else{
//                Looper.prepare();
//                Toast.makeText(select.this,"查询失败",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        }

    };

    Runnable runnablef = new Runnable() {
        @Override
        public void run() {
            Map<String,String>user = getuser_mes(select.this);
            Http_purchase httpconn = new Http_purchase();
            String connectURL = "http://"+url.URL+"/purchase.php";
            String a = user.get("username");
            flag = httpconn.gotoConn(a,res,housename,connectURL);
            if(flag){
                Intent intent = new Intent(select.this,myhouse.class);
                startActivity(intent);
            }else{
//                Looper.prepare();
//                Toast.makeText(select.this,"查询失败",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        }

    };

    private Map<String,String> getuser_mes(Context context){
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
}
