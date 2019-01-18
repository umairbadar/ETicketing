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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lubna.eticketing.R;

import java.util.ArrayList;

/**
 * Created by lubna on 10/26/2018.
 */

public class listviewAdapter extends BaseAdapter {


    public ArrayList<Model> SurveyList;
    Activity activity;
    SharedPreferences sp;

    @Override
    public int getCount() {
        return SurveyList.size();

    }

    @Override
    public Object getItem(int position) {
        return SurveyList.get(position);
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listticket, null);
            holder = new ViewHolder();
            holder.mSNo = (TextView) convertView.findViewById(R.id.sNo);
            holder.msurvey = (TextView) convertView.findViewById(R.id.survey);
            holder.msite = (TextView) convertView.findViewById(R.id.sitename);



            holder.lview = (LinearLayout) convertView.findViewById(R.id.relativeLayout1);
            convertView.setTag(holder);
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(10);
            shape.setColor(Color.parseColor("#ff669900"));
            holder.lview.setBackground(shape);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        Model item =SurveyList.get(position);

        holder.mSNo.setText(item.getsNo());
        holder.msurvey.setText(item.getTicket());
        holder.msite.setText(item.getSitename());
        return convertView;
    }
    public listviewAdapter(Activity activity, ArrayList<Model> productList) {
        super();
        this.activity = activity;
        this.SurveyList = productList;
        this.sp = sp;
    }
    private class ViewHolder {
        LinearLayout lview;
        TextView mSNo;
        TextView msurvey;
        TextView msite;



    }


}
