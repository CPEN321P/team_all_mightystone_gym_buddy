package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConnectionToBackend {
    private static AccountModelFromBackend accountModelFromBackend;
    private static Account accountFromBackend;

//    public static Account getAccountInformationFromId(String id) {
//
//        Request getAccountInformation = new Request.Builder()
//                .url("http://20.172.9.70:8081/users/userId/" + id)
//                .build();
//
//        client.newCall(getAccountInformation).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                try (ResponseBody responseBody = response.body()) {
//                    if (!response.isSuccessful())
//                        throw new IOException("Unexpected code " + response);
//
//                    accountModelFromBackend = new Gson().fromJson(response.body().string(), AccountModelFromBackend.class);
//                    accountFromBackend = setAccountInformationFromBackend(false, accountModelFromBackend);
//                }
//            }
//        });
//        return accountFromBackend;
//    }

    public static Account getAccountInformationFromEmail(String email) {


        Request getAccountInformation = new Request.Builder()
                .url("http://20.172.9.70:8081/users/userEmail/" + email)
                .build();

        client.newCall(getAccountInformation).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    Log.d("HURRAY!!!!", "REACHED GET");
                    accountModelFromBackend = new Gson().fromJson(response.body().string(), AccountModelFromBackend.class);
                    if(accountModelFromBackend == null)
                        Log.d("THIS IS WHAT YOURE LOOKING FOR", "ACCOUNT MODEL IS ISSUE");
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", accountModelFromBackend.getId());

                    accountFromBackend = setAccountInformationFromBackend(false, accountModelFromBackend);
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", accountFromBackend.getGender());

                }
            }
        });
        if(accountFromBackend== null)
            Log.d("THIS IS WHAT YOURE LOOKING FOR","account is null");
        else
        Log.d("THIS IS WHAT YOURE LOOKING FOR", "account is not null!!!");


        return accountFromBackend;
    }

    public static Account setAccountInformationFromBackend(boolean getFriendsAndBlocked, AccountModelFromBackend accountModel){
        Account returnedAccount = new Account(accountModel.getName(),accountModel.getEmail(),accountModel.getAge().intValue(), accountModel.getWeight().intValue(), accountModel.getGender(),"User", new ArrayList<>(), new ArrayList<>());
//        account.setUsername(accountModelFromBackend.getName());
//        account.setEmailAddress(accountModelFromBackend.getEmail());
//        account.setAge(accountModelFromBackend.getAge().intValue());
//        account.setWeight(accountModelFromBackend.getWeight().intValue());
//        account.setRole("User");
//        account.setGender(accountModelFromBackend.getGender());

//        if(getFriendsAndBlocked){
//            for(int i = 0; i < accountModelFromBackend.getFriends().size(); i++) {
//                Account friendAccount = setAccountInformationFromBackend(false, getAccountInformationFromId(accountModelFromBackend.getFriends().get(i)));
//                account.getFriendsList().add(friendAccount);
//            }
//        }
        Log.d("THIS IS WHAT YOURE", returnedAccount.getUsername());
        return returnedAccount;

    }


}
