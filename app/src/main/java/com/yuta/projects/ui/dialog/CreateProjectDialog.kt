package com.yuta.projects.ui.dialog

import android.app.DatePickerDialog
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuta.app.R
import com.yuta.common.ui.CancelableDialog
import com.yuta.common.util.DateUtils
import com.yuta.common.util.FieldUtils.getData
import com.yuta.common.util.FieldUtils.trimmedText
import com.yuta.common.util.FileUtils
import com.yuta.common.util.KeyboardUtils
import com.yuta.common.util.UserUtils
import com.yuta.domain.model.Team
import com.yuta.projects.ui.adapter.ProjectTeamSearchAdapter
import com.yuta.projects.viewmodel.ProjectDialogsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.Locale

open class CreateProjectDialog(
    private val fragment: Fragment,
    private val onCreateSuccessCallback: () -> Unit = {}
) : CancelableDialog(R.layout.dialog_create_project, fragment.requireActivity()) {

    companion object {
        private const val EMPTY_STRING = ""
    }

    protected val submitButton: Button by lazy { dialog.findViewById(R.id.submit) }
    private val closeButton: ImageView by lazy { dialog.findViewById(R.id.close) }
    protected val deadlineField: TextView by lazy { dialog.findViewById(R.id.date_field) }
    protected val projectName: EditText by lazy { dialog.findViewById(R.id.project_name) }
    protected val projectDesc: EditText by lazy { dialog.findViewById(R.id.project_desc) }
    private val searchField: EditText by lazy { dialog.findViewById(R.id.find_team) }
    protected val radioGroup: RadioGroup by lazy { dialog.findViewById(R.id.radio_group) }
    private val pickTeamContainer: View by lazy { dialog.findViewById(R.id.pick_team_container) }
    private val addedText: TextView by lazy { dialog.findViewById(R.id.added_team_text) }
    protected val fileName: TextView by lazy { dialog.findViewById(R.id.file_name) }
    private val emptySearch: TextView by lazy { dialog.findViewById(R.id.empty_search_text) }
    private val pickTechTaskButton: Button by lazy { dialog.findViewById(R.id.pick_tech_task) }
    private val searchButton: Button by lazy { dialog.findViewById(R.id.btn_search) }

    protected lateinit var addedTeamSearchAdapter: ProjectTeamSearchAdapter
    private lateinit var searchAdapter: ProjectTeamSearchAdapter
    protected var techTaskUri: Uri? = null

    private val projectViewModel: ProjectDialogsViewModel by fragment.viewModels()

    override fun start() {
        super.start()
        setupButtons()
        setupEditViews()
        datePickerInitialize()
        radioGroupInitialize()
        initializeRecyclerViews()
    }

    private fun setupButtons() {
        closeButton.setOnClickListener { dismiss() }

        pickTechTaskButton.setOnClickListener { pdfPickerLauncher.launch("application/pdf") }

        searchButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), searchButton)
            searchTeam(searchField.trimmedText())
        }

        submitButton.setOnClickListener {
            KeyboardUtils.hideKeyboard(fragment.requireActivity(), submitButton)
            createProject()
        }
    }

    private fun setupEditViews() {
        fileName.text = EMPTY_STRING

        setupEditView(projectName)
        setupEditView(projectDesc)
        setupEditView(searchField)
    }

    private fun createProject() {
        projectViewModel.viewModelScope.launch {
            val isCreated = projectViewModel.create(
                userId = UserUtils.getUserId(fragment.requireContext()),
                name = projectName.getData().toString(),
                description = projectDesc.getData().toString(),
                deadline = DateUtils.formatToIso(deadlineField.getData().toString()),
                teamId = projectViewModel.addedTeams.firstOrNull()?.id,
                filename = FileUtils.getFilename(fragment.requireContext(), techTaskUri),
                technicalTask = techTaskUri?.let { context.contentResolver.openInputStream(it) }
            ).first()
            handleCreateResult(isCreated)
        }
    }

    private fun handleCreateResult(isCreated: Boolean) {
        if (isCreated) {
            onCreateSuccessCallback()
            dismiss()
        }
    }

    private fun datePickerInitialize() {
        dialog.findViewById<View>(R.id.pick_date).setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                fragment.requireActivity(),
                { _, year, month, dayOfMonth ->
                    deadlineField.text =
                        String.format(Locale.getDefault(), "%02d.%02d.%04d", dayOfMonth, month + 1, year)
                    updateSubmitButtonState()
                },
                calendar[YEAR],
                calendar[MONTH],
                calendar[DAY_OF_MONTH]
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }
    }

    private fun radioGroupInitialize() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.create_with_team) {
                pickTeamContainer.isVisible = true
            } else {
                pickTeamContainer.isVisible = false
                projectViewModel.addedTeams.clear()
            }
            updateSubmitButtonState()
        }
    }

    private fun searchTeam(text: String) {
        projectViewModel.viewModelScope.launch {
            projectViewModel.searchTeams(
                text,
                UserUtils.getUserId(fragment.requireContext()),
                projectViewModel.addedTeams.firstOrNull()?.id
            )
                .collect { updateList(searchAdapter, it) }
        }
    }

    private fun updateList(adapter: ProjectTeamSearchAdapter, teams: List<Team>) {
        emptySearch.isVisible = teams.isEmpty()
        adapter.refillList(teams)
    }

    private fun initializeRecyclerViews() {
        dialog.findViewById<RecyclerView>(R.id.added_teams).apply {
            layoutManager = LinearLayoutManager(fragment.requireContext())
            addedTeamSearchAdapter = ProjectTeamSearchAdapter(
                projectViewModel.addedTeams,
            ) {
                updateSubmitButtonState()
                updateAddedTextVisibility()
            }
            adapter = addedTeamSearchAdapter
        }

        dialog.findViewById<RecyclerView>(R.id.search_teams).apply {
            layoutManager = LinearLayoutManager(fragment.requireContext())
            searchAdapter = ProjectTeamSearchAdapter(
                mutableListOf(),
                addedTeamSearchAdapter
            ) {
                updateSubmitButtonState()
                updateAddedTextVisibility()
            }
            adapter = searchAdapter
        }
    }

    private fun setupEditView(editText: EditText) {
        editText.doOnTextChanged { text, _, _, _ ->
            when (editText) {
                projectName, projectDesc -> updateSubmitButtonState()
                searchField -> searchButton.isEnabled = !text.isNullOrBlank()
            }
        }
    }

    fun updateAddedTextVisibility() {
        addedText.isVisible = projectViewModel.addedTeams.isNotEmpty()
    }

    protected fun updateSubmitButtonState() {
        val isValid =
            projectName.text.isNotBlank() && projectDesc.text.isNotBlank() && deadlineField.text.isNotBlank() &&
                    (pickTeamContainer.isGone || projectViewModel.addedTeams.isNotEmpty())
        submitButton.isEnabled = isValid
    }

    private fun setTechTask(uri: Uri) {
        techTaskUri = uri
        fileName.text = FileUtils.getFilename(fragment.requireContext(), uri)
    }

    private val pdfPickerLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { setTechTask(it) }
        }
}
