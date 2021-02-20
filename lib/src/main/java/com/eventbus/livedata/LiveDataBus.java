package com.eventbus.livedata;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;

public enum LiveDataBus {
    INSTANCE;
    private final Map<String, BusMutableLiveData<Object>> bus;

    LiveDataBus() {
        this.bus = new HashMap<>();
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(key);
    }

    public <T> MutableLiveData<T> with(Class<T> type) {
        return with(type.getCanonicalName(), type);
    }
}