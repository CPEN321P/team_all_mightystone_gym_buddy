package com.example.cpen321tutorial1;

import android.app.Application;

import com.google.android.gms.common.api.Api;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;

public class GlobalClass extends Application {

    public static Account myAccount = new Account();
    public final static OkHttpClient client = new OkHttpClient();

}
