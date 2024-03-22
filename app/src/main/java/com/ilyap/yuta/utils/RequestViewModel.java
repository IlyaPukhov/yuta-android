package com.ilyap.yuta.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.ilyap.yuta.models.AuthResponse;
import com.ilyap.yuta.models.CheckTeamNameResponse;
import com.ilyap.yuta.models.EditUserResponse;
import com.ilyap.yuta.models.ProjectResponse;
import com.ilyap.yuta.models.ProjectsResponse;
import com.ilyap.yuta.models.SearchTeamResponse;
import com.ilyap.yuta.models.SearchUserResponse;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.TeamsResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;
import lombok.Cleanup;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collector;

import static com.ilyap.yuta.utils.RequestUtils.getRequest;
import static com.ilyap.yuta.utils.RequestUtils.getRootUrl;
import static com.ilyap.yuta.utils.RequestUtils.postFormDataRequest;
import static com.ilyap.yuta.utils.RequestUtils.postRequest;

public final class RequestViewModel extends ViewModel {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Object> resultLiveData = new MutableLiveData<>();

    private static JSONArray getMembersIdArray(@NonNull List<User> members) {
        return members.stream()
                .map(User::getId)
                .collect(Collector.of(JSONArray::new, JSONArray::put, JSONArray::put));
    }

    public LiveData<Object> getResultLiveData() {
        return resultLiveData;
    }

    // PROJECTS
    public void deleteProject(int projectId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("team_id", projectId);
            String json = postRequest(getFullUrl("projects"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getProject(int projectId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(getFullUrl("projects?project_id=") + projectId);
            resultLiveData.postValue(JsonUtils.parse(json, ProjectResponse.class));
        });
    }

    public void searchTeams(String name, int leaderId, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            String requestString = String.format(Locale.getDefault(),
                    "projects?team_name=%s&leader_id=%d%s", name, leaderId,
                    teamId >= 0 ? "&project_team_id=" + teamId : "");

            String json = getRequest(getFullUrl(requestString));
            resultLiveData.postValue(JsonUtils.parse(json, SearchTeamResponse.class));
        });
    }

    public void editProject(int projectId, String name, String description, String deadline, String status, Path techTaskPath, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("project_id", projectId);
            params.put("project_name", name);
            params.put("project_description", description);
            params.put("project_deadline", deadline);
            if (teamId >= 0) {
                params.put("project_team_id", teamId);
            }
            params.put("project_status", status);

            String json;
            if (techTaskPath != null) {
                try {
                    @Cleanup InputStream is = Files.newInputStream(techTaskPath);
                    json = postFormDataRequest(getFullUrl("projects"), params, is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                json = postRequest(getFullUrl("projects"), params);
            }
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void createProject(int managerId, String name, String description, String deadline, Path techTaskPath, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("manager_id", managerId);
            params.put("project_name", name);
            params.put("project_description", description);
            params.put("project_deadline", deadline);
            if (teamId >= 0) {
                params.put("project_team_id", teamId);
            }

            String json;
            if (techTaskPath != null) {
                try {
                    @Cleanup InputStream is = Files.newInputStream(techTaskPath);
                    json = postFormDataRequest(getFullUrl("projects"), params, is);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                json = postRequest(getFullUrl("projects"), params);
            }
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getProjects(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(getFullUrl("projects?user_id=" + userId));
            resultLiveData.postValue(JsonUtils.parse(json, ProjectsResponse.class));
        });
    }

    // TEAMS
    public void deleteTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "delete_team");
            params.put("team_id", teamId);
            String json = postRequest(getFullUrl("teams"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("action", "get_team_info");
            params.put("team_id", teamId);
            String json = postRequest(getFullUrl("teams"), params);
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
            String json = postRequest(getFullUrl("teams"), params);
            resultLiveData.postValue(JsonUtils.parse(json, SearchUserResponse.class));
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
            String json = postRequest(getFullUrl("teams"), params);
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
            String json = postRequest(getFullUrl("teams"), params);
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
            String json = postRequest(getFullUrl("teams"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getTeams(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(getFullUrl("teams?user_id=" + userId));
            resultLiveData.postValue(JsonUtils.parse(json, TeamsResponse.class));
        });
    }

    // PROFILE
    public void updateUserData(int userId, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            params.put("action", "update_data");
            params.put("password", password);
            String json = postRequest(getFullUrl("profile"), params);
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

            String json = postRequest(getFullUrl("profile"), params);
            resultLiveData.postValue(JsonUtils.parse(json, EditUserResponse.class));
        });
    }

    public void getUser(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            String json = getRequest(getFullUrl("profile?user_id=" + userId));
            resultLiveData.postValue(JsonUtils.parse(json, User.class));
        });
    }


    // AUTHORIZATION
    public void auth(String login, String password) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("login", login);
            params.put("password", password);
            String json = postRequest(getFullUrl("authorization"), params);
            resultLiveData.postValue(JsonUtils.parse(json, AuthResponse.class));
        });
    }


    private void clearResultLiveData() {
        resultLiveData.setValue(null);
    }

    private String getFullUrl(String path) {
        return String.format("%s/api/%s", getRootUrl(), path);
    }
}