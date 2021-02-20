package com.eventbus.livedata;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

/**
 * 将Observer再包裹一层
 */
public class BusObserverWrapper<T> implements Observer<T> {
    private final Observer<T> observer;

    public BusObserverWrapper(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void onChanged(@Nullable T t) {
        if (observer != null) {
            if (isCallOnObserve()) {
                return;
            }
            observer.onChanged(t);
        }
    }

    private boolean isCallOnObserve() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 0) {
            for (StackTraceElement element : stackTrace) {
                if (LiveData.class.getName().equals(element.getClassName())
                        && "observeForever".equals(element.getMethodName())) {
                    return true;
                }
            }
        }
        return false;
    }
}