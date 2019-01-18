package com.example.lubna.eticketing.Ticketing;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lubna.eticketing.R;

import java.util.ArrayList;

public class listAdapterSMD extends BaseAdapter {

    public ArrayList<ModelSMD> DepartList;
    Activity activity;
    SharedPreferences sp;

    @Override
    public int getCount() {
        return DepartList.size();
    }

    @Override
    public Object getItem(int position) {
        return DepartList.get(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public long getItemId(int position) {
        return position;
    }

    public listAdapterSMD(ArrayList<ModelSMD> departList, Activity activity) {
        DepartList = departList;
        this.activity = activity;
    }

    private class ViewHolder {
        RelativeLayout lview;
        TextView departName,departID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.ticketmanagerdepart, null);
            holder = new ViewHolder();

            holder.departName = (TextView) convertView.findViewById(R.id.departName);
            holder.departID = (TextView) convertView.findViewById(R.id.departID);

            holder.lview = (RelativeLayout) convertView.findViewById(R.id.relativeLayout1);

            convertView.setTag(holder);
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor("#ff669900"));
            holder.lview.setBackground(shape);
        }

        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ModelSMD item = DepartList.get(position);
        holder.departName.setText(item.getDep_name());
        holder.departID.setText(item.getId());
        return convertView;
    }
}
