package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.JsonFunctions.NumToLocalDate;
import static com.example.cpen321tutorial1.JsonFunctions.NumToLocalTime;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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

    private Account account;


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

    public void subscribeToGym(String gymId){

    }

    public ArrayList<Event> getScheduleByUser (final String UserId){
        Callable<ArrayList<Event>> asyncCall = new Callable<ArrayList<Event>>() {
            @Override
            public ArrayList<Event> call() throws Exception {

                Request getEventInformation = new Request.Builder()
                        .url("https://20.172.9.70/schedules/byId/" + UserId)
                        .build();

                Response response = client.newCall(getEventInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ScheduleModelFromBackend[] scheduleModelFromBackend = new Gson().fromJson(jsonResponse, ScheduleModelFromBackend[].class);

                    if (scheduleModelFromBackend == null) {
                        throw new IOException("Account model is null");
                    }

                    //return null;
                    return setEventInformationFromBackend(scheduleModelFromBackend);
                }
            }
        };

        Future<ArrayList<Event>> future = executorService.submit(asyncCall);
        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }
    }

    public ArrayList<Event> getScheduleByUserAndDate (final String UserId, final LocalDate date){
        Callable<ArrayList<Event>> asyncCall = new Callable<ArrayList<Event>>() {
            @Override
            public ArrayList<Event> call() throws Exception {

                Request getEventInformation = new Request.Builder()
                        .url("https://20.172.9.70/schedules/byUser/" + UserId + "/" + date)
                        .build();

                Response response = client.newCall(getEventInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ScheduleModelFromBackend SingleScheduleModelFromBackend = new Gson().fromJson(jsonResponse, ScheduleModelFromBackend.class);

                    if (SingleScheduleModelFromBackend == null) {
                        throw new IOException("Account model is null");
                    }

                    //return null;
                    return setSingleEventInformationFromBackend(SingleScheduleModelFromBackend);
                }
            }
        };

        Future<ArrayList<Event>> future = executorService.submit(asyncCall);
        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }
    }




    public Account getAccountInformationFromEmail(final String email) {
        Callable<Account> asyncCall = new Callable<Account>() {
            @Override
            public Account call() throws Exception {
                Request getAccountInformation = new Request.Builder()
                        .url("https://20.172.9.70/users/userEmail/" + email)
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
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static ArrayList<Event> setSingleEventInformationFromBackend(ScheduleModelFromBackend scheduleModel){

        String userId = scheduleModel.getUserId();
        ArrayList<Event> ReturnedEvent =  new ArrayList<>();

        long dateint = scheduleModel.getDate();
        LocalDate date = NumToLocalDate(dateint);

        List<EventModelFromBackend> TheExercises = scheduleModel.getExercises();
        for (int j = 0; j < TheExercises.size(); j++){
            String name = TheExercises.get(j).getName();
            LocalTime StartTime = NumToLocalTime(TheExercises.get(j).getTimeStart());
            LocalTime EndTime = NumToLocalTime(TheExercises.get(j).getTimeEnd());
            Event TheEvent = new Event(name, date, StartTime, EndTime);

            ReturnedEvent.add(TheEvent);
        }
        return ReturnedEvent;
    }

    public static ArrayList<Event> setEventInformationFromBackend(ScheduleModelFromBackend[] scheduleModel){

        String userId = scheduleModel[0].getUserId();
        ArrayList<Event> ReturnedEvent =  new ArrayList<>();
        for (int i = 0; i < scheduleModel.length; i++)
        {
            long dateint = scheduleModel[i].getDate();
            LocalDate date = NumToLocalDate(dateint);

            List<EventModelFromBackend> TheExercises = scheduleModel[i].getExercises();
            for (int j = 0; j < TheExercises.size(); j++){
                String name = TheExercises.get(j).getName();
                LocalTime StartTime = NumToLocalTime(TheExercises.get(j).getTimeStart());
                LocalTime EndTime = NumToLocalTime(TheExercises.get(j).getTimeEnd());
                Event TheEvent = new Event(name, date, StartTime, EndTime);

                ReturnedEvent.add(TheEvent);
            }
        }
        return ReturnedEvent;
    }

    public static Account setAccountInformationFromBackend(boolean getFriendsAndBlocked, AccountModelFromBackend accountModel){
        Account returnedAccount = new Account(accountModel.getName(),accountModel.getEmail(),accountModel.getAge().intValue(), accountModel.getWeight().intValue(), accountModel.getGender(),"User", new ArrayList<>(), new ArrayList<>());
//        account.setUsername(accountModelFromBackend.getName());
//        account.setEmailAddress(accountModelFromBackend.getEmail());
//        account.setAge(accountModelFromBackend.getAge().intValue());
//        account.setWeight(accountModelFromBackend.getWeight().intValue());
//        account.setRole("User");
//        account.setGender(accountModelFromBackend.getGender());
        returnedAccount.setUserId(accountModel.getId());

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
