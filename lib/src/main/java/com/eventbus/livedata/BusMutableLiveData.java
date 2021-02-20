package com.eventbus.livedata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class BusMutableLiveData<T> extends MutableLiveData<T> {
    private final Map<Observer<? super T>, Observer<? super T>> observerMap = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
        try {
            hook(observer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (!observerMap.containsKey(observer)) {
            observerMap.put(observer, new BusObserverWrapper<>(observer));
        }
        super.observeForever(observerMap.get(observer));
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Observer<? super T> realObserver;
        if (observerMap.containsKey(observer)) {
            realObserver = observerMap.remove(observer);
        } else {
            realObserver = observer;
        }
        super.removeObserver(realObserver);
    }

    /**
     * 把LifecycleBoundObserver继承的ObserverWrapper里的mLastVersion的值设为LiveData的mVersion
     */
    private void hook(@NonNull Observer<? super T> observer) throws Exception {
        Field mObserversField = LiveData.class.getDeclaredField("mObservers");
        mObserversField.setAccessible(true);
        Object mObservers = mObserversField.get(this);

        if (mObservers == null) {
            throw new NullPointerException("Can not find mObservers!");
        }

        Method getMethod = mObservers.getClass().getDeclaredMethod("get", Object.class);
        getMethod.setAccessible(true);
        // 拿到Entry<Observer, ObserverWrapper>
        Object mapEntry = getMethod.invoke(mObservers, observer);

        Object lifecycleBoundObserver = null;
        if (mapEntry instanceof Map.Entry) {
            lifecycleBoundObserver = ((Map.Entry) mapEntry).getValue();
        }
        if (lifecycleBoundObserver == null) {
            throw new NullPointerException("ObserverWrapper can not be bull!");
        }

        Class<?> ObserverWrapperClass = lifecycleBoundObserver.getClass().getSuperclass();
        Field mLastVersionField = ObserverWrapperClass.getDeclaredField("mLastVersion");
        mLastVersionField.setAccessible(true);

        Field mVersionField = LiveData.class.getDeclaredField("mVersion");
        mVersionField.setAccessible(true);
        Object mVersion = mVersionField.get(this);

        mLastVersionField.set(lifecycleBoundObserver, mVersion);
    }
}