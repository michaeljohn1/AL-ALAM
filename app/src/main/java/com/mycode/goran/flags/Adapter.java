package com.mycode.goran.flags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Adapter extends BaseAdapter {

    private Context context;
    private List<Ranking> lstRanking;

    public Adapter(Context context, List<Ranking> lstRanking) {
        this.context = context;
        this.lstRanking = lstRanking;
    }

    @Override
    public int getCount() {
        return lstRanking.size();
    }

    @Override
    public Object getItem(int position) {
        return lstRanking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.row,null);
        }

        ImageView imgTop = null;
        if (view != null) {
            imgTop = view.findViewById(R.id.imgTop);
        }
        TextView txtTop = null;
        if (view != null) {
            txtTop = view.findViewById(R.id.txtTop);
        }

        if(position == 0 )// top1
        {
            assert imgTop != null;
            imgTop.setImageResource(R.drawable.top1);
        }
        else if(position == 1) // top 2
        {
            assert imgTop != null;
            imgTop.setImageResource(R.drawable.top2);
        }
        else {
            assert imgTop != null;
            imgTop.setImageResource(R.drawable.top3);
        }

        txtTop.setText(String.format("%.1f",lstRanking.get(position).getScore()));
        return view;

    }
}
