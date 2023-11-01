package com.example.cpen321tutorial1;

public class JsonFunctions {

    final static String TAG = "Json";

    public static String JsonName(String name){
        String Json = "{\"name\": \""+ name + "\"}";
        return Json;
    }
}
