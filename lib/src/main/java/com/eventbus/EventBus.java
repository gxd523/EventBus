package com.eventbus;

import com.eventbus.impl.Observable;
import com.eventbus.impl.Subscriber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EventBus {
    private ConcurrentMap<Class, Observable> observableConcurrentMap = new ConcurrentHashMap<>();

    private EventBus() {
    }

    public static <T> Subscriber<T> register(Class<T> clazz) {
        return EventBus.get().registerObserver(clazz);
    }

    public static <T> void unregister(Class<T> clazz, Subscriber<T> subscriber) {
        EventBus.get().unregisterObserver(clazz, subscriber);
    }

    public static <T> void post(T t) {
        EventBus.get().postEvent(t);
    }

    private static EventBus get() {
        return Holder.INSTANCE;
    }

    private <T> Subscriber<T> registerObserver(Class<T> clazz) {
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable == null) {
            observable = new Observable<>();
            observableConcurrentMap.put(clazz, observable);
        }

        Subscriber<T> subscriber = new Subscriber<>();
        observable.addObserver(subscriber);
        return subscriber;
    }

    private <T> void unregisterObserver(Class<T> clazz, Subscriber<T> subscriber) {
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable != null) {
            observable.removeObserver(subscriber);
            if (observable.getObserverSize() == 0) {
                observableConcurrentMap.remove(clazz);
            }
        }
    }

    private <T> void postEvent(T t) {
        Class clazz = t.getClass();
        Observable<T> observable = observableConcurrentMap.get(clazz);
        if (observable != null) {
            observable.notifyAllObservers(t);
        }
    }

    private static class Holder {
        private static final EventBus INSTANCE = new EventBus();
    }
}
