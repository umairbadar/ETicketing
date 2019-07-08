package com.example.lubna.eticketing.Ticketing.Quotation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lubna.eticketing.R;

import java.util.List;

public class Adapter_Quotation_Item extends RecyclerView.Adapter<Adapter_Quotation_Item.ViewHolder> {

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
    public void onBindViewHolder(ViewHolder holder, int position) {

        Model_Quotation_Item item = list.get(position);
        holder.tv_item.setText(item.getProduct_name());
        holder.tv_qty.setText(item.getQty());
        holder.tv_cost.setText(item.getCost());
        holder.tv_subTotal.setText(item.getSub_total());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_item,tv_qty,tv_cost,tv_subTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_item = itemView.findViewById(R.id.tv_item);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_subTotal = itemView.findViewById(R.id.tv_subtotal);
        }
    }
}
