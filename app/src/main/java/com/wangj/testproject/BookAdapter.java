package com.wangj.testproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.OnLongClickListener;

/**
 * Created by dell on 2018/6/3.
 * Created by qiyueqing on 2018/6/3.
 */

public class BookAdapter extends Adapter<BookAdapter.ViewHolder> {

    private List<book> mBookList;
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private LayoutInflater inflater;

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button bookImage;

        public ViewHolder(View view) {
            super(view);
            bookImage = view.findViewById(R.id.id_book_image);

        }
    }

    public BookAdapter(Context context, List<book> bookList) {
        this.mContext=context;
        mBookList = bookList;
        inflater=LayoutInflater.from(mContext);

    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(view);
        return (ViewHolder) holder;
    }

    public int getItemCount() {
        return mBookList.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder holder, final int position) {
        book book = mBookList.get(position);
        holder.bookImage.setBackgroundResource(book.getImageId());
        if (mOnItemClickListener != null) {
            holder.bookImage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder.bookImage.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }
}
