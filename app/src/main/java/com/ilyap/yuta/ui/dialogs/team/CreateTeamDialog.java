package com.ilyap.yuta.ui.dialogs.team;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.CheckTeamNameResponse;
import com.ilyap.yuta.models.SearchResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.UserAdapter;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.TeamsFragment;
import com.ilyap.yuta.utils.RequestViewModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class CreateTeamDialog extends CustomInteractiveDialog {
    protected RequestViewModel viewModel;
    protected EditText teamName;
    private EditText searchField;
    protected Button submitButton;
    private Button searchButton;
    protected UserAdapter searchAdapter;
    protected UserAdapter membersAdapter;
    private TextView error;
    private TextView emptySearch;
    private boolean isTeamNameUnique;
    private List<User> searchUsers;
    protected List<User> addedMembers;

    public CreateTeamDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.create_team_dialog);
    }

    @Override
    public void start() {
        super.start();
        searchUsers = new ArrayList<>();
        addedMembers = new ArrayList<>();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        submitButton = dialog.findViewById(R.id.submit);
        searchButton = dialog.findViewById(R.id.btnSearch);
        teamName = dialog.findViewById(R.id.team_name);
        searchField = dialog.findViewById(R.id.find_name);
        error = dialog.findViewById(R.id.error_text);
        emptySearch = dialog.findViewById(R.id.empty_search_text);

        setupEditView(teamName);
        setupEditView(searchField);
        recyclerViewsInitialize();

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        searchButton.setOnClickListener(v -> searchUsers());
        submitButton.setOnClickListener(v -> createTeam());
    }

    private void createTeam() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.createTeam(getUserId(activity), getData(teamName), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((TeamsFragment) fragment).updateCarousels();
            dismiss();
        });
    }

    protected void searchUsers() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.searchUsers(getData(searchField), getUserId(activity), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof SearchResponse)) return;
            updateList(searchAdapter, ((SearchResponse) result).getUsers());
        });
    }

    private void updateList(UserAdapter adapter, List<User> users) {
        messageVisibility(emptySearch, !users.isEmpty());
        adapter.updateList(users);
    }

    protected RequestViewModel isNameUnique(String name) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.checkTeamName(name);
        return viewModel;
    }

    protected String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.equals("")) {
                return text;
            }
        }
        return null;
    }

    private void recyclerViewsInitialize() {
        RecyclerView searchUsersView = dialog.findViewById(R.id.searchUsers);
        RecyclerView addedMembersView = dialog.findViewById(R.id.addedMembers);

        LinearLayoutManager addedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        addedMembersView.setLayoutManager(addedLayoutManager);

        LinearLayoutManager searchLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchUsersView.setLayoutManager(searchLayoutManager);

        membersAdapter = new UserAdapter(getContext(), addedMembers, null);
        addedMembersView.setAdapter(membersAdapter);

        searchAdapter = new UserAdapter(getContext(), searchUsers, membersAdapter);
        searchUsersView.setAdapter(searchAdapter);
    }

    private void messageVisibility(View message, boolean isValid) {
        message.setVisibility(isValid ? GONE : VISIBLE);
    }

    private void setupEditView(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText == teamName) {
                    isNameUnique(s.toString()).getResultLiveData().observe(fragment, result -> {
                        if (!(result instanceof CheckTeamNameResponse)) return;
                        isTeamNameUnique = ((CheckTeamNameResponse) result).isUnique();
                        submitButton.setEnabled(isTeamNameUnique);
                        messageVisibility(error, isTeamNameUnique);
                    });
                } else if (editText == searchField) {
                    searchButton.setEnabled(!s.equals(null) && !s.toString().trim().equals(""));
                }
            }
        });
    }
}