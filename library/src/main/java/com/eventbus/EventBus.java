package com.eventbus;

import com.eventbus.impl.Observable;
import com.eventbus.impl.Subscriber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventBus {
    private ConcurrentMap<Class, Observable> observableConcurrentMap = new ConcurrentHashMap<>();

    private EventBus() {
    }

    public static EventBus get() {
        return Holder.INSTANCE;
    }

    public <T> Subscriber<T> register(Class<T> clazz) {
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable == null) {
            observable = new Observable<>();
            observableConcurrentMap.put(clazz, observable);
        }

        Subscriber<T> subscriber = new Subscriber<>();
        observable.addObserver(subscriber);
        return subscriber;
    }

    public <T> void unregister(Class<T> clazz, Subscriber<T> subscriber) {
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable != null) {
            observable.removeObserver(subscriber);
            if (observable.getObserverSize() == 0) {
                observableConcurrentMap.remove(clazz);
            }
        }
    }

    public <T> void post(T t) {
        Class clazz = t.getClass();
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable != null) {
            observable.notifyAllObservers(t);
        }
    }

    public int getObservableSize() {
        return observableConcurrentMap.size();
    }

    public int getObserverSize(Class clazz) {
        Observable observable = observableConcurrentMap.get(clazz);
        return observable != null ? observable.getObserverSize() : 0;
    }

    private static class Holder {
        private static final EventBus INSTANCE = new EventBus();
    }
}
