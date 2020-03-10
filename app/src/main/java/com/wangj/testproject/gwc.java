package com.wangj.testproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View.OnClickListener;

import android.widget.AdapterView.OnItemClickListener;


public class gwc extends Activity implements OnClickListener
{

    private static final int INITIALIZE = 0;

    private ListView mListView;// 列表

    private ListAdapter mListAdapter;// adapter

    private List<DataBean> mListData = new ArrayList<DataBean>();// 数据

    private boolean isBatchModel;// 是否可删除模式

    private RelativeLayout mBottonLayout;
    private CheckBox mCheckAll; // 全选 全不选

    private TextView mEdit; // 切换到删除模式

    private TextView mPriceAll; // 商品总价

    private TextView mSelectNum; // 选中数量

    private TextView mFavorite; // 移到收藏夹

    private TextView mDelete; // 删除 结算

    private int totalPrice = 0; // 商品总价

    String username;

    String message[]={};

    public boolean flag=false;

    public boolean flag2=false;

    String shopname;

    String pl;

    String content;

    connectURL url = new connectURL();

    /** 批量模式下，用来记录当前选中状态 */
    private SparseArray<Boolean> mSelectState = new SparseArray<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gwc);
        Map<String,String>user = getuser_mes(gwc.this);
        username = user.get("username");
        initView();
        initListener();
        loadData();
    }

    private void doDelete(List<Integer> ids)
    {
        for (int i = 0; i < mListData.size(); i++)
        {
            long dataId = mListData.get(i).getId();
            for (int j = 0; j < ids.size(); j++)
            {
                int deleteId = ids.get(j);
                if (dataId == deleteId)
                {
                    shopname = mListData.get(i).getContent();
                    pl = mListData.get(i).getplace();
                    Thread thread = new Thread(runnable);
                    thread.start();
                    mListData.remove(i);
                    i--;
                    ids.remove(j);
                    j--;
                    while(thread.isAlive()){
                    }
                }
            }
            Toast.makeText(gwc.this,"删除成功",Toast.LENGTH_SHORT).show();
        }

        refreshListView();
        mSelectState.clear();
        totalPrice = 0;
        mSelectNum.setText("已选" + 0 + "件商品");
        mPriceAll.setText("￥" + 0.00 + "");
        mCheckAll.setChecked(false);

    }

    private void dopurchase(List<Integer> ids)
    {
        for (int i = 0; i < mListData.size(); i++)
        {
            long dataId = mListData.get(i).getId();
            for (int j = 0; j < ids.size(); j++)
            {
                int deleteId = ids.get(j);
                if (dataId == deleteId)
                {
                    shopname = mListData.get(i).getShopName();
                    content = mListData.get(i).getContent();
                    Thread thread = new Thread(runnablelx);
                    thread.start();
                    while(thread.isAlive()){
                    }

                }
            }
            Toast.makeText(gwc.this,"联系发布者",Toast.LENGTH_SHORT).show();
        }

        refreshListView();
        mSelectState.clear();
        totalPrice = 0;
        mSelectNum.setText("已选" + 0 + "件商品");
        mPriceAll.setText("￥" + 0.00 + "");
        mCheckAll.setChecked(false);

    }


    private final List<Integer> getSelectedIds()
    {
        ArrayList<Integer> selectedIds = new ArrayList<Integer>();
        for (int index = 0; index < mSelectState.size(); index++)
        {
            if (mSelectState.valueAt(index))
            {
                selectedIds.add(mSelectState.keyAt(index));
            }
        }
        return selectedIds;
    }

    private void initView()
    {

        mBottonLayout = (RelativeLayout) findViewById(R.id.cart_rl_allprie_total);
        mCheckAll = (CheckBox) findViewById(R.id.check_box);
        mEdit = (TextView) findViewById(R.id.subtitle);
        mPriceAll = (TextView) findViewById(R.id.tv_cart_total);
        mSelectNum = (TextView) findViewById(R.id.tv_cart_select_num);
        mFavorite = (TextView) findViewById(R.id.tv_cart_move_favorite);
        mDelete = (TextView) findViewById(R.id.tv_cart_buy_or_del);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setSelector(R.drawable.list_selector);

    }

    private void initListener()
    {
        mEdit.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mCheckAll.setOnClickListener(this);

    }

    private void loadData()
    {
        new LoadDataTask().execute(new Params(INITIALIZE));
    }

    private void refreshListView()
    {
        if (mListAdapter == null)
        {
            mListAdapter = new ListAdapter();
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener(mListAdapter);

        } else
        {
            mListAdapter.notifyDataSetChanged();

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

    ///////////////////////////////////////////////////////////////////////////////////////////获取数据

    private List<DataBean> getData()
    {
        int maxId = 0;
        if (mListData != null && mListData.size() > 0)
            maxId = mListData.get(mListData.size() - 1).getId();
        List<DataBean> result = new ArrayList<DataBean>();
        DataBean data = null;
        Http_gwc httpconn = new Http_gwc();
        String connectURL = "http://"+url.URL+"/gwc.php";
        flag = httpconn.gotoConn(username,connectURL);
        System.out.println(flag);
        if(flag){
            message=httpconn.result.split(",");
            for(int i=0;i<message.length;i++) {
                data = new DataBean();
                data.setId(maxId + i + 1);// 从最大Id的下一个开始
                //data.setShopName("我的" + (maxId + 1 + i) + "店铺");
                data.setShopName(message[i]);
                i++;
                data.setContent(message[i]);
                i++;
                //data.setContent("我的购物车里面的第" + (maxId + 1 + i) + "个品");
                data.setplace(message[i]);
                i++;
                data.setpic(message[i]);
                data.setCarNum(1);
                data.setPrice(1000f);
                result.add(data);
            }
        }
        return result;
    }

    class Params
    {
        int op;

        public Params(int op)
        {
            this.op = op;
        }

    }

    class Result
    {
        int op;
        List<DataBean> list;
    }

    private class LoadDataTask extends AsyncTask<Params, Void, Result>
    {
        @Override
        protected Result doInBackground(Params... params)
        {
            Params p = params[0];
            Result result = new Result();
            result.op = p.op;
            try
            {// 模拟耗时
                Thread.sleep(500L);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            result.list = getData();
            return result;
        }

        @Override
        protected void onPostExecute(Result result)
        {
            super.onPostExecute(result);
            if (result.op == INITIALIZE)
            {
                mListData = result.list;
            } else
            {
                mListData.addAll(result.list);
                Toast.makeText(getApplicationContext(), "添加成功！", Toast.LENGTH_SHORT).show();
            }

            refreshListView();
        }

    }

    private class ListAdapter extends BaseAdapter implements OnItemClickListener
    {
        connectURL url = new connectURL();
        String imgpath;
        Bitmap bitmap;

        @Override
        public int getCount()
        {
            return mListData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;
            View view = convertView;
            if (view == null)
            {
                view = LayoutInflater.from(gwc.this).inflate(R.layout.cart_list_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else
            {
                holder = (ViewHolder) view.getTag();
            }

            DataBean data = mListData.get(position);
            bindListItem(holder, data);
            holder.add.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    // TODO Auto-generated method stub

                    int _id = (int) mListData.get(position).getId();

                    boolean selected = mSelectState.get(_id, false);

                    mListData.get(position).setCarNum(mListData.get(position).getCarNum() + 1);

                    notifyDataSetChanged();

                    if (selected)
                    {
                        totalPrice += mListData.get(position).getPrice();
                        mPriceAll.setText("￥" + totalPrice + "");

                    }



                }
            });

            holder.red.setOnClickListener(new OnClickListener()
            {

                @Override
                public void onClick(View v)
                {

                    // TODO Auto-generated method stub
                    if (mListData.get(position).getCarNum() == 1)
                        return;

                    int _id = (int) mListData.get(position).getId();

                    boolean selected = mSelectState.get(_id, false);
                    mListData.get(position).setCarNum(mListData.get(position).getCarNum() - 1);
                    notifyDataSetChanged();

                    if (selected)
                    {
                        totalPrice -= mListData.get(position).getPrice();
                        mPriceAll.setText("￥" + totalPrice + "");

                    }

                }
            });
            return view;
        }

        ///////////////////////////////////////////////////////////////////////

        private void bindListItem(ViewHolder holder, DataBean data)
        {

            holder.shopName.setText(data.getShopName());
            holder.content.setText(data.getContent());
            holder.place.setText(data.getplace());
            holder.price.setText("￥" + data.getPrice());
            holder.carNum.setText(data.carNum + "");
            imgpath = "http://"+url.URL+"/"+data.getpic();
            System.out.println(imgpath);
            Thread thread = new Thread(runnableget);
            thread.start();
            while(thread.isAlive()){
            }
            if(bitmap == null){
                holder.image.setImageResource(R.mipmap.error);
            }else{
                holder.image.setImageBitmap(bitmap);
            }
            int _id = data.getId();

            boolean selected = mSelectState.get(_id, false);
            holder.checkBox.setChecked(selected);

        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            DataBean bean = mListData.get(position);

            ViewHolder holder = (ViewHolder) view.getTag();
            int _id = (int) bean.getId();

            boolean selected = !mSelectState.get(_id, false);
            holder.checkBox.toggle();
            if (selected)
            {
                mSelectState.put(_id, true);
                totalPrice += bean.getCarNum() * bean.getPrice();
            } else
            {
                mSelectState.delete(_id);
                totalPrice -= bean.getCarNum() * bean.getPrice();
            }
            mSelectNum.setText("已选" + mSelectState.size() + "件商品");
            mPriceAll.setText("￥" + totalPrice + "");
            if (mSelectState.size() == mListData.size())
            {
                mCheckAll.setChecked(true);
            } else
            {
                mCheckAll.setChecked(false);
            }
        }

        Runnable runnableget = new Runnable() {
            @Override
            public void run() {
                bitmap=getImage(imgpath);
            }

        };
    }


    class ViewHolder
    {
        CheckBox checkBox;

        ImageView image;
        TextView shopName;
        TextView content;
        TextView carNum;
        TextView price;
        TextView add;
        TextView red;
        TextView place;

        public ViewHolder(View view)
        {
            checkBox = (CheckBox) view.findViewById(R.id.check_box);
            shopName = (TextView) view.findViewById(R.id.tv_source_name);
            image = (ImageView) view.findViewById(R.id.iv_adapter_list_pic);
            content = (TextView) view.findViewById(R.id.tv_hname);
            carNum = (TextView) view.findViewById(R.id.tv_num);
            price = (TextView) view.findViewById(R.id.tv_price);
            add = (TextView) view.findViewById(R.id.tv_add);
            red = (TextView) view.findViewById(R.id.tv_reduce);
            place = (TextView) view.findViewById(R.id.tv_place);

        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {

            case R.id.subtitle:
                isBatchModel = !isBatchModel;
                if (isBatchModel)
                {
                    mEdit.setText(getResources().getString(R.string.menu_enter));
                    mDelete.setText(getResources().getString(R.string.menu_del));
                    mBottonLayout.setVisibility(View.GONE);
                    mFavorite.setVisibility(View.VISIBLE);

                } else
                {
                    mEdit.setText(getResources().getString(R.string.menu_edit));

                    mFavorite.setVisibility(View.GONE);
                    mBottonLayout.setVisibility(View.VISIBLE);
                    mDelete.setText(getResources().getString(R.string.menu_sett));
                }

                break;

            case R.id.check_box:
                if (mCheckAll.isChecked())
                {

                    totalPrice = 0;
                    if (mListData != null)
                    {
                        mSelectState.clear();
                        int size = mListData.size();
                        if (size == 0)
                        {
                            return;
                        }
                        for (int i = 0; i < size; i++)
                        {
                            int _id = (int) mListData.get(i).getId();
                            mSelectState.put(_id, true);
                            totalPrice += mListData.get(i).getCarNum() * mListData.get(i).getPrice();
                        }
                        refreshListView();
                        mPriceAll.setText("￥" + totalPrice + "");
                        mSelectNum.setText("已选" + mSelectState.size() + "件商品");

                    }
                } else
                {
                    if (mListAdapter != null)
                    {
                        totalPrice = 0;
                        mSelectState.clear();
                        refreshListView();
                        mPriceAll.setText("￥" + 0.00 + "");
                        mSelectNum.setText("已选" + 0 + "件商品");

                    }
                }
                break;

            case R.id.tv_cart_buy_or_del:
                if (isBatchModel)
                {
                    List<Integer> ids = getSelectedIds();
                    doDelete(ids);
                } else
                {
                    List<Integer> ids = getSelectedIds();
                    dopurchase(ids);
                }

                break;

            default:
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Http_gwcdelete httpconn = new Http_gwcdelete();
            String connectURL = "http://"+url.URL+"/gwcdelete.php";
            flag2 = httpconn.gotoConn(username,shopname,pl,connectURL);
        }
    };

    Runnable runnablelx = new Runnable() {
        @Override
        public void run() {
            Map<String,String>user = getuser_mes(gwc.this);
            String own = user.get("username");
            Http_fwxqf httpconn = new Http_fwxqf();
            String connectURL = "http://"+url.URL+"/purchase.php";
            flag = httpconn.gotoConn(own,shopname,content,connectURL);
        }
    };


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

