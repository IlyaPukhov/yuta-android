package com.ilyap.yuta.ui.dialogs.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.User;
import com.ilyap.yuta.ui.adapters.UserAdapter;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProjectsFragment;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static java.util.Calendar.*;

@SuppressWarnings("ConstantConditions")
public class CreateProjectDialog extends CustomInteractiveDialog {
    private static final int RADIO_CREATE_WITH_TEAM = R.id.create_with_team;
    public static final int PICK_PDF_REQUEST = 1;
    @SuppressLint("StaticFieldLeak")
    private static TextView fileName;
    protected RequestViewModel viewModel;
    private Button submitButton;
    private TextView dateField;
    private static Uri techTaskUri;
    private View pickTeamContainer;
    private EditText projectName;
    private EditText projectDesc;
    private TextView emptySearch;

    public CreateProjectDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_create_project);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);


        fileName = dialog.findViewById(R.id.file_name);
        fileName.setText("");
        submitButton = dialog.findViewById(R.id.submit);
        pickTeamContainer = dialog.findViewById(R.id.pick_team_container);

        emptySearch = dialog.findViewById(R.id.empty_search_text);
        projectName = dialog.findViewById(R.id.project_name);
        projectDesc = dialog.findViewById(R.id.project_desc);

        setupEditView(projectName);
        setupEditView(projectDesc);
        datePickerInitialize();
        radioGroupInitialize();

        dialog.findViewById(R.id.pick_tech_task).setOnClickListener(v -> pickTechTask());
        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
            createProject();
        });
    }

    @SneakyThrows
    private void createProject() {
        int managerId = getUserId(activity);
        String name = projectName.getText().toString();
        String deadline = dateField.getText().toString();
        String description = projectDesc.getText().toString();

        // optional
        Path techTaskPath = Paths.get(techTaskUri.getPath());
        InputStream inputStream = Files.newInputStream(techTaskPath);

        int teamId = -1;

        // EDIT String status = spinner.getSelectedItem().toString();
    }

    private void radioGroupInitialize() {
        ((RadioGroup) dialog.findViewById(R.id.radio_group)).setOnCheckedChangeListener(
                (group, checkedId) ->
                        pickTeamContainer.setVisibility(checkedId == RADIO_CREATE_WITH_TEAM ? VISIBLE : GONE
                        ));
    }

    private void pickTechTask() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((ProjectsFragment) fragment).pdfPickerLauncher.launch(Intent.createChooser(intent, activity.getString(R.string.pick_tech_task_file)));
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            setTechTask(data.getData());
        }
    }

    private static void setTechTask(Uri uri) {
        techTaskUri = uri;
        fileName.setText(uri.getLastPathSegment());
    }

//    EDIT
//    private void statusSpinnerInitialize() {
//        spinner = findViewById(R.id.status);
//        ArrayAdapter<ProjectStatus> adapter = new ArrayAdapter<>(
//                getContext(),
//                android.R.layout.simple_spinner_item,
//                ProjectStatus.values()
//        );
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setSelection(0);

//    }

    private void datePickerInitialize() {
        dateField = dialog.findViewById(R.id.date_field);

        dialog.findViewById(R.id.pick_date).setOnClickListener(v -> {
            Calendar calendar = getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year1, month1, dayOfMonth) -> {
                        dateField.setText(String.format(
                                Locale.getDefault(), "%02d.%02d.%04d", dayOfMonth, month1 + 1, year1)
                        );
                        calendar.clear();
                        calendar.set(year1, month1, dayOfMonth);
                    },
                    calendar.get(DAY_OF_MONTH),
                    calendar.get(MONTH),
                    calendar.get(YEAR)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private String getData(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.isEmpty()) {
                return text;
            }
        }
        return null;
    }

    private void searchTeam() {
//        viewModel.getResultLiveData().removeObservers(fragment);
//        viewModel.searchUsers(getData(searchField), getUserId(activity), addedMembers);
//        viewModel.getResultLiveData().observe(fragment, result -> {
//            if (!(result instanceof SearchResponse)) return;
//            updateList(searchAdapter, ((SearchResponse) result).getUsers());
//        });
    }

    private void updateList(@NonNull UserAdapter adapter, @NonNull List<User> users) {
        messageVisibility(emptySearch, !users.isEmpty());
        adapter.updateList(users);
    }

    private void recyclerViewsInitialize() {
//        RecyclerView searchUsersView = dialog.findViewById(R.id.searchUsers);
//        RecyclerView addedMembersView = dialog.findViewById(R.id.addedMembers);
//
//        LinearLayoutManager addedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        addedMembersView.setLayoutManager(addedLayoutManager);
//
//        LinearLayoutManager searchLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        searchUsersView.setLayoutManager(searchLayoutManager);
//
//        membersAdapter = new UserAdapter(this,addedMembers, null);
//        addedMembersView.setAdapter(membersAdapter);
//
//        searchAdapter = new UserAdapter(this, searchUsers, membersAdapter);
//        searchUsersView.setAdapter(searchAdapter);
    }

    public void updateAddedTextVisibility() {
//        addedText.setVisibility((addedTeam != null && !addedTeam.isEmpty()) ? VISIBLE : GONE);
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
                editText.setEnabled(!s.equals(null) && !s.toString().trim().isEmpty());
            }
        });
    }

}