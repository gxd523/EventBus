package com.eventbus;

import com.eventbus.impl.Observable;
import com.eventbus.impl.Subscriber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每种事件对应一个被观察者，每个被观察者对应多个观察者
 */
public enum EventBus {
    INSTANCE;
    private Map<String, Observable> observableConcurrentMap = new ConcurrentHashMap<>();

    public static <T> Subscriber<T> register(Class<T> clazz) {
        return EventBus.INSTANCE.registerObserver(clazz);
    }

    public static <T> void unregister(Class<T> clazz, Subscriber<T> subscriber) {
        EventBus.INSTANCE.unregisterObserver(clazz, subscriber);
    }

    public static <T> void post(T t) {
        EventBus.INSTANCE.postEvent(t);
    }

    private <T> Subscriber<T> registerObserver(Class<T> clazz) {
        String className = clazz.getName();
        Observable<T> observable = observableConcurrentMap.get(className);
        if (observable == null) {
            observable = new Observable<>();
            observableConcurrentMap.put(className, observable);
        }

        Subscriber<T> subscriber = new Subscriber<>();
        observable.addObserver(subscriber);
        return subscriber;
    }

    private <T> void unregisterObserver(Class<T> clazz, Subscriber<T> subscriber) {
        String className = clazz.getName();
        Observable<T> observable = observableConcurrentMap.get(className);
        if (observable != null) {
            observable.removeObserver(subscriber);
            if (observable.getObserverSize() == 0) {
                observableConcurrentMap.remove(className);
            }
        }
    }

    private <T> void postEvent(T t) {
        String className = t.getClass().getName();
        Observable<T> observable = observableConcurrentMap.get(className);
        if (observable != null) {
            observable.notifyAllObservers(t);
        }
    }
}
