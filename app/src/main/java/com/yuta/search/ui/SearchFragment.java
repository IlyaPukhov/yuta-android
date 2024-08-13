package com.yuta.search.ui;

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
import com.yuta.__old.R;
import com.yuta.app.domain.model.response.SearchUsersResponse;
import com.yuta.app.domain.model.entity.User;
import com.yuta.common.ui.CustomDialog;
import com.yuta.__old.ui.dialog.user.LogoutDialog;
import com.yuta.app.network.RequestViewModel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@NoArgsConstructor
public class SearchFragment extends Fragment {

    private View emptyText;
    private List<User> userDto;
    private View view;
    private RequestViewModel viewModel;
    private UserSearchAdapter searchAdapter;
    private View progressLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        emptyText = view.findViewById(R.id.empty_text);
        progressLayout = view.findViewById(R.id.progressLayout);
        EditText searchField = view.findViewById(R.id.search_user);

        recyclerViewInitialize();
        setupEditView(searchField);

        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList(userDto);
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
                if (count != 0) {
                    updateSearchResult(s.toString());
                } else {
                    userDto.clear();
                    updateList(userDto);
                }
            }
        });
    }

    private void updateSearchResult(String searchText) {
        progressLayout.setVisibility(VISIBLE);
        viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        viewModel.searchUsers(searchText);
        viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
            if (!(result instanceof SearchUsersResponse)) return;
            progressLayout.setVisibility(GONE);

            userDto = ((SearchUsersResponse) result).getUsersDtos();
            emptyText.setVisibility(userDto.isEmpty() ? VISIBLE : GONE);
            updateList(userDto);
        });
    }

    private void updateList(List<User> userDto) {
        searchAdapter.refillList(userDto);
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.search_list);
        userDto = new ArrayList<>();
        searchAdapter = new UserSearchAdapter(requireActivity(), userDto);

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