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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lubna.eticketing.R;

import java.util.ArrayList;

public class listviewAdapterQL extends BaseAdapter {

    public ArrayList<ModelQL> QuotationList;
    Activity activity;
    SharedPreferences sp;

    @Override
    public int getCount() {
        return QuotationList.size();
    }

    @Override
    public Object getItem(int position) {
        return QuotationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.quotationlist, null);
            holder = new ViewHolder();
            holder.txtQID = (TextView) convertView.findViewById(R.id.txtQID);
            holder.txtID = (TextView) convertView.findViewById(R.id.txtID);
            holder.txtSender = (TextView) convertView.findViewById(R.id.txtSender);
            holder.txtSite = (TextView) convertView.findViewById(R.id.txtSite);

            holder.lview = (LinearLayout) convertView.findViewById(R.id.relativeLayout1);
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
        ModelQL item = QuotationList.get(position);
        holder.txtQID.setText(item.getId());
        holder.txtID.setText(item.getQuotation_id());
        holder.txtSite.setText(item.getSite());
        holder.txtSender.setText(item.getSender());
        return convertView;
    }

    public listviewAdapterQL(ArrayList<ModelQL> quotationList, Activity activity) {
        QuotationList = quotationList;
        this.activity = activity;
        this.sp = sp;
    }

    private class ViewHolder {
        LinearLayout lview;
        TextView txtQID,txtID;
        TextView txtSender;
        TextView txtSite;
    }
}
