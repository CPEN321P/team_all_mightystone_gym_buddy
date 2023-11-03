package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.JsonFunctions.NumToLocalDate;
import static com.example.cpen321tutorial1.JsonFunctions.NumToLocalTime;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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

    public void subscribeToGym(String gymId) {

    }

    //SCHEDULE FUNCTIONS!!!!

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

    //ACCOUNT FUNCTIONS!!!

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
                    //Log.d("THIS IS WHAT YOURE LOOKING FOR", accountModelFromBackend.getEmail());
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

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

    public Account setAccountInformationFromBackend(boolean getFriendsAndBlocked, AccountModelFromBackend accountModel){
        Account returnedAccount = new Account(accountModel.getName(),accountModel.getEmail(),accountModel.getAge().intValue(), accountModel.getWeight().intValue(), accountModel.getGender(),"User", new ArrayList<>(), new ArrayList<>());
        returnedAccount.setUserId(accountModel.getId());
        return returnedAccount;

    }

    public ArrayList<Account> getAllFriends(final String userId) {

        ArrayList<Account> listOfAllAccounts = new ArrayList<>();
        Callable<ArrayList<Account>> asyncCall = new Callable<ArrayList<Account>>() {
            @Override
            public ArrayList<Account> call() throws Exception {
                Request getFriendsProfiles = new Request.Builder()
                        .url("https://20.172.9.70/users/userId/"+ userId+ "/friends")
                        .build();

                Response response = client.newCall(getFriendsProfiles).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType = new TypeToken<ArrayList<AccountModelFromBackend>>(){}.getType();
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    List<AccountModelFromBackend> listOfFriends = new Gson().fromJson(jsonResponse, listType);
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", "FRIENDS GOTTTTT");


                    if (listOfFriends == null) {
                        throw new IOException("Gym model is null");
                    }

                    for(int i = 0; i<listOfFriends.size(); i++){
                        listOfAllAccounts.add(setAccountInformationFromBackend(false, listOfFriends.get(i)));

                    }

                    return listOfAllAccounts;

                }
            }
        };

        Future<ArrayList<Account>> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }




    }

    public ArrayList<Account> getRecommendedUsers(final String userId) {

        ArrayList<Account> listOfAllAccounts = new ArrayList<>();
        Callable<ArrayList<Account>> asyncCall = new Callable<ArrayList<Account>>() {
            @Override
            public ArrayList<Account> call() throws Exception {
                Request getRecommendedProfiles = new Request.Builder()
                        .url("https://20.172.9.70/users/userId/"+ userId+ "/recommendedUsers")
                        .build();

                Response response = client.newCall(getRecommendedProfiles).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType = new TypeToken<ArrayList<AccountModelFromBackend>>(){}.getType();
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    List<AccountModelFromBackend> listOfRecommended = new Gson().fromJson(jsonResponse, listType);
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", "GYMS GOTTTTT");


                    if (listOfRecommended == null) {
                        throw new IOException("Gym model is null");
                    }

                    for(int i = 0; i<listOfRecommended.size(); i++){
                        listOfAllAccounts.add(setAccountInformationFromBackend(false, listOfRecommended.get(i)));

                    }

                    return listOfAllAccounts;

                }
            }
        };

        Future<ArrayList<Account>> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }


    }




    //GYM FUNCTIONS!!!!

    public ArrayList<Gym> getAllGyms() {

        ArrayList<Gym> listOfAllGyms = new ArrayList<>();
        Callable<ArrayList<Gym>> asyncCall = new Callable<ArrayList<Gym>>() {
            @Override
            public ArrayList<Gym> call() throws Exception {
                Request getGymInformation = new Request.Builder()
                        .url("https://20.172.9.70/gyms")
                        .build();

                Response response = client.newCall(getGymInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType = new TypeToken<ArrayList<GymModelFromBackend>>(){}.getType();
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    List<GymModelFromBackend> listOfGymModels = new Gson().fromJson(jsonResponse, listType);
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", "GYMS GOTTTTT");


                    if (listOfGymModels == null) {
                        throw new IOException("Gym model is null");
                    }

                    Log.d("THIS IS WHAT YOURE LOOKING FOR", listOfGymModels.size()+"");
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", listOfGymModels.get(0).getName());


                    for(int i = 0; i<listOfGymModels.size(); i++){
                        listOfAllGyms.add(setGymInformationFromBackend(listOfGymModels.get(i)));

                    }

                    return listOfAllGyms;

                }
            }
        };

        Future<ArrayList<Gym>> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }




    }

    private Gym setGymInformationFromBackend(GymModelFromBackend gymModelFromBackend) {
        Gym returnedGym = new Gym();


        returnedGym.setName(gymModelFromBackend.getName());
        returnedGym.setAddress(gymModelFromBackend.getLocation());
        //returnedGym.setImage(R.drawable.gym);

        //FOR NOW WE ARE ADDING THE IMAGE SINCE THAT FIELD DOESNT EXIST ON THE BACKEND

        return returnedGym;
    }

    public void shutdown() {
        executorService.shutdown();
    }



}
