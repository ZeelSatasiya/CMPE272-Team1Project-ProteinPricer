package com.example.protien_pricer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<FoodItem> items;

    private RecyclerViewClickListener listener;

    public RecyclerAdapter(ArrayList<FoodItem> items, RecyclerViewClickListener listener){
        this.items = items;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView desc;
        TextView brand;
        Button go_button;
        private int index;

        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            desc = itemView.findViewById(R.id.item_desc);
            brand = itemView.findViewById(R.id.item_brand);

            itemView.setOnClickListener(this);
        }

        public void setIndex(int index) {
            this.index = index;
        }
        public int getIndex(){
            return this.index;
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
        holder.desc.setText(items.get(position).getDescription());
        holder.brand.setText("Brand: " + items.get(position).getBrand());
        holder.setIndex(items.get(position).getIndex());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
}
