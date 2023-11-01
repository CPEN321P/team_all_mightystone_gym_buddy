package com.example.cpen321tutorial1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonFunctions {

    final static String TAG = "Json";

    public static void NewCallPost(OkHttpClient client, Request requestName) {
        client.newCall(requestName).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    Log.d(TAG, "POST Something");
                }
            }
        });
    }

    public static String JsonName(String name){
        String Json = "\"name\": \""+ name + "\"";
        return Json;
    }

    public static String JsonPhone(int phone){
        String Json = "\"phone\": \""+ String.valueOf(phone) + "\"";
        return Json;
    }

    public static String JsonEmail(String email){
        String Json = "\"email\": \""+ email + "\"";
        return Json;
    }

    public static String JsonAge(int age){
        String Json = "\"age\": \""+ String.valueOf(age) + "\"";
        return Json;
    }

    public static String JsonGender(String gender){
        String Json = "\"gender\": \""+ gender + "\"";
        return Json;
    }

    public static String JsonWeight(int weight){
        String Json = "\"weight\": \""+ String.valueOf(weight) + "\"";
        return Json;
    }

    public static String JsonPfp(String pfp){
        String Json = "\"pfp\": \""+ pfp + "\"";
        return Json;
    }

    public static String JsonFriends(ArrayList<String> friends){
        String FriendsString = convertArrayListToJson(friends);
        String Json = "\"friends\": \""+ FriendsString + "\"";
        return Json;
    }

    public static String JsonFriendRequests(ArrayList<String> friendsRequests){
        String FriendsRequestString = convertArrayListToJson(friendsRequests);
        String Json = "\"friendRequests\": \""+ FriendsRequestString + "\"";
        return Json;
    }

    public static String JsonDescription(String Description){
        String Json = "\"description\": \""+ Description + "\"";
        return Json;
    }

    public static String JsonHomeGym(String HomeGym){
        String Json = "\"homeGym\": \""+ HomeGym + "\"";
        return Json;
    }

    public static String JsonReported(int reported){
        String JsonReportedString = convertIntegerToJson(reported);
        String Json = "\"reported\": \""+ JsonReportedString + "\"";
        return Json;
    }

    public static String JsonChats(ArrayList<String> chats){
        String ChatsString = convertArrayListToJson(chats);
        String Json = "\"chats\": \""+ ChatsString + "\"";
        return Json;
    }

    public static String JsonBlockedUsers(ArrayList<String> blockedUsers){
        String blockedUsersString = convertArrayListToJson(blockedUsers);
        String Json = "\"blockedUsers\": \""+ blockedUsersString + "\"";
        return Json;
    }

    public static String convertArrayListToJson(ArrayList<String> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : arrayList) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }

    public static String convertIntegerToJson(int value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("integerValue", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


}
