package com.ilyap.yuta.ui.dialogs;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.ilyap.yuta.R;
import com.ilyap.yuta.models.CheckTeamNameResponse;
import com.ilyap.yuta.models.SearchResponse;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.ListAdapter;
import com.ilyap.yuta.ui.fragments.TeamsFragment;
import com.ilyap.yuta.utils.RequestViewModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class CreateTeamDialog extends CustomInteractiveDialog {
    private RequestViewModel viewModel;
    private EditText teamName;
    private EditText searchField;
    private Button submitButton;
    private Button searchButton;
    private ListView searchUsersView;
    private ListView addedMembersView;
    private ListAdapter searchAdapter;
    private TextView error;
    private boolean isTeamNameUnique;
    private List<User> searchUsers;
    private List<User> addedMembers;

    public CreateTeamDialog(Context context, TeamsFragment teamsFragment) {
        super(context, teamsFragment);
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
        searchUsersView = dialog.findViewById(R.id.searchUsers);
        addedMembersView = dialog.findViewById(R.id.addedMembers);

        setupEditView(teamName);
        setupEditView(searchField);
        listViewsInit();

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

    private void searchUsers() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.searchUsers(getData(searchField), getUserId(activity), addedMembers);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof SearchResponse)) return;
            updateSearchList(((SearchResponse) result).getUsers());
        });
    }

    private void updateSearchList(List<User> users) {
        searchAdapter.clear();
        searchAdapter.addAll(users);
    }

    private RequestViewModel isNameUnique(String name) {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.checkTeamName(name);
        return viewModel;
    }

    private String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.equals("")) {
                return text;
            }
        }
        return null;
    }

    private void listViewsInit() {
        ListAdapter membersAdapter = new ListAdapter(getContext(), R.layout.item_user_list, addedMembers, null);
        addedMembersView.setAdapter(membersAdapter);

        searchAdapter = new ListAdapter(getContext(), R.layout.item_user_list, searchUsers, membersAdapter);
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