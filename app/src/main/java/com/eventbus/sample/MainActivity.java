package com.eventbus.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eventbus.EventBus;
import com.eventbus.core.Callback;
import com.eventbus.impl.Subscriber;

public class MainActivity extends Activity {
    private Subscriber<String> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subscriber = EventBus.get().register(String.class);
        subscriber.subscribe(new Callback<String>() {
            @Override
            public void call(String s) {
                Log.d("gxd", "MainActivity.call-->" + s);
            }
        });
    }

    public void onBtnClick(View view) {
        EventBus.get().post("a message");
    }

    @Override
    protected void onDestroy() {
        EventBus.get().unregister(String.class, subscriber);
        super.onDestroy();
    }
}
