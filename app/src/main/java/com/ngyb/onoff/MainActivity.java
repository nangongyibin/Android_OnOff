package com.ngyb.onoff;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ngyb.aswitch.OnOffView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnOffView oov = findViewById(R.id.oov);
        oov.setStateListener(new OnOffView.StateListener() {
            @Override
            public void currentState(boolean isOpen) {
                Log.e(TAG, "currentState: " + isOpen);
            }
        });
        oov.setStatus(true);
    }
}
