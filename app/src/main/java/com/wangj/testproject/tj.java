package com.wangj.testproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class tj extends Activity implements OnPageChangeListener {
    /**
     * ViewPager
     */
    private ViewPager viewPager;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray ;


    ListView lvProduct;

    public boolean flag=false;
    String message[]={};
    ArrayList list = new ArrayList<>();
    connectURL url = new connectURL();
    String imgpath;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tj);

        Thread thread = new Thread(runnable);
        thread.start();
        while(thread.isAlive()){
        }
        ViewGroup group = (ViewGroup)findViewById(R.id.viewGroupf);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        lvProduct = (ListView) findViewById(R.id.tjlb);
        final ProductAdaptertj adapter = new ProductAdaptertj(tj.this, (ArrayList<HashMap<String, String>>) list);
        lvProduct.setAdapter(adapter);

        lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String,String> zc = new HashMap<String,String>();
                    zc = (Map<String, String>) list.get(position);
                    Intent intent = new Intent(tj.this,fwxq.class);
                    intent.putExtra("username",zc.get("username"));
                    intent.putExtra("housename",zc.get("housename"));
                    intent.putExtra("place",zc.get("place"));
                    startActivity(intent);
            }
        });


        //载入图片资源ID
        imgIdArray = new int[]{R.mipmap.tj2, R.mipmap.tj3, R.mipmap.tj1, R.mipmap.tj4};


        //将点点加入到ViewGroup中
        tips = new ImageView[imgIdArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(10,10));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }


        //将图片装载到数组中
        mImageViews = new ImageView[imgIdArray.length];
        for(int i=0; i<mImageViews.length; i++){
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }

        //设置Adapter
        viewPager.setAdapter(new MyAdapter());
        //设置监听，主要是设置点点的背景
        viewPager.setOnPageChangeListener(this);
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
        viewPager.setCurrentItem((mImageViews.length) * 100);


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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.zf_list, parent, false);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.zf_name);
                holder.tvplace = (TextView) convertView.findViewById(R.id.zf_information);
                holder.tvhousename = (TextView) convertView.findViewById(R.id.zf_demond);
                holder.tvpic = convertView.findViewById(R.id.zf_pic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
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

        static class ViewHolder {
            public TextView tvName;
            public TextView tvhousename;
            public TextView tvplace;
            public ImageView tvpic;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bitmap=getImage(imgpath);
            }

        };
    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_tj httpconn = new Http_tj();
            String connectURL = "http://"+url.URL+"/tj.php";
            flag = httpconn.gotoConn(connectURL);
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
                Looper.prepare();
                Toast.makeText(tj.this,"查询失败",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        }
    };

    /**
     *
     * @author xiaanming
     *
     */
    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            return mImageViews[position % mImageViews.length];
        }



    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
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

}
