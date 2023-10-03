package com.example.fitbrov2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class foodAdapter extends RecyclerView.Adapter<foodAdapter.ViewHolder> {

    ArrayList<food> foodArrayList;

    public foodAdapter(ArrayList<food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }

    public void setFilteredList(ArrayList<food> filteredList){
        this.foodArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public foodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull foodAdapter.ViewHolder viewHolder, int i) {
        food History = foodArrayList.get(i);
        viewHolder.foodName.setText(History.getFoodName());
        viewHolder.quantity.setText(String.valueOf(History.getQuantity()));
        viewHolder.totalCalorie.setText(String.valueOf(History.getTotalCalorie()));
        viewHolder.date.setText(History.getDate());
        viewHolder.time.setText(History.getTime());
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView foodName, date, quantity, time, totalCalorie;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.itemFoodName);
            date = (TextView) itemView.findViewById(R.id.itemRecordDate);
            quantity = (TextView) itemView.findViewById(R.id.itemQuantity);
            time = (TextView) itemView.findViewById(R.id.itemRecordTime);
            totalCalorie = (TextView) itemView.findViewById(R.id.itemTotalCalorie);
        }
    }
}
