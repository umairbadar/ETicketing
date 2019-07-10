package com.example.lubna.eticketing.Ticketing.Quotation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.example.lubna.eticketing.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Quotation_Item extends RecyclerView.Adapter<Adapter_Quotation_Item.ViewHolder> {

    public static final List<String> idList = new ArrayList<>();
    public static final List<String> priceList = new ArrayList<>();
    List<Model_Quotation_Item> list;
    Context context;

    public Adapter_Quotation_Item(List<Model_Quotation_Item> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotation_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Model_Quotation_Item item = list.get(position);
        holder.tv_item.setText(item.getProduct_name());
        holder.tv_qty.setText(item.getQty());
        holder.tv_cost.setText(item.getCost());

        String approved_qty = item.getApproved_quantity();
        String status = item.getStatus();

        if (status.equals("pending")){
            holder.tv_subTotal.setText(item.getSub_total());
        } else {
            int sub_total = Integer.parseInt(item.getApproved_quantity()) * Integer.parseInt(item.getCost());
            holder.tv_subTotal.setText(String.valueOf(sub_total));
            holder.checkBoxID.setChecked(true);
            holder.checkBoxID.setEnabled(false);
        }

        if (approved_qty.equals("0")){
            holder.tv_approved_qty.setText("x");
        } else {
            holder.tv_approved_qty.setText(item.getApproved_quantity());
        }


       /*

        if (status.equals("approved")){
            holder.layout1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    5.5f));

        if (approved_qty.equals("0")){
            holder.tv_approved_qty.setText("x");
        } else {

        }
        } else {
            holder.layout1.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4.5f));
            holder.tv_approved_qty.setVisibility(View.GONE);
        }*/

        holder.checkBoxID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    idList.add(item.getSr_product_id());
                    priceList.add(holder.tv_qty.getText().toString());
                    /*Toast.makeText(context, idList.toString() + " " + priceList.toString(),
                            Toast.LENGTH_SHORT).show();*/
                } else {
                    idList.remove(item.getSr_product_id());
                    priceList.remove(holder.tv_qty.getText().toString());
                    /*Toast.makeText(context, idList.toString() + " " + priceList.toString(),
                            Toast.LENGTH_SHORT).show();*/
                }
            }
        });

        holder.tv_qty.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                priceList.remove(holder.tv_qty.getText().toString());
                idList.remove(item.getSr_product_id());
            }

            public void afterTextChanged(Editable s) {
                priceList.add(holder.tv_qty.getText().toString());
                idList.add(item.getSr_product_id());
                /*Toast.makeText(context, idList.toString() + " " + priceList.toString(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout1;
        TextView tv_item, tv_cost, tv_subTotal,tv_approved_qty;
        EditText tv_qty;
        CheckBox checkBoxID;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_subTotal = itemView.findViewById(R.id.tv_subtotal);
            tv_approved_qty = itemView.findViewById(R.id.tv_approved_qty);
            checkBoxID = itemView.findViewById(R.id.checkboxID);
            layout1 = itemView.findViewById(R.id.layout1);
        }
    }
}
