package com.ilyap.yuta.utils;

import static com.ilyap.yuta.utils.RequestUtils.ROOT_API_URL;
import static com.ilyap.yuta.utils.RequestUtils.getRequestJson;
import static com.ilyap.yuta.utils.RequestUtils.postRequestJson;
import static com.ilyap.yuta.utils.UserUtils.EMPTY_DATA;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.models.EditResponse;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class RequestViewModel extends ViewModel {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Object> resultLiveData = new MutableLiveData<>();

    public LiveData<Object> getResultLiveData() {
        return resultLiveData;
    }

    public void updateUserData(int userId, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", String.valueOf(userId));
            params.put("action", "update_data");
            params.put("password", password);
            try {
                String json = postRequestJson(ROOT_API_URL + "profile", params);
                resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void editUserData(int userId, User user) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", String.valueOf(userId));
            params.put("action", "edit_data");

            String biography = user.getBiography();
            if (!biography.equals(EMPTY_DATA)) {
                params.put("biography", biography);
            }

            String phone = user.getPhoneNumber();
            if (!phone.equals(EMPTY_DATA)) {
                params.put("phone_number", phone);
            }

            String email = user.geteMail();
            if (!email.equals(EMPTY_DATA)) {
                params.put("e_mail", email);
            }

            String vk = user.getVk();
            if (!vk.equals(EMPTY_DATA)) {
                params.put("vk", vk);
            }

            try {
                String json = postRequestJson(ROOT_API_URL + "profile", params);
                resultLiveData.postValue(JsonUtils.parse(json, EditResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void auth(String login, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("login", login);
            params.put("password", password);
            try {
                String json = postRequestJson(ROOT_API_URL + "authorization", params);
                resultLiveData.postValue(JsonUtils.parse(json, AuthResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void getUser(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            try {
                String json = getRequestJson(ROOT_API_URL + "profile?user_id=" + userId);
                resultLiveData.postValue(JsonUtils.parse(json, User.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void getTeams(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            try {
                String json = getRequestJson(ROOT_API_URL + "teams?user_id=" + userId);
                resultLiveData.postValue(JsonUtils.parse(json, TeamResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void clearResultLiveData() {
        resultLiveData.setValue(null);
    }
}