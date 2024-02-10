package com.ilyap.yuta.utils;

import androidx.annotation.NonNull;

import com.ilyap.yuta.models.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/** @noinspection BlockingMethodInNonBlockingContext*/
@UtilityClass
public class RequestUtils {
    public static final String ROOT_URL = "http://192.168.1.226:8000";

    @NonNull
    @SneakyThrows
    public static String postRequest(String urlString, Map<String, Object> params) {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);

        JSONObject jsonParams = new JSONObject(params);
        try (OutputStreamWriter os = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8)) {
            os.write(jsonParams.toString());
            os.flush();

            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
                    .lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    @NonNull
    @SneakyThrows
    public static String getRequest(String urlString) {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
                    .lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    public static void deleteUserPhotoRequest(User user) {
        // TODO
    }

    public static void uploadUserPhotoRequest(User user) {
        // TODO
    }

    public static void cropUserPhotoRequest(int imageWidth, int imageHeight, int croppedWidth, int croppedHeight, int offsetX, int offsetY) {
        // TODO
    }
}