package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConnectionToBackend {
    private AccountModelFromBackend accountModelFromBackend;
    public Account accountFromBackend;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();


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

    public Account getAccountInformationFromEmail(final String email) {
        Callable<Account> asyncCall = new Callable<Account>() {
            @Override
            public Account call() throws Exception {
                Request getAccountInformation = new Request.Builder()
                        .url("http://20.172.9.70:8081/users/userEmail/" + email)
                        .build();

                Response response = client.newCall(getAccountInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    AccountModelFromBackend accountModelFromBackend = new Gson().fromJson(jsonResponse, AccountModelFromBackend.class);

                    if (accountModelFromBackend == null) {
                        throw new IOException("Account model is null");
                    }

                    return setAccountInformationFromBackend(false, accountModelFromBackend);
                }
            }
        };

        Future<Account> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while fetching account information", e);
        }
    }

    public void shutdown() {
        executorService.shutdown();
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
