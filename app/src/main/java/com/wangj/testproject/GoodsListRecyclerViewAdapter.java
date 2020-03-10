package com.wangj.testproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ZhengJiao on 2017/4/27.
 */
public class GoodsListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private int type = 0;//0:LinearViewHolder  1:GridViewHolder
    public GoodsListRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    //点击切换布局的时候通过这个方法设置type
    public void setType(int type) {
        this.type = type;
    }

    @Override
    //用来获取当前项Item是哪种类型的布局
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView;
        if (viewType == 0) {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_goods_list, parent, false);
            LinearViewHolder linearViewHolder = new LinearViewHolder(baseView);
            return linearViewHolder;
        } else {
            baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gridview_goods_list, parent, false);
            GridViewHolder gridViewHolder = new GridViewHolder(baseView);
            return gridViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (type==0){
            LinearViewHolder linearViewHolder= (LinearViewHolder) holder;
            linearViewHolder.tvName.setText("杭州电子科技大学XX路XX街");
            linearViewHolder.tvPriceYZ.setText("杭州电子科技大学软件工程17级");
            linearViewHolder.tvPrice.setText("联系电话：XXXXXXXXXX");

            linearViewHolder.iv.setImageResource(R.mipmap.tj3);
        }else {
            GridViewHolder gridViewHolder= (GridViewHolder) holder;
            gridViewHolder.tvName.setText("杭州电子科技大学XX路XX街");
            gridViewHolder.tvPriceYZ.setText("杭州电子科技大学软件工程17级");
            gridViewHolder.tvPrice.setText("联系电话：XXXXXXXXXX");
           gridViewHolder.iv.setImageResource(R.mipmap.tj4);
            if (position%2==0)
                gridViewHolder.rightView.setVisibility(View.VISIBLE);
            else
                gridViewHolder.rightView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return 35;
    }

    public static class LinearViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvName, tvPriceYZ, tvPrice;

        public LinearViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.imageview);
            tvName= (TextView) itemView.findViewById(R.id.tv_name);
            tvPriceYZ= (TextView) itemView.findViewById(R.id.tv_price_yz);
            tvPrice= (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

    public static class GridViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv;
        private TextView tvName, tvPriceYZ, tvPrice;
        private View rightView;

        public GridViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.imageview);
            tvName= (TextView) itemView.findViewById(R.id.tv_name);
            tvPriceYZ= (TextView) itemView.findViewById(R.id.tv_price_yz);
            tvPrice= (TextView) itemView.findViewById(R.id.tv_price);
            rightView=itemView.findViewById(R.id.view_right);
        }
    }

}
