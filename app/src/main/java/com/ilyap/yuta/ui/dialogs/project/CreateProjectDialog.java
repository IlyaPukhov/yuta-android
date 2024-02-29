package com.ilyap.yuta.ui.dialogs.project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.ui.fragments.ProjectsFragment;
import com.ilyap.yuta.utils.ProjectStatus;
import com.ilyap.yuta.utils.RequestViewModel;

import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.*;

@SuppressWarnings("ConstantConditions")
public class CreateProjectDialog extends CustomInteractiveDialog {
    public static final int PICK_PDF_REQUEST = 1;
    @SuppressLint("StaticFieldLeak")
    private static TextView fileName;
    protected RequestViewModel viewModel;
    private Button submitButton;
    private Spinner spinner;
    private TextView dateField;
    private static Uri techTaskUri;

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
        statusSpinnerInitialize();
        datePickerInitialize();

        dialog.findViewById(R.id.pick_tech_task).setOnClickListener(v -> pickTechTask());
        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
            createProject();
        });
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

    private void statusSpinnerInitialize() {
        spinner = findViewById(R.id.status);
        ArrayAdapter<ProjectStatus> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                ProjectStatus.values()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

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

    private void createProject() {
        String date = dateField.getText().toString();
        String status = spinner.getSelectedItem().toString();
    }
}