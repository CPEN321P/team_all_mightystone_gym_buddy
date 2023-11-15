package com.example.cpen321tutorial1;

import android.app.Application;


import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class GlobalClass extends Application {

    public static Account myAccount = new Account();

    public static Gym myGym = new Gym();

    public static ArrayList<Event> MyeventsList = new ArrayList<>();

    public final static OkHttpClient client =
            new OkHttpClient.Builder().
                    hostnameVerifier((hostname, session) -> true).build();
    //public final static OkHttpClient client = new OkHttpClient();

    public static Manager manager = new Manager();

    public static AccountModelFromBackend accountFromBackend;



    }







