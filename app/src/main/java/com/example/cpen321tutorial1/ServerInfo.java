package com.example.cpen321tutorial1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

public class ServerInfo extends AppCompatActivity {

    TextView textView;

    private Calendar calendar;
    private String currentTime;

    private String ipAddress;
    private String TheResponseFinalTime;
    private String TheResponseFinalServerIP;
    private String TheResponseFinalName;

    private int TimeTest;
    private int IPTest;
    private int NameTest;

    final static String TAG = "ServerInfo";

    private String SERVER_ADDRESS = "http://20.151.69.123:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        textView = findViewById(R.id.textView);

        calendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        currentTime = timeFormat.format(calendar.getTime());

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (wifiManager.isWifiEnabled()) {
            int TheipAddress = wifiManager.getConnectionInfo().getIpAddress();
            ipAddress = formatIpAddress(TheipAddress);
        } else {
            ipAddress =  "Wi-Fi is not enabled";
        }

        GetTheServerTime();
        GetTheServerIP();
        GetTheServerName();

        while (TimeTest != 1 || IPTest != 1 || NameTest != 1)
        {
            Log.d(TAG, "Looping");
        }

        textView.setText(
                "Server IP Address: " + TheResponseFinalServerIP + "\n"
                        + "Client IP Address: " +  ipAddress + "\n" +"\n"
                        + "Server local time: " + TheResponseFinalTime + "\n"
                        + "Client local time: " + currentTime + "\n"
                        + "My Name: " + TheResponseFinalName + "\n"
                        + "Logged in: " + MainActivity.getStringName() + "\n"
        );

        Log.d(TAG, "1145141919810");
    }

    private String formatIpAddress(int ipAddress) {
        return ((ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF));
    }

    private static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private void GetTheServerTime(){
        OkHttpClient client = new OkHttpClient();
        String TheUrl = SERVER_ADDRESS + "/LocalTime";

        Request request = new Request.Builder()
                .url(TheUrl)
                .build();
        Log.d(TAG, "Time TEST");
        TimeTest = 0;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                TimeTest = 1;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String TheResponse = response.body().string();
                Log.d(TAG, TheResponse);
                GetResponseTime(TheResponse);
                TimeTest = 1;
            }
        });


    }

    private void GetTheServerIP(){
        OkHttpClient client = new OkHttpClient();
        String TheUrl = SERVER_ADDRESS + "/ServerIP";

        Request request = new Request.Builder()
                .url(TheUrl)
                .get()
                .build();
        Log.d(TAG, "IP TEST");
        IPTest = 0;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "IP Failed");
                e.printStackTrace();
                IPTest = 1;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String TheResponse = response.body().string();
                Log.d(TAG, TheResponse);
                GetResponseIP(TheResponse);
                IPTest = 1;
            }
        });
    }

    private void GetTheServerName(){
        OkHttpClient client = new OkHttpClient();
        String TheUrl = SERVER_ADDRESS + "/Name";

        Request request = new Request.Builder()
                .url(TheUrl)
                .get()
                .build();
        Log.d(TAG, "Name TEST");
        NameTest = 0;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "Name Failed");
                e.printStackTrace();
                NameTest = 1;
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String TheResponse = response.body().string();
                Log.d(TAG, TheResponse);
                GetResponseName(TheResponse);
                NameTest = 1;
            }
        });
    }

    private void GetResponseTime(String response){
        TheResponseFinalTime = response;
    }
    private void GetResponseName(String response){
        TheResponseFinalName = response;
    }
    private void GetResponseIP(String response){
        TheResponseFinalServerIP = response;
    }


}