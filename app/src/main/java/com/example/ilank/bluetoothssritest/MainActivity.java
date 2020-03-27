package com.example.ilank.bluetoothssritest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BlueToothReadAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int ACCESS_CODE_COARSE_LOCATION = 1;
        final int ACCESS_CODE_FINE_LOCATION = 2;

        request_bluetooth_permissions(Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_CODE_COARSE_LOCATION);
        request_bluetooth_permissions(Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_CODE_FINE_LOCATION);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_devices);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BlueToothReadAdapter();
        recyclerView.setAdapter(mAdapter);

        BluetoothAdapter BA;
        BA = BluetoothAdapter.getDefaultAdapter();
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);


        BA.getBluetoothLeScanner().startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);

                BluetoothRead bluetoothRead = new BluetoothRead(result.getRssi(), result.getDevice().getAddress());
                mAdapter.addReading(bluetoothRead);

            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);

                for (ScanResult result : results){
                    BluetoothRead bluetoothRead = new BluetoothRead(result.getRssi(), result.getDevice().getAddress());
                    String name = result.getDevice().getName();
                    if (name == null){
                        name = result.getScanRecord().getDeviceName();
                    }
                    mAdapter.addReading(bluetoothRead);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        });

    }

    private void request_bluetooth_permissions(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(getApplicationContext(), "You should grant us location permissions in order to locate bluetooth devices!", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},requestCode);
            }
        }
    }

    final static public class BleUtil {
        private final static String TAG=BleUtil.class.getSimpleName();
        public static BleAdvertisedData parseAdertisedData(byte[] advertisedData) {
            List<UUID> uuids = new ArrayList<UUID>();
            String name = null;
            if( advertisedData == null ){
                return new BleAdvertisedData(uuids, name);
            }

            ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
            while (buffer.remaining() > 2) {
                byte length = buffer.get();
                if (length == 0) break;

                byte type = buffer.get();
                switch (type) {
                    case 0x02: // Partial list of 16-bit UUIDs
                    case 0x03: // Complete list of 16-bit UUIDs
                        while (length >= 2) {
                            uuids.add(UUID.fromString(String.format(
                                    "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                            length -= 2;
                        }
                        break;
                    case 0x06: // Partial list of 128-bit UUIDs
                    case 0x07: // Complete list of 128-bit UUIDs
                        while (length >= 16) {
                            long lsb = buffer.getLong();
                            long msb = buffer.getLong();
                            uuids.add(new UUID(msb, lsb));
                            length -= 16;
                        }
                        break;
                    case 0x09:
                        byte[] nameBytes = new byte[length-1];
                        buffer.get(nameBytes);
                        name = new String(nameBytes, StandardCharsets.UTF_8);
                        break;
                    default:
                        buffer.position(buffer.position() + length - 1);
                        break;
                }
            }
            return new BleAdvertisedData(uuids, name);
        }
    }


    public static class BleAdvertisedData {
        private List<UUID> mUuids;
        private String mName;
        public BleAdvertisedData(List<UUID> uuids, String name){
            mUuids = uuids;
            mName = name;
        }

        public List<UUID> getUuids(){
            return mUuids;
        }

        public String getName(){
            return mName;
        }
    }
}
