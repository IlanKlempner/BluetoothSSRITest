package com.example.ilank.bluetoothssritest;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BlueToothReadAdapter extends RecyclerView.Adapter<BlueToothReadAdapter.MyViewHolder> {

    private ArrayList<BluetoothRead> data;
    private static AverageManager averageManager;

    public BlueToothReadAdapter() {
        data = new ArrayList<>();
        averageManager = new AverageManager(5);
    }

    public BlueToothReadAdapter(ArrayList<BluetoothRead> bluetoothReads) {
        data = bluetoothReads;
        averageManager = new AverageManager(5);
    }

    public void addReading(BluetoothRead bluetoothRead) {
        Log.d("TAG", "addReading: " + bluetoothRead.getAddress() + " " + bluetoothRead.getRssi());
        int i;
        for (i = 0; i < data.size(); i++) {
            BluetoothRead originalRead = data.get(i);
            String address = bluetoothRead.getAddress();
            if (address.equals(data.get(i).getAddress())) {
                int retValue = averageManager.insert(address, bluetoothRead.getRssi());
                if(retValue == -1){
                    Log.e("ErrorTag", "Error in AverageManager.insert()");
                }
                originalRead.setRssi((int) averageManager.getRunningAverage(address));
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
