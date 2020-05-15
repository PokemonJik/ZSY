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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class chat extends AppCompatActivity {
    connectURL url = new connectURL();
    String hostid,customerid,room_id;
    ArrayList list = new ArrayList<>();
    String message[]={};

    ListView lvProduct;
    public boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        final Intent intent=getIntent();
        customerid=intent.getStringExtra("myid");
        hostid=intent.getStringExtra("hostid");
        System.out.println("这里是chat，myid："+customerid);
        System.out.println("这里是chat，hostid："+hostid);
        Thread thread = new Thread(run_room);
        thread.start();
        while(thread.isAlive()){
        }
        System.out.println("room_id: " + room_id);
        Thread thread2 = new Thread(run_content);
        thread2.start();
        while(thread2.isAlive()){
        }

        ImageButton p=(ImageButton)findViewById(R.id.information);
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Intent intent = new Intent(chat.this,information.class);
                Map<String,String>user = getuser_mes(chat.this);
                String id=user.get("id");
                intent.putExtra("customerid",customerid);
                intent.putExtra("hostid",hostid);
                if(id.equals(hostid)){
                    intent.putExtra("id",customerid);
                }else {
                    intent.putExtra("id", hostid);
                }
                startActivity(intent);
            }
        });
        lvProduct = (ListView) findViewById(R.id.word);
        final ProductAdaptertj adapter = new ProductAdaptertj(chat.this, (ArrayList<HashMap<String, String>>) list);
        lvProduct.setAdapter(adapter);

        Button b=findViewById(R.id.btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View source) {
                Thread thread = new Thread(run_send);
                thread.start();
                while(thread.isAlive()){}
                adapter.notifyDataSetChanged();
                EditText e=findViewById(R.id.text);
                e.setText(" ");
            }
        });
    }
//    date_default_timezone_set('PRC');
//    $date= date('Y-m-d H:i:s',time());
//    echo $date;
    Runnable run_room= new Runnable() {
        @Override
        public void run() {
            Http_chatroom httpconn = new Http_chatroom();
            String connectURL = "http://"+url.URL+"/chatroom.php";
            flag = httpconn.gotoConn(hostid,customerid,connectURL);
            if (flag) {
                room_id = httpconn.result;

            }

        }
    };
    Runnable run_send= new Runnable() {
        @Override
        public void run() {
            Http_sendword httpconn = new Http_sendword();
            String connectURL = "http://"+url.URL+"/sentword.php";
            Map<String,String>user = getuser_mes(chat.this);
            String name = user.get("username");
            EditText e=findViewById(R.id.text);
            String content=e.getText().toString();
            Map<String,String> record = new HashMap<String,String>();
            record.put("name",name);
            record.put("content",content);
            System.out.println(room_id);
            System.out.println(name);
            System.out.println(content);
            flag = httpconn.gotoConn(room_id,name,content,connectURL);
            if (flag) {
                String time = httpconn.result;
                record.put("time",time);
                list.add(record);
            }

        }
    };
    Runnable run_content= new Runnable() {
        @Override
        public void run() {
            Http_content httpconn = new Http_content();
            String connectURL = "http://"+url.URL+"/chatcontent.php";
            flag = httpconn.gotoConn(room_id,connectURL);
            if (flag) {
                message=httpconn.result.split(",");
                for(int i=0;i<message.length;i++) {
                    Map<String,String> record = new HashMap<String,String>();
                    record.put("name",message[i]);
                    System.out.println(message[i]);
                    i++;
                    record.put("content",message[i]);
                    System.out.println(message[i]);
                    i++;
                    record.put("time",message[i]);
                    System.out.println(message[i]);
                    System.out.println("-----------");
                    list.add(record);
                }
            }

        }
    };
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
            chat.ProductAdaptertj.ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.chat_list, parent, false);
                holder = new chat.ProductAdaptertj.ViewHolder();
                holder.name= (TextView) convertView.findViewById(R.id.name);
                holder.content = (TextView) convertView.findViewById(R.id.content);
                holder.time= (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (chat.ProductAdaptertj.ViewHolder) convertView.getTag();
            }

            HashMap<String, String> map = list.get(position);
            holder.name.setText(map.get("name"));
            holder.content.setText(map.get("content"));
            holder.time.setText(map.get("time"));
            return convertView;
        }

        private static class ViewHolder {
            private TextView name;
            private TextView time;
            private TextView content;
        }
    }

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
