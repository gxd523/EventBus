package com.eventbus.core;

/**
 * 被观察者
 */
public interface Subject<T> {
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void removeAll();

    void notifyAllObservers(T t);

    void notify(Observer<T> observer, T t);

    int getObserverSize();
}
