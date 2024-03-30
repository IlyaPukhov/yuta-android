package com.ilyap.yuta.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.SearchUsersResponse;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.SearchAdapter;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.user.LogoutDialog;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@NoArgsConstructor
public class SearchFragment extends Fragment {
    private View emptyText;
    private List<User> users;
    private View view;
    private RequestViewModel viewModel;
    private SearchAdapter searchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        emptyText = view.findViewById(R.id.empty_text);
        EditText searchField = view.findViewById(R.id.search_user);

        recyclerViewInitialize();
        setupEditView(searchField);

        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList(users);
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
                if (s.length() != 0) {
                    updateSearchResult(s.toString());
                } else {
                    users.clear();
                    updateList(users);
                }
            }
        });
    }

    private void updateSearchResult(String searchText) {
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.searchUsers(searchText);
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof SearchUsersResponse)) return;
            users = ((SearchUsersResponse) result).getUsers();
            emptyText.setVisibility(users.isEmpty() ? VISIBLE : GONE);
            updateList(users);
        });
    }

    private void updateList(List<User> users) {
        searchAdapter.updateList(users);
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.search_list);
        users = new ArrayList<>();
        searchAdapter = new SearchAdapter(requireActivity(), users);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(searchAdapter);
    }

    private void openLogoutDialog() {
        CustomDialog logoutDialog = new LogoutDialog(view.getContext(), this);
        logoutDialog.start();
    }
}