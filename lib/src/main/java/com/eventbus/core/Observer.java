package com.eventbus.core;

public interface Observer<T> {
    void update(Subject<T> subject, T t);

    void subscribe(Callback<T> callback);
}
