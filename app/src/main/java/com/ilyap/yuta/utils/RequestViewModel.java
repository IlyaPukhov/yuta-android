package com.ilyap.yuta.utils;

import static com.ilyap.yuta.utils.RequestUtils.ROOT_URL;
import static com.ilyap.yuta.utils.RequestUtils.getRequest;
import static com.ilyap.yuta.utils.RequestUtils.postRequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.models.CheckTeamNameResponse;
import com.ilyap.yuta.models.EditUserResponse;
import com.ilyap.yuta.models.SearchResponse;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collector;

public final class RequestViewModel extends ViewModel {
    private static final String ROOT_API_URL = ROOT_URL + "/api/";
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Object> resultLiveData = new MutableLiveData<>();

    public LiveData<Object> getResultLiveData() {
        return resultLiveData;
    }

    public void deleteTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "delete_team");
            params.put("team_id", teamId);
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "get_team_info");
            params.put("team_id", teamId);
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, Team.class));
        });
    }

    public void searchUsers(String userName, int leaderId, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "search_user");
            params.put("user_name", userName);
            params.put("leader_id", leaderId);
            params.put("members_id", getMembersIdArray(members));
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, SearchResponse.class));
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
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, CheckTeamNameResponse.class));
        });
    }

    public void checkTeamName(String name) {
        checkTeamName(name, -1);
    }

    public void editTeam(int teamId, String teamName, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "edit_team");
            params.put("team_id", teamId);
            params.put("team_name", teamName);
            params.put("members_id", getMembersIdArray(members));
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void createTeam(int leaderId, String teamName, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "create_team");
            params.put("team_name", teamName);
            params.put("leader_id", leaderId);
            params.put("members_id", getMembersIdArray(members));
            String json = postRequest(ROOT_API_URL + "teams", params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    private static JSONArray getMembersIdArray(@NonNull List<User> members) {
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
            String json = postRequest(ROOT_API_URL + "profile", params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
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

            String email = user.getEMail();
            if (email != null) {
                params.put("e_mail", email);
            }

            String vk = user.getVk();
            if (vk != null) {
                params.put("vk", vk);
            }

            String json = postRequest(ROOT_API_URL + "profile", params);
            resultLiveData.postValue(JsonUtils.parse(json, EditUserResponse.class));
        });
    }

    public void auth(String login, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("login", login);
            params.put("password", password);
            String json = postRequest(ROOT_API_URL + "authorization", params);
            resultLiveData.postValue(JsonUtils.parse(json, AuthResponse.class));
        });
    }

    public void getUser(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(ROOT_API_URL + "profile?user_id=" + userId);
            resultLiveData.postValue(JsonUtils.parse(json, User.class));
        });
    }

    public void getTeams(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(ROOT_API_URL + "teams?user_id=" + userId);
            resultLiveData.postValue(JsonUtils.parse(json, TeamResponse.class));
        });
    }


    private void clearResultLiveData() {
        resultLiveData.setValue(null);
    }
}