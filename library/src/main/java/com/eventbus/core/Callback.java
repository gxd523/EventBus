package com.eventbus.core;

public interface Callback<T> {
    void call(T t);
}