package com.example.cpen321tutorial1;

import android.app.Application;
import android.util.Log;


import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GlobalClass extends Application {

    public static Account myAccount = new Account();
    public static ArrayList<Event> MyeventsList = new ArrayList<>();
    public final static OkHttpClient client = new OkHttpClient.Builder().hostnameVerifier((hostname, session) -> true).build();
    //public final static OkHttpClient client = new OkHttpClient();

    public static Manager manager = new Manager();

    public static AccountModelFromBackend accountFromBackend;



    }







