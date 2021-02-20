package com.eventbus.sample;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import com.eventbus.EventBus;
import com.eventbus.core.Callback;
import com.eventbus.impl.Subscriber;
import com.eventbus.livedata.LiveDataBus;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class MainActivity extends LifecycleActivity {
    private Subscriber<Event> subscriber;
    private MutableLiveData<Event> liveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscriber = EventBus.register(Event.class);
        subscriber.subscribe(new Callback<Event>() {
            @Override
            public void call(Event event) {
                Log.d("gxd", "EventBus-->" + event.getMsg());
            }
        });

        liveData = LiveDataBus.INSTANCE.with(Event.class);
        liveData.observe(this, new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                Log.d("gxd", "LiveDataBus-->" + event.getMsg());
            }
        });
    }

    public void onBtnClick(View view) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                EventBus.post(new Event("a EventBus message"));
                liveData.postValue(new Event("a LiveData message"));
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        EventBus.unregister(Event.class, subscriber);
        super.onDestroy();
    }
}
