package com.eventbus.impl;

import com.eventbus.core.Observer;
import com.eventbus.core.Subject;

import java.util.HashSet;
import java.util.Set;

public class Observable<T> implements Subject<T> {
    private final Set<Observer<T>> observerSet = new HashSet<>();

    @Override
    public void addObserver(Observer<T> observer) {
        observerSet.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observerSet.remove(observer);
    }

    @Override
    public void removeAll() {
        observerSet.clear();
    }

    @Override
    public void notifyAllObservers(T t) {
        for (Observer<T> observer : observerSet) {
            observer.update(this, t);
        }
    }

    @Override
    public void notify(Observer<T> observer, T t) {
        observer.update(this, t);
    }

    @Override
    public int getObserverSize() {
        return observerSet.size();
    }
}
