package com.ilyap.yuta.ui.dialogs.project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.ilyap.yuta.R;
import com.ilyap.yuta.ui.dialogs.CustomInteractiveDialog;
import com.ilyap.yuta.utils.ProjectStatus;
import com.ilyap.yuta.utils.RequestViewModel;

import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.*;

@SuppressWarnings("ConstantConditions")
public class CreateProjectDialog extends CustomInteractiveDialog {
    protected RequestViewModel viewModel;
    private Button submitButton;
    private Spinner spinner;
    private TextView dateField;

    public CreateProjectDialog(Context context, Fragment fragment) {
        super(context, fragment);
        setDialogLayout(R.layout.dialog_create_project);
    }

    @Override
    public void start() {
        super.start();
        viewModel = new ViewModelProvider(fragment).get(RequestViewModel.class);

        submitButton = dialog.findViewById(R.id.submit);
        statusSpinnerInitialize();

        dialog.findViewById(R.id.close).setOnClickListener(v -> dismiss());
        submitButton.setOnClickListener(v -> {
            hideKeyboard(submitButton);
            createProject();
        });

        View datePick = dialog.findViewById(R.id.pick_date);
        dateField = dialog.findViewById(R.id.date_field);


        datePick.setOnClickListener(v -> {
            Calendar calendar = getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, year1, month1, dayOfMonth) -> {
                        dateField.setText(String.format(
                                Locale.getDefault(), "%d.%d.%d", dayOfMonth, month1 + 1, year1)
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

    private void createProject() {
        String date = dateField.getText().toString();
        String status = getContext().getString(
                ((ProjectStatus) spinner.getSelectedItem()).getStringId()
        );
    }
}