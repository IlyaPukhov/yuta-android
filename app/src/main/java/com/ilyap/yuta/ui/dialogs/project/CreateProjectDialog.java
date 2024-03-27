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
import com.ilyap.yuta.models.SearchTeamResponse;
import com.ilyap.yuta.models.Team;
import com.ilyap.yuta.models.UpdateResponse;
import com.ilyap.yuta.ui.adapters.TeamSearchAdapter;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProjectsFragment;
import com.ilyap.yuta.utils.RequestViewModel;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ilyap.yuta.utils.UserUtils.getUserId;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

@SuppressWarnings("ConstantConditions")
public class CreateProjectDialog extends CustomInteractiveDialog {
    public static final int PICK_PDF_REQUEST = 1;
    private static final int RADIO_CREATE_WITH_TEAM = R.id.create_with_team;
    @SuppressLint("StaticFieldLeak")
    protected static TextView fileName;
    protected static Uri techTaskUri;
    private final List<Team> addedTeams = new ArrayList<>();
    private final List<Team> searchTeams = new ArrayList<>();
    protected RequestViewModel viewModel;
    protected Button submitButton;
    protected TextView deadlineField;
    protected TeamSearchAdapter searchAdapter;
    protected TeamSearchAdapter teamSearchAdapter;
    protected EditText projectName;
    protected EditText projectDesc;
    private Button searchButton;
    private TextView addedText;
    private View pickTeamContainer;
    private TextView emptySearch;
    private EditText searchField;

    public CreateProjectDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_create_project);
    }

    public static void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            setTechTask(data.getData());
        }
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        fileName = dialog.findViewById(R.id.file_name);
        fileName.setText("");
        techTaskUri = null;

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
        String name = getData(projectName);
        String description = getData(projectDesc);
        String deadline = getFormattedDate(getData(deadlineField));
        InputStream techTaskInputStream = techTaskUri != null ? fragment.requireActivity().getContentResolver().openInputStream(techTaskUri) : null;
        int teamId = getCurrentTeamId();

        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.createProject(managerId, name, description, deadline, getData(fileName), techTaskInputStream, teamId);
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof UpdateResponse)) return;
            ((ProjectsFragment) fragment).updateLists();
            dismiss();
        });
    }

    private static void setTechTask(Uri uri) {
        techTaskUri = uri;
        fileName.setText(new File(uri.getPath()).getName());
    }

    protected int getCurrentTeamId() {
        return addedTeams.stream()
                .findFirst()
                .map(Team::getId)
                .orElse(-1);
    }

    protected String getFormattedDate(String date) {
        DateTimeFormatter sourceDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, sourceDateFormatter);

        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"));
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

    private void datePickerInitialize() {
        deadlineField = dialog.findViewById(R.id.date_field);

        dialog.findViewById(R.id.pick_date).setOnClickListener(v -> {
            Calendar calendar = getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    activity,
                    (view, year1, month1, dayOfMonth) -> {
                        deadlineField.setText(String.format(
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

    protected String getData(TextView editText) {
        if (editText != null) {
            String text = editText.getText().toString().trim();

            if (!text.isEmpty()) {
                return text;
            }
        }
        return null;
    }

    private void searchTeam() {
        viewModel.getResultLiveData().removeObservers(fragment);
        viewModel.searchTeams(getData(searchField), getUserId(activity), getCurrentTeamId());
        viewModel.getResultLiveData().observe(fragment, result -> {
            if (!(result instanceof SearchTeamResponse)) return;
            updateList(searchAdapter, ((SearchTeamResponse) result).getTeams());
        });
    }

    private void updateList(@NonNull TeamSearchAdapter adapter, @NonNull List<Team> teams) {
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

        teamSearchAdapter = new TeamSearchAdapter(this, addedTeams, null);
        addedTeamsView.setAdapter(teamSearchAdapter);

        searchAdapter = new TeamSearchAdapter(this, searchTeams, teamSearchAdapter);
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
        boolean isValid = isFilledTextViews(projectName.getText(), projectDesc.getText(), deadlineField.getText());
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