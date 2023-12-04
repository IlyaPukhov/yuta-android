package com.ilyap.yuta.utils;

import static com.ilyap.yuta.utils.RequestUtils.ROOT_API_URL;
import static com.ilyap.yuta.utils.RequestUtils.getRequest;
import static com.ilyap.yuta.utils.RequestUtils.postRequest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.models.CheckTeamNameResponse;
import com.ilyap.yuta.models.EditUserResponse;
import com.ilyap.yuta.models.SearchResponse;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collector;

public final class RequestViewModel extends ViewModel {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Object> resultLiveData = new MutableLiveData<>();

    public LiveData<Object> getResultLiveData() {
        return resultLiveData;
    }

    public void searchUsers(String userName, int leaderId, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "search_user");
            params.put("user_name", userName);
            params.put("leader_id", leaderId);
            params.put("members_id", getMembersIdArray(members));
            try {
                String json = postRequest(ROOT_API_URL + "teams", params);
                resultLiveData.postValue(JsonUtils.parse(json, SearchResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void checkTeamName(String name, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "check_team_name");
            if (teamId >= 0) {
                params.put("team_id", teamId);
            }
            params.put("team_name", name);
            try {
                String json = postRequest(ROOT_API_URL + "teams", params);
                resultLiveData.postValue(JsonUtils.parse(json, CheckTeamNameResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void checkTeamName(String name) {
        checkTeamName(name, -1);
    }

    public void createTeam(int leaderId, String teamName, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "create_team");
            params.put("team_name", teamName);
            params.put("leader_id", leaderId);
            params.put("members_id", getMembersIdArray(members));
            try {
                String json = postRequest(ROOT_API_URL + "teams", params);
                resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static JSONArray getMembersIdArray(List<User> members) {
        return members.stream()
                .map(User::getId)
                .collect(Collector.of(JSONArray::new, JSONArray::put, JSONArray::put));
    }

    public void updateUserData(int userId, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("action", "update_data");
            params.put("password", password);
            try {
                String json = postRequest(ROOT_API_URL + "profile", params);
                resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void editUserData(int userId, User user) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("action", "edit_data");

            String biography = user.getBiography();
            if (biography != null) {
                params.put("biography", biography);
            }

            String phone = user.getPhoneNumber();
            if (phone != null) {
                params.put("phone_number", phone);
            }

            String email = user.geteMail();
            if (email != null) {
                params.put("e_mail", email);
            }

            String vk = user.getVk();
            if (vk != null) {
                params.put("vk", vk);
            }

            try {
                String json = postRequest(ROOT_API_URL + "profile", params);
                resultLiveData.postValue(JsonUtils.parse(json, EditUserResponse.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void auth(String login, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("login", login);
            params.put("password", password);
            try {
                String json = postRequest(ROOT_API_URL + "authorization", params);
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
                String json = getRequest(ROOT_API_URL + "profile?user_id=" + userId);
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
                String json = getRequest(ROOT_API_URL + "teams?user_id=" + userId);
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