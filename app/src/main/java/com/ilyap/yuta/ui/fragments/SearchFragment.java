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
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.SearchAdapter;
import com.ilyap.yuta.ui.dialogs.CustomDialog;
import com.ilyap.yuta.ui.dialogs.user.LogoutDialog;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.View.VISIBLE;

@NoArgsConstructor
public class SearchFragment extends Fragment {
    private View progressLayout;
    private List<User> users;
    private View view;
    private RequestViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = new ViewModelProvider(this).get(RequestViewModel.class);

        progressLayout = view.findViewById(R.id.progressLayout);
        EditText searchField = view.findViewById(R.id.search_user);

        recyclerViewInitialize();
        setupEditView(searchField);

        view.findViewById(R.id.log_out).setOnClickListener(v -> openLogoutDialog());
        return view;
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
                progressLayout.setVisibility(VISIBLE);
                updateSearchResult(s.toString());
            }
        });
    }

    private void updateSearchResult(String searchText) {
        // TODO: 26.03.2024
        //  viewModel.getResultLiveData().removeObservers(getViewLifecycleOwner());
        //  viewModel.search(s.toString());
        //  viewModel.getResultLiveData().observe(getViewLifecycleOwner(), result -> {
        //      if (!(result instanceof SearchResponse)) return;
        //      progressLayout.setVisibility(GONE);
        //      users = ((SearchResponse) result).getUsers();
        //  });
    }

    private void recyclerViewInitialize() {
        RecyclerView recyclerView = view.findViewById(R.id.search_list);
        users = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(AppCompatResources.getDrawable(requireContext(), R.drawable.divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(new SearchAdapter(requireActivity(), users));
    }

    private void openLogoutDialog() {
        CustomDialog logoutDialog = new LogoutDialog(view.getContext(), this);
        logoutDialog.start();
    }
}