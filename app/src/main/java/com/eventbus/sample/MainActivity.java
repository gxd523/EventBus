package com.eventbus.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eventbus.EventBus;
import com.eventbus.core.Callback;
import com.eventbus.impl.Subscriber;

public class MainActivity extends Activity {
    private Subscriber<Event> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscriber = EventBus.register(Event.class);
        subscriber.subscribe(new Callback<Event>() {
            @Override
            public void call(Event event) {
                Log.d("gxd", "MainActivity.call-->" + event.getMsg());
            }
        });
    }

    public void onBtnClick(View view) {
        EventBus.post("a message");
    }

    @Override
    protected void onDestroy() {
        EventBus.unregister(Event.class, subscriber);
        super.onDestroy();
    }
}
