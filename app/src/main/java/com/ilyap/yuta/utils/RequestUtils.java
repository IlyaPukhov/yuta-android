package com.ilyap.yuta.utils;

import androidx.annotation.NonNull;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@UtilityClass
public class RequestUtils {
    private static final int MAX_BUFFER_SIZE = 1024 * 1024;
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    @Getter
    private static String rootUrl;

    @NonNull
    @SneakyThrows
    public static String postFormDataRequest(String urlString, Map<String, Object> params, InputStream is) {
        HttpURLConnection urlConnection = getPostHttpURLConnection(urlString);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
        urlConnection.setRequestProperty("Cache-Control", "no-cache");
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        JSONObject jsonParams = new JSONObject(params);
        try (BufferedInputStream inputStream = new BufferedInputStream(is);
             DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream())) {
            os.writeBytes(jsonParams.toString());

            os.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
            os.writeBytes("Content-Disposition: form-data" + CRLF);
            os.writeBytes("Content-Type: application/octet-stream" + CRLF);
            os.writeBytes(CRLF);

            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.writeBytes(CRLF);
            os.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);

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
    public static String postRequest(String urlString, Map<String, Object> params) {
        HttpURLConnection urlConnection = getPostHttpURLConnection(urlString);
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");

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

    @NotNull
    private static HttpURLConnection getPostHttpURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        return urlConnection;
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

    //TODO убрать в релизе
    public static void setRootUrl(String ipv4) {
        rootUrl = String.format("http://%s:8000", ipv4);
    }
}