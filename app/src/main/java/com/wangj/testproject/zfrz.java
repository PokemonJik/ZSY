package com.wangj.testproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class zfrz extends AppCompatActivity {
    ListView lvProduct;
    public boolean flag=false;
    String username;
    String message1[]={};
    String message2[]={};
    ArrayList list = new ArrayList<>();
    connectURL url = new connectURL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_list);
        lvProduct = (ListView) findViewById(R.id.lv);
        //getListData();

        Map<String,String>user = getuser_mes(zfrz.this);
        username = user.get("username");

        Thread thread = new Thread(runnable);
        thread.start();

        while(thread.isAlive()){
        }



        ProductAdapter adapter = new ProductAdapter(zfrz.this, (ArrayList<HashMap<String, String>>) list);
        lvProduct.setAdapter(adapter);

    }

//    private void getListData() {
//        CommonRequest request = new CommonRequest();
//        sendHttpPostRequest(URL_PRODUCT, request, new ResponseHandler() {
//            @Override
//            public void success(CommonResponse response) {
//                LoadingDialogUtil.cancelLoading();
//                if (response.getDataList().size() > 0) {
//                    ProductAdapter adapter = new ProductAdapter(ListActivity.this, response.getDataList());
//                    lvProduct.setAdapter(adapter);
//                } else {
//                    DialogUtil.showHintDialog(ListActivity.this, "列表数据为空", true);
//                }
//            }
//
//            @Override
//            public void fail(String failCode, String failMsg) {
//                LoadingDialogUtil.cancelLoading();
//            }
//        }, true);
//    }

    static class ProductAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> list;

        public ProductAdapter(Context context, ArrayList<HashMap<String, String>> list) {
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvDescribe = (TextView) convertView.findViewById(R.id.tv_information);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_demond);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            HashMap<String, String> map = list.get(position);
            holder.tvName.setText("房屋名:"+map.get("name"));
            holder.tvDescribe.setText("合租发起人:"+map.get("describe"));
            holder.tvPrice.setText("室友:"+map.get("price"));

            return convertView;
        }

        private static class ViewHolder {
            private TextView tvName;
            private TextView tvDescribe;
            private TextView tvPrice;
        }
    }


    private Map<String,String>getuser_mes(Context context){
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


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_zfrz httpconn = new Http_zfrz();
            String connectURL = "http://"+url.URL+"/zfrz.php";
            flag = httpconn.gotoConn(username,connectURL);
            if(flag){
                message1=httpconn.result.split("/");
                for(int i=0;i<message1.length;i++) {
                    int j=0;
                    message2=message1[i].split(",");
                    Map<String,String> record = new HashMap<String,String>();
                    record.put("name",message2[j]);//房屋人
                    j++;
                    record.put("describe",message2[j]);//房屋名
                    j++;
                    record.put("price",message2[j]);//合租室友
                    list.add(record);
                }
            }else{
//                Looper.prepare();
//                Toast.makeText(zfrz.this,"查询失败",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }

        }
    };
}

