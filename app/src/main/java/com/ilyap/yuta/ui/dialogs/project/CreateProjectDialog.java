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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ilyap.yuta.R;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.ui.adapters.TeamAdapter;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProjectsFragment;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    protected Button submitButton;
    private Button searchButton;
    private TextView dateField;
    private TextView addedText;
    protected TeamAdapter searchAdapter;
    protected TeamAdapter teamAdapter;
    private static Uri techTaskUri;
    private View pickTeamContainer;
    private EditText projectName;
    private EditText projectDesc;
    private TextView emptySearch;
    private final List<Team> addedTeams = new ArrayList<>();
    private final List<Team> searchTeams = new ArrayList<>();
    private EditText searchField;

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
        pickTeamContainer = dialog.findViewById(R.id.pick_team_container);

        searchField = dialog.findViewById(R.id.find_name);
        searchButton = dialog.findViewById(R.id.btn_search);
        submitButton = dialog.findViewById(R.id.submit);

        addedText = dialog.findViewById(R.id.added_team_text);
        emptySearch = dialog.findViewById(R.id.empty_search_text);
        projectName = dialog.findViewById(R.id.project_name);
        projectDesc = dialog.findViewById(R.id.project_desc);

        setupEditView(projectName);
        setupEditView(projectDesc);
        datePickerInitialize();
        radioGroupInitialize();

        dialog.findViewById(R.id.pick_tech_task).setOnClickListener(v -> pickTechTask());
        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        searchButton.setOnClickListener(v -> searchTeam());
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
        //int teamId = -1;
    }

    private void radioGroupInitialize() {
        ((RadioGroup) dialog.findViewById(R.id.radio_group)).setOnCheckedChangeListener(
                (group, checkedId) -> {
                    if (checkedId == RADIO_CREATE_WITH_TEAM) {
                        pickTeamContainer.setVisibility(VISIBLE);
                        recyclerViewInitialize();
                    } else {
                        pickTeamContainer.setVisibility(GONE);
                    }
                }
        );
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
        fileName.setText(new File(uri.getPath()).getName());
    }

    private void datePickerInitialize() {
        dateField = dialog.findViewById(R.id.date_field);

        dialog.findViewById(R.id.pick_date).setOnClickListener(v -> {
            Calendar calendar = getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    activity,
                    (view, year1, month1, dayOfMonth) -> {
                        dateField.setText(String.format(
                                Locale.getDefault(), "%02d.%02d.%04d", dayOfMonth, month1 + 1, year1)
                        );
                        calendar.clear();
                        calendar.set(year1, month1, dayOfMonth);
                        updateSubmitButtonState();
                    },
                    calendar.get(DAY_OF_MONTH),
                    calendar.get(MONTH),
                    calendar.get(YEAR)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private String getData(TextView editText) {
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

    private void updateList(@NonNull TeamAdapter adapter, @NonNull List<Team> teams) {
        messageVisibility(emptySearch, !teams.isEmpty());
        adapter.updateList(teams);
    }

    private void recyclerViewInitialize() {
        RecyclerView searchTeamView = dialog.findViewById(R.id.search_teams);
        RecyclerView addedTeamsView = dialog.findViewById(R.id.added_teams);

        LinearLayoutManager searchLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        searchTeamView.setLayoutManager(searchLayoutManager);

        LinearLayoutManager addedLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        addedTeamsView.setLayoutManager(addedLayoutManager);

        teamAdapter = new TeamAdapter(this, addedTeams, null);
        addedTeamsView.setAdapter(teamAdapter);

        searchAdapter = new TeamAdapter(this, searchTeams, teamAdapter);
        searchTeamView.setAdapter(searchAdapter);
    }

    public void updateAddedTextVisibility() {
        addedText.setVisibility((addedTeams != null && !addedTeams.isEmpty()) ? VISIBLE : GONE);
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
                if (editText == projectName || editText == projectDesc) {
                    updateSubmitButtonState();
                } else if (editText == searchField) {
                    searchButton.setEnabled(isFilledTextViews(s));
                }
            }
        });
    }

    private void updateSubmitButtonState() {
        boolean isValid = isFilledTextViews(projectName.getText(), projectDesc.getText(), dateField.getText());
        submitButton.setEnabled(isValid);
    }

    private boolean isFilledTextViews(CharSequence... sequences) {
        for (CharSequence s : sequences) {
            if (s.equals(null) || s.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}