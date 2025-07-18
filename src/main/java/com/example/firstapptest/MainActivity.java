package com.example.firstapptest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private Switch ledSwitch;
    private TextView textView;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ledSwitch = (Switch) findViewById(R.id.led_switch);
        textView = (TextView) findViewById(R.id.textView);
    }
    public void swichPressed(View view){
        boolean isChecked = ((Switch) view).isChecked();
        String url;
        if (isChecked) {
            url = "http://10.0.2.2:5000/led/on";
//            textView.setText("LED is ON");
        } else {
            url = "http://10.0.2.2:5000/led/off";
//            textView.setText("LED is OFF");
        }
        sendRequest(url);
    }

    private void sendRequest(String url) {
        if (client == null) {
            client = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // עדכון UI צריך להיעשות ב-UI thread
                Log.e("HTTP", "Request failed", e);
                runOnUiThread(() -> textView.setText("Request failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resp = response.body().string();

                runOnUiThread(() -> textView.setText("Server response: " + resp));
            }
        });
    }

}