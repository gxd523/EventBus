package com.eventbus.impl;

import com.eventbus.core.Callback;
import com.eventbus.core.Observer;
import com.eventbus.core.Subject;

/**
 * Subscriber、Callback又可以看做一对被观察者、观察者
 */
public class Subscriber<T> implements Observer<T> {
    private Callback<T> callback;

    @Override
    public void update(Subject<T> subject, T t) {
        callback.call(t);
    }

    @Override
    public void subscribe(Callback<T> callback) {
        this.callback = callback;
    }
}
