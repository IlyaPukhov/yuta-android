package com.yuta.__old.ui.dialog.team;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuta.__old.R;
import com.yuta.app.domain.model.response.TeamCheckNameResponse;
import com.yuta.app.domain.model.response.SearchUsersResponse;
import com.yuta.app.domain.model.response.UpdateResponse;
import com.yuta.app.domain.model.entity.User;
import com.yuta.__old.ui.adapter.TeamUserSearchAdapter;
import com.yuta.common.ui.CustomInteractiveDialog;
import com.yuta.__old.ui.fragment.TeamsFragment;
import com.yuta.app.network.RequestViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yuta.common.util.UserUtils.getUserId;

@SuppressWarnings("ConstantConditions")
public class CreateTeamDialog extends CustomInteractiveDialog {
    protected final List<User> addedMembers = new ArrayList<>();
    private final List<User> searchUserDtos = new ArrayList<>();
    protected RequestViewModel viewModel;
    protected EditText teamName;
    protected Button submitButton;
    protected TeamUserSearchAdapter searchAdapter;
    protected TeamUserSearchAdapter membersAdapter;
    private EditText searchField;
    private Button searchButton;
    private TextView error;
    private TextView emptySearch;
    private TextView addedText;
    private boolean isTeamNameUnique;

    public CreateTeamDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_create_team);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        submitButton = dialog.findViewById(R.id.submit);
        searchButton = dialog.findViewById(R.id.btnSearch);
        teamName = dialog.findViewById(R.id.team_name);
        searchField = dialog.findViewById(R.id.find_name);
        error = dialog.findViewById(R.id.error_text);
        emptySearch = dialog.findViewById(R.id.empty_search_text);
        addedText = dialog.findViewById(R.id.added_members);

        setupEditView(teamName);
        setupEditView(searchField);
        recyclerViewsInitialize();

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        searchButton.setOnClickListener(v -> {
            hideKeyboard(searchButton);
            searchUsers();
        });
        submitButton.setOnClickListener(v -> {
            hideKeyboard(teamName);
            createTeam();
        });
    }

    private void createTeam() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.createTeam(getUserId(activity), getData(teamName), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((TeamsFragment) fragment).updateLists();
            dismiss();
        });
    }

    protected void searchUsers() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.searchUsers(getData(searchField), getUserId(activity), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof SearchUsersResponse)) return;
            updateList(searchAdapter, ((SearchUsersResponse) result).getUsersDtos());
        });
    }

    private void updateList(@NonNull TeamUserSearchAdapter adapter, @NonNull List<User> userDto) {
        messageVisibility(emptySearch, !userDto.isEmpty());
        adapter.refillList(userDto);
    }

    protected RequestViewModel isNameUnique(String name) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.checkUniqueTeamName(name);
        return viewModel;
    }

    protected String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.isEmpty()) {
                return text;
            }
        }
        return null;
    }

    private void recyclerViewsInitialize() {
        RecyclerView addedMembersView = dialog.findViewById(R.id.addedMembers);
        addedMembersView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        membersAdapter = new TeamUserSearchAdapter(this, addedMembers, null);
        addedMembersView.setAdapter(membersAdapter);

        RecyclerView searchUsersView = dialog.findViewById(R.id.searchUsers);
        searchUsersView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        searchAdapter = new TeamUserSearchAdapter(this, searchUserDtos, membersAdapter);
        searchUsersView.setAdapter(searchAdapter);
    }

    public void updateAddedTextVisibility() {
        addedText.setVisibility((addedMembers != null && !addedMembers.isEmpty()) ? VISIBLE : GONE);
    }

    private void messageVisibility(@NonNull View message, boolean isValid) {
        message.setVisibility(isValid ? GONE : VISIBLE);
    }

    private void setupEditView(@NonNull EditText editText) {
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
                        if (!(result instanceof TeamCheckNameResponse)) return;
                        isTeamNameUnique = ((TeamCheckNameResponse) result).getUnique();
                        submitButton.setEnabled(isTeamNameUnique);
                        messageVisibility(error, isTeamNameUnique);
                    });
                } else if (editText == searchField) {
                    searchButton.setEnabled(!s.equals(null) && !s.toString().trim().isEmpty());
                }
            }
        });
    }
}