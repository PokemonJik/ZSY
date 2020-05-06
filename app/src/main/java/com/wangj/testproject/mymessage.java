package com.wangj.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mymessage extends AppCompatActivity {
    ListView lvProduct;
    String my_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage);
        Map<String,String> user = getuser_mes(mymessage.this);
        my_id=user.get("id");
        System.out.println("my_id:"+my_id);
        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }
        lvProduct = (ListView) findViewById(R.id.mylist);

        final mymessage.ProductAdaptertj adapter = new mymessage.ProductAdaptertj(mymessage.this, (ArrayList<HashMap<String, String>>) list);
        lvProduct.setAdapter(adapter);

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> zc = new HashMap<String,String>();
                zc = (Map<String, String>) list.get(position);
                System.out.println("map大小："+zc.size());
                Intent intent = new Intent(mymessage.this,chat.class);
                intent.putExtra("hostid",my_id);
                intent.putExtra("myid",zc.get("customer_id"));
                startActivity(intent);
            }
        });


        Button back=(Button) findViewById(R.id.mbt1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(mymessage.this,fy.class);
                startActivity(intent);

            }
        });

    }

    String message[]={};
    public boolean flag=false;
    connectURL url = new connectURL();
    ArrayList list = new ArrayList<>();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {


            Http_mymessage httpconn = new Http_mymessage();

            String connectURL = "http://"+url.URL+"/mymessage.php";
            flag = httpconn.gotoConn(my_id,connectURL);
            if(flag){
                message=httpconn.result.split(",");
                for(int i=0;i<message.length;i++) {
                    Map<String,String> record = new HashMap<String,String>();
                    record.put("customer_id",message[i]);
                    i++;
                    record.put("name",message[i]);
                    list.add(record);
                }
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

    static class ProductAdaptertj extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> list;
        connectURL url = new connectURL();
        String imgpath;
        Bitmap bitmap;

        public ProductAdaptertj(Context context, ArrayList<HashMap<String, String>> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            mymessage.ProductAdaptertj.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.zf_list, parent, false);
                holder = new mymessage.ProductAdaptertj.ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.zf_name);
                holder.tvplace = (TextView) convertView.findViewById(R.id.zf_information);
                holder.tvhousename = (TextView) convertView.findViewById(R.id.zf_demond);
                holder.tvpic = convertView.findViewById(R.id.zf_pic);

                convertView.setTag(holder);
            } else {
                holder = (mymessage.ProductAdaptertj.ViewHolder) convertView.getTag();
            }

            HashMap<String, String> map = list.get(position);
            holder.tvplace.setText(map.get("name"));
            return convertView;
        }

        private static class ViewHolder {
            private TextView tvName;
            private TextView tvhousename;
            private TextView tvplace;
            private ImageView tvpic;
        }

    }

}
