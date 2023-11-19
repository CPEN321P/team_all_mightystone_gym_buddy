package com.example.cpen321tutorial1;

import android.app.Application;


import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class GlobalClass extends Application {

    public static Account myAccount = new Account();

    public static ArrayList<Event> MyeventsList = new ArrayList<>();

    public final static OkHttpClient client =
            new OkHttpClient.Builder().
                    hostnameVerifier((hostname, session) -> true).build();
    //public final static OkHttpClient client = new OkHttpClient();

    public static Manager manager = new Manager();

    public static AccountModelFromBackend accountFromBackend;

    public static int TestTempPeopleList = 0;
    //1 for testing, 0 for actually running//

    public static ArrayList<Account> TempPeopleList = new ArrayList<>();

    }







