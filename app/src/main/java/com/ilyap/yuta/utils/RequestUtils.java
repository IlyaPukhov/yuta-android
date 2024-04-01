package com.ilyap.yuta.utils;

import androidx.annotation.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@UtilityClass
public class RequestUtils {
    public static final String FILENAME = "filename";
    public static final String FILE_KEY_NAME = "file_key_name";

    private static final int MAX_BUFFER_SIZE = 1024 * 1024;
    private static final String CRLF = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    private static String rootUrl;

    @SneakyThrows
    public static String postFormDataRequest(String urlString, Map<String, Object> params, @NonNull InputStream is) {
        HttpURLConnection urlConnection = postHttpURLConnection(urlString);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
        urlConnection.setRequestProperty("Cache-Control", "no-cache");
        urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

        String filename = (String) params.get(FILENAME);
        String fileKeyName = (String) params.get(FILE_KEY_NAME);
        params.remove(FILENAME);
        params.remove(FILE_KEY_NAME);

        try (DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream())) {
            addFormDataFields(os, params);
            addFilePart(os, fileKeyName, filename, is);
            os.writeBytes(CRLF);
            os.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + CRLF);
            os.flush();

            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
                    .lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } finally {
            is.close();
            urlConnection.disconnect();
        }
    }

    @SneakyThrows
    public void addFormDataFields(DataOutputStream os, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            os.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
            os.writeBytes("Content-Disposition: form-data; " +
                    "name=\"" + entry.getKey() + "\"" + CRLF);
            os.writeBytes("Content-Type: text/plain" + CRLF);
            os.writeBytes(CRLF);

            os.writeBytes(entry.getValue() + CRLF);
        }
    }

    @SneakyThrows
    public void addFilePart(DataOutputStream os, String fieldName, String filename, InputStream is) {
        os.writeBytes(TWO_HYPHENS + BOUNDARY + CRLF);
        os.writeBytes("Content-Disposition: form-data; " +
                "name=\"" + fieldName + "\"; " +
                "filename=\"" + filename + "\""
                + CRLF);
        os.writeBytes("Content-Type: application/octet-stream" + CRLF);
        os.writeBytes(CRLF);

        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.writeBytes(CRLF);
    }

    @SneakyThrows
    public static String postRequest(String urlString, Map<String, Object> params) {
        HttpURLConnection urlConnection = postHttpURLConnection(urlString);
        urlConnection.setRequestProperty("Content-Type", "application/json");

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

    @SneakyThrows
    private static HttpURLConnection postHttpURLConnection(String urlString) {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        return urlConnection;
    }

    @SneakyThrows
    public static String getRequest(String urlString) {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Accept", "application/json;charset=utf-8");
        try {
            return new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))
                    .lines()
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getRootUrl() {
        return rootUrl;
    }

    //TODO убрать в релизе
    public static void setRootUrl(String ipv4) {
        rootUrl = String.format("http://%s:8000", ipv4);
    }
}