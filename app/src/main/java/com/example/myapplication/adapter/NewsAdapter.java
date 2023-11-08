package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.News;

import java.util.List;

public class NewsAdapter  extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<News> list;
        public NewsAdapter(Context context, int myLayout, List<News> list) {
        this.context = context;
        this.myLayout = myLayout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setHinhList(List<News> hinhList) {
        list.clear();
        list.addAll(hinhList);
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(myLayout,null);
        TextView txttieude= convertView.findViewById(R.id.txttieude);
        ImageView imagehinh = convertView.findViewById(R.id.txthinh);
        News news = list.get(position);


        // Hiển thị hình ảnh từ URL sử dụng Glide hoặc Picasso
        Glide.with(context).load(news.hinh).into(imagehinh);

        // Hiển thị tiêu đề tin tức
        txttieude.setText(news.tieude);
        return convertView;
    }

}
