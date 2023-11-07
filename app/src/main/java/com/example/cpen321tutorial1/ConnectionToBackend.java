package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
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

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ConnectionToBackend {
    private AccountModelFromBackend accountModelFromBackend;
    public Account accountFromBackend;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    final static String TAG = "ConnectionToBackend";

    private Account account;

    public void subscribeToGym(String gymId) {

    }

    //SCHEDULE FUNCTIONS!!!!

    public ArrayList<Event> getScheduleByUser (final String UserId){
        Callable<ArrayList<Event>> asyncCall = new Callable<ArrayList<Event>>() {
            @Override
            public ArrayList<Event> call() throws Exception {

                Request getEventInformation = new Request.Builder()
                        .url("https://20.172.9.70/schedules/byUser/" + UserId)
                        .build();

                Response response = client.newCall(getEventInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ScheduleModelFromBackend[] scheduleModelFromBackend = new Gson().fromJson(jsonResponse, ScheduleModelFromBackend[].class);

                    if (scheduleModelFromBackend == null) {
                        throw new IOException("Schedule model is null");
                    }


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

    public ArrayList<Event> getScheduleByUser (final String UserId, final LocalDate date){
        Callable<ArrayList<Event>> asyncCall = new Callable<ArrayList<Event>>() {
            @Override
            public ArrayList<Event> call() throws Exception {
                String JsonDate = JsonFunctions.DateToStringNum(date);


                Request getEventInformation = new Request.Builder()
                        .url("https://20.172.9.70/schedules/byUser/" + UserId + "/" + JsonDate)
                        .build();

                Response response = client.newCall(getEventInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ScheduleModelFromBackend SingleScheduleModelFromBackend = new Gson().fromJson(jsonResponse, ScheduleModelFromBackend.class);

                    if (SingleScheduleModelFromBackend == null) {
                        throw new IOException("Single Schedule model is null");
                    }

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
        //Log.d(TAG, Integer.toString(scheduleModel.length));
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

    public Account getAccountInformation(String userIdOrEmail) {

        Callable<Account> asyncCall = new Callable<Account>() {
            @Override
            public Account call() throws Exception {
                Request getAccountInformation;

                if(userIdOrEmail.contains("@")) {
                    getAccountInformation = new Request.Builder()
                            .url("https://20.172.9.70/users/userEmail/" + userIdOrEmail)
                            .build();

                } else {
                    getAccountInformation= new Request.Builder()
                            .url("https://20.172.9.70/users/userId/" + userIdOrEmail)
                            .build();
                }

                Response response = client.newCall(getAccountInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    AccountModelFromBackend accountModelFromBackend = new Gson().fromJson(jsonResponse, AccountModelFromBackend.class);
                    //Log.d("THIS IS WHAT YOURE LOOKING FOR", accountModelFromBackend.getEmail());
                    //Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    if (accountModelFromBackend == null) {
                        throw new IOException("Account model is null");
                    }

                    return setAccountInformationFromBackend(accountModelFromBackend);
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

    public Account setAccountInformationFromBackend(AccountModelFromBackend accountModel){
        Account returnedAccount = new Account(accountModel.getName(),accountModel.getEmail(),accountModel.getAge().intValue(), accountModel.getWeight().intValue(), accountModel.getGender(),new ArrayList<>(), new ArrayList<>());
        returnedAccount.setUserId(accountModel.getId());
        return returnedAccount;

    }

    public ArrayList<Account> getAllInList(final String userId, int friendsOrBlocked) {

        ArrayList<Account> listOfAllAccounts = new ArrayList<>();
        Callable<ArrayList<Account>> asyncCall = new Callable<ArrayList<Account>>() {
            @Override
            public ArrayList<Account> call() throws Exception {
                Request getProfiles;

                //getting friends
                if(friendsOrBlocked == 0) {
                    getProfiles = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/"+ userId+ "/friends")
                            .build();
                }
                //getting blocked
                else{
                    getProfiles = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/"+ userId+ "/blockedUsers")
                            .build();
                }


                Response response = client.newCall(getProfiles).execute();

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
                        listOfAllAccounts.add(setAccountInformationFromBackend(listOfFriends.get(i)));

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
                        listOfAllAccounts.add(setAccountInformationFromBackend(listOfRecommended.get(i)));

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


    //CHAT FUNCTIONS!!!

    public ArrayList<Chat> getAllChatsFromUserId(final String userId) {

        ArrayList<Chat> listOfAllChats = new ArrayList<>();
        Callable<ArrayList<Chat>> asyncCall = new Callable<ArrayList<Chat>>() {
            @Override
            public ArrayList<Chat> call() throws Exception {
                Request getRecommendedProfiles = new Request.Builder()
                        .url("https://20.172.9.70/chat/userId/"+ userId)
                        .build();

                Response response = client.newCall(getRecommendedProfiles).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType = new TypeToken<ArrayList<ChatModelFromBackend>>(){}.getType();
                    //Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    List<ChatModelFromBackend> listOfChats = new Gson().fromJson(jsonResponse, listType);
                    //Log.d("THIS IS WHAT YOURE LOOKING FOR", "GYMS GOTTTTT");


                    if (listOfChats == null) {
                        throw new IOException("Gym model is null");
                    }

                    for(int i = 0; i<listOfChats.size(); i++){
                        listOfAllChats.add(setChatInformationFromBackend(listOfChats.get(i)));

                    }

                    return listOfAllChats;

                }
            }
        };

        Future<ArrayList<Chat>> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }

    }

    private Chat setChatInformationFromBackend(ChatModelFromBackend chatModelFromBackend) {
        Chat returnedChat = new Chat();

        Account otherAccount;

        if(chatModelFromBackend.members.get(0) == myAccount.getUserId()){
            otherAccount = getAccountInformation(chatModelFromBackend.members.get(1));
        } else {
            otherAccount = getAccountInformation(chatModelFromBackend.members.get(0));
        }
        returnedChat.setOtherAccount(otherAccount);
        returnedChat.setChatId(chatModelFromBackend.get_id());
        returnedChat.setChatModelFromBackend(chatModelFromBackend);

        return returnedChat;

    }




    //MANAGER FUNCTIONS!!!

    public Manager getManagerInformationFromEmail(String email) {
        //Log.d("THIS IS WHAT YOURE LOOKING FOR", "PLS");

        Callable<Manager> asyncCall = new Callable<Manager>() {

            @Override
            public Manager call() throws Exception {

                //Log.d("THIS IS WHAT YOURE LOOKING FOR", email);

                Request getManagerInformation = new Request.Builder()
                        .url("https://20.172.9.70/gymsUsers/userEmail/" + email)
                        .build();

                Response response = client.newCall(getManagerInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ManagerModelFromBackend managerModelFromBackend = new Gson().fromJson(jsonResponse, ManagerModelFromBackend.class);
                    Log.d("THIS IS WHAT YOURE LOOKING FOR", jsonResponse);

                    if (managerModelFromBackend == null) {

                        throw new IOException("Account model is null");
                    }

                    return setManagerFromBackend(managerModelFromBackend);
                }
            }
        };

        Future<Manager> future = executorService.submit(asyncCall);

        try {
            return future.get(); // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }




    }

    private Manager setManagerFromBackend(ManagerModelFromBackend managerModelFromBackend) {
        Manager returnedManager = new Manager();

        returnedManager.set_id(managerModelFromBackend.get_id());
        returnedManager.setName(managerModelFromBackend.getName());
        returnedManager.setUsername(managerModelFromBackend.getUsername());
        returnedManager.setEmail(managerModelFromBackend.getEmail());

        return returnedManager;
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
