package com.wangj.testproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myhouse extends AppCompatActivity {


    ListView lvProduct;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myhouse);
        System.out.println("这里是myhouse\n");
        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }
        lvProduct = (ListView) findViewById(R.id.mylist);

        final myhouse.ProductAdaptertj adapter = new myhouse.ProductAdaptertj(myhouse.this, (ArrayList<HashMap<String, String>>) list);
        lvProduct.setAdapter(adapter);

        Button back=(Button) findViewById(R.id.mbt1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(myhouse.this,fy.class);
                startActivity(intent);

            }
        });
        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> zc = new HashMap<String,String>();
                zc = (Map<String, String>) list.get(position);
                Intent intent = new Intent(myhouse.this,myhome.class);
                intent.putExtra("username",zc.get("username"));
                intent.putExtra("housename",zc.get("housename"));
                intent.putExtra("place",zc.get("place"));
                startActivity(intent);
            }
        });

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
            myhouse.ProductAdaptertj.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.zf_list, parent, false);
                holder = new myhouse.ProductAdaptertj.ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.zf_name);
                holder.tvplace = (TextView) convertView.findViewById(R.id.zf_information);
                holder.tvhousename = (TextView) convertView.findViewById(R.id.zf_demond);
                holder.tvpic = convertView.findViewById(R.id.zf_pic);

                convertView.setTag(holder);
            } else {
                holder = (myhouse.ProductAdaptertj.ViewHolder) convertView.getTag();
            }

            HashMap<String, String> map = list.get(position);
            holder.tvName.setText(map.get("username"));
            holder.tvplace.setText(map.get("place"));
            holder.tvhousename.setText(map.get("housename"));
            imgpath = "http://"+url.URL+"/"+map.get("pic");
            System.out.println(imgpath);
            Thread thread = new Thread(runnable);
            thread.start();
            while(thread.isAlive()){
            }
            if(bitmap == null){
                holder.tvpic.setImageResource(R.mipmap.error);
            }else{
                holder.tvpic.setImageBitmap(bitmap);
            }

            return convertView;
        }

        private static class ViewHolder {
            private TextView tvName;
            private TextView tvhousename;
            private TextView tvplace;
            private ImageView tvpic;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bitmap=getImage(imgpath);
            }

        };
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

    String message[]={};
    public boolean flag=false;
    connectURL url = new connectURL();
    ArrayList list = new ArrayList<>();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Map<String,String>user = getuser_mes(myhouse.this);
            String name = user.get("username");
            Http_myhouse httpconn = new Http_myhouse();
            System.out.println(name+"       name");
            String connectURL = "http://"+url.URL+"/myhouse.php";
            flag = httpconn.gotoConn(connectURL,name);
            if(flag){
                message=httpconn.result.split(",");
                for(int i=0;i<message.length;i++) {
                    Map<String,String> record = new HashMap<String,String>();
                    record.put("username",message[i]);
                    i++;
                    record.put("housename",message[i]);
                    i++;
                    record.put("place",message[i]);
                    i++;
                    record.put("pic",message[i]);
                    list.add(record);
                }
            }else{
//                Looper.prepare();
//                Toast.makeText(myhouse.this,"查询失败",Toast.LENGTH_SHORT).show();
//                Looper.loop();
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
