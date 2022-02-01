package com.example.firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{

    Context context;
    List<OrderData> orderDataList;
    OrderInterface orderInterface;


    public OrderAdapter(Context context, List<OrderData> orderDataList, OrderInterface orderInterface) {
        this.context = context;
        this.orderDataList = orderDataList;
        this.orderInterface = orderInterface;
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDescription , textViewDate;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescription= itemView.findViewById(R.id.item_order_tv_description);
            textViewDate= itemView.findViewById(R.id.item_order_tv_date);

        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(view);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderData orderData = orderDataList.get(position);
        holder.textViewDescription.setText(orderData.getRequestDescription());
        holder.textViewDate.setText(orderData.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderInterface.onOrderClick(orderData);
            }
        });
    }


    @Override
    public int getItemCount() {
        return  orderDataList.size();
    }

 //عشان يعرضلي بيانات اوردر معين

    public interface OrderInterface{
        void onOrderClick(OrderData orderData);

    }
}
