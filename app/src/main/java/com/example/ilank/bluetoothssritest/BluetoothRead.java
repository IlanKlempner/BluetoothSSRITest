package com.example.ilank.bluetoothssritest;

import java.util.Objects;

public class BluetoothRead {
    private int rssi;
    private String address;
    private int numOfReads;
    private int totalRssi;
    private String displayName = null;

    public BluetoothRead() {
        rssi = 0;
        address = "00:00:00:00";
        numOfReads = 0;
        totalRssi = 0 ;
    }

    public BluetoothRead(int rssi, String address) {
        this.rssi = rssi;
        this.address = address;
        this.numOfReads = 0;
        totalRssi = 0;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getNumOfReads() {
        return numOfReads;
    }

    public int getTotalRssi() {
        return totalRssi;
    }

    public void setTotalRssi(int totalRssi) {
        this.totalRssi = totalRssi;
    }

    public void setNumOfReads(int numOfReads) {
        this.numOfReads = numOfReads;
    }

    public String getAddress() {
        return address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BluetoothRead that = (BluetoothRead) o;
        return rssi == that.rssi &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rssi, address);
    }
}
