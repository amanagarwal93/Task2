package com.example.task2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task2.R;
import com.example.task2.retrofit.ResponseData;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {

    private ResponseData responseData;

    public DataAdapter(ResponseData data) {
        this.responseData = data;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_layout, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        holder.number.setText(responseData.getResponse().get(position).getNumber());
        holder.type.setText(responseData.getResponse().get(position).getType());
        holder.address.setText(responseData.getResponse().get(position).getAddress());
        holder.username.setText(responseData.getResponse().get(position).getFname() + " " + responseData.getResponse().get(position).getLname());
    }

    @Override
    public int getItemCount() {
        return responseData.getResponse().size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView username;
        AppCompatTextView type;
        AppCompatTextView address;
        AppCompatTextView number;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            type = itemView.findViewById(R.id.type);
            address = itemView.findViewById(R.id.address);
            number = itemView.findViewById(R.id.mobileNumber);
        }

    }
}
