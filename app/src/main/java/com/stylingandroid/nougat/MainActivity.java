package com.stylingandroid.nougat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.stylingandroid.nougat.messenger.ServiceScheduler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServiceScheduler serviceScheduler = ServiceScheduler.newInstance(this);
        if (serviceScheduler.isEnabled()) {
            serviceScheduler.startService();
        }
    }
}
