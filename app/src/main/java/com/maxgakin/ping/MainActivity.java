package com.maxgakin.ping;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.maxgakin.ping.R;

public class MainActivity extends AppCompatActivity {
    private volatile boolean isRunning = true;
    private Thread pingThread;
    private final int checkTimeoutInSeconds = 5;
    ImageView okView;
    ImageView errorView;
    TextView tw;

    private void UpdateText(String text) {
        tw.post(() -> {
            tw.setText(text);
        });
    }

    private void ChangeStatusIcons(Boolean isInternetAvailable)
    {
        tw.post(() -> {
            okView.setVisibility(isInternetAvailable?View.VISIBLE:View.INVISIBLE);
            errorView.setVisibility(isInternetAvailable?View.INVISIBLE:View.VISIBLE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Ping","onResume() called");
        CreateAndStartPingThread();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("Ping","onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("Ping","onPause() called");
        StopPingThread();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw = findViewById(R.id.label);
        okView = findViewById(R.id.okStatusImage);
        errorView = findViewById(R.id.errorStatusImage);
    }

    private void StopPingThread() {
        isRunning = false;
        try {
            pingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void CreateAndStartPingThread() {
        isRunning = true;

        pingThread = new Thread(() -> {
            try  {
                while (isRunning) {
                    new InternetCheck(adapterAddress -> {
                        Boolean isAvailable = (!adapterAddress.isEmpty());
                        UpdateText("Is internet available? " + isAvailable );
                        ChangeStatusIcons(isAvailable);
                    });
                    for (int i=0; isRunning && i<checkTimeoutInSeconds*10; i++) {
                        Thread.sleep(100l);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pingThread.start();
    }
}
