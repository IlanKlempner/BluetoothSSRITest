package com.example.ilank.bluetoothssritest;

import java.util.Hashtable;
import java.util.Objects;
import java.util.PriorityQueue;

public class AverageManager {

    private int windowSize;
    private Hashtable<String, PriorityQueue<Integer>> memory;

    AverageManager(int windowSize) {
        this.windowSize = windowSize;
        this.memory = new Hashtable<>();
    }

    int insert(String address, int value) {
        if (!memory.containsKey(address)) {
            memory.put(address, new PriorityQueue<Integer>());
        }

        PriorityQueue<Integer> currQueue = memory.get(address);
        try {
            assert currQueue != null;
            while (currQueue.size() >= windowSize) {
                currQueue.remove();
            }
        } catch (NullPointerException e) {
            return -1;
        }

        currQueue.add(value);
        return 0;
    }

    double getRunningAverage(String address) {
        double average = 0.0;
        for (Object object : Objects.requireNonNull(memory.get(address))) {
            average += (int) object;
        }
        return average / windowSize;
    }

}

