package com.example.lubna.eticketing.Ticketing.Quotation;

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

public class QuotationListDepartAdapter extends BaseAdapter {

    public ArrayList<ModelQD> DepartList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.quotationdepart, null);
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

        ModelQD item = DepartList.get(position);
        holder.departName.setText(item.getDep_name());
        holder.departID.setText(item.getId());
        return convertView;
    }

    public  QuotationListDepartAdapter(Activity activity, ArrayList<ModelQD> departList)
    {
        super();
        this.activity = activity;
        this.DepartList = departList;
        this.sp = sp;
    }
    private class ViewHolder {
        RelativeLayout lview;
        TextView departName,departID;
    }
}
