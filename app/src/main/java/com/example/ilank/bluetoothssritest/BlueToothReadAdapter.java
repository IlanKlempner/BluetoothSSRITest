package com.example.ilank.bluetoothssritest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Struct;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlueToothReadAdapter extends RecyclerView.Adapter<BlueToothReadAdapter.MyViewHolder> {

    private ArrayList<BluetoothRead> data;

    public BlueToothReadAdapter() {
        data = new ArrayList<>();
    }

    public BlueToothReadAdapter(ArrayList<BluetoothRead> bluetoothReads) {
        data = bluetoothReads;
    }

    public void addReading(BluetoothRead bluetoothRead) {
        Log.d("TAG", "addReading: " + bluetoothRead.getAddress() + " " + bluetoothRead.getRssi());
        int i;
        for (i = 0; i < data.size(); i++) {
            BluetoothRead originalRead = data.get(i);
            if (bluetoothRead.getAddress().equals(data.get(i).getAddress())) {
                originalRead.setNumOfReads(originalRead.getNumOfReads() + 1);
                originalRead.setTotalRssi(originalRead.getTotalRssi() + bluetoothRead.getRssi());
                originalRead.setRssi(originalRead.getTotalRssi() / originalRead.getNumOfReads());
                data.set(i,originalRead);
                notifyDataSetChanged();
                return;
            }
        }
        data.add(bluetoothRead);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.deviceTV.setText(data.get(position).getAddress());
        holder.rssiTV.setText(data.get(position).getRssi()+"");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView deviceTV;
        private TextView rssiTV;

        MyViewHolder(View itemView) {
            super(itemView);
            deviceTV = itemView.findViewById(R.id.text_view_device);
            rssiTV = itemView.findViewById(R.id.text_view_rssi);
        }
    }
}
