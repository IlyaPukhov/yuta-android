package com.ilyap.yuta.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    private JsonUtils() {
    }

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static <T> T parse(String jsonString, Class<T> pojoClass) {
        return gson.fromJson(jsonString, pojoClass);
    }

    public static <T> List<T> parseArray(String jsonString, Class<T> pojoClass) {
        return gson.fromJson(jsonString, TypeToken.getParameterized(ArrayList.class, pojoClass).getType());
    }
}