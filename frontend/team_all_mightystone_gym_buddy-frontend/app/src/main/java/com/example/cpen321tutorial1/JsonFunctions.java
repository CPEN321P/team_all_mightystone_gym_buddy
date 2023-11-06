
package com.example.cpen321tutorial1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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

            @Override public void onResponse(Call call, Response response)
                    throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    Log.d(TAG, "POST Something");
                }
            }
        });
    }

    public static String JsonUserId(String userId){
        String Json = "\"userId\": \""+ userId + "\"";
        return Json;
    }

    public static String JsonLocation(String location){
        String Json = "\"location\": \""+ location + "\"";
        return Json;
    }

    public static String JsonDate(LocalDate date){
        DecimalFormat formatter1 = new DecimalFormat("0000");
        DecimalFormat formatter2 = new DecimalFormat("00");
        Log.d(TAG, "Test11");
        String YearString = formatter1.format(date.getYear());
        String MonthString = formatter2.format(date.getMonthValue());
        String DayString = formatter2.format(date.getDayOfMonth());
        String DateString = MonthString + DayString + YearString;
        String Json = "\"date\": \"" + DateString + "\"";
        Log.d(TAG, Json);
        return Json;
    }

    public static String JsonStartTime(LocalTime time){
        Log.d(TAG, "Test111");
        DecimalFormat formatter = new DecimalFormat("00");
        String HourString = formatter.format(time.getHour());
        String MinString = formatter.format(time.getMinute());
        String TimeString = HourString + MinString;
        String Json = "\"timeStart\": \"" + TimeString + "\"";
        Log.d(TAG, Json);
        return Json;
    }

    public static String JsonEndTime(LocalTime time){
        Log.d(TAG, "Test111");
        DecimalFormat formatter = new DecimalFormat("00");
        String HourString = formatter.format(time.getHour());
        String MinString = formatter.format(time.getMinute());
        String TimeString = HourString + MinString;
        String Json = "\"timeEnd\": \""+ TimeString + "\"";
        Log.d(TAG, Json);
        return Json;
    }

    public static String JsonName(String name){
        String Json = "\"name\": \""+ name + "\"";
        return Json;
    }

    public static String JsonUsername(String username){
        String Json = "\"username\": \""+ username + "\"";
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
        String Json = "\"reported\": \"" + JsonReportedString + "\"";
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

    public static String JsonWeightEvent(String Weight){
        String Json = "\"weight\": \""+ Weight + "\"";
        return Json;
    }

    public static String JsonSets(int Sets){
        String Json = "\"sets\": \""+ String.valueOf(Sets) + "\"";
        return Json;
    }

    public static String JsonReps(int Reps){
        String Json = "\"reps\": \""+ String.valueOf(Reps) + "\"";
        return Json;
    }

    public static String JsonEvent(String JsonName, String JsonWeight,
                                   String JsonSets, String JsonReps,
                                   String JsonTimeStart, String JsoneTimeEnd) {
        String JsonEvent = "{" + JsonName + "," + JsonWeight + "," +
                JsonSets + "," + JsonReps + "," + JsonTimeStart +
                "," + JsoneTimeEnd + "}";
        return JsonEvent;
    }

    public static String JsonSchedule(String JsonEvent){
        String JsonEventList = "[" + JsonEvent + "]";
        String Json = "\"exercises\": " + JsonEventList;
        return Json;
    }

    public static String convertArrayListToJson (ArrayList<String> arrayList) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : arrayList) {
            jsonArray.put(item);
        }
        return jsonArray.toString();
    }



    public static String ConvertEventArrayListToJson
            (ArrayList<Event> Events, String userId, LocalDate date){
        String JsonUserId = JsonUserId(userId);
        String JsonDate = JsonDate(date);
        String JsonExercises = "\"exercises\": [";
        for (int i = 0; i < Events.size(); i++){
            String JsonName = JsonName(Events.get(i).getName());
            String JsonTimeStart = JsonStartTime(Events.get(i).getStartTime());
            String JsonTimeEnd = JsonEndTime(Events.get(i).getEndTime());
            JsonExercises = JsonExercises + "{" + JsonName + "," +
                    JsonTimeStart + "," + JsonTimeEnd + "}";
            if (i != Events.size()-1) {
                JsonExercises = JsonExercises + ",";
            }
        }
        JsonExercises = JsonExercises + "]";
        String Json = "{" + JsonUserId + "," +
                JsonDate + "," + JsonExercises + "}";
        return Json;
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



    public static LocalDate NumToLocalDate(Long DateNum){
        DecimalFormat formatter = new DecimalFormat("00000000");
        char[] dateArray = formatter.format(DateNum).toCharArray();
        String Day = "";
        String Month = "";
        String Year = "";
        for (int i = 0; i < dateArray.length; i++)
        {
            if (i == 0 || i == 1){
                Month += dateArray[i];
            } else if (i == 2 || i == 3) {
                Day += dateArray[i];
            }
            else{
                Year += dateArray[i];
            }
        }
        String TheDate = Year + "-" + Month + "-" + Day;
        LocalDate localDate = LocalDate.parse(TheDate);
        return localDate;
    }

    public static LocalTime NumToLocalTime(Long TimeNum){
        Log.d(TAG, Long.toString(TimeNum));
        DecimalFormat formatter = new DecimalFormat("0000");
        char[] TimeArray = formatter.format(TimeNum).toCharArray();
        String Min = "";
        String Hour = "";
        for (int i = 0; i < TimeArray.length; i++)
        {
            if (i == 0 || i == 1){
                Hour += TimeArray[i];
            } else if (i == 2 || i == 3) {
                Min += TimeArray[i];
            }
        }
        String TheTime = Hour + ":" + Min;
        LocalTime localTime = LocalTime.parse(TheTime);
        return localTime;
    }

    public static String TimeToStringNum(LocalTime time){
        DecimalFormat formatter = new DecimalFormat("00");
        String HourString = formatter.format(time.getHour());
        String MinString = formatter.format(time.getMinute());
        String TimeString = HourString + MinString;
        return TimeString;
    }

    public static String DateToStringNum(LocalDate date){
        DecimalFormat formatter1 = new DecimalFormat("0000");
        DecimalFormat formatter2 = new DecimalFormat("00");
        Log.d(TAG, "Test11");
        String YearString = formatter1.format(date.getYear());
        String MonthString = formatter2.format(date.getMonthValue());
        String DayString = formatter2.format(date.getDayOfMonth());
        String DateString = MonthString + DayString + YearString;
        return DateString;
    }



}

