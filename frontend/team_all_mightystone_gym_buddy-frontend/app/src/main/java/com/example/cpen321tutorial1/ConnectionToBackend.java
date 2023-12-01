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

    //private AccountModelFromBackend accountModelFromBackend;

    //public Account accountFromBackend;

    //public Account account;

    private final ExecutorService executorService =
            Executors.newSingleThreadExecutor();

    final static String TAG = "ConnectionToBackend";

    /*
    public void subscribeToGym(String gymId) {

    }
     */

    //SCHEDULE FUNCTIONS!!!!

    public ArrayList<Event> getScheduleByUser (final String UserId){
        Callable<ArrayList<Event>> asyncCall =
                new Callable<ArrayList<Event>>() {
            @Override
            public ArrayList<Event> call() throws Exception {

                Request getEventInformation = new Request.Builder()
                        .url("https://20.172.9.70/schedules/byUser/" + UserId)
                        .build();

                Response response =
                        client.newCall(getEventInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ScheduleModelFromBackend[] scheduleModelFromBackend =
                            new Gson().fromJson(jsonResponse, ScheduleModelFromBackend[].class);

                    if (scheduleModelFromBackend == null) {
                        throw new IOException("Schedule model is null");
                    }


                    return setEventInformationFromBackend
                            (scheduleModelFromBackend);
                }
            }
        };

        Future<ArrayList<Event>> future = executorService.submit(asyncCall);
        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
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
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Event> setSingleEventInformationFromBackend
            (ScheduleModelFromBackend scheduleModel){
        
        ArrayList<Event> ReturnedEvent =  new ArrayList<>();

        long dateint = scheduleModel.getDate();
        LocalDate date = NumToLocalDate(dateint);

        List<EventModelFromBackend> TheExercises = scheduleModel.getExercises();

        for (int j = 0; j < TheExercises.size(); j++){
            String name = TheExercises.get(j).getName();

            LocalTime StartTime =
                    NumToLocalTime(TheExercises.get(j).getTimeStart());
            LocalTime EndTime =
                    NumToLocalTime(TheExercises.get(j).getTimeEnd());
            Event TheEvent = new Event(name, date, StartTime, EndTime);

            ReturnedEvent.add(TheEvent);
        }

        return ReturnedEvent;
    }

    public static ArrayList<Event> setEventInformationFromBackend
            (ScheduleModelFromBackend[] scheduleModel){
        ArrayList<Event> ReturnedEvent =  new ArrayList<>();
        for (int i = 0; i < scheduleModel.length; i++)
        {
            long dateint = scheduleModel[i].getDate();
            LocalDate date = NumToLocalDate(dateint);

            List<EventModelFromBackend> TheExercises =
                    scheduleModel[i].getExercises();
            for (int j = 0; j < TheExercises.size(); j++){
                String name = TheExercises.get(j).getName();

                LocalTime StartTime =
                        NumToLocalTime(TheExercises.get(j).getTimeStart());
                LocalTime EndTime =
                        NumToLocalTime(TheExercises.get(j).getTimeEnd());
                Event TheEvent =
                        new Event(name, date, StartTime, EndTime);

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

                Response response =
                        client.newCall(getAccountInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    AccountModelFromBackend accountModelFromBackend =
                            new Gson().fromJson(jsonResponse,
                                    AccountModelFromBackend.class);

                    if (accountModelFromBackend == null) {
                        throw new IOException("Account model is null");
                    }

                    return setAccountInformationFromBackend
                            (accountModelFromBackend);
                }
            }
        };

        Future<Account> future = executorService.submit(asyncCall);

        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Account setAccountInformationFromBackend
            (AccountModelFromBackend accountModel) throws IOException {

        Account returnedAccount =
                new Account(accountModel.getName(),
                accountModel.getEmail(),
                accountModel.getAge().intValue(),
                accountModel.getWeight().intValue(),
                accountModel.getGender(),new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());

        returnedAccount.setUserId(accountModel.getId());


        //getting gym
        if(accountModel.getHomeGym().isEmpty()) {
            returnedAccount.setMyGym(null);
            return returnedAccount;
        }

        Request getGymInformation = new Request.Builder()
                .url("https://20.172.9.70/gyms/gymId/" + accountModel.getHomeGym())
                .build();
        Response response =
                client.newCall(getGymInformation).execute();
        if(response.isSuccessful()){
        ResponseBody responseBody = response.body();
        String jsonResponse = responseBody.string();

        GymModelFromBackend gymModelFromBackend =
                new Gson().fromJson(jsonResponse, GymModelFromBackend.class);
        Gym gym = setGymInformationFromBackend(gymModelFromBackend);
        returnedAccount.setMyGym(gym);
        }
        return returnedAccount;

    }

    public ArrayList<Account> getAllInList(final String userId, int typeOfProfile) {

        ArrayList<Account> listOfAllAccounts = new ArrayList<>();
        Callable<ArrayList<Account>> asyncCall = new Callable<ArrayList<Account>>() {
            @Override
            public ArrayList<Account> call() throws Exception {
                Request getProfiles;

                //getting friends
                if(typeOfProfile == 0) {
                    getProfiles = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/"+ userId+ "/friends")
                            .build();
                }
                //getting blocked
                else if (typeOfProfile == 1){
                    getProfiles = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/"+ userId+ "/blockedUsers")
                            .build();
                } 
                //getting recommended users
                else {
                    getProfiles = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/"+
                                    userId+
                                    "/recommendedUsers")
                            .build();
                }


                Response response =
                        client.newCall(getProfiles).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType =
                            new TypeToken<ArrayList<AccountModelFromBackend>>(){}.getType();

                    List<AccountModelFromBackend> listOfFriends =
                            new Gson().fromJson(jsonResponse, listType);



                    if (listOfFriends == null) {
                        throw new IOException("List is null");
                    }

                    for(int i = 0; i<listOfFriends.size(); i++){
                        listOfAllAccounts.add
                                (setAccountInformationFromBackend(listOfFriends.get(i)));

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

    public ArrayList<ChatModelFromBackend> getAllChatsFromUserId(final String userId) {

        //ArrayList<ChatModelFromBackend> listOfAllChats = new ArrayList<>();
        Callable<ArrayList<ChatModelFromBackend>> asyncCall = new Callable<ArrayList<ChatModelFromBackend>>() {
            @Override
            public ArrayList<ChatModelFromBackend> call() throws Exception {
                Request getRecommendedProfiles = new Request.Builder()
                        .url("https://20.172.9.70/chat/allChats/"+ userId)
                        .build();

                Response response =
                        client.newCall(getRecommendedProfiles).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    Type listType = new TypeToken<ArrayList<ChatModelFromBackend>>(){}.getType();

                    ArrayList<ChatModelFromBackend> listOfChats =
                            new Gson().fromJson(jsonResponse, listType);


                    if (listOfChats == null) {
                        throw new IOException("Chat model is null");
                    }

                    return listOfChats;

                }
            }
        };

        Future<ArrayList<ChatModelFromBackend>> future = executorService.submit(asyncCall);

        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
            //throw new RuntimeException("Error while fetching account information", e);
        }

    }

    public ChatModelFromBackend getChatFromFriendId(String friendId) {

        Callable<ChatModelFromBackend> asyncCall = new Callable<ChatModelFromBackend>() {
            @Override
            public ChatModelFromBackend call() throws Exception {
                Request getChatInformation;


                getChatInformation = new Request.Builder()
                            .url("https://20.172.9.70/chat/userId/" + myAccount.getUserId() + "/" + friendId)
                            .build();


                Response response =
                        client.newCall(getChatInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ChatModelFromBackend chatModelFromBackend =
                            new Gson().fromJson(jsonResponse,
                                    ChatModelFromBackend.class);

                    if (chatModelFromBackend == null) {
                        throw new IOException("Chat model is null");
                    }

                    return chatModelFromBackend;
                }
            }
        };

        Future<ChatModelFromBackend> future = executorService.submit(asyncCall);

        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }


    }





    //MANAGER FUNCTIONS!!!

    public Manager getManagerInformationFromEmail(String email) {

        Callable<Manager> asyncCall = new Callable<Manager>() {

            @Override
            public Manager call() throws Exception {

                Request getManagerInformation = new Request.Builder()
                        .url("https://20.172.9.70/gymsUsers/userEmail/" + email)
                        .build();

                Response response =
                        client.newCall(getManagerInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();
                    ManagerModelFromBackend managerModelFromBackend =
                            new Gson().fromJson(jsonResponse, ManagerModelFromBackend.class);

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

    private Manager setManagerFromBackend
            (ManagerModelFromBackend managerModelFromBackend) {
        Manager returnedManager = new Manager();

        returnedManager.set_id(managerModelFromBackend.get_id());
        returnedManager.setName(managerModelFromBackend.getName());
        returnedManager.setUsername(managerModelFromBackend.getUsername());
        returnedManager.setEmail(managerModelFromBackend.getEmail());
        returnedManager.setGymId(managerModelFromBackend.getGymId());
        returnedManager.setAnnouncements(managerModelFromBackend.getAnnouncements());

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

                Response response =
                        client.newCall(getGymInformation).execute();

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }

                try (ResponseBody responseBody = response.body()) {
                    String jsonResponse = responseBody.string();

                    Type listType =
                            new TypeToken<ArrayList<GymModelFromBackend>>(){}.getType();


                    List<GymModelFromBackend> listOfGymModels =
                            new Gson().fromJson(jsonResponse, listType);


                    if (listOfGymModels == null) {
                        throw new IOException("Gym model is null");
                    }



                    for(int i = 0; i<listOfGymModels.size(); i++){
                        listOfAllGyms.add
                                (setGymInformationFromBackend(listOfGymModels.get(i)));
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

    public Gym getGymById (final String Id){
        Callable<Gym> asyncCall =
                new Callable<Gym>() {
                    @Override
                    public Gym call() throws Exception {
                        Request getEventInformation = new Request.Builder()
                                .url("https://20.172.9.70/gyms/gymId/" + Id)
                                .build();
                        Response response =
                                client.newCall(getEventInformation).execute();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response.code());
                        }

                        try (ResponseBody responseBody = response.body()) {
                            String jsonResponse = responseBody.string();
                            GymModelFromBackend gymModelFromBackend =
                                    new Gson().fromJson(jsonResponse, GymModelFromBackend.class);

                            if (gymModelFromBackend == null) {
                                throw new IOException("Schedule model is null");
                            }

                            return setGymInformationFromBackend(gymModelFromBackend);
                        }
                    }
                };

        Future<Gym> future = executorService.submit(asyncCall);
        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Gym getGymByEmail (final String Email){
        Callable<Gym> asyncCall =
                new Callable<Gym>() {
                    @Override
                    public Gym call() throws Exception {

                        Request getEventInformation = new Request.Builder()
                                .url("https://20.172.9.70/gyms/byEmail/" + Email)
                                .build();

                        Response response =
                                client.newCall(getEventInformation).execute();

                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response.code());
                        }

                        try (ResponseBody responseBody = response.body()) {
                            String jsonResponse = responseBody.string();
                            GymModelFromBackend GymModelFromBackend =
                                    new Gson().fromJson(jsonResponse, GymModelFromBackend.class);

                            if (GymModelFromBackend == null) {
                                throw new IOException("Schedule model is null");
                            }


                            return setGymInformationFromBackend(GymModelFromBackend);
                        }
                    }
                };

        Future<Gym> future = executorService.submit(asyncCall);
        try {
            return future.get();
            // This will block until the async call is complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Gym setGymInformationFromBackend
            (GymModelFromBackend gymModelFromBackend) {

        Gym returnedGym = new Gym();
        returnedGym.setName(gymModelFromBackend.getName());
        returnedGym.setAddress(gymModelFromBackend.getLocation());
        returnedGym.setPhone(gymModelFromBackend.getPhone());
        returnedGym.setEmail(gymModelFromBackend.getEmail());
        returnedGym.setDescription(gymModelFromBackend.getDescription());
        returnedGym.setGymId(gymModelFromBackend.get_id());
        //returnedGym.setImage(R.drawable.gym);

        //FOR NOW WE ARE ADDING THE IMAGE
        // SINCE THAT FIELD DOESNT EXIST ON THE BACKEND

        return returnedGym;
    }

    public void shutdown() {
        executorService.shutdown();
    }



}
