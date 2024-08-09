package com.yuta.domain.usecase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.yuta.app.domain.model.entity.User;
import com.yuta.app.domain.model.response.AuthorizationResponse;
import com.yuta.app.domain.model.response.ProjectResponse;
import com.yuta.app.domain.model.response.ProjectsResponse;
import com.yuta.app.domain.model.response.SearchTeamsResponse;
import com.yuta.app.domain.model.response.SearchUsersResponse;
import com.yuta.app.domain.model.response.TeamCheckNameResponse;
import com.yuta.app.domain.model.response.TeamResponse;
import com.yuta.app.domain.model.response.TeamsResponse;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.yuta.app.domain.model.response.UserResponse;
import com.yuta.app.network._RequestUtils;
import com.yuta.app.utils.JsonUtils;
import org.json.JSONArray;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collector;

import static com.yuta.app.network._RequestUtils.FILENAME;
import static com.yuta.app.network._RequestUtils.FILE_KEY_NAME;
import static com.yuta.app.network._RequestUtils.getRequest;
import static com.yuta.app.network._RequestUtils.postFormDataRequest;
import static com.yuta.app.network._RequestUtils.postRequest;
import static java.util.stream.Collectors.joining;

public final class _RequestViewModel extends ViewModel {
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<Object> resultLiveData = new MutableLiveData<>();

    // PROJECTS
    public void deleteProject(int projectId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("project_id", projectId);
            String json = postRequest(getFullUrl("projects"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void searchTeams(String name, int leaderId, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("team_name", name);
            params.put("leader_id", leaderId);
            if (teamId >= 0) {
                params.put("project_team_id", teamId);
            }
            String json = getRequest(getFullUrl("projects", params));
            resultLiveData.postValue(JsonUtils.parse(json, SearchTeamsResponse.class));
        });
    }

    public void editProject(int projectId, String name, String description,
                            String deadline, @Nullable String filename, String status, @Nullable InputStream is, int teamId) {
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

            resultLiveData.postValue(JsonUtils.parse(postProjectJsonRequest(is, params, filename), UpdateResponse.class));
        });
    }

    public void createProject(int managerId, String name, String description,
                              String deadline, @Nullable String filename, @Nullable InputStream is, int teamId) {
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

            resultLiveData.postValue(JsonUtils.parse(postProjectJsonRequest(is, params, filename), UpdateResponse.class));
        });
    }

    private String postProjectJsonRequest(@Nullable InputStream is, HashMap<String, Object> params, @Nullable String filename) {
        String json;
        if (is != null && filename != null) {
            params.put(FILENAME, filename);
            params.put(FILE_KEY_NAME, "project_technical_task");
            json = postFormDataRequest(getFullUrl("projects"), params, is);
        } else {
            json = postRequest(getFullUrl("projects"), params);
        }
        return json;
    }

    public void getProject(int projectId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("project_id", projectId);
            String json = getRequest(getFullUrl("projects", params));
            resultLiveData.postValue(JsonUtils.parse(json, ProjectResponse.class));
        });
    }

    public void getProjects(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            String json = getRequest(getFullUrl("projects", params));
            resultLiveData.postValue(JsonUtils.parse(json, ProjectsResponse.class));
        });
    }

    // TEAMS
    public void deleteTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
            params.put("team_id", teamId);
            String json = postRequest(getFullUrl("teams"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void searchUsers(String userName, int leaderId, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_name", userName);
            params.put("leader_id", leaderId);
            params.put("members_id", getMembersIdArray(members));
            String json = getRequest(getFullUrl("teams", params));
            resultLiveData.postValue(JsonUtils.parse(json, SearchUsersResponse.class));
        });
    }

    public void checkUniqueTeamName(String name, int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("team_name", name);
            if (teamId >= 0) {
                params.put("team_id", teamId);
            }
            String json = getRequest(getFullUrl("teams", params));
            resultLiveData.postValue(JsonUtils.parse(json, TeamCheckNameResponse.class));
        });
    }

    public void checkUniqueTeamName(String name) {
        checkUniqueTeamName(name, -1);
    }

    public void editTeam(int teamId, String teamName, List<User> members) {
        clearResultLiveData();
        executor.execute(() -> {
            HashMap<String, Object> params = new HashMap<>();
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
            params.put("leader_id", leaderId);
            params.put("team_name", teamName);
            params.put("members_id", getMembersIdArray(members));
            String json = postRequest(getFullUrl("teams"), params);
            resultLiveData.postValue(JsonUtils.parse(json, UpdateResponse.class));
        });
    }

    public void getTeam(int teamId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("team_id", teamId);
            String json = getRequest(getFullUrl("teams", params));
            resultLiveData.postValue(JsonUtils.parse(json, TeamResponse.class));
        });
    }

    public void getTeams(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            String json = getRequest(getFullUrl("teams", params));
            resultLiveData.postValue(JsonUtils.parse(json, TeamsResponse.class));
        });
    }

    //SEARCH
    public void searchUsers(String userName) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_name", userName);
            String json = getRequest(getFullUrl("search", params));
            resultLiveData.postValue(JsonUtils.parse(json, SearchUsersResponse.class));
        });
    }

    public void getUser(int userId) {
        clearResultLiveData();
        executor.execute(() -> {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", userId);
            String json = getRequest(getFullUrl("profile", params));
            resultLiveData.postValue(JsonUtils.parse(json, UserResponse.class));
        });
    }

    public LiveData<Object> getResultLiveData() {
        return resultLiveData;
    }

    private static JSONArray getMembersIdArray(@NonNull List<User> members) {
        return members.stream()
                .map(User::getId)
                .collect(Collector.of(JSONArray::new, JSONArray::put, JSONArray::put));
    }

    private void clearResultLiveData() {
        resultLiveData.setValue(null);
    }

    private String getFullUrl(String path) {
        return String.format("%s/api/%s/", _RequestUtils.getRootUrl(), path);
    }

    private String getFullUrl(String path, Map<String, Object> params) {
        String fullPath = getFullUrl(path);
        String queryParams = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining("&", "?", ""));

        return fullPath + queryParams;
    }
}